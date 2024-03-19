package com.ca.mfd.prc.pps.entity;

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
 * @Description: 电池预成组下发小单元实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池预成组下发小单元")
@TableName("PRC_PPS_MODULE_ISSUE_UNIT")
public class PpsModuleIssueUnitEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_ISSUE_UNIT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 模组外键
     */
    @Schema(title = "模组外键")
    @TableField("PRC_PPS_MODULE_ISSUE_MODULE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcPpsModuleIssueModuleId = Constant.DEFAULT_ID;


    /**
     * 计划号
     */
    @Schema(title = "计划号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 生产线体
     */
    @Schema(title = "生产线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 小单元编码
     */
    @Schema(title = "小单元编码")
    @TableField("UNIT_CODE")
    private String unitCode = StringUtils.EMPTY;


//    /**
//     * 小单元数量
//     */
//    @Schema(title = "小单元数量")
//    @TableField("UNIT_NUM")
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Integer unitNum = 0;


    /**
     * 上端板物料号
     */
    @Schema(title = "上端板物料号")
    @TableField("UP_BOARD_CODE")
    private String upBoardCode = StringUtils.EMPTY;


//    /**
//     * 上端板数量
//     */
//    @Schema(title = "上端板数量")
//    @TableField("UP_BOARD_NUM")
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Integer upBoardNum = 0;


    /**
     * 下端板物料号
     */
    @Schema(title = "下端板物料号")
    @TableField("DOWN_BOARD_CODE")
    private String downBoardCode = StringUtils.EMPTY;


//    /**
//     * 下端板数量
//     */
//    @Schema(title = "下端板数量")
//    @TableField("DOWN_BOARD_NUM")
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Integer downBoardNum = 0;


    /**
     * 电芯编码
     */
    @Schema(title = "电芯编码")
    @TableField("CELL_CODE")
    private String cellCode = StringUtils.EMPTY;


    /**
     * 电芯数量
     */
    @Schema(title = "电芯数量")
    @TableField("CELL_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer cellNum = 0;

    /**
     * 小单元成组顺序
     */
    @Schema(title = "小单元成组顺序")
    @TableField("CELL_GROUP_SEQ")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer cellGroupSeq = 0;

    /**
     * 是否存在假电芯
     */
    @Schema(title = "是否存在假电芯")
    @TableField("IS_FAKE_BATTERY_CELL")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFakeBatteryCell = false;

    /**
     * 首电芯极性，0：负极，1：正极
     */
    @Schema(title = "首电芯极性，0：负极，1：正极")
    @TableField("FIRST_BATTERY_CELL_POLARITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer firstBatteryCellPolarity = 0;


}