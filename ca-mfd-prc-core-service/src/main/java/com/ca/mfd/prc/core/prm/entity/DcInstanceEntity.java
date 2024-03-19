package com.ca.mfd.prc.core.prm.entity;

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
 * @Description: DC配置实例实体
 * @author inkelink
 * @date 2023年11月10日
 * @变更说明 BY inkelink At 2023年11月10日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "DC配置实例")
@TableName("PRC_DC_INSTANCE")
public class DcInstanceEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_DC_INSTANCE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 第一个数字类型
     */
    @Schema(title = "第一个数字类型")
    @TableField("FIRST_INT_FILE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer firstIntFile = 0;

    /**
     * 第二个数字类型
     */
    @Schema(title = "第二个数字类型")
    @TableField("SECOND_INT_FILE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer secondIntFile = 0;

    /**
     * 第一个字符串类型
     */
    @Schema(title = "第一个字符串类型")
    @TableField("FIRST_STRING_FILE")
    private String firstStringFile = StringUtils.EMPTY;

    /**
     * 第二个字符串类型
     */
    @Schema(title = "第二个字符串类型")
    @TableField("SECOND_STRING_FILE")
    private String secondStringFile = StringUtils.EMPTY;

    /**
     * 第一个下拉类型
     */
    @Schema(title = "第一个下拉类型")
    @TableField("FIRST_DOWN_FILE")
    private String firstDownFile = StringUtils.EMPTY;

    /**
     * 第二个下拉类型
     */
    @Schema(title = "第二个下拉类型")
    @TableField("SECOND_DOWN_FILE")
    private String secondDownFile = StringUtils.EMPTY;

    /**
     * 第一个时间类型
     */
    @Schema(title = "第一个时间类型")
    @TableField("FIRST_DATE_FILE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date firstDateFile;

    /**
     * 第二个时间类型
     */
    @Schema(title = "第二个时间类型")
    @TableField("SECOND_DATE_FILE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date secondDateFile;

    /**
     * 第一个布尔类型
     */
    @Schema(title = "第一个布尔类型")
    @TableField("FIRST_BOOL_FILE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean firstBoolFile = false;

    /**
     * 第二个布尔类型
     */
    @Schema(title = "第二个布尔类型")
    @TableField("SECOND_BOOL_FILE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean secondBoolFile = false;

    /**
     * 第一个图片类型
     */
    @Schema(title = "第一个图片类型")
    @TableField("FIRST_PIC_FILE")
    private String firstPicFile = StringUtils.EMPTY;

    /**
     * 第二个图片类型
     */
    @Schema(title = "第二个图片类型")
    @TableField("SECOND_PIC_FILE")
    private String secondPicFile = StringUtils.EMPTY;

}