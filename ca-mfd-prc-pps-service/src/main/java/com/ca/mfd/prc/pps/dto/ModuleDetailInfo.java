package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.pps.entity.PpsModuleIssueUnitEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ModuleDetailInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ModuleDetailInfo", description = "")
public class ModuleDetailInfo {

//    @Schema(description = "拆分数量")
//    private List<PpsModuleIssueSpacerEntity> spacerInfos = new ArrayList<>();

    @Schema(description = "拆分数量")
    private List<PpsModuleIssueUnitEntity> unitInfos = new ArrayList<>();

}
