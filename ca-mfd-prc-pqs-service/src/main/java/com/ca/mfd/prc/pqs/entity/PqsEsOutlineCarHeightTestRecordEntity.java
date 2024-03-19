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
 * @Description: 外廓车高测试记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "外廓车高测试记录")
@TableName("PRC_PQS_ES_OUTLINE_CAR_HEIGHT_TEST_RECORD")
public class PqsEsOutlineCarHeightTestRecordEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_OUTLINE_CAR_HEIGHT_TEST_RECORD_ID", type = IdType.INPUT)
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
     * 轮胎尺寸
     */
    @Schema(title = "轮胎尺寸")
    @TableField("TIRE_SIZE")
    private String tireSize = StringUtils.EMPTY;


    /**
     * AL左前
     */
    @Schema(title = "AL左前")
    @TableField("AL")
    private String al = StringUtils.EMPTY;


    /**
     * AR右前
     */
    @Schema(title = "AR右前")
    @TableField("AR")
    private String ar = StringUtils.EMPTY;


    /**
     * A左右差
     */
    @Schema(title = "A左右差")
    @TableField("LEFT_RIGHT_DIFFERENCE_A")
    private String leftRightDifferenceA = StringUtils.EMPTY;


    /**
     * BL左后
     */
    @Schema(title = "BL左后")
    @TableField("BL")
    private String bl = StringUtils.EMPTY;


    /**
     * BR右后
     */
    @Schema(title = "BR右后")
    @TableField("BR")
    private String br = StringUtils.EMPTY;


    /**
     * B左右差
     */
    @Schema(title = "B左右差")
    @TableField("LEFT_RIGHT_DIFFERENCE_B")
    private String leftRightDifferenceB = StringUtils.EMPTY;


    /**
     * 是否合格
     */
    @Schema(title = "是否合格")
    @TableField("QUALIFIED_OR_NOT")
    private Boolean qualifiedOrNot = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}