package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigEntity;

import java.util.List;

/**
 * 开工检查项配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsWorkplaceConfigService extends ICrudService<EpsWorkplaceConfigEntity> {

    /**
     * 设置确认
     *
     * @param list
     */
    void setConfirmList(List<EpsWorkplaceConfigEntity> list);
}