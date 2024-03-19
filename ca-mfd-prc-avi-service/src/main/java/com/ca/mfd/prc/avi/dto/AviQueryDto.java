package com.ca.mfd.prc.avi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 前端AVI站点对象数据
 * <p>
 * AviOperDto class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
public class AviQueryDto { /**



    /**
     * avi代码
     */
    @Schema(title = "avi代码")
    private String aviCode = StringUtils.EMPTY;

    /**
     * avi类型
     */
    @Schema(title = "avi类型")
    private Integer aviType;

    /**
     * sn
     */
    @Schema(title = "sn")
    private String sn = StringUtils.EMPTY;

}
