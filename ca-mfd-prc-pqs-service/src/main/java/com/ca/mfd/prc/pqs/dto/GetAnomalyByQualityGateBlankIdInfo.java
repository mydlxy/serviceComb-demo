package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * GetAnomalyByQualityGateBlankIdInfo
 *
 * @author eric.zhou
 * @date 2023/4/17
 */
@Data
public class GetAnomalyByQualityGateBlankIdInfo {
    /**
     * 缺陷编号
     */
    private String anomalyId = StringUtils.EMPTY;

    /**
     * 缺陷描述
     */
    private String description;

    /**
     * 是否激活缺陷
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isActivate = false;

    /**
     * 激活时间
     */
    private String createTime;
}
