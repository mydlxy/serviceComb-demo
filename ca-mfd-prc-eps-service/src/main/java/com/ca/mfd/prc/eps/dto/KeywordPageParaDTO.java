package com.ca.mfd.prc.eps.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 车辆数据集合
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class KeywordPageParaDTO {
    /**
     * PageIndex
     */
    private Integer pageIndex = 0;

    /**
     * PageSize
     */
    private Integer pageSize = 0;

    /**
     * Keyword
     */
    private String keyword = StringUtils.EMPTY;
}
