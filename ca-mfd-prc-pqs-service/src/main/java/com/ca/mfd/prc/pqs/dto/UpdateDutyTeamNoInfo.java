package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-08-24-11:18
 * @Description:
 */
@Data
public class UpdateDutyTeamNoInfo {
    private List<String> vehicleDefectAnomalyIds;

    /**
     * 责任线体编号
     */
    @Schema(title = "责任线体编号")
    private String dutyPmAreaId = StringUtils.EMPTY;

    /**
     * 责任线体名称
     */
    @Schema(title = "责任线体名称")
    private String dutyPmAreaName;

    /**
     * 责任班组
     */
    @Schema(title = "责任班组")
    private String dutyTeamNo;

    /**
     * 责任班组描述
     */
    @Schema(title = "责任班组描述")
    private String dutyTeamNoRemark;
}
