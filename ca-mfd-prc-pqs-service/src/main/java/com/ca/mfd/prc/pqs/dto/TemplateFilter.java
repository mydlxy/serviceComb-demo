package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 模板筛选
 *
 * @Author: joel
 * @Date: 2023-08-13-20:52
 * @Description:
 */
@Data
@Schema(description = "模板筛选")
public class TemplateFilter {
    /**
     * 物料号
     */
    @Schema(title = "物料号")
    private String materialNo = StringUtils.EMPTY;
    /**
     * 工单类型
     */
    @Schema(title = "工单类型")
    private String entryType = StringUtils.EMPTY;
    /**
     * 工序代码
     */
    @Schema(title = "工序代码")
    private String processCode = StringUtils.EMPTY;
}
