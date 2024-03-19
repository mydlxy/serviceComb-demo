package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 接收QMS的组件格式
 *
 * @Author: joel
 * @Date: 2023-04-25-16:16
 * @Description:
 */
@Data
@Schema(description = "接收QMS的组件格式")
public class QmsComponent {
    /**
     * 一级部件名称
     */
    @Schema(title = "一级部件名称")
    private String firstcomponentname;

    private List<QmsComponentData> items;
}
