package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidMBomEntity;
import com.ca.mfd.prc.pm.dto.WorkshopCodeMaterialRelaDTO;
import com.ca.mfd.prc.pm.entity.PmBopBomEntity;

import java.util.List;

/**
 *
 * @Description: MBOM详情服务
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
public interface IPmBopBomService extends ICrudService<PmBopBomEntity> {

    /**
     * 从bom数据同步过来
     * @param datas
     */
    void syncFromMBom(List<MidMBomEntity> datas);

    List<String> getFeatureFromBom (String workshopCode,String workStationCode);

    void copyProductMaterialMasterToMBom(WorkshopCodeMaterialRelaDTO workshopCodeMaterialRelaDTO);

    List<PmBopBomEntity> getAllDatas();

    PmBopBomEntity getBopBomByRowNumAndMaterialNo(String bomRowNum, String materialNo);
}