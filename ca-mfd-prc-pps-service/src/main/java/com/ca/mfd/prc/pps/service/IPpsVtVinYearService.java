package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsVtVinYearEntity;

/**
 * VIN号年份配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsVtVinYearService extends ICrudService<PpsVtVinYearEntity> {
    /**
     * 根据年份查询
     *
     * @param year 查询年份
     * @return 实体
     */
    PpsVtVinYearEntity getEntityByYear(String year);
}