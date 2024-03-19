package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 关重参数信息(数据项)
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "关重参数信息(数据项)")
public class ParameterData {

    /**
     * 参数名称
     */
    @Schema(title = "参数名称")
    private String field;

    /**
     * 参考值
     */
    @Schema(title = "参考值")
    private String referenceValue;

    /**
     * 实际值
     */
    @Schema(title = "实际值")
    private String value;

}
