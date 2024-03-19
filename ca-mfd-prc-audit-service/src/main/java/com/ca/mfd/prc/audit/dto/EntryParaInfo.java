package com.ca.mfd.prc.audit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class EntryParaInfo {
    /**
     * 排除代码
     */
    private String key;

    private String workstationCode;

    private String status;

    private Integer pageSize = 0;

    private Integer pageIndex = 0;

    /**
     * 类别:1、整车  5 冲压(零部件)
     */
    @Schema(title = "类别:1、整车  5 冲压(零部件)")
    private Integer category = 1;
}
