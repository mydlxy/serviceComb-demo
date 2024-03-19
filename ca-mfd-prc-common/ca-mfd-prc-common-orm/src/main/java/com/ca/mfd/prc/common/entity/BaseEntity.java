/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 * @Description: 基础实体类，所有实体都需要继承
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Data
public abstract class BaseEntity implements Serializable {


    /**
     * 是否可用, N表示不可用，Y表示可用
     */
    @Schema(title = "是否可用, N表示不可用，Y表示可用")
    @TableField("FLAG")
    @JsonIgnore
    private String flag = Constant.SYS_FLAGS_YES;

    /**
     * 是否删除
     */
    @Schema(title = "是否删除")
    @TableField("IS_DELETE")
    @JsonIgnore
    private Boolean isDelete = false;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long createdBy = Constant.DEFAULT_USER_ID;


    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @TableField(value = "CREATION_DATE", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();

    /**
     * 创建人名称
     */
    @Schema(title = "创建人名称")
    @TableField(value = "CREATED_USER", fill = FieldFill.INSERT)
    private String createdUser = StringUtils.EMPTY;

    /**
     * 最后更新人名称
     */
    @Schema(title = "最后更新人名称")
    @TableField(value = "LAST_UPDATED_USER", fill = FieldFill.INSERT_UPDATE)
    private String lastUpdatedUser = StringUtils.EMPTY;

    /**
     * 最后更新人员
     */
    @Schema(title = "最后更新人员")
    @TableField(value = "LAST_UPDATED_BY", fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long lastUpdatedBy = Constant.DEFAULT_USER_ID;

    /**
     * 最后更新时间
     */
    @Schema(title = "最后更新时间")
    @TableField(value = "LAST_UPDATE_DATE", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate = new Date();

    /**
     * 最后更新请求标识
     */
    @Schema(title = "最后更新请求标识")
    @TableField("LAST_UPDATED_TRACEID")
    private String lastUpdatedTraceid = StringUtils.EMPTY;

    /**
     * 预留字段1
     */
    @Schema(title = "预留字段1")
    @TableField("ATTRIBUTE1")
    private String attribute1 = StringUtils.EMPTY;

    /**
     * 预留字段2
     */
    @Schema(title = "预留字段2")
    @TableField("ATTRIBUTE2")
    private String attribute2 = StringUtils.EMPTY;

    /**
     * 预留字段3
     */
    @Schema(title = "预留字段3")
    @TableField("ATTRIBUTE3")
    private String attribute3 = StringUtils.EMPTY;

    /**
     * 预留字段4
     */
    @Schema(title = "预留字段4")
    @TableField("ATTRIBUTE4")
    private String attribute4 = StringUtils.EMPTY;

    /**
     * 预留字段5
     */
    @Schema(title = "预留字段5")
    @TableField("ATTRIBUTE5")
    private String attribute5 = StringUtils.EMPTY;

    /**
     * 预留字段6
     */
    @Schema(title = "预留字段6")
    @TableField("ATTRIBUTE6")
    private String attribute6 = StringUtils.EMPTY;

    /**
     * 预留字段7
     */
    @Schema(title = "预留字段7")
    @TableField("ATTRIBUTE7")
    private String attribute7 = StringUtils.EMPTY;

    /**
     * 预留字段8
     */
    @Schema(title = "预留字段8")
    @TableField("ATTRIBUTE8")
    private String attribute8 = StringUtils.EMPTY;

    /**
     * 预留字段9
     */
    @Schema(title = "预留字段9")
    @TableField("ATTRIBUTE9")
    private String attribute9 = StringUtils.EMPTY;

    /**
     * 预留字段10
     */
    @Schema(title = "预留字段10")
    @TableField("ATTRIBUTE10")
    private String attribute10 = StringUtils.EMPTY;
}