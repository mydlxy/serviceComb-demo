package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
@Data
public class ShowQgWorkplaceAomalyDataInfo {

    /**
     * id
     */
    @Schema(title = "id")
    private Long id;

    /**
     * 图片
     */
    @Schema(title = "图片")
    private String image;


    private Boolean isLayout;

    /**
     * 色块集合
     */
    @Schema(title = "色块集合")
    private List<GateBlankInfo> gateBlanks;
}
