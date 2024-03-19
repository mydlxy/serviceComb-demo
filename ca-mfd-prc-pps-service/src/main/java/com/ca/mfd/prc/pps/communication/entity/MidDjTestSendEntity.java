package com.ca.mfd.prc.pps.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 电检手动发数据
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电检手动发数据")
@TableName("PRC_MID_DJ_TEST_SEND")
public class MidDjTestSendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_DJ_TEST_SEND_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    @TableField("VEHICLE_MATERIAL_NUMBERS")
    private String vehicleMaterialNumbers = StringUtils.EMPTY;


    /**
     * VIN号
     */
    @Schema(title = "VIN号")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 定编号
     */
    @Schema(title = "定编号")
    @TableField("DING_CODE")
    private String dingCode = StringUtils.EMPTY;


    /**
     * 计划上线日期
     */
    @Schema(title = "计划上线日期")
    @TableField("SPECIFY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date specifyDate = new Date();

}