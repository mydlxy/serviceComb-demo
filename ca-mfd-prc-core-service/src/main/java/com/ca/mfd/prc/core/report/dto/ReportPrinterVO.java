package com.ca.mfd.prc.core.report.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * ReportPrinterVO class
 *
 * @author luowenbing
 * @date 2023/09/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ReportPrinterVO")
public class ReportPrinterVO implements Serializable {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id;

    @Schema(title = "打印机名称")
    @JsonAlias(value = {"printName", "printName"})
    private String printName = StringUtils.EMPTY;

    @Schema(title = "IP")
    @JsonAlias(value = {"ip", "Ip"})
    private String ip = StringUtils.EMPTY;

    @Schema(title = "Model")
    @JsonAlias(value = {"model", "Model"})
    private String model = StringUtils.EMPTY;
}
