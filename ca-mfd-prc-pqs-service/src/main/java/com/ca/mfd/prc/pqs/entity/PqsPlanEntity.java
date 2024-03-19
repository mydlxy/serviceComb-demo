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

import java.math.BigDecimal;

/**
 * @author inkelink
 * @Description: 检验计划配置实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检验计划配置")
@TableName("PRC_PQS_PLAN")
public class PqsPlanEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PLAN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    private String isEnabled = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 检验单类型编码
     */
    @Schema(title = "检验单类型编码")
    @TableField("INSPECTION_ENTRY_TYPE")
    private String inspectionEntryType = StringUtils.EMPTY;


    /**
     * 检验单类型描述
     */
    @Schema(title = "检验单类型描述")
    @TableField("INSPECTION_ENTRY_TYPE_DESCPRITION")
    private String inspectionEntryTypeDescprition = StringUtils.EMPTY;


    /**
     * 触发类型
     */
    @Schema(title = "触发类型")
    @TableField("TRIGGER_TYPE")
    private Integer triggerType = 0;


    /**
     * 触发值
     */
    @Schema(title = "触发值")
    @TableField("TRIGGER_VALUE")
    private BigDecimal triggerValue = BigDecimal.valueOf(0);


    /**
     * 当前值
     */
    @Schema(title = "当前值")
    @TableField("CURRENT_VALUE")
    private BigDecimal currentValue = BigDecimal.valueOf(0);


    /**
     * 下次触发条件
     */
    @Schema(title = "下次触发条件")
    @TableField("NEXT_TRIGGER_CONDITION")
    private String nextTriggerCondition = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}