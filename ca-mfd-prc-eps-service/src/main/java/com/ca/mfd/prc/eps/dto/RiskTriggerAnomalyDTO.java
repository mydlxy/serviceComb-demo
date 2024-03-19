package com.ca.mfd.prc.eps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * RiskTriggerAnomalyDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RiskTriggerAnomalyDTO")
public class RiskTriggerAnomalyDTO {

    /**
     * WorkplaceId
     */
    @Schema(title = "WorkplaceId")
    @JsonAlias(value = {"WorkplaceId", "workplaceId"})
    private String workplaceId = StringUtils.EMPTY;

    /**
     * StartDisplay
     */
    @Schema(title = "StartDisplay")
    @JsonAlias(value = {"StartDisplay", "startDisplay"})
    private Integer startDisplay = 0;

    /**
     * EndDisplay
     */
    @Schema(title = "EndDisplay")
    @JsonAlias(value = {"EndDisplay", "endDisplay"})
    private Integer endDisplay = 0;

    /**
     * WorkplaceName
     */
    @Schema(title = "WorkplaceName")
    @JsonAlias(value = {"WorkplaceName", "workplaceName"})
    private String workplaceName = StringUtils.EMPTY;

    /**
     * DefectAnomalyDes
     */
    @Schema(title = "DefectAnomalyDes")
    @JsonAlias(value = {"DefectAnomalyDes", "defectAnomalyDes"})
    private String defectAnomalyDes = StringUtils.EMPTY;

    /**
     * Sns
     */
    @Schema(title = "Sns")
    @JsonAlias(value = {"Sns", "sns"})
    private List<String> sns;

}