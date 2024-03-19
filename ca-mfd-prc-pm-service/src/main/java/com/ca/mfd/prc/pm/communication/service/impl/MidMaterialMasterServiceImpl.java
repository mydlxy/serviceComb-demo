package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidMaterialMasterMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialMasterService;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 物料主数据服务实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@Service
public class MidMaterialMasterServiceImpl extends MidBomBaseServiceImpl<IMidMaterialMasterMapper, MidMaterialMasterEntity, MidMaterialMasterEntity> implements IMidMaterialMasterService {
    private static final Logger logger = LoggerFactory.getLogger(MidMaterialMasterServiceImpl.class);

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_MATERIAL_MASTER";
    @Qualifier("pmThreadPoolTaskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidMaterialMasterEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidMaterialMasterEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidMaterialMasterEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidMaterialMasterEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidMaterialMasterEntity> getAllDatas() {
        List<MidMaterialMasterEntity> datas = localCache.getObject(cacheName);
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
        super.fetchDataFromApi("materialmaster_receive","物料主数据信息",ApiTypeConst.BOM_MATERIAL_MAIN,true);
//        int currentPage = 1;
//        String apiUrl = sysConfigurationProvider.getConfiguration("materialmaster_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[materialmaster_receive]");
//        }
////        while (true) {
//            String reqNo = UUIDUtils.getGuid();
//            logger.info("物料主数据信息[" + reqNo + "]开始接收数据");
//            MidApiLogEntity loginfo = new MidApiLogEntity();
//            loginfo.setApiType(ApiTypeConst.BOM_MATERIAL_MAIN);
//            int status = 1;
//            String errMsg = "";
//            loginfo.setDataLineNo(0);
//            loginfo.setRequestStartTime(new Date());
//            List<MidMaterialMasterEntity> materialMasterEntities = new ArrayList<>();
//            try {
//                loginfo.setStatus(0);
//                midApiLogService.insert(loginfo);
//                midApiLogService.saveChange();
//
//                // 构建API请求参数
//                Map param=new HashMap();
//                param.put("serviceId",reqNo);
////                param.put("pageindex",currentPage);
//                // 发起HTTP请求
//                String responseData = apiPtService.postapi(apiUrl, param, null);
//
//                logger.warn("API平台测试url调用：" + responseData);
//                BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//                materialMasterEntities = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidMaterialMasterEntity.class);
//
//                for (MidMaterialMasterEntity et : materialMasterEntities) {
//                    et.setExeStatus(0);
//                    et.setExeTime(new Date());
//                    et.setExeMsg(StringUtils.EMPTY);
//                    et.setPrcMidApiLogId(loginfo.getId());
//                }
//                this.insertBatch(materialMasterEntities,200,false,1);
//                this.saveChange();
//                status = 1;
//                errMsg = "物料主数据信息[" + reqNo + "]处理失败:";
//                logger.info(errMsg);
//            } catch (Exception ex) {
//                status = 5;
//                errMsg = "物料主数据信息[" + reqNo + "]处理失败:";
//                logger.info(errMsg);
//                logger.error(errMsg, ex);
//            }
//            loginfo.setRequestStopTime(new Date());
//            loginfo.setStatus(status);
//            loginfo.setRemark(errMsg);
//            midApiLogService.update(loginfo);
//            midApiLogService.saveChange();
//            logger.info("物料主数据信息[" + reqNo + "]执行完成:");
//
//            // 检查是否还有更多数据
////            if (materialMasterEntities.size()>=1000) {
////                currentPage++; // 请求下一页
////            } else {
////                break; // 没有更多数据，结束循环
////            }
////        }
    }

    @Override
    public void excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmProductMaterialMasterService materialMasterService = SpringContextUtils.getBean(IPmProductMaterialMasterService.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.BOM_MATERIAL_MAIN, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                List<MidMaterialMasterEntity> datas = this.getListByLog(apilog.getId());

                threadPoolTaskExecutor.execute(() -> {
                    try {
                        materialMasterService.syncFromBom(datas);
                        materialMasterService.saveChange();
                    } catch (Exception e) {
                        logger.info("保存失败,错误消息 {}", e.getMessage());
                    }
                });

                success = true;

            } catch (Exception exception) {
                logger.debug("数据保存异常：{}", exception.getMessage());
            }
            try {
                midApiLogService.clearChange();
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogEnd);
                midApiLogService.saveChange();
            } catch (Exception exception) {
                logger.debug("日志保存异常：{}", exception.getMessage());
            }
        }
    }

    @Override
    public List<MidMaterialMasterEntity> getListByLog(Long logid) {
        QueryWrapper<MidMaterialMasterEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidMaterialMasterEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

    @Override
    protected MidMaterialMasterEntity getEntity(MidMaterialMasterEntity midMaterialMasterEntity, Long loginfoId) {
        midMaterialMasterEntity.setExeStatus(0);
        midMaterialMasterEntity.setExeTime(new Date());
        midMaterialMasterEntity.setExeMsg(StringUtils.EMPTY);
        midMaterialMasterEntity.setPrcMidApiLogId(loginfoId);
        return midMaterialMasterEntity;
    }

    @Override
    protected List<MidMaterialMasterEntity> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidMaterialMasterEntity.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        return new HashMap(1);
    }
}