package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 关重参数信息
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "关重参数信息")
public class ParameterItem {

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    private String equipment;

    /**
     * 岗位
     */
    @Schema(title = "工位")
    private String acquisitionstation;

    /**
     * 时间
     */
    @Schema(title = "时间")
    private String acquisitiontime;

    /**
     * 数据项集合
     */
    @Schema(title = "数据项集合")
    private List<List<ParameterData>> datas = new ArrayList<>();

}
