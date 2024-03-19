package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * EntryHalfLock
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "EntryHalfLock", description = "")
public class EntryHalfLock {
    @Schema(description = "entryIds")
    private List<Long> entryIds;
}
