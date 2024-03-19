package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviFrozenEntity;
import com.ca.mfd.prc.common.service.ICrudService;

/**
 * 冻结产品
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviFrozenService extends ICrudService<AviFrozenEntity> {
    /**
     * 冻结确认
     *
     * @param frozenId 冻结ID
     */
    void confirm(String frozenId);

    /**
     * 取消冻结
     *
     * @param frozenId 解冻ID
     */
    void unFrozen(String frozenId);
}