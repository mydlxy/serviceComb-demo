package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-17-19:18
 * @Description:
 */
@Data
public class InitConfigurationItemInfo {
    /**
     * 组件
     */
    @Schema(title = "组件")
    private ComboInfoDTO component = new ComboInfoDTO();

    /**
     * 位置
     */
    @Schema(title = "位置")
    private ComboInfoDTO position = new ComboInfoDTO();

    /**
     * 对应缺陷ID
     */
    @Schema(title = "对应缺陷ID")
    private List<String> anomalyIds = new ArrayList<>();
}
