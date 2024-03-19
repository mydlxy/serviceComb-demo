package com.ca.mfd.prc.pps.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * ProcessRelationInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-24
 */
@Data
@Schema(title = "ProcessRelationInfo", description = "")
public class ProcessRelationInfo {
    /**
     * 工序代码
     */
    @Schema(description = "工序代码")
    private String processCode;
    /**
     * 工序名称
     */
    @Schema(description = "工序名称")
    private String processName;

    /**
     * 线体代码;1、普通、2、设备、3、模具
     */
    @Schema(description = "线体代码")
    private String lineCode;

    /**
     * 线体名称;1、普通、2、设备、3、模具
     */
    @Schema(description = "线体名称")
    private String lineName;
    /**
     * 工艺类型;0、过程工艺 1、首道工艺 2末道工艺
     */
    @Schema(description = "工艺类型")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int processType;

    /**
     * 车间代码
     */
    @Schema(description = "车间代码")
    private String shopCode;

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 预留字段1
     */
    @Schema(title = "预留字段1")
    @TableField("ATTRIBUTE1")
    private String attribute1 = StringUtils.EMPTY;
}
