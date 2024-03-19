package com.ca.mfd.prc.core.report.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * PrintData class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrintData")
public class PrintData implements Serializable {

    /**
     * 打印类型1、DX，2、标记命令
     */
    @Schema(title = "打印类型1、DX，2、标记命令")
    @JsonAlias(value = {"type", "Type"})
    private int type = 1;

    /**
     * 打印内容
     */
    @Schema(title = "打印内容")
    @JsonAlias(value = {"content", "Content"})
    private String content = "";
}
