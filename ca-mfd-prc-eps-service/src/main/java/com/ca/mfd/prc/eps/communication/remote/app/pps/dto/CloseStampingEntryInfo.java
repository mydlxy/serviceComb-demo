package com.ca.mfd.prc.eps.communication.remote.app.pps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * CloseStampingEntryInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "CloseStampingEntryInfo", description = "")
public class CloseStampingEntryInfo {
    @Schema(description = "entryId")
    @JsonAlias(value = {"EntryId", "entryId"})
    private String entryId = StringUtils.EMPTY;

    @Schema(description = "reportCount")
    @JsonAlias(value = {"ReportCount", "reportCount"})
    private Integer reportCount = 0;
}
