package com.ca.mfd.prc.common.dto.pps;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单返回
 *
 * @author inkelink eric.zhou
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "订单返回")
public class OrderModelResponse {

    @Schema(title = "计划数量")
    @JsonProperty("QTY")
    public int planQty;
    @Schema(title = "预计上线时间")
    @JsonProperty("PLANDATE")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    public Date estimatedStartDt;
    @Schema(title = "销售订单防冻液备注")
    @JsonProperty("FDY")
    public Object fdyRemark;
    @Schema(title = "comp")
    private String comp;
    @Schema(title = "生产订单号")
    @JsonProperty("WOORNO")
    private String orderNo;
    @Schema(title = "车型代码")
    @JsonProperty("MATERIAL")
    private String materialNo;
    @Schema(title = "生产订单入库仓库")
    @JsonProperty("WAREHOUSE")
    private String wareHouse;

    @Schema(title = "对应的底盘号或批次号")
    @JsonProperty("CLOT")
    private String sn;

    @Schema(title = "出口国家")
    @JsonProperty("COUNTRY")
    private Object country;

    @Schema(title = "DMS订单号")
    @JsonProperty("DMSORNO")
    private Object dmsOrderNd;

    @Schema(title = "销售订单备注")
    @JsonProperty("BZ")
    private Object remark;

    @Schema(title = "对应的销售订单号")
    @JsonProperty("SALEORNO")
    private String saleorNo;

    public OrderModelResponse() {

        this.orderNo = "";
        this.materialNo = "";
        this.saleorNo = "";
        this.remark = "";
        this.dmsOrderNd = "";
        this.country = "";
        this.sn = "";
        this.wareHouse = "";
        this.fdyRemark = "";
    }

}
