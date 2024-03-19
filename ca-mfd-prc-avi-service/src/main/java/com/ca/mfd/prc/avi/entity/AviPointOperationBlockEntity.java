package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: AVI车辆过点工艺完成阻塞检查
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AVI车辆过点工艺完成阻塞检查")
//@TableName("AVI_POINT_OPERATION_BLOCK")
@TableName("PRC_AVI_POINT_OPERATION_BLOCK")
public class AviPointOperationBlockEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_POINT_OPERATION_BLOCK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间名称
     */
    //    @Schema(title = "车间名称")
    //    @TableField("PM_AREA_NAME")
    //    private String pmAreaName = StringUtils.EMPTY;
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 车间ID
     */
    //    @Schema(title = "车间ID")
    //    @TableField("PM_AREA_ID")
    //    private String pmAreaId = StringUtils.EMPTY;

    /**
     * 线体名
     */
    //    @Schema(title = "线体名")
    //    @TableField("PM_WORK_CENTER_NAME")
    //    private String pmWorkCenterName = StringUtils.EMPTY;
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体ID
     */
    //    @Schema(title = "线体ID")
    //    @TableField("PM_WORK_CENTER_ID")
    //    private String pmWorkCenterId = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    //    @Schema(title = "AVI_Code")
    //    @TableField("AVI_CODE")
    //    private String aviCode = StringUtils.EMPTY;
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI_ID
     */
    //    @Schema(title = "AVI_ID")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    //    @Schema(title = "是否启用")
    //    @TableField("IS_ENABLE")
    //    private Boolean isEnable = true;
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isEnable = false;

}
