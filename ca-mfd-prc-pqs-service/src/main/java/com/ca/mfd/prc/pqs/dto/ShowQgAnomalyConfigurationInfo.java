package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * QG岗位缺陷配置模型
 *
 * @Author: joel
 * @Date: 2023-04-17-18:30
 * @Description:
 */
@Data
@Schema(description = "QG岗位缺陷配置模型")
public class ShowQgAnomalyConfigurationInfo {
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
     * 是否启用九宫格
     */
    @Schema(title = "是否启用九宫格")
    private Boolean isLayout;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 岗位集合
     */
    @Schema(title = "岗位集合")
    private List<ComboInfoDTO> workPlaces = new ArrayList<>();

    /**
     * 色块集合
     */
    @Schema(title = "色块集合")
    private List<GateBlankInfo> gateBlanks = new ArrayList<>();
}
