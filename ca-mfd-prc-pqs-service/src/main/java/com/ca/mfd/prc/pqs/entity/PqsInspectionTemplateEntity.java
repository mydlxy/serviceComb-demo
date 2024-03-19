package com.ca.mfd.prc.pqs.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 检验模板管理实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "templateCode")
@Schema(description = "检验模板管理")
@TableName("PRC_PQS_INSPECTION_TEMPLATE")
public class PqsInspectionTemplateEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_INSPECTION_TEMPLATE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 模板代码
     */
    @Schema(title = "模板代码")
    @TableField("TEMPLATE_CODE")
    private String templateCode = StringUtils.EMPTY;


    /**
     * 模板名称
     */
    @Schema(title = "模板名称")
    @TableField("TEMPLATE_NAME")
    private String templateName = StringUtils.EMPTY;


    /**
     * 工序代码
     */
    @Schema(title = "工序代码")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;


    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 检验工单类型编码
     */
    @Schema(title = "检验工单类型编码")
    @TableField("ENTRY_TYPE")
    private String entryType = StringUtils.EMPTY;


    /**
     * 检验工单类型描述
     */
    @Schema(title = "检验工单类型描述")
    @TableField("ENTRY_TYPE_DESC")
    private String entryTypeDesc = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled = false;

    /**
     * 模板备注
     */
    @Schema(title = "模板备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField(exist = false)
    private String groupName = StringUtils.EMPTY;


    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    @TableField(exist = false)
    private Integer displayNo1 = 0;


    /**
     * 检验项类型
     */
    @Schema(title = "检验项类型")
    @TableField(exist = false)
    private String itemTypeCode = StringUtils.EMPTY;


    /**
     * 检验项类型名称
     */
    @Schema(title = "检验项类型名称")
    @TableField(exist = false)
    private String itemTypeName = StringUtils.EMPTY;


    /**
     * 检验标准
     */
    @Schema(title = "检验标准")
    @TableField(exist = false)
    private String standard = StringUtils.EMPTY;


    /**
     * 检查项代码
     */
    @Schema(title = "检查项代码")
    @TableField(exist = false)
    private String itemCode = StringUtils.EMPTY;


    /**
     * 检查项名称
     */
    @Schema(title = "检查项名称")
    @TableField(exist = false)
    private String itemName = StringUtils.EMPTY;


    /**
     * 目标值
     */
    @Schema(title = "目标值")
    @TableField(exist = false)
    private String target = StringUtils.EMPTY;


    /**
     * 下限值
     */
    @Schema(title = "下限值")
    @TableField(exist = false)
    private String lowerLimit = StringUtils.EMPTY;


    /**
     * 上限值
     */
    @Schema(title = "上限值")
    @TableField(exist = false)
    private String upperLimit = StringUtils.EMPTY;


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField(exist = false)
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField(exist = false)
    private String remark1 = StringUtils.EMPTY;

    @TableField(exist = false)
    private boolean dataCheck = true;
}