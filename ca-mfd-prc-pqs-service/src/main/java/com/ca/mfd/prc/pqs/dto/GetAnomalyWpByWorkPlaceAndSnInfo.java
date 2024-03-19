package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 岗位常用缺陷
 *
 * @Author: eric.zhou
 * @Date: 2023-04-14-14:49
 * @Description:
 */
@Data
@Schema(description = "岗位常用缺陷")
public class GetAnomalyWpByWorkPlaceAndSnInfo {

    /**
     * 缺陷编号
     */
    @Schema(title = "缺陷编号")
    private String anomalyId = StringUtils.EMPTY;

    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    private String description;

    /**
     * 是否激活缺陷
     */
    @Schema(title = "是否激活缺陷")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isActivate = false;
}
