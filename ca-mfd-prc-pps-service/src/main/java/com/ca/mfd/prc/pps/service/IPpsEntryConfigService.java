package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;

import java.util.List;

/**
 * 工单订阅配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsEntryConfigService extends ICrudService<PpsEntryConfigEntity> {
    /**
     * 根据shopCode.areaCodes 查询
     *
     * @param shopCode     车间code
     * @param generateType
     * @param areaCodes    areaCodes
     * @return 对列表集合
     */
    List<PpsEntryConfigEntity> getPpsEntryConfigListByShopCode(String shopCode, Integer generateType, List<String> areaCodes);

    /**
     * 根据shopCode.areaCodes 查询
     *
     * @param shopCode     车间code
     * @param generateType
     * @param planModel    planModel(模糊查询)
     * @return 对列表集合
     */
    List<PpsEntryConfigEntity> getPpsEntryConfigListByShopCode(String shopCode, Integer generateType, String planModel);

    /**
     * 根据linecode model 查询
     *
     * @param lineCode
     * @param planModel planModel
     * @return 首条
     */
    PpsEntryConfigEntity getFirstByLineCode(String lineCode, String planModel);

    /**
     * 根据linecode model 查询
     *
     * @param lineCode
     * @return 首条
     */
    PpsEntryConfigEntity getFirstByLineCode(String lineCode);

    /**
     * 根据linecode model 查询
     *
     * @param subCode
     * @return 首条
     */
    PpsEntryConfigEntity getFirstBySubCode(String subCode);

    /**
     * 根据shopCode.areaCodes 查询
     *
     * @param shopCode     车间code
     * @param generateType
     * @param areaCode     areaCodes
     * @return 对列表集合
     */
    PpsEntryConfigEntity getFirstByShopCode(String shopCode, Integer generateType, String areaCode);

    /**
     * 添加配置
     *
     * @param model
     */
    void addSeqConfig(PpsEntryConfigEntity model);
}