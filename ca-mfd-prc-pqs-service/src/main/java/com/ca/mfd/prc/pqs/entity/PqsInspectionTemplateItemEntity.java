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
 * @Description: 检验模板-项目实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"prcPqsInspectionTemplateId", "itemCode"})
@Schema(description = "检验模板-项目")
@TableName("PRC_PQS_INSPECTION_TEMPLATE_ITEM")
public class PqsInspectionTemplateItemEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_INSPECTION_TEMPLATE_ITEM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 模板标识
     */
    @Schema(title = "模板标识")
    @TableField("PRC_PQS_INSPECTION_TEMPLATE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsInspectionTemplateId = Constant.DEFAULT_ID;


    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;


    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 检验项类型
     */
    @Schema(title = "检验项类型")
    @TableField("ITEM_TYPE_CODE")
    private String itemTypeCode = StringUtils.EMPTY;


    /**
     * 检验项类型名称
     */
    @Schema(title = "检验项类型名称")
    @TableField("ITEM_TYPE_NAME")
    private String itemTypeName = StringUtils.EMPTY;


    /**
     * 检验标准
     */
    @Schema(title = "检验标准")
    @TableField("STANDARD")
    private String standard = StringUtils.EMPTY;


    /**
     * 检查项代码
     */
    @Schema(title = "检查项代码")
    @TableField("ITEM_CODE")
    private String itemCode = StringUtils.EMPTY;


    /**
     * 检查项名称
     */
    @Schema(title = "检查项名称")
    @TableField("ITEM_NAME")
    private String itemName = StringUtils.EMPTY;


    /**
     * 目标值
     */
    @Schema(title = "目标值")
    @TableField("TARGET")
    private String target = StringUtils.EMPTY;


    /**
     * 下限值
     */
    @Schema(title = "下限值")
    @TableField("LOWER_LIMIT")
    private String lowerLimit = StringUtils.EMPTY;


    /**
     * 上限值
     */
    @Schema(title = "上限值")
    @TableField("UPPER_LIMIT")
    private String upperLimit = StringUtils.EMPTY;


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    @TableField(exist = false)
    private boolean dataCheck = true;

}