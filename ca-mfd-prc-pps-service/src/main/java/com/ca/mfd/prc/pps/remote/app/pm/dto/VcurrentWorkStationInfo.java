package com.ca.mfd.prc.pps.remote.app.pm.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author joel
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
public class VcurrentWorkStationInfo {

    @Schema(title = "工位编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "工位代码")
    private String workstationCode;

    @Schema(title = "工位名称")
    private String workstationName;

    @Schema(title = "岗位类型 1 生产 2 QG 3 返修")
    private Integer workstationType = 0;

    @Schema(title = "工位号")
    private Integer workstationNo = 0;

    @Schema(title = "开发距离")
    private Integer beginDistance = 0;

    @Schema(title = "预警距离")
    private Integer alarmDistance = 0;

    @Schema(title = "结束距离")
    private Integer endDistance = 0;

    @Schema(title = "过点去向")
    private String routhPath;

    @Schema(title = "过点确认")
    private String routeCheck = StringUtils.EMPTY;

    @Schema(title = "工位关联质量门")
    private String stations = StringUtils.EMPTY;

    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;
}
