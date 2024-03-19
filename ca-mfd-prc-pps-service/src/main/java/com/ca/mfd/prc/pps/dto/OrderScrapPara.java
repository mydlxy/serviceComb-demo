package com.ca.mfd.prc.pps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 报废参数提交
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "报废参数提交", description = "")
public class OrderScrapPara {

    @Schema(description = "产品条码")
    private String barcode = StringUtils.EMPTY;

    @Schema(description = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7:备件")
    private String orderCategory = StringUtils.EMPTY;

    @Schema(description = "备注")
    private String remark = StringUtils.EMPTY;

}
