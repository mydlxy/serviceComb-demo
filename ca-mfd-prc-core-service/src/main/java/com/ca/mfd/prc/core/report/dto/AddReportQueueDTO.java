package com.ca.mfd.prc.core.report.dto;

import com.ca.mfd.prc.common.enums.PrintTargetType;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ReportQueueDTO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ReportQueueDTO")
public class AddReportQueueDTO implements Serializable {
    /**
     * 打印代码
     */
    @Schema(title = "打印代码")
    @JsonAlias(value = {"printCode", "PrintCode"})
    private String printCode = StringUtils.EMPTY;
    /**
     * 目标外键
     */
    @Schema(title = "目标外键")
    @JsonAlias(value = {"targetId", "TargetId"})
    private long targetId = 0;
    /**
     * 目标类型
     */
    @Schema(title = "打印代码")
    @JsonAlias(value = {"targetType", "TargetType"})
    private PrintTargetType targetType = PrintTargetType.UnKnown;
    /**
     * 打印时间
     */
    @Schema(title = "打印时间")
    @JsonAlias(value = {"printDt", "PrintDt"})
    private Long printDt;

    /**
     * 传递给报表的参数字典
     */
    @Schema(title = "传递给报表的参数字典")
    @JsonAlias(value = {"parameters", "Parameters"})
    private Map<String, String> parameters = new HashMap<>();
    /**
     * 备注，关键点
     */
    @Schema(title = "备注，关键点")
    @JsonAlias(value = {"remark", "Remark"})
    private String remark = StringUtils.EMPTY;
    /**
     * 打印数量,小于等于0代表按模板配置的数量打印，否则按设置的数量打印
     */
    @Schema(title = "打印数量,小于等于0代表按模板配置的数量打印，否则按设置的数量打印")
    @JsonAlias(value = {"printNumber", "PrintNumber"})
    private int printNumber = -1;
}
