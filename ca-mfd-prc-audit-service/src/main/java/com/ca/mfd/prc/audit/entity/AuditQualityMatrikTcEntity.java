package com.ca.mfd.prc.audit.entity;

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
 * @Description: AUDIT百格图-车型实体
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "AUDIT百格图-车型")
@TableName("PRC_AUDIT_QUALITY_MATRIK_TC")
public class AuditQualityMatrikTcEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AUDIT_QUALITY_MATRIK_TC_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 百格图ID
     */
    @Schema(title = "百格图ID")
    @TableField("PRC_AUDIT_QUALITY_MATRIK_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcAuditQualityMatrikId = Constant.DEFAULT_ID;


    /**
     * 车型代码
     */
    @Schema(title = "车型代码")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 车型名称
     */
    @Schema(title = "车型名称")
    @TableField("MODEL_NAME")
    private String modelName = StringUtils.EMPTY;


}