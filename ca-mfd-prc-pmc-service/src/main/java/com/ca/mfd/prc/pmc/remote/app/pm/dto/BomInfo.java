package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class BomInfo {

    @Schema(title = "物料号")
    private String materialNo = StringUtils.EMPTY;
    @Schema(title = "物料中文名")
    private String materialCn = StringUtils.EMPTY;
    @Schema(title = "物料英文名")
    private String materialEn = StringUtils.EMPTY;
    @Schema(title = "数量")
    private BigDecimal quantity = BigDecimal.valueOf(0);
    @Schema(title = "单位")
    private String unit = StringUtils.EMPTY;
    @Schema(title = "工厂代码")
    private String orgCode = StringUtils.EMPTY;
    @Schema(title = "车间代码")
    private String shopCode = StringUtils.EMPTY;
    @Schema(title = "线体代码")
    private String lineCode = StringUtils.EMPTY;
    @Schema(title = "工位代码")
    private String stationCode = StringUtils.EMPTY;
    @Schema(title = "重量")
    private BigDecimal weight = BigDecimal.valueOf(0);
    @Schema(title = "重量单位")
    private String weightUnit = StringUtils.EMPTY;
    @Schema(title = "每包数量")
    private BigDecimal packageCount = BigDecimal.valueOf(0);
    @Schema(title = "行为  I U D")
    private String action = StringUtils.EMPTY;

    public BomInfo() {
        this.filterEmpty();
    }

    /**
     * 对空字段过滤
     */
    public BomInfo filterEmpty() {
        if (this.materialNo == null) {
            this.materialNo = "";
        }
        if (this.materialCn == null) {
            this.materialCn = "";
        }
        if (this.materialEn == null) {
            this.materialEn = "";
        }
        if (this.unit == null) {
            this.unit = "";
        }
        if (this.orgCode == null) {
            this.orgCode = "";
        }
        if (this.shopCode == null) {
            this.shopCode = "";
        }
        if (this.lineCode == null) {
            this.lineCode = "";
        }
        if (this.stationCode == null) {
            this.stationCode = "";
        }
        if (this.weightUnit == null) {
            this.weightUnit = "";
        }
        if (this.action == null) {
            this.action = "";
        }

        if (this.quantity == null) {
            this.quantity = BigDecimal.valueOf(0);
        }
        if (this.weight == null) {
            this.weight = BigDecimal.valueOf(0);
        }
        if (this.packageCount == null) {
            this.packageCount = BigDecimal.valueOf(0);
        }
        return this;
    }
}