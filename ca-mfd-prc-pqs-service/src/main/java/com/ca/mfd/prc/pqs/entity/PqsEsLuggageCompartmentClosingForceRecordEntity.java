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
 * @Description: 行李箱关闭力记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "行李箱关闭力记录")
@TableName("PRC_PQS_ES_LUGGAGE_COMPARTMENT_CLOSING_FORCE_RECORD")
public class PqsEsLuggageCompartmentClosingForceRecordEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_ES_LUGGAGE_COMPARTMENT_CLOSING_FORCE_RECORD_ID", type = IdType.INPUT)
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
     * 检测值
     */
    @Schema(title = "检测值")
    @TableField("DETECTION_VALUE")
    private String detectionValue = StringUtils.EMPTY;


    /**
     * 是否合格
     */
    @Schema(title = "是否合格")
    @TableField("QUALIFIED_FLAG")
    private Boolean qualifiedFlag = false;


    /**
     * 测试人
     */
    @Schema(title = "测试人")
    @TableField("TESTER")
    private String tester = StringUtils.EMPTY;


}