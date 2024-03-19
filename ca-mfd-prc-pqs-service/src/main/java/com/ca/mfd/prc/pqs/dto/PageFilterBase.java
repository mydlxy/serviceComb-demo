package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页筛选对象
 *
 * @Author: joel
 * @Date: 2023-04-19-14:15
 * @Description:
 */
@Data
public class PageFilterBase {
    private Integer pageIndex;
    private Integer pageSize;
    private String key = StringUtils.EMPTY;

    public PageFilterBase() {
        pageIndex = 1;
        pageIndex = 9999;
    }
}
