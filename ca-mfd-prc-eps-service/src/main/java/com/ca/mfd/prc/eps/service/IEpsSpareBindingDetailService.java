package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsSpareBindingDetailEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 备件绑定明细服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsSpareBindingDetailService extends ICrudService<EpsSpareBindingDetailEntity> {


    /**
     * 获取撬上面的备件VIN号集合
     *
     * @param virtualVin
     * @return
     */
    List<String> getSpareParVins(String virtualVin);

    /**
     * 备件过点 查询虚拟VIN号
     *
     * @param id 主键
     * @return 虚拟VIN号集合
     */
    List<String> getPartVirtualVinByPartTrackId(String id);
}