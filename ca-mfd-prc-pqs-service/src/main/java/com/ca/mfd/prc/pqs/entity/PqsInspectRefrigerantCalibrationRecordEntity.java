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
 * @Description: 冷媒标定记录实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "冷媒标定记录")
@TableName("PRC_PQS_INSPECT_REFRIGERANT_CALIBRATION_RECORD")
public class PqsInspectRefrigerantCalibrationRecordEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_REFRIGERANT_CALIBRATION_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 加注项目时间
     */
    @Schema(title = "加注项目时间")
    @TableField("FILLING_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date fillingTime;


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("TEAM")
    private String team = StringUtils.EMPTY;


    /**
     * 线上冷媒左
     */
    @Schema(title = "线上冷媒左")
    @TableField("ONLINE_REFRIGERANT_LEFT")
    private String onlineRefrigerantLeft = StringUtils.EMPTY;


    /**
     * 线上冷媒右
     */
    @Schema(title = "线上冷媒右")
    @TableField("ONLINE_REFRIGERANT_RIGHT")
    private String onlineRefrigerantRight = StringUtils.EMPTY;


    /**
     * 离线冷媒
     */
    @Schema(title = "离线冷媒")
    @TableField("OFFLINE_REFRIGERANT")
    private String offlineRefrigerant = StringUtils.EMPTY;


}