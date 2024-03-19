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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 电池预成组下发模组实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池预成组下发模组")
@TableName("PRC_PPS_MODULE_ISSUE_MODULE")
public class PpsModuleIssueModuleEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_ISSUE_MODULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 主体外键
     */
    @Schema(title = "主体外键")
    @TableField("PRC_PPS_MODULE_ISSUE_MAIN_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcPpsModuleIssueMainId = Constant.DEFAULT_ID;


    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 电池型号
     */
    @Schema(title = "电池型号")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


    /**
     * 生产线体
     */
    @Schema(title = "生产线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 模组编码
     */
    @Schema(title = "模组编码")
    @TableField("MODULE_CODE")
    private String moduleCode = StringUtils.EMPTY;


//    /**
//     * 模组数量
//     */
//    @Schema(title = "模组数量")
//    @TableField("MODULE_NUM")
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Integer moduleNum = 0;


    /**
     * 下发状态 0 未下发 1 已下发 2 已完成
     */
    @Schema(title = "下发状态 0 未下发 1 已下发 2 已完成")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 模组生产顺序
     */
    @Schema(title = "模组生产顺序")
    @TableField("MODULE_PRO_SEQ")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer moduleProSeq = 0;

    /**
     * 模组入箱顺序
     */
    @Schema(title = "模组入箱顺序")
    @TableField("INBOUND_SEQ")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer InboundSeq = 0;

    /**
     * 入箱位置
     */
    @Schema(title = "入箱位置")
    @TableField("INBOUND_POSITION")
    private String inboundPosition = StringUtils.EMPTY;

    /**
     * 入箱角度
     */
    @Schema(title = "入箱角度")
    @TableField("INBOUND_ANGLE")
    private BigDecimal inboundAngle;

}