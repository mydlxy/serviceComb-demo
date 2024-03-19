package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 批量维护物料
 *
 * @Author: joel
 * @Date: 2023-04-12-20:03
 * @Description:
 */
@Data
@Schema(description = "批量维护物料")
public class BatchMaterialMasterInfo {
    /**
     * 组件编号
     */
    @Schema(title = "组件编号")
    private String defectCompomemtId = StringUtils.EMPTY;

    /**
     * 物料集合
     */
    @Schema(title = "物料集合")
    private List<MaterialMasterInfo> materialMasterList;
}
