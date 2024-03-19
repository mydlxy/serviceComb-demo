package com.ca.mfd.prc.eps.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

/**
 * @author inkelink
 * @Description: 追溯组件配置实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯组件配置")
@TableName("PRC_PM_TRACE_COMPONENT")
public class PmTraceComponentEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_TRACE_COMPONENT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    @TableField("TRACE_COMPONENT_CODE")
    private String traceComponentCode = StringUtils.EMPTY;


    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    @TableField("TRACE_COMPONENT_DESCRIPTION")
    private String traceComponentDescription = StringUtils.EMPTY;


    /**
     * 是否关键件
     */
    @Schema(title = "是否关键件")
    @TableField("IS_KEY_PARTS")
    private Boolean isKeyParts = false;


    /**
     * 是否上传
     */
    @Schema(title = "是否上传")
    @TableField("ENABLED_UPLOAD")
    private Boolean enabledUpload = false;


    /**
     * 组件类型
     */
    @Schema(title = "组件类型")
    @TableField("CATEGORY")
    private String category;


    /**
     * 组件类型描述
     */
    @Schema(title = "组件类型描述")
    @TableField("CATEGORY_DESC")
    private String categoryDesc;


}