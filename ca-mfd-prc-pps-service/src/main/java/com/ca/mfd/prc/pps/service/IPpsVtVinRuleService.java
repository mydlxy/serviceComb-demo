package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsVtVinRuleEntity;

import java.util.List;

/**
 * VIN配置,前7位
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsVtVinRuleService extends ICrudService<PpsVtVinRuleEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<ComboInfoDTO>
     */
    List<ComboInfoDTO> getVinList();


    /**
     * 创建VIN号 标准方法 其他使用此方法生成项目
     *
     * @param ppsOrderInfo 订单实体
     * @return 创建的vin号
     */
    String createVin(PpsOrderEntity ppsOrderInfo);

    /**
     * 获取所有的数据
     *
     * @return List<PpsVtVinRuleEntity>
     */
    List<PpsVtVinRuleEntity> getAllDatas();

}