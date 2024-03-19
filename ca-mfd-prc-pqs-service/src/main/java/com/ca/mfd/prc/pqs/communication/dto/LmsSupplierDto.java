package com.ca.mfd.prc.pqs.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mason
 * @Description: LMS供应商dto
 * @date 2024年2月19日
 */
@Data
public class LmsSupplierDto {

    @Schema(title = "供应商编码")
    private String supplierCode = StringUtils.EMPTY;

    @Schema(title = "供应商名称")
    private String supplierName = StringUtils.EMPTY;
}
