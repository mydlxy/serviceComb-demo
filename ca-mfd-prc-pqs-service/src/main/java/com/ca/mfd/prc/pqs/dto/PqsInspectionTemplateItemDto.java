package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 检验项模型
 *
 * @Author: joel
 * @Date: 2023-04-14-14:34
 * @Description:
 */
@Data
@Schema(description = "检验项模型")
public class PqsInspectionTemplateItemDto {
    /**
     * 主键ID
     */
    @Schema(title = "主键ID")
    private Long id = Constant.DEFAULT_ID;

    /**
     * 检验项目ID
     */
    @Schema(title = "检验项目ID")
    private Long inspectItemId = Constant.DEFAULT_ID;

    /**
     * 检验模板ID
     */
    @Schema(title = "检验模板ID")
    private Long inspectTemplateId = Constant.DEFAULT_ID;

    /**
     * 分组 --可编辑
     */
    @Schema(title = "分组 --可编辑")
    private String groupName;

    /**
     * 显示顺序 --可编辑
     */
    @Schema(title = "显示顺序 --可编辑")
    private Integer displayNo = 0;

    /**
     * 质量特性 --可编辑
     */
    @Schema(title = "质量特性 --可编辑")
    private String checkAttribute;

    /**
     * 检测方法 --可编辑 下拉
     */
    @Schema(title = "检测方法 --可编辑 下拉")
    private String checkWay = StringUtils.EMPTY;

    /**
     * 检测标准 --只读
     */
    @Schema(title = "检测标准 --只读")
    private String standard = StringUtils.EMPTY;

    /**
     * 检查项代码 --只读
     */
    @Schema(title = "检查项代码 --只读")
    private String code = StringUtils.EMPTY;

    /**
     * 检查项名称
     */
    @Schema(title = "检查项名称")
    private String name = StringUtils.EMPTY;

    /**
     * 结果类型
     */
    @Schema(title = "结果类型")
    private Integer resultType = 0;

    /**
     * 结果类型
     */
    @Schema(title = "结果类型")
    private String target;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    private String lowerLimit;

    /**
     * 上限值
     */
    @Schema(title = "上限值")
    private String upperLimit;

    /**
     * 单位
     */
    @Schema(title = "单位")
    private String unit;

    /**
     * 备注 --可编辑
     */
    @Schema(title = "备注 --可编辑")
    private String remark = StringUtils.EMPTY;
}
