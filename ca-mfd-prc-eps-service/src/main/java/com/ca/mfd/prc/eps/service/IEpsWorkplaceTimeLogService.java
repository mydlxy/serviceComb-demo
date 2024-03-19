package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceTimeLogEntity;

import java.util.List;

/**
 * 岗位时间日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsWorkplaceTimeLogService extends ICrudService<EpsWorkplaceTimeLogEntity> {

    /**
     * 获取列表
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    List<EpsWorkplaceTimeLogEntity> getBySn(String workstationCode, String sn);
}