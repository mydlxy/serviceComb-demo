package com.ca.mfd.prc.pmc.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntModelEntity;

import java.util.List;

/**
 * 设备建模
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IPmcAlarmEquipmntModelService extends ICrudService<PmcAlarmEquipmntModelEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<PmcAlarmEquipmntModelEntity> getAllDatas();

    /**
     * 根据搜索条件获取设备建模列表
     *
     * @param shop
     * @param key
     * @return
     */
    List<PmcAlarmEquipmntModelEntity> getAlarmEquipmntModelByShop(String shop, String key);

    /**
     * 获取一级报警设备
     *
     * @param shopCode
     * @return
     */
    List<ComboInfoDTO> getAlarmEquipmntLevelOne(String shopCode);
}