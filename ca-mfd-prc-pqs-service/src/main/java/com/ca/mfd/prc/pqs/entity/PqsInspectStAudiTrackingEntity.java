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
 * @Description: 冲压车间内部奥迪特评审问题跟踪表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "冲压车间内部奥迪特评审问题跟踪表")
@TableName("PRC_PQS_INSPECT_ST_AUDI_TRACKING")
public class PqsInspectStAudiTrackingEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_ST_AUDI_TRACKING_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


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
     * 原因
     */
    @Schema(title = "原因")
    @TableField("REASON")
    private String reason = StringUtils.EMPTY;


    /**
     * 措施
     */
    @Schema(title = "措施")
    @TableField("MEASURE")
    private String measure = StringUtils.EMPTY;


    /**
     * 时间节点
     */
    @Schema(title = "时间节点")
    @TableField("TIME_NODE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date timeNode;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATE")
    private String state = StringUtils.EMPTY;


}