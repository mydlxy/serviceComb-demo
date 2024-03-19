package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsSpareConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 区域对应备件配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsSpareConfigService extends ICrudService<EpsSpareConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsSpareConfigEntity> getAllDatas();

    /**
     * 获取所有的数据
     *
     * @return
     */
    List<ComboDataDTO>  getByLineCodes(String lineCode);
}