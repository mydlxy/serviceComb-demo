package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.MaterialInfoDTO;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterImgEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 物料图片
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmProductMaterialMasterImgService extends ICrudService<PmProductMaterialMasterImgEntity> {

    /**
     * 检索物料列表
     *
     * @param searchKey 搜索关键字
     * @return
     */
    List<MaterialInfoDTO> getMaterialInfos(String searchKey);
}