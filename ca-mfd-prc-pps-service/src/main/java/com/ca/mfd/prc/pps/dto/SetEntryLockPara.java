package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * SetEntryLockPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "SetEntryLockPara", description = "")
public class SetEntryLockPara {
    @Schema(description = "entryIds")
    private List<String> entryIds;
    @Schema(description = "shopCode")
    private String shopCode;
}
