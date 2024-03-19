package com.ca.mfd.prc.eps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.communication.dto.MidDjEcuCarResultDto;
import com.ca.mfd.prc.eps.communication.entity.MidDjEcuCarResultEntity;

/**
 *
 * @Description: 电检软件结果信息数据服务
 * @author inkelink
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
public interface IMidDjEcuCarResultService extends ICrudService<MidDjEcuCarResultEntity> {
    /**
     * 根据vin号查询软件结果信息数据
     * @param vinCode
     */
    MidDjEcuCarResultDto queryEcuCarByVinCode(String vinCode);

}