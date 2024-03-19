package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * 产品跟踪站点终端操作日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviOperationLogService extends ICrudService<AviOperationLogEntity> {

    /**
     * 插入AVI日志
     *
     * @param aviId
     * @param operName
     * @param operation
     */
    void insertAviLog(String aviId, String operName, String operation);

    /// <summary>
    /// 插入AVI日志(批量）

    /**
     * 插入AVI日志(批量）
     *
     * @param aviId
     * @param operName
     * @param operations
     */
    void insertAviLog(String aviId, String operName, List<String> operations);
}