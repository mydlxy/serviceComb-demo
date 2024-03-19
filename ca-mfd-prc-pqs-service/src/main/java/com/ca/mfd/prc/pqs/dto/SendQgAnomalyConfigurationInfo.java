package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-17-20:08
 * @Description:
 */
@Data
@Schema(description = "QG岗位缺陷配置模型")
public class SendQgAnomalyConfigurationInfo {
    /**
     * id
     */
    @Schema(title = "id")
    private Long id;

    /**
     * 名称
     */
    @Schema(title = "名称")
    private String name;

    /**
     * 型号
     */
    @Schema(title = "型号")
    private String model;

    /**
     * 图片
     */
    @Schema(title = "图片")
    private String image;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 岗位集合
     */
    @Schema(title = "工位集合")
    private List<String> workPlaceIds = new ArrayList<>();

    /**
     * 色块集合
     */
    @Schema(title = "色块集合")
    private List<GateBlank> gateBlanks = new ArrayList<>();
}
