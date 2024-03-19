package com.ca.mfd.prc.pqs.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class ProductRoutePointDto {

    /**
     * 编码
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 区域代码
     */
    private String areaCode = StringUtils.EMPTY;

    /**
     * 区域名称
     */
    private String areaName = StringUtils.EMPTY;

    /**
     * 路有点代码
     */
    private String rcCode = StringUtils.EMPTY;

    /**
     * 是否激活
     */
    private Boolean isActive = false;
}
