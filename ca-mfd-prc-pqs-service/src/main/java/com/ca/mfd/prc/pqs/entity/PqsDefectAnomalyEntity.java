package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 组合缺陷库实体
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"defectAnomalyCode"})
@Schema(description = "组合缺陷库")
@TableName("PRC_PQS_DEFECT_ANOMALY")
public class PqsDefectAnomalyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_DEFECT_ANOMALY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 组合代码
     */
    @Schema(title = "组合代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    @TableField("RESPONSIBLE_DEPT_CODE")
    private String responsibleDeptCode = StringUtils.EMPTY;


    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    @TableField("RESPONSIBLE_DEPT_NAME")
    private String responsibleDeptName = StringUtils.EMPTY;


    /**
     * 缺陷等级代码
     */
    @Schema(title = "缺陷等级代码")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    @TableField("COMPONENT_DESCRIPTION")
    private String componentDescription = StringUtils.EMPTY;


    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    @TableField("DEFECT_CODE_CODE")
    private String defectCodeCode = StringUtils.EMPTY;


    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    @TableField("DEFECT_CODE_DESCRIPTION")
    private String defectCodeDescription = StringUtils.EMPTY;


    /**
     * 位置代码
     */
    @Schema(title = "位置代码")
    @TableField("POSITION_CODE")
    private String positionCode = StringUtils.EMPTY;


    /**
     * 位置描述
     */
    @Schema(title = "位置描述")
    @TableField("POSITION_DESCRIPTION")
    private String positionDescription = StringUtils.EMPTY;


    /**
     * 数据来源;0:MES 其他：其他系统
     */
    @Schema(title = "数据来源;0:MES 其他：其他系统")
    @TableField("SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;


    /**
     * 拼音简码
     */
    @Schema(title = "拼音简码")
    @TableField("SHORT_CODE")
    private String shortCode = StringUtils.EMPTY;

    /**
     * 数据校验
     */
    @TableField(exist = false)
    private boolean dataCheck = true;
}