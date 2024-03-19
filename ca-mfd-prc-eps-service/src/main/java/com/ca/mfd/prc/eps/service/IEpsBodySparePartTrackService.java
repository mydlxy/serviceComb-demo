package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsBodySparePartTrackEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 焊装车间备件运输跟踪服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsBodySparePartTrackService extends ICrudService<EpsBodySparePartTrackEntity> {

    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsBodySparePartTrackEntity> getAllDatas();

    /**
     * 根据 虚拟VIN号 查询备件运输跟踪
     *
     * @param vehicleSn 虚拟VIN号
     * @return 返回 焊装车间备件运输跟踪
     */
    EpsBodySparePartTrackEntity getEntityByVirtualVin(String vehicleSn);

    /**
     * 呼叫空撬
     *
     * @param workstationCode
     * @return
     */
    void callingCarrier(String workstationCode);

    /**
     * 绑定之后放撬
     *
     * @param virtualVin
     * @return
     */
    void releasePassSuccess(String virtualVin);
}