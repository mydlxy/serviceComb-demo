package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.entity.PmcStopCodeEntity;

import java.util.List;

/**
 * 停线代码
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcStopCodeService extends ICrudService<PmcStopCodeEntity> {
    /**
     * 获取所有数据
     *
     * @return
     */
    List<PmcStopCodeEntity> getAllDatas();

}