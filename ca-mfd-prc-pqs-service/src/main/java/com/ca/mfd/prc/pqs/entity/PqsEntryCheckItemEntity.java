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
 * @Description: 工单检验项实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工单检验项")
@TableName("PRC_PQS_ENTRY_CHECK_ITEM")
public class PqsEntryCheckItemEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_CHECK_ITEM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;


    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;


    /**
     * 检验标准
     */
    @Schema(title = "检验标准")
    @TableField("STANDARD")
    private String standard = StringUtils.EMPTY;

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
     * 检验结果;0 未检验 1 合格 2不合格
     */
    @Schema(title = "检验结果;0 未检验 1 合格 2不合格")
    @TableField("CHECK_VALUE")
    private String checkValue = StringUtils.EMPTY;

    /**
     * 检验结论
     */
    @Schema(title = "检验结论")
    @TableField("CHECK_RESULT")
    private Integer checkResult = 0;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}