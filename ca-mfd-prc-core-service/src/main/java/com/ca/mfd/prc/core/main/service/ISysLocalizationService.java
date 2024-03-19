package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysLocalizationEntity;

import java.util.List;

/**
 * 国际化
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysLocalizationService extends ICrudService<SysLocalizationEntity> {

    /**
     * 获取所有的数据
     */
    List<SysLocalizationEntity> getAllDatas();
}