package com.ca.mfd.prc.common.dto.eps;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * EqumentWoData
 *
 * @author inkelink eric.zhou
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "EqumentWoData")
public class EqumentWoData {

    @Schema(title = "岗位")
    private String workPlaceName;

    @Schema(title = "顺序号")
    private Integer disPlayNo = 0;

    @Schema(title = "设备编号")
    private String equmentNo;

    @Schema(title = "车身号")
    private String sn;

    @Schema(title = "工艺区段")
    private String woAre;

    @Schema(title = "工艺代码")
    private String woCode;

    @Schema(title = "下限值")
    private String woDownlimit;

    @Schema(title = "工艺名称")
    private String woName;

    @Schema(title = "参数结果")
    private String woResult;

    @Schema(title = "标准值")
    private String woStandard;

    @Schema(title = "参数单位")
    private String woUnit;

    @Schema(title = "上限值")
    private String woUplimit;

    @Schema(title = "参数值")
    private String woValue;


}
