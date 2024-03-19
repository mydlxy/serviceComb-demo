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

import java.util.Date;

/**
 *
 * @Description: 预成组入箱数据实体
 * @author inkelink
 * @date 2024年02月23日
 * @变更说明 BY inkelink At 2024年02月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "预成组入箱数据")
@TableName("PRC_EPS_MODULE_INBOX_DATA")
public class EpsModuleInboxDataEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_INBOX_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 策略标识
     */
    @Schema(title = "策略标识")
    @TableField("STRATEGY_SIGN")
    private String strategySign;


    /**
     * 电池型号
     */
    @Schema(title = "电池型号")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


    /**
     * 策略描述
     */
    @Schema(title = "策略描述")
    @TableField("STRATEGY_DESCRIPTION")
    private String strategyDescription = StringUtils.EMPTY;


    /**
     * L1生产线体
     */
    @Schema(title = "L1生产线体")
    @TableField("L1_LINE_CODE")
    private String l1LineCode = StringUtils.EMPTY;


    /**
     * L2生产线体
     */
    @Schema(title = "L2生产线体")
    @TableField("L2_LINE_CODE")
    private String l2LineCode = StringUtils.EMPTY;


    /**
     * L3生产线体
     */
    @Schema(title = "L3生产线体")
    @TableField("L3_LINE_CODE")
    private String l3LineCode = StringUtils.EMPTY;


    /**
     * L4生产线体
     */
    @Schema(title = "L4生产线体")
    @TableField("L4_LINE_CODE")
    private String l4LineCode = StringUtils.EMPTY;


    /**
     * L5生产线体
     */
    @Schema(title = "L5生产线体")
    @TableField("L5_LINE_CODE")
    private String l5LineCode = StringUtils.EMPTY;


    /**
     * L6生产线体
     */
    @Schema(title = "L6生产线体")
    @TableField("L6_LINE_CODE")
    private String l6LineCode = StringUtils.EMPTY;


    /**
     * L7生产线体
     */
    @Schema(title = "L7生产线体")
    @TableField("L7_LINE_CODE")
    private String l7LineCode = StringUtils.EMPTY;


    /**
     * L8生产线体
     */
    @Schema(title = "L8生产线体")
    @TableField("L8_LINE_CODE")
    private String l8LineCode = StringUtils.EMPTY;


    /**
     * R1生产线体
     */
    @Schema(title = "R1生产线体")
    @TableField("R1_LINE_CODE")
    private String r1LineCode = StringUtils.EMPTY;


    /**
     * R2生产线体
     */
    @Schema(title = "R2生产线体")
    @TableField("R2_LINE_CODE")
    private String r2LineCode = StringUtils.EMPTY;


    /**
     * R3生产线体
     */
    @Schema(title = "R3生产线体")
    @TableField("R3_LINE_CODE")
    private String r3LineCode = StringUtils.EMPTY;


    /**
     * R4生产线体
     */
    @Schema(title = "R4生产线体")
    @TableField("R4_LINE_CODE")
    private String r4LineCode = StringUtils.EMPTY;


    /**
     * R5生产线体
     */
    @Schema(title = "R5生产线体")
    @TableField("R5_LINE_CODE")
    private String r5LineCode = StringUtils.EMPTY;


    /**
     * R6生产线体
     */
    @Schema(title = "R6生产线体")
    @TableField("R6_LINE_CODE")
    private String r6LineCode = StringUtils.EMPTY;


    /**
     * R7生产线体
     */
    @Schema(title = "R7生产线体")
    @TableField("R7_LINE_CODE")
    private String r7LineCode = StringUtils.EMPTY;


    /**
     * R8生产线体
     */
    @Schema(title = "R8生产线体")
    @TableField("R8_LINE_CODE")
    private String r8LineCode = StringUtils.EMPTY;


    /**
     * L1模组编码
     */
    @Schema(title = "L1模组编码")
    @TableField("L1_MODULE_CODE")
    private String l1ModuleCode = StringUtils.EMPTY;


    /**
     * L3模组编码
     */
    @Schema(title = "L3模组编码")
    @TableField("L3_MODULE_CODE")
    private String l3ModuleCode = StringUtils.EMPTY;


    /**
     * L2模组编码
     */
    @Schema(title = "L2模组编码")
    @TableField("L2_MODULE_CODE")
    private String l2ModuleCode = StringUtils.EMPTY;


    /**
     * L4模组编码
     */
    @Schema(title = "L4模组编码")
    @TableField("L4_MODULE_CODE")
    private String l4ModuleCode = StringUtils.EMPTY;


    /**
     * L5模组编码
     */
    @Schema(title = "L5模组编码")
    @TableField("L5_MODULE_CODE")
    private String l5ModuleCode = StringUtils.EMPTY;


    /**
     * L6模组编码
     */
    @Schema(title = "L6模组编码")
    @TableField("L6_MODULE_CODE")
    private String l6ModuleCode = StringUtils.EMPTY;


    /**
     * L7模组编码
     */
    @Schema(title = "L7模组编码")
    @TableField("L7_MODULE_CODE")
    private String l7ModuleCode = StringUtils.EMPTY;


    /**
     * L8模组编码
     */
    @Schema(title = "L8模组编码")
    @TableField("L8_MODULE_CODE")
    private String l8ModuleCode = StringUtils.EMPTY;


    /**
     * R1模组编码
     */
    @Schema(title = "R1模组编码")
    @TableField("R1_MODULE_CODE")
    private String r1ModuleCode = StringUtils.EMPTY;


    /**
     * R2模组编码
     */
    @Schema(title = "R2模组编码")
    @TableField("R2_MODULE_CODE")
    private String r2ModuleCode = StringUtils.EMPTY;


    /**
     * R3模组编码
     */
    @Schema(title = "R3模组编码")
    @TableField("R3_MODULE_CODE")
    private String r3ModuleCode = StringUtils.EMPTY;


    /**
     * R4模组编码
     */
    @Schema(title = "R4模组编码")
    @TableField("R4_MODULE_CODE")
    private String r4ModuleCode = StringUtils.EMPTY;


    /**
     * R5模组编码
     */
    @Schema(title = "R5模组编码")
    @TableField("R5_MODULE_CODE")
    private String r5ModuleCode = StringUtils.EMPTY;


    /**
     * R6模组编码
     */
    @Schema(title = "R6模组编码")
    @TableField("R6_MODULE_CODE")
    private String r6ModuleCode = StringUtils.EMPTY;


    /**
     * R7模组编码
     */
    @Schema(title = "R7模组编码")
    @TableField("R7_MODULE_CODE")
    private String r7ModuleCode = StringUtils.EMPTY;


    /**
     * R8模组编码
     */
    @Schema(title = "R8模组编码")
    @TableField("R8_MODULE_CODE")
    private String r8ModuleCode = StringUtils.EMPTY;


    /**
     * 模组入箱Json数据
     */
    @Schema(title = "模组入箱Json数据")
    @TableField("IN_BOX_JSON")
    private String inBoxJson;


    /**
     * 涂胶JOB  JSON
     */
    @Schema(title = "涂胶JOB  JSON")
    @TableField("GLUING_JOB_JSON")
    private String gluingJobJson;


    /**
     * 入箱去向标记
     */
    @Schema(title = "入箱去向标记")
    @TableField("ROUTE_SIGN")
    private String routeSign;


    /**
     * 电池RIN码
     */
    @Schema(title = "电池RIN码")
    @TableField("RIN")
    private String rin;


}