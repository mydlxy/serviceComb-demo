package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.pps.entity.PpsModuleIssueMainEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuleMainDetailDto
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ModuleMainDetailDto", description = "")
public class ModuleMainDetailDto {

    @Schema(description = "main")
    private PpsModuleIssueMainEntity main;

    @Schema(description = "modules")
    private List<PpsModuleIssueModuleEntity> modules = new ArrayList<>();

}
