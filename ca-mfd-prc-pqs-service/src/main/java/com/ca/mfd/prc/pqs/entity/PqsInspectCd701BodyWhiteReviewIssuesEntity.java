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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: CD701白车身评审问题实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "CD701白车身评审问题")
@TableName("PRC_PQS_INSPECT_CD701_BODY_WHITE_REVIEW_ISSUES")
public class PqsInspectCd701BodyWhiteReviewIssuesEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_CD701_BODY_WHITE_REVIEW_ISSUES_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 评审日期
     */
    @Schema(title = "评审日期")
    @TableField("REVIEW_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date reviewDate;


    /**
     * VIN
     */
    @Schema(title = "VIN")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 技术员
     */
    @Schema(title = "技术员")
    @TableField("TECHNICIAN")
    private String technician = StringUtils.EMPTY;


    /**
     * 评审人
     */
    @Schema(title = "评审人")
    @TableField("REVIEWER")
    private String reviewer = StringUtils.EMPTY;


    /**
     * 问题描述
     */
    @Schema(title = "问题描述")
    @TableField("PROBLEM_DESCRIPTION")
    private String problemDescription = StringUtils.EMPTY;


    /**
     * 问题回复
     */
    @Schema(title = "问题回复")
    @TableField("QUESTION_RESPONSE")
    private String questionResponse = StringUtils.EMPTY;


    /**
     * 牵头部门
     */
    @Schema(title = "牵头部门")
    @TableField("LEADING_DEPARTMENT")
    private String leadingDepartment = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}