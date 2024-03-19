package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 检验计划触发类型配置实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检验计划触发类型配置")
@TableName("PRC_PQS_PLAN_TRIGGER_TYPE")
public class PqsPlanTriggerTypeEntity extends BaseEntity {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @TableId(value = "PRC_PQS_PLAN_TRIGGER_TYPE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 类型编码
     */
    @Schema(title = "类型编码")
    @TableField("TRIGGER_TYPE_CODE")
    private String triggerTypeCode = StringUtils.EMPTY;


    /**
     * 类型名称
     */
    @Schema(title = "类型名称")
    @TableField("TRIGGER_TYPE_NAME")
    private String triggerTypeName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}