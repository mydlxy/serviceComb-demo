package com.ca.mfd.prc.core.report.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * ReportPrintQueueDycVO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ReportPrintQueueDycVO")
public class ReportPrintQueueDycVO implements Serializable {
    /**
     * 打印机名称
     */
    @Schema(title = "打印机名称")
    @JsonAlias(value = {"printName", "PrintName"})
    public String printName;
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonAlias(value = {"id", "Id"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private long Id;
    /*
      打印机外键
     */
    @Schema(title = "打印机外键")
    @JsonAlias(value = {"peportPrinterId", "ReportPrinterId"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private long reportPrinterId;
    /**
     * 打印代码
     */
    @Schema(title = "打印代码")
    @JsonAlias(value = {"code", "Code"})
    private String code;
    /**
     * 打印机IP
     */
    @Schema(title = "打印机名称")
    @JsonAlias(value = {"ip", "Ip"})
    private String ip;

    /**
     * 打印机型号
     */
    @Schema(title = "打印机型号")
    @JsonAlias(value = {"model", "Model"})
    private String model;

    /**
     * 打印数量
     */
    @Schema(title = "打印数量")
    @JsonAlias(value = {"printNumber", "PrintNumber"})
    private Integer printNumber;

    /**
     * 打印时间
     */
    @Schema(title = "打印时间")
    @JsonAlias(value = {"printDt", "PrintDt"})
    private Date printDt;

    /**
     * 参数
     */
    @Schema(title = "参数")
    @JsonAlias(value = {"paramaters", "Paramaters"})
    private String paramaters;


    /**
     * 报表文件地址
     */
    @Schema(title = "报表文件地址")
    @JsonAlias(value = {"path", "Path"})
    private String path;

    /**
     * 打印数量
     */
    @Schema(title = "报表文件地址")
    @JsonAlias(value = {"printCount", "PrintCount"})
    private Integer printCount;

    /**
     * 打印内容
     */
    @Schema(title = "打印内容")
    @JsonAlias(value = {"data", "Data"})
    private PrintData data = new PrintData();
}
