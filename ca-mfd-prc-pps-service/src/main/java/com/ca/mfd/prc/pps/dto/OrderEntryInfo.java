package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 车间订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "OrderEntryInfo", description = "")
public class OrderEntryInfo {

    @Schema(description = "工单ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    @Schema(description = "计划订单号")
    private String planNo;

    @Schema(description = "订单id关联BOM与特征用")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long orderId = Constant.DEFAULT_ID;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "型号")
    private String model;

    @Schema(description = "工单状态 1 未上线  2 锁定  3正在生成  4 已经完成   5 报废")
    private Integer status = 0;

    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
    private String orderCategory = StringUtils.EMPTY;

    @Schema(description = "工单顺序号")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    @Schema(description = "SN")
    private String sn;

    @Schema(description = "条码")
    private String barcode;

    @Schema(description = " 订阅码")
    private String subScriubCode;

    @Schema(description = " 生产顺序号(除开整车其他产品使用)")
    private String productionNo = StringUtils.EMPTY;

    @Schema(description = " 焊装生产顺序号")
    private String bodyNo = StringUtils.EMPTY;

    @Schema(description = " 涂装生产顺序号")
    private String paintNo = StringUtils.EMPTY;

    @Schema(description = " 总装生产顺序号")
    private String assemblyNo = StringUtils.EMPTY;

    @Schema(description = "工单号")
    private String entryNo;

    @Schema(description = "工单类别")
    private Integer entryType = 0;

    @Schema(description = "工单物料")
    private String materialNo;

    @Schema(description = "产品编码")
    private String productMaterialNo;

    @Schema(description = "特征1")
    private String characteristic1;

    @Schema(description = "特征2")
    private String characteristic2;

    @Schema(description = "特征3")
    private String characteristic3;

    @Schema(description = "特征4")
    private String characteristic4;

    @Schema(description = "特征5")
    private String characteristic5;

    @Schema(description = "特征6")
    private String characteristic6;

    @Schema(description = "特征7")
    private String characteristic7;

    @Schema(description = "特征8")
    private String characteristic8;

    @Schema(description = "特征9")
    private String characteristic9;

    @Schema(description = "特征10")
    private String characteristic10;

    @Schema(description = "选装包")
    private String attribute1;

    @Schema(description = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date estimatedStartDt;

    @Schema(description = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date estimatedEndDt;

    @Schema(description = "实际上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date actualStartDt;

    @Schema(description = "实际下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date actualEndDt;

    @Schema(description = "车间代码")
    private String workshopCode;

    @Schema(description = "生产线代码")
    private String areaCode;

    @Schema(description = "更新时间(订单)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date lastUpdateDate;

    @Schema(description = "更新时间(工单)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date entryUpdateDate;
}
