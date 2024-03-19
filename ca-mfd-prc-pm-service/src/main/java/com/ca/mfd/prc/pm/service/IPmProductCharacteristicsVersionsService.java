package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.SetCharacteristicsEnablePara;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsVersionsEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 特征版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductCharacteristicsVersionsService extends ICrudService<PmProductCharacteristicsVersionsEntity> {
    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param checkCode          物料版本
     * @return 特征
     */
    PmProductCharacteristicsVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode);

    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征
     */
    PmProductCharacteristicsVersionsEntity getByMaterialNoVersions(String productMaterialNo, String versions);

    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征集合
     */
    List<PmProductCharacteristicsEntity> getCharacteristicsData(String productMaterialNo, String versions);

    /**
     * 维护特征（主要针对于一计划一特征）
     *
     * @param data 特征数据
     * @return bom 版本号
     */
    String maintainCharacteristics(MaintainCharacteristicsInfo data,String attr);

    /**
     * 获取特征详细数据
     *
     * @param characteristicsVersions
     * @return 特征实体
     */
    PmProductCharacteristicsVersionsEntity getByCharacteristicsVersions(String characteristicsVersions);

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取特征版本信息
     */
    String getCharacteristicsVersions(String productMaterialNo);

    /**
     * 获取特征版本列表
     *
     * @param productMaterialNo
     * @return
     */
    List<String> getCharacteristicsVersionsList(String productMaterialNo);

    /**
     * 设置特征启用版本
     *
     * @param param
     */
    void setCharacteristicsEnable(SetCharacteristicsEnablePara param);

    /**
     * 根据物料号查询特征版本
     *
     * @param productMaterialNo 物料号
     * @return 特征版本
     */
    PmProductCharacteristicsVersionsEntity getCharacteristicsVersionsByCode(String productMaterialNo);

    /**
     * 复制
     *
     * @param productMaterialNo 物料号
     * @return 特征版本
     */
     String copyCharacteristics(String productMaterialNo, String characteristicsVersions);
}