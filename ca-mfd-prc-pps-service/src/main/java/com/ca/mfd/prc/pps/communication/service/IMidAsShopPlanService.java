package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidAsShopPlanEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: AS车间计划服务
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
public interface IMidAsShopPlanService extends ICrudService<MidAsShopPlanEntity> {

    /**
     * 获取计划号对应车间计划LOGid
     *
     * @param vrn
     * @param releaseVer
     * @return
     */
    Long getLogId(String vrn, String releaseVer);

    /**
     * 执行数据处理逻辑
     */
    String excute(String logid);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    List<MidAsShopPlanEntity> getListByLog(Long logid);

}