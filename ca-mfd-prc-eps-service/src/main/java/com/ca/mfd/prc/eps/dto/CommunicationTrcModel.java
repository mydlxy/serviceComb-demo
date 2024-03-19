package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * CommunicationTrcModel
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "CommunicationTrcModel")
public class CommunicationTrcModel {

    @Schema(title = "车辆信息")
    private String vin = StringUtils.EMPTY;

    @Schema(title = "工艺代码")
    private String woCode = StringUtils.EMPTY;

    @Schema(title = "工艺名称")
    private String woName = StringUtils.EMPTY;

    @Schema(title = "返回业务需要截取后的条码")
    private String barCode = StringUtils.EMPTY;

    @Schema(title = "返回业务完整条码")
    private String fullBarCode = StringUtils.EMPTY;

    @Schema(title = "零件号")
    private String partsNumber = StringUtils.EMPTY;

    @Schema(title = "完整零件号")
    private String fullPartsNumber = StringUtils.EMPTY;

    @Schema(title = "序列号")
    private String serialNumber = StringUtils.EMPTY;

    @Schema(title = "批次号")
    private String batchNumber = StringUtils.EMPTY;

    @Schema(title = "供应商")
    private String vendorNumber = StringUtils.EMPTY;

    @Schema(title = "组件号")
    private String componentCode = StringUtils.EMPTY;

}