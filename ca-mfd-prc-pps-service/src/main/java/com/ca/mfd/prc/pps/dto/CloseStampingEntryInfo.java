package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportCount = 0;
}
