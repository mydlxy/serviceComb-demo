package com.ca.mfd.prc.eps.entity;

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
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 海克斯康光学检验数据实体
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "海克斯康光学检验数据")
@TableName("PRC_EPS_HKSK_LIGHT_CHECK_DATA")
public class EpsHkskLightCheckDataEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_HKSK_LIGHT_CHECK_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 检测工位
     */
    @Schema(title = "检测工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 检验员
     */
    @Schema(title = "检验员")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;


    /**
     * 是否预警
     */
    @Schema(title = "是否预警")
    @TableField("EW_FLAG")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean ewFlag = false;


    /**
     * 预警项
     */
    @Schema(title = "预警项")
    @TableField("EW_ITEM")
    private String ewItem = StringUtils.EMPTY;


    /**
     * 是否报警
     */
    @Schema(title = "是否报警")
    @TableField("ALARM_FLAG")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean alarmFlag = false;


    /**
     * 报警项
     */
    @Schema(title = "报警项")
    @TableField("ALARM_ITEM")
    private String alarmItem = StringUtils.EMPTY;


    /**
     * 全尺寸(L2）数据合格率
     */
    @Schema(title = "全尺寸(L2）数据合格率")
    @TableField("I2_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private BigDecimal i2Result = BigDecimal.valueOf(0);


    /**
     * 关重控制点合格率
     */
    @Schema(title = "关重控制点合格率")
    @TableField("KEY_POINT_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private BigDecimal keyPointResult = BigDecimal.valueOf(0);


    /**
     * 结果（1合格 2不合格）
     */
    @Schema(title = "结果（1合格 2不合格）")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;


    /**
     * 检验日期
     */
    @Schema(title = "检验日期")
    @TableField("INSPECT_DT")
    private String inspectDt = StringUtils.EMPTY;


    /**
     * 检验时间
     */
    @Schema(title = "检验时间")
    @TableField("INSPECT_TIME")
    private String inspectTime = StringUtils.EMPTY;


}