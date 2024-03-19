package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 各车间数据监控内容实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "各车间数据监控内容")
@TableName("PRC_PQS_INSPECT_CONTENT")
public class PqsInspectContentEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_CONTENT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间
     */
    @Schema(title = "车间")
    @TableField("WORK_SHOP_CODE")
    private String workShopCode = StringUtils.EMPTY;


    /**
     * 要求来源
     */
    @Schema(title = "要求来源")
    @TableField("REQUIRED_SOURCE")
    private String requiredSource = StringUtils.EMPTY;


    /**
     * 要求内容
     */
    @Schema(title = "要求内容")
    @TableField("REQUIRED_CONTENT")
    private String requiredContent = StringUtils.EMPTY;


    /**
     * 采集方式1
     */
    @Schema(title = "采集方式1")
    @TableField("COLLECT_METHOD_1")
    private String collectMethod1 = StringUtils.EMPTY;


    /**
     * 采集方式2
     */
    @Schema(title = "采集方式2")
    @TableField("COLLECT_METHOD_2")
    private String collectMethod2 = StringUtils.EMPTY;


    /**
     * 数据属性
     */
    @Schema(title = "数据属性")
    @TableField("DATA_ATTRIBUTES")
    private String dataAttributes = StringUtils.EMPTY;


    /**
     * 采集内容
     */
    @Schema(title = "采集内容")
    @TableField("COLLECT_CONTENT")
    private String collectContent = StringUtils.EMPTY;


    /**
     * CD701项目要求
     */
    @Schema(title = "CD701项目要求")
    @TableField("CD701_PROJECT_REQUIREMENTS")
    private String cd701ProjectRequirements = StringUtils.EMPTY;


    /**
     * 数据类型
     */
    @Schema(title = "数据类型")
    @TableField("DATA_TYPE")
    private String dataType = StringUtils.EMPTY;


    /**
     * 应用方式
     */
    @Schema(title = "应用方式")
    @TableField("APPLICATION_METHODS")
    private String applicationMethods = StringUtils.EMPTY;


    /**
     * 示例
     */
    @Schema(title = "示例")
    @TableField("EXAMPLE")
    private String example = StringUtils.EMPTY;


    /**
     * IOT是否采集
     */
    @Schema(title = "IOT是否采集")
    @TableField("IOT_COLLECT_FLAG")
    private Boolean iotCollectFlag = false;


}