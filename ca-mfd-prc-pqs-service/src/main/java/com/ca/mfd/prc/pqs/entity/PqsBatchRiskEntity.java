package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 批次件问题排查（质量围堵）实体
 * @author inkelink
 * @date 2023年11月08日
 * @变更说明 BY inkelink At 2023年11月08日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "批次件问题排查（质量围堵）")
@TableName("PRC_PQS_BATCH_RISK")
public class PqsBatchRiskEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_BATCH_RISK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 风险问题补充说明
     */
    @Schema(title = "风险问题补充说明")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 发起人
     */
    @Schema(title = "发起人")
    @TableField("START_BY")
    private String startBy = StringUtils.EMPTY;


    /**
     * 发起时间
     */
    @Schema(title = "发起时间")
    @TableField("START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDt = new Date();


    /**
     * 关闭人
     */
    @Schema(title = "关闭人")
    @TableField("CLOSE_BY")
    private String closeBy = StringUtils.EMPTY;


    /**
     * 关闭时间
     */
    @Schema(title = "关闭时间")
    @TableField("CLOSE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date closeDt;


    /**
     * 关闭说明
     */
    @Schema(title = "关闭说明")
    @TableField("CLOSE_REMARK")
    private String closeRemark = StringUtils.EMPTY;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 是否发送LMS
     */
    @Schema(title = "是否发送LMS")
    @TableField("IS_SEND_LMS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSendLms = false;
}