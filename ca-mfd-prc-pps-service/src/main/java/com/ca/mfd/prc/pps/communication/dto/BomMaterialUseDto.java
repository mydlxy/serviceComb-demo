package com.ca.mfd.prc.pps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS整车计划和车间计划
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "物料使用信息")
public class BomMaterialUseDto {


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 物料中文名称
     */
    @Schema(title = "物料中文名称")
    private String materialNameCh = StringUtils.EMPTY;


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    private List vehicleMaterialNumbers = new ArrayList();


}