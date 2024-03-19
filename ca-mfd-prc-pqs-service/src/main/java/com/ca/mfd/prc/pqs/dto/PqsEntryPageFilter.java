package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @Author: joel
 * @Date: 2023-04-19-11:03
 * @Description:
 */
@Data
@Schema(description = "工单分页筛选对象")
public class PqsEntryPageFilter extends PageFilterBase {

    /**
     * 状态
     */
    @Schema(title = "状态")
    private String status = StringUtils.EMPTY;

    /**
     * 区域
     */
    @Schema(title = "区域")
    private String area;

    /**
     * 工序
     */
    @Schema(title = "工序")
    private String processCode = StringUtils.EMPTY;

    /**
     * 工单类型
     */
    @Schema(title = "工单类型")
    private String entryType = StringUtils.EMPTY;

    /**
     * 状态集合
     */
    @Schema(title = "状态集合")
    public int[] getStaus() {
        if (StringUtils.isBlank(status)) {
            return new int[]{1, 2, 3, 90};
        } else {
            return Arrays.asList(status.split(",")).stream().mapToInt(Integer::parseInt).toArray();
        }
    }
}
