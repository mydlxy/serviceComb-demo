package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 关重参数信息
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "关重参数信息")
public class PartsItem {

    /**
     * 是否3C件 0 否  1 是
     */
    @Schema(title = "是否3C件")
    private Integer cCCpieces = 0;
    /**
     * 产生线体名称
     */
    @Schema(title = "产生线体名称")
    private String scanline;
    /**
     * 关重件条码
     */
    @Schema(title = "关重件条码")
    private String barCode;
    /**
     * 零件类别
     */
    @Schema(title = "零件类别")
    private String partscategory;
    /**
     * 零件号
     */
    @Schema(title = "零件号")
    private String partsnumber;
    /**
     * 序列号
     */
    @Schema(title = "序列号")
    private String serialnumber;
    /**
     * 批次号
     */
    @Schema(title = "批次号")
    private String batchnumber;
    /**
     * 组件
     */
    @Schema(title = "组件")
    private String assembly;
    /**
     * 采集工位
     */
    @Schema(title = "采集工位")
    private String acquisitionstation;

    /**
     * 采集人
     */
    @Schema(title = "采集人")
    private String gatherer;

    /**
     * 采集时间
     */
    @Schema(title = "采集时间")
    private String acquisitiontime;

}
