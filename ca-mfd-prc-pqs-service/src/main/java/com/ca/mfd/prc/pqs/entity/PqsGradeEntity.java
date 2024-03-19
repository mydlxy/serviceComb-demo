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
 * @Description: 缺陷等级配置实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"gradeCode"})
@Schema(description = "缺陷等级配置")
@TableName("PRC_PQS_GRADE")
public class PqsGradeEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_GRADE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
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
     * 分值
     */
    @Schema(title = "分值")
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

    /**
     * 数据校验
     */
    @TableField(exist = false)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private boolean dataCheck = true;
}