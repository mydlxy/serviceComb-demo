package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.entity.MiddleRequest;
import com.ca.mfd.prc.common.entity.MiddleResponse;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ServiceMethodRequest;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.RestClientUtils;
import com.ca.mfd.prc.core.main.entity.SysRequestConfigEntity;
import com.ca.mfd.prc.core.main.mapper.ISysRequestConfigMapper;
import com.ca.mfd.prc.core.main.service.ISysRequestConfigService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统内部站点访问
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysRequestConfigServiceImpl extends AbstractCrudServiceImpl<ISysRequestConfigMapper, SysRequestConfigEntity> implements ISysRequestConfigService {

    private static final Object lockObj = new Object();
    private final String cacheName = "PRC_SYS_REQUEST_CONFIG";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private RestClientUtils restClientUtils;

    @Autowired
    private IdentityHelper identityHelper;

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(SysRequestConfigEntity entity) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("REQUEST_KEY", entity.getRequestKey(), ConditionOper.Equal));
        SysRequestConfigEntity sysRequestConfigEntity = this.getData(dtos).stream().findFirst().orElse(null);
        if (sysRequestConfigEntity != null) {
            throw new InkelinkException("请求key已存在" + entity.getRequestKey());
        }
        removeCache();
    }

    @Override
    public void afterUpdate(SysRequestConfigEntity entity) {
        validDataUnique(entity.getId(), "REQUEST_KEY", entity.getRequestKey(), "已经存在请求RequestKey为%s的数据", null, null);
        super.afterUpdate(entity);
    }

    @Override
    public boolean update(LambdaUpdateWrapper<SysRequestConfigEntity> lambdaUpdateWrapper) {
        removeCache();
        return super.update(lambdaUpdateWrapper);
    }

    @Override
    public void delete(List<ConditionDto> conditions, Boolean isLogic) {
        removeCache();
        super.delete(conditions, isLogic);
    }

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public List<SysRequestConfigEntity> getAllDatas() {
        List<SysRequestConfigEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public MiddleResponse defaultRequestClinet(String key, Object content) {
        try {
            SysRequestConfigEntity sysRequestConfigEntity = this.getAllDatas().stream().filter(s -> s.getRequestKey().equals(key)).findFirst().orElse(null);
            if (sysRequestConfigEntity != null) {
                String sourceString = JsonUtils.toJsonString(content);
                Map request = JsonUtils.parseObject(sourceString, Map.class);
                String useRoutName = sysRequestConfigEntity.getRequestUrl() == null ? "" : sysRequestConfigEntity.getRequestUrl();
                MiddleRequest req = new MiddleRequest();
                req.setParams(request);
                req.setUseRoutName(useRoutName);
                req.setAttachToken(!StringUtils.isBlank(identityHelper.getToken()));
                if (sysRequestConfigEntity.getRequestMethod() == 1) {
                    req.setServiceTypeEnum(ServiceMethodRequest.Post);
                } else {
                    req.setServiceTypeEnum(ServiceMethodRequest.Get);
                }
                req.setServiceDomainKey(sysRequestConfigEntity.getServiceDomainKey());

                MiddleResponse data = restClientUtils.executeAsync(req);
                //TODO 存日志未实现
                if (data.getSuccess()) {
                    setBodyPar(key, sourceString);
                } else {
                    throw new InkelinkException(data.getErrorMessage());
                }
                return data;
            } else {
                throw new InkelinkException(key + "请求地址未找到配置项");
            }
        } catch (Exception e) {
            log.error(key + ":调用失败", e);
            throw new InkelinkException(key + ":" + e.getMessage());
        }
    }

    /**
     * 写入请求参数
     *
     * @param key
     * @param sourceString
     */
    private void setBodyPar(String key, String sourceString) {
        List<ConditionDto> dtos = new ArrayList<>();
        try {
            dtos.add(new ConditionDto("REQUEST_KEY", key, ConditionOper.Equal));
            SysRequestConfigEntity model = this.getData(dtos).stream().findFirst().orElse(null);
            if (model != null) {
                if (Strings.isNullOrEmpty(model.getBodyPar())) {
                    LambdaUpdateWrapper<SysRequestConfigEntity> uset = new LambdaUpdateWrapper<>();
                    uset.set(SysRequestConfigEntity::getBodyPar, sourceString);
                    uset.eq(SysRequestConfigEntity::getId, model.getId());
                    this.update(uset);
                    this.saveChange();
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

//    private ResultVO executeAsync(Integer requestMethod, String useRoutName,Map request, Map<String, String> header) {
//        if(requestMethod==1){
//            String body="";
//            try {
//                ResponseEntity<String> requestTxt = restTemplateUtil.postJson(useRoutName, request, header, String.class);
//                body = requestTxt.getBody();
//                log.info(useRoutName+":接口返回结果："+requestTxt.getStatusCodeValue()+body);
//                if(requestTxt.getStatusCode()== HttpStatus.OK){
//                    return new ResultVO().ok(JsonUtils.parseObject(requestTxt.getBody(),JSONObject.class));
//                }else {
//                    return new ResultVO().error(requestTxt.getStatusCodeValue(),requestTxt.getBody());
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//                body = ExceptionUtils.getErrorMessage(e);
//                log.error("", e);
//            }
//        }else {
//            //TODO get方法处理
//            return new ResultVO();
//        }
//        return null;
//    }
}