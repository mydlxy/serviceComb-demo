package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: AS批次计划服务
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
public interface IMidAsBathPlanService extends ICrudService<MidAsBathPlanEntity> {

    /**
     * 查询记录
     *
     * @param plannos
     * @return
     */
    List<MidAsBathPlanEntity> getByPlanNos(List<String> plannos);

    /**
     * 执行数据处理逻辑
     */
    String excute(String logid);
 
    /**
     * 保存批次计划
     *
     * @param logid
     * @return
     */
    List<MidAsBathPlanEntity> getListByLog(Long logid);

}