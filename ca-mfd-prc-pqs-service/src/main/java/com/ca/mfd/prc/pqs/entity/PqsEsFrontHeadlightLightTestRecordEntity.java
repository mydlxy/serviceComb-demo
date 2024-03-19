package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 前大灯灯光测试记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "前大灯灯光测试记录")
@TableName("PRC_PQS_ES_FRONT_HEADLIGHT_LIGHT_TEST_RECORD")
public class PqsEsFrontHeadlightLightTestRecordEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_FRONT_HEADLIGHT_LIGHT_TEST_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("TEST_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date testDate;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * VIN
     */
    @Schema(title = "VIN")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 远光水平（左）
     */
    @Schema(title = "远光水平（左）")
    @TableField("HIGH_BEAM_LEVEL_L")
    private String highBeamLevelL = StringUtils.EMPTY;


    /**
     * 远光垂直（左）
     */
    @Schema(title = "远光垂直（左）")
    @TableField("HIGH_BEAM_VERTICAL_L")
    private String highBeamVerticalL = StringUtils.EMPTY;


    /**
     * 照度（左）
     */
    @Schema(title = "照度（左）")
    @TableField("ILLUMINANCE_L")
    private String illuminanceL = StringUtils.EMPTY;


    /**
     * 远光灯高（左）
     */
    @Schema(title = "远光灯高（左）")
    @TableField("HIGH_BEAM_HEADLIGHTS_L")
    private String highBeamHeadlightsL = StringUtils.EMPTY;


    /**
     * 近光水平（左）
     */
    @Schema(title = "近光水平（左）")
    @TableField("LOW_BEAM_LEVEL_L")
    private String lowBeamLevelL = StringUtils.EMPTY;


    /**
     * 近光垂直（左）
     */
    @Schema(title = "近光垂直（左）")
    @TableField("LOW_BEAM_VERTICAL_L")
    private String lowBeamVerticalL = StringUtils.EMPTY;


    /**
     * 近光灯高（左）
     */
    @Schema(title = "近光灯高（左）")
    @TableField("LOW_BEAM_HIGH_L")
    private String lowBeamHighL = StringUtils.EMPTY;


    /**
     * 灯高比（左）
     */
    @Schema(title = "灯高比（左）")
    @TableField("LAMP_HEIGHT_RATIO_L")
    private String lampHeightRatioL = StringUtils.EMPTY;


    /**
     * 远光水平（右）
     */
    @Schema(title = "远光水平（右）")
    @TableField("HIGH_BEAM_LEVEL_R")
    private String highBeamLevelR = StringUtils.EMPTY;


    /**
     * 远光垂直（右）
     */
    @Schema(title = "远光垂直（右）")
    @TableField("HIGH_BEAM_VERTICAL_R")
    private String highBeamVerticalR = StringUtils.EMPTY;


    /**
     * 照度（右）
     */
    @Schema(title = "照度（右）")
    @TableField("ILLUMINANCE_R")
    private String illuminanceR = StringUtils.EMPTY;


    /**
     * 远光灯高（右）
     */
    @Schema(title = "远光灯高（右）")
    @TableField("HIGH_BEAM_HEADLIGHTS_R")
    private String highBeamHeadlightsR;


    /**
     * 近光水平（右）
     */
    @Schema(title = "近光水平（右）")
    @TableField("LOW_BEAM_LEVEL_R")
    private String lowBeamLevelR;


    /**
     * 近光垂直（右）
     */
    @Schema(title = "近光垂直（右）")
    @TableField("LOW_BEAM_VERTICAL_R")
    private String lowBeamVerticalR;


    /**
     * 近光灯高（右）
     */
    @Schema(title = "近光灯高（右）")
    @TableField("LOW_BEAM_HIGH_R")
    private String lowBeamHighR;


    /**
     * 灯高比（右）
     */
    @Schema(title = "灯高比（右）")
    @TableField("LAMP_HEIGHT_RATIO_R")
    private String lampHeightRatioR;


}