package com.ca.mfd.prc.eps.remote.app.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AnomalyActivity
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "AnomalyActivity")
public class ActiveAnomalyInfo {

    /**
     * 岗位编号
     */
    @Schema(title = "工位编号")
    private String workplaceId = StringUtils.EMPTY;

    /**
     * 岗位名称
     */
    @Schema(title = "工位名称")
    private String workplaceName;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    private String tpsCode;

    /**
     * 岗位编号
     */
    @Schema(title = "岗位编号")
    private String workstationCode;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    private String sn;

    /**
     * 需要激活的缺陷列表
     */
    @Schema(title = "需要激活的缺陷列表")
    private List<AnomalyDto> anomalyInfos = new ArrayList<>();

}
