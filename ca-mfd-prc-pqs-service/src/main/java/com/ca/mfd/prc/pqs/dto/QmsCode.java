package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 缺陷
 *
 * @Author: joel
 * @Date: 2023-04-10-14:49
 * @Description:
 */
@Data
@Schema(description = "缺陷")
public class QmsCode {
    /**
     * 一级缺陷类别
     */
    @Schema(title = "一级缺陷类别")
    private String classIDefects;

    /**
     * 一级缺陷类别
     */
    @Schema(title = "一级缺陷类别")
    private String classIIDefects;

    /**
     * 缺陷项
     */
    @Schema(title = "缺陷项")
    private List<QmsCodeItem> childen;
}
