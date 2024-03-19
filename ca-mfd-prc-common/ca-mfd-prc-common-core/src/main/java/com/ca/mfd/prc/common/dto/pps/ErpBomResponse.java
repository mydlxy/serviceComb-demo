package com.ca.mfd.prc.common.dto.pps;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ErpBomOrCharacter
 *
 * @author inkelink eric.zhou
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class ErpBomResponse {

    @Schema(title = "整车物料号或者自制件物料号")
    @JsonProperty("ProductNO")
    private String productNo;
    @Schema(title = "BOM版本")
    @JsonProperty("VERSION")
    private String version;
    @Schema(title = "层级")
    @JsonProperty("Level")
    private String level;
    @Schema(title = "父物料号")
    @JsonProperty("ProductMaterialNo")
    private String productMaterialNo;
    @Schema(title = "零部件号（子物料号）")
    @JsonProperty("MaterialNo")
    private String materialNo;
    @Schema(title = "中文名称")
    @JsonProperty("MaterialCN")
    private String materialCn;
    @Schema(title = "用量")
    @JsonProperty("Quantity")
    private BigDecimal quantity;
    @Schema(title = "单位")
    @JsonProperty("Unit")
    private String unit;
    @Schema(title = "来源")
    @JsonProperty("From")
    private String from;
    @Schema(title = "去向")
    @JsonProperty("HQ")
    private String hq;
    @Schema(title = "MP标识  采购，自制")
    @JsonProperty("MPType")
    private String mpType;
    @Schema(title = "物料类型  大包件、看板件")
    @JsonProperty("MaterialType")
    private String materialType;
    @Schema(title = "零件名称英文")
    @JsonProperty("MaterialEn")
    private String materialEn;
    @Schema(title = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonProperty("StartDate")
    private Date startDate;
    @Schema(title = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonProperty("EndDate")
    private Date endDate;
    @Schema(title = "物料颜色")
    @JsonProperty("BodyColor")
    private String bodyColor;
    @Schema(title = "备注")
    @JsonProperty("Memo")
    private String memo;

    public ErpBomResponse() {
        this.productNo = "";
        this.version = "";
        this.level = "";
        this.productMaterialNo = "";
        this.materialNo = "";
        this.unit = "";
        this.from = "";
        this.hq = "";

        this.mpType = "";
        this.materialType = "";
        this.materialEn = "";
        this.bodyColor = "";
        this.memo = "";
    }

}
