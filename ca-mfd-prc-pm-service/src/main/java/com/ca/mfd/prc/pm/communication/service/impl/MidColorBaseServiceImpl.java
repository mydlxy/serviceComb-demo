package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.entity.MidColorBaseEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidColorBaseMapper;
import com.ca.mfd.prc.pm.communication.service.IMidColorBaseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 颜色代码库中间表服务实现
 * @author inkelink
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
@Service
public class MidColorBaseServiceImpl extends MidBomBaseServiceImpl<IMidColorBaseMapper, MidColorBaseEntity,MidColorBaseEntity> implements IMidColorBaseService {

    private static final Logger logger = LoggerFactory.getLogger(MidMaterialMasterServiceImpl.class);
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_COLOR_BASE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidColorBaseEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidColorBaseEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidColorBaseEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidColorBaseEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidColorBaseEntity> getAllDatas() {
        List<MidColorBaseEntity> datas = localCache.getObject(cacheName);
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
    public void receive() {
        super.fetchDataFromApi("colorbase_receive","颜色代码库信息",ApiTypeConst.BOM_COLOR_BASE,false);
//        int currentPage = 1;
//        String apiUrl = sysConfigurationProvider.getConfiguration("colorbase_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[colorbase_receive]");
//        }
//
//            String reqNo = UUIDUtils.getGuid();
//            logger.info("颜色代码库信息[" + reqNo + "]开始接收数据");
//            MidApiLogEntity loginfo = new MidApiLogEntity();
//            loginfo.setApiType(ApiTypeConst.BOM_COLOR_BASE);
//            int status = 1;
//            String errMsg = "";
//            loginfo.setDataLineNo(0);
//            loginfo.setRequestStartTime(new Date());
//            List<MidColorBaseEntity> colorBaseEntities = new ArrayList<>();
//            try {
//                loginfo.setStatus(0);
//                midApiLogService.insert(loginfo);
//                midApiLogService.saveChange();
//
//                // 构建API请求参数
//                Map param =new HashMap<>();
//                param.put("serviceId",reqNo);
//                // 发起HTTP请求
//                String responseData = apiPtService.postapi(apiUrl, param, null);
//
//                logger.warn("API平台测试url调用：" + responseData);
//                BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//                colorBaseEntities = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidColorBaseEntity.class);
//
//                for (MidColorBaseEntity et : colorBaseEntities) {
//                    et.setExeStatus(0);
//                    et.setExeTime(new Date());
//                    et.setExeMsg(StringUtils.EMPTY);
//                    et.setPrcMidApiLogId(loginfo.getId());
//                }
//                this.insertBatch(colorBaseEntities);
//                this.saveChange();
//                errMsg = "颜色代码库信息[" + reqNo + "]处理失败:";
//                logger.info(errMsg);
//            } catch (Exception ex) {
//                status = 5;
//                errMsg = "颜色代码库信息[" + reqNo + "]处理失败:";
//                logger.info(errMsg);
//                logger.error(errMsg, ex);
//            }
//            loginfo.setRequestStopTime(new Date());
//            loginfo.setStatus(status);
//            loginfo.setRemark(errMsg);
//            midApiLogService.update(loginfo);
//            midApiLogService.saveChange();
//            logger.info("颜色代码库信息[" + reqNo + "]执行完成:");
    }

    @Override
    public List<MidColorBaseEntity> getByClorCode(String colorCode) {
        QueryWrapper<MidColorBaseEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidColorBaseEntity::getColorCode, colorCode);
        return selectList(qry);
    }

    @Override
    protected MidColorBaseEntity getEntity(MidColorBaseEntity midColorBaseEntity, Long loginfoId) {
        midColorBaseEntity.setExeStatus(0);
        midColorBaseEntity.setExeTime(new Date());
        midColorBaseEntity.setExeMsg(StringUtils.EMPTY);
        midColorBaseEntity.setPrcMidApiLogId(loginfoId);
        return midColorBaseEntity;
    }

    @Override
    protected List<MidColorBaseEntity> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidColorBaseEntity.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        return new HashMap<>(1);
    }
}