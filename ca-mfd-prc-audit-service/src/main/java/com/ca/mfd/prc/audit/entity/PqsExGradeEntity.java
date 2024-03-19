package com.ca.mfd.prc.audit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 精致工艺扣分等级配置实体
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "精致工艺扣分等级配置")
@TableName("PRC_PQS_EX_GRADE")
public class PqsExGradeEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_EX_GRADE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 等级编码
     */
    @Schema(title = "等级编码")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 等级名称
     */
    @Schema(title = "等级名称")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 分类标准
     */
    @Schema(title = "分类标准")
    @TableField("CLASSIFICATION")
    private String classification = StringUtils.EMPTY;


    /**
     * 扣分
     */
    @Schema(title = "扣分")
    @TableField("SCORE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer score = 0;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    @TableField(exist = false)
    private boolean dataCheck = true;

}