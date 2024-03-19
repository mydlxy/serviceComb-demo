/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author inkelink
 */
@Data
@Schema(description = "分页数据")
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "每页条数")
    public int pageSize;
    @Schema(title = "当前页号")
    public int pageIndex;
    @Schema(title = "总记录数")
    private int total;
    @Schema(title = "列表数据")
    private List<T> datas = new ArrayList<>();

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, long total) {
        this.datas = list;
        this.total = (int) total;
    }

    public PageData() {
        this.datas = new ArrayList<>();
    }
}