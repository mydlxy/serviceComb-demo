package com.ca.mfd.prc.bdc.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RouteLaneItems")
public class RouteLaneItems implements Serializable {
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    @Schema(title = "代码")
    private int laneCode;

    @Schema(title = "名称")
    private String laneName = StringUtils.EMPTY;

    @Schema(title = "车道属性 1正常车道  2 返回道 3直通道 4维修车道")
    private int laneType;

    @Schema(title = "最大计算层级")
    private int maxLevel;

    @Schema(title = "最大容量")
    private int maxValue;

    @Schema(title = "入车控制")
    private Boolean allowIn;

    @Schema(title = "出车控制")
    private Boolean allowOut;

    @Schema(title = "入车按钮控制")
    private Boolean buttonIn;

    @Schema(title = "出车按钮控制")
    private Boolean buttonOut;

    @Schema(title = "库位（011203:01-代表1排,12-代表12列,03-代表3层）")
    private String bdcStorage = StringUtils.EMPTY;

    @Schema(title = "排序")
    private Integer displayNo;
}
