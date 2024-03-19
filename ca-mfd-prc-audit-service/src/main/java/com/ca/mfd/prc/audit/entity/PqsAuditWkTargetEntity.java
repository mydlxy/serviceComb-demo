package com.ca.mfd.prc.audit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author inkelink
 * @Description: Audit质量周目标设置实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Audit质量周目标设置")
@TableName("PRC_PQS_AUDIT_WK_TARGET")
public class PqsAuditWkTargetEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId("PRC_PQS_AUDIT_WK_TARGET_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 模板类型代码
     */
    @Schema(title = "模板类型代码")
    @TableField("TEMPLATE_CODE")
    private String templateCode = StringUtils.EMPTY;


    /**
     * 模板类型
     */
    @Schema(title = "模板类型")
    @TableField("TEMPLATE_DESC")
    private String templateDesc = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    @TableField("DEPT_CODE")
    private String deptCode = StringUtils.EMPTY;


    /**
     * 责任部门描述
     */
    @Schema(title = "责任部门描述")
    @TableField("DEPT_NAME")
    private String deptName = StringUtils.EMPTY;


    /**
     * 年份
     */
    @Schema(title = "年份")
    @TableField("YEAR")
    private String year = StringUtils.EMPTY;


    /**
     * 周数
     */
    @Schema(title = "周数")
    @TableField("WEEK")
    private String week = StringUtils.EMPTY;


    /**
     * 目标扣分
     */
    @Schema(title = "目标扣分")
    @TableField("TARGET")
    private BigDecimal target = BigDecimal.valueOf(0);


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}