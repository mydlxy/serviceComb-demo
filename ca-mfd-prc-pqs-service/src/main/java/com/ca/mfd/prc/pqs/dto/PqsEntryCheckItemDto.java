package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 检验项列表
 *
 * @Author: joel
 * @Date: 2023-04-13-15:26
 * @Description:
 */
@Data
@Schema(description = "检验项列表")
public class PqsEntryCheckItemDto {

    /**
     * 主键ID
     */
    @Schema(title = "主键ID")
    private Long id = Constant.DEFAULT_ID;

    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    private Integer displayNo = 0;

    /**
     * 检验项分组
     */
    @Schema(title = "检验项分组")
    private String groupName = StringUtils.EMPTY;

    /**
     * 工单号
     */
    @Schema(title = "工单号")
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 检验标准
     */
    @Schema(title = "检验标准")
    private String standard = StringUtils.EMPTY;

    /**
     * 检查项代码
     */
    @Schema(title = "检查项代码")
    private String itemCode = StringUtils.EMPTY;

    /**
     * 检查项名称
     */
    @Schema(title = "检查项名称")
    private String itemName = StringUtils.EMPTY;

    /**
     * 结果类型
     */
    @Schema(title = "结果类型")
    private String itemTypeCode = StringUtils.EMPTY;

    /**
     * 结果类型描述
     */
    @Schema(title = "结果类型描述")
    private String itemTypeName = StringUtils.EMPTY;

    /**
     * 检验结果值
     */
    @Schema(title = "检验结果值")
    private String checkValue = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * 目标值
     */
    @Schema(title = "目标值")
    private String target = StringUtils.EMPTY;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    private String lowerLimit = StringUtils.EMPTY;
    /**
     * 上限值
     */
    @Schema(title = "上限值")
    private String upperLimit = StringUtils.EMPTY;
    /**
     * 单位
     */
    @Schema(title = "单位")
    private String unit = StringUtils.EMPTY;

    /**
     * 判定结果
     */
    private Integer checkResult;
}
