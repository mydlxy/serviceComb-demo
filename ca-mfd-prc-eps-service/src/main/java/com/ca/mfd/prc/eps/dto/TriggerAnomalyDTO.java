package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.eps.remote.app.pqs.dto.AnomalyDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TriggerAnomalyDTO
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class TriggerAnomalyDTO {
    /**
     * 岗位编号
     */
    private String workplaceId = StringUtils.EMPTY;

    /**
     * 岗位名称
     */
    private String workplaceName = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    private String tpsCode = StringUtils.EMPTY;

    /**
     * 需要激活的缺陷列表
     */
    private List<AnomalyDto> anomalyInfos = new ArrayList<>();
}
