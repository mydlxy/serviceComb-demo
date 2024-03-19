package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 零部件缺陷记录服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsMmDefectAnomalyService extends ICrudService<PqsMmDefectAnomalyEntity> {

    /**
     * 获取所有零部件缺陷记录
     *
     * @return
     */
    List<PqsMmDefectAnomalyEntity> getAllDatas();

    /**
     * 获取车辆所有缺陷列表
     *
     * @param sn
     * @return
     */
    List<PqsMmDefectAnomalyEntity> getVehicleDefectAnomaly(String sn);

    /**
     * 获取车辆是否有未关闭的缺陷
     *
     * @param sn
     * @return
     */
    Boolean vehicleDefectAnomalyBySn(String sn);

    /**
     * 修改缺陷责任部门-等级
     *
     * @param info
     */
    void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info);
}