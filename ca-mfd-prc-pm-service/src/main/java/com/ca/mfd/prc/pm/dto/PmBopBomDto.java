package com.ca.mfd.prc.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName PmBopBomDto
 * @description: TODO
 * @date 2023年11月25日
 * @version: 1.0
 */
@Data
public class PmBopBomDto {
    /**
     * BOP行号
     */
    @Schema(title = "BOP行号")
    private String rowNum = StringUtils.EMPTY;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialCode = StringUtils.EMPTY;

    /**
     * 物料编码
     */
    @Schema(title = "物料名称")
    private String materialName = StringUtils.EMPTY;
    /**
     * 车系
     */
    @Schema(title = "车系")
    private String vehicleSeries = StringUtils.EMPTY;

    /**
     * 用量
     */
    @Schema(title = "用量")
    private String quantity = StringUtils.EMPTY;

    /**
     * 计量单位
     */
    @Schema(title = "计量单位")
    private String measureUnit = StringUtils.EMPTY;

    /**
     * 研发供货状态
     */
    @Schema(title = "研发供货状态")
    private String rdSupplyStatus = StringUtils.EMPTY;

    /**
     * 切换状态
     */
    @Schema(title = "是否切换件")
    private boolean breakPointFlag = false;

    /**
     * 工位编码集合，多个工位之间用英文逗号分割
     */
    private String workstationCodes = StringUtils.EMPTY;

    /**
     * 工位零件数量
     */
    private String workstationMaterialNums = StringUtils.EMPTY;

}
