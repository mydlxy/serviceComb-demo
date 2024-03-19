package com.ca.mfd.prc.pps.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.dto.LmsWeOnlineQueueDTO;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsMainUpLineConfigMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineConfigService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @Description: 焊装主体上线队列服务实现
 * @author inkelink
 * @date 2024年01月18日
 * @变更说明 BY inkelink At 2024年01月18日
 */
@Service
public class PpsMainUpLineConfigServiceImpl extends AbstractCrudServiceImpl<IPpsMainUpLineConfigMapper, PpsMainUpLineConfigEntity> implements IPpsMainUpLineConfigService {

    private static final Logger logger = LoggerFactory.getLogger(PpsMainUpLineConfigServiceImpl.class);

    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private IPpsEntryService ppsEntryService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    private final String cacheName = "PRC_PPS_MAIN_UP_LINE_CONFIG";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsMainUpLineConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsMainUpLineConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsMainUpLineConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsMainUpLineConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsMainUpLineConfigEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PpsMainUpLineConfigEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PpsMainUpLineConfigEntity model, boolean isUpdate) {

        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("CONFIG_CODE", model.getConfigCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PpsMainUpLineConfigEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("配置编码" + model.getConfigCode() + "已经存在，不允许重复录入！");
        }
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsMainUpLineConfigEntity>
     */
    @Override
    public List<PpsMainUpLineConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PpsMainUpLineConfigEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

    /**
     * 获取lms焊装工单上线队列
     */
    public List<LmsWeOnlineQueueDTO> getWeOnlineQueue(List<String> subCodes, Boolean isSendLms, Boolean isChangeStatus) {
        List<PpsMainUpLineConfigEntity> cfgs = getAllDatas().stream().filter(c -> subCodes.contains(c.getConfigCode()))
                .collect(Collectors.toList());
        if (cfgs == null || cfgs.isEmpty()) {
            throw new InkelinkException("没有找到订阅码对应的配置信息" + JsonUtils.toJsonString(subCodes));
        }
        List<LmsWeOnlineQueueDTO> result = new ArrayList<>();
        QueryWrapper<PpsEntryEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsEntryEntity::getSubscriubeCode, subCodes)
                .eq(PpsEntryEntity::getEntryType, 2)
                .eq(PpsEntryEntity::getStatus, 2)
                .orderByAsc(PpsEntryEntity::getDisplayNo);
        List<PpsEntryEntity> list = ppsEntryService.getTopDatas(100, qry);
        if (list == null || list.isEmpty()) {
            return result;
        }
        for (PpsEntryEntity entry : list) {
            entry.setStatus(30);
            List<PmProductBomEntity> boms = ppsOrderService.getOrderBom(entry.getOrderNo());

            //根据订阅吗获取配置的组件，匹配BOM查询出对应的物料
            PpsMainUpLineConfigEntity cfgComponet = cfgs.stream().
                    filter(c -> StringUtils.isNotBlank(c.getComponentCode())
                            && StringUtils.equals(c.getConfigCode(), entry.getSubscriubeCode()))
                    .findFirst().orElse(null);
            if (cfgComponet != null) {
                PmProductBomEntity bomEntry = boms.stream().filter(c -> c.getMaterialNo() != null
                                && c.getMaterialNo().startsWith(cfgComponet.getComponentCode()))
                        .findFirst().orElse(null);
                if (bomEntry != null) {
                    LmsWeOnlineQueueDTO dto = new LmsWeOnlineQueueDTO();
                    dto.setVin(entry.getSn());
                    dto.setMaterialCode(bomEntry.getMaterialNo());
                    dto.setUniqueCode(entry.getId().toString());
                    result.add(dto);
                }
            }
        }
        Boolean sendFlag = false;
        if (isSendLms) {
            String sendMsg = sendLms(result);
            if (StringUtils.isBlank(sendMsg)) {
                sendFlag = true;
            } else {
                throw new InkelinkException(sendMsg);
            }
        }
        if (isChangeStatus && sendFlag) {
            ppsEntryService.updateBatchById(list);
            ppsEntryService.saveChange();
        }
        return result;
    }

    private String sendLms(List<LmsWeOnlineQueueDTO> fbacks) {
        String reqNo = UUIDUtils.getGuid();
        //校验
        if (fbacks == null || fbacks.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("lmsweonqueue_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[lmsweonqueue_send]");
        }
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_WEQUEUE_START);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            loginfo.setDataLineNo(fbacks.size());
            try {
                String sendData = JsonUtils.toJsonString(fbacks);
                loginfo.setReqData(sendData);

                String ars = apiPtService.postapi(apiPath, fbacks, null);
                loginfo.setResponseData(ars);
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "Lms焊装上线队列[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception ex) {
                logger.error("", fbacks);
                status = 5;
                errMsg = "Lms焊装上线队列[" + reqNo + "]处理失败:" + ex.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "Lms焊装上线队列[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            this.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("Lms焊装上线队列[" + reqNo + "]执行完成:");
        if (status == 1) {
            return "";
        } else {
            return errMsg;
        }
    }

}