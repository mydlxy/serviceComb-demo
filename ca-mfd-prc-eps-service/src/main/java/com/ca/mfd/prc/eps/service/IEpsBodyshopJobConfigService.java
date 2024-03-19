package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.VehicleJobInfo;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 焊装车间执行码配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsBodyshopJobConfigService extends ICrudService<EpsBodyshopJobConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsBodyshopJobConfigEntity> getAllDatas();

    /**
     * 获取车辆各个区域的执行码
     *
     * @param sn
     * @return
     */
    VehicleJobInfo getJobConfigBySn(String sn);
}