package com.ca.mfd.prc.eps.entity;

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
 * @author inkelink ${email}
 * @Description: 装配指示配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "装配指示配置")
@TableName("PRC_EPS_ASSEMBLE_INDICATE_CONFIG")
public class EpsAssembleIndicateConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_ASSEMBLE_INDICATE_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间名
     */
    @Schema(title = "车间名")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间ID
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体名
     */
    @Schema(title = "线体名")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 线体ID
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 工位名
     */
    @Schema(title = "工位名")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 工位ID
     */
    @Schema(title = "工位编码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("SEQUENCE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sequenceNum = 0;

    /**
     * 指示内容
     */
    @Schema(title = "指示内容")
    @TableField("OPER_CONTENT")
    private String operContent = StringUtils.EMPTY;

    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;

    /**
     * 图片
     */
    @Schema(title = "图片")
    @TableField("OPER_IMG")
    private String operImg = StringUtils.EMPTY;

}
