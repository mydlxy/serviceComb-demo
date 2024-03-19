package com.ca.mfd.prc.pmc.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "")
public class TeamLeaderInfoDTO {

    private String lineCode;

    private String teamNo;

    private List<String> workstationCodes = new ArrayList<>();
}
