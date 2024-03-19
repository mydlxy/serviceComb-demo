package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RevocationReportPara
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Data
@Schema(title = "RevocationReportPara", description = "")
public class RevocationReportPara {

    @Schema(description = "reportId")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long reportId;
}
