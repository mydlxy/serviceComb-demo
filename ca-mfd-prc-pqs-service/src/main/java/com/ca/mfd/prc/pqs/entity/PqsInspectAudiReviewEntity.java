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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 车间内部奥迪特评审报告实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "车间内部奥迪特评审报告")
@TableName("PRC_PQS_INSPECT_AUDI_REVIEW")
public class PqsInspectAudiReviewEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_AUDI_REVIEW_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 零件名称
     */
    @Schema(title = "零件名称")
    @TableField("MATERINAL_NAME")
    private String materinalName = StringUtils.EMPTY;


    /**
     * 问题现象
     */
    @Schema(title = "问题现象")
    @TableField("PROBLEM_PHENOMENON")
    private String problemPhenomenon = StringUtils.EMPTY;


    /**
     * 分值
     */
    @Schema(title = "分值")
    @TableField("SCORE")
    private BigDecimal score = BigDecimal.valueOf(0);


    /**
     * 现场图片
     */
    @Schema(title = "现场图片")
    @TableField("PHOTOS")
    private String photos = StringUtils.EMPTY;


    /**
     * 评审日期
     */
    @Schema(title = "评审日期")
    @TableField("REVIEW_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date reviewDate;


    /**
     * 评审人
     */
    @Schema(title = "评审人")
    @TableField("REVIEWER")
    private String reviewer = StringUtils.EMPTY;


    /**
     * 责任单位班组
     */
    @Schema(title = "责任单位班组")
    @TableField("RESPONSIBLE_UNIT_TEAM")
    private String responsibleUnitTeam = StringUtils.EMPTY;


    /**
     * 是否返修/翻库
     */
    @Schema(title = "是否返修/翻库")
    @TableField("REPAIR_FLAG")
    private Boolean repairFlag = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}