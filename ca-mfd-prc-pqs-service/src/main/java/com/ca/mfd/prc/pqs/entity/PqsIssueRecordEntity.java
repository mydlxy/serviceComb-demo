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
 * @Description: 问题预警记录实体
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "问题预警记录")
@TableName("PRC_PQS_ISSUE_RECORD")
public class PqsIssueRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ISSUE_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录编号
     */
    @Schema(title = "记录编号")
    @TableField("RECORD_NO")
    private String recordNo = StringUtils.EMPTY;


    /**
     * 策略编号
     */
    @Schema(title = "策略编号")
    @TableField("POLICY_NO")
    private String policyNo = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 分类标准
     */
    @Schema(title = "分类标准")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 组合缺陷代码
     */
    @Schema(title = "组合缺陷代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 计数
     */
    @Schema(title = "计数")
    @TableField("COUNTER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer counter = 0;


    /**
     * 是否发送QMS
     */
    @Schema(title = "是否发送QMS")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @TableField("SEND_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date sendDate = new Date();


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}