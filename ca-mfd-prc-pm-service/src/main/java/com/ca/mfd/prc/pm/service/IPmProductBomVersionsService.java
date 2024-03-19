package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.pm.dto.SetBomEnablePara;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: BOM版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductBomVersionsService extends ICrudService<PmProductBomVersionsEntity> {

    /**
     * 导入cd701
     */
    void inputCD701Bom(String tbname, String productNo);

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param checkCode         物料版本
     * @return 物料结合
     */
    PmProductBomVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode);

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料结合
     */
    PmProductBomVersionsEntity getByProductMaterialNoBomVerson(String productMaterialNo, String bomVersions);

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料结合
     */
    List<PmProductBomEntity> getBomData(String productMaterialNo, String bomVersions);

    /**
     * 维护BOM（主要针对于一计划一BOM）
     *
     * @param bomData
     * @return String
     */
    String maintainBom(MaintainBomDTO bomData);

    /**
     * 获取BOM最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    String getBomVersions(String productMaterialNo);

    /**
     * 获取版本列表
     *
     * @param productMaterialNo 物料编号
     * @return
     */
    List<String> getBomVersionsList(String productMaterialNo);

    /**
     * 设置BOM启用版本
     *
     * @param para
     * @return
     */
    void setBomEnable(SetBomEnablePara para);

    /**
     * 根据物料号查询版本号
     *
     * @param productMaterialNo
     * @return 物料号列表
     */
    List<PmProductBomVersionsEntity> getBomVersionsByCode(String productMaterialNo);

    /**
     * 根据零件号获取整车物料版本信息
     *
     * @param materialNo
     * @param orderCategory
     * @return
     */
    PmProductBomVersionsEntity getVersionByMaterialNo(String materialNo, String orderCategory);

    /**
     * 保存bom数据到逐渐
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    void saveBomToCom(String productMaterialNo, String bomVersions);

    /**
     * 复制
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    String copyBom(String productMaterialNo, String bomVersions);
}