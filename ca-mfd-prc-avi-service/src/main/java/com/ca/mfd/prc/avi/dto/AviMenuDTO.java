package com.ca.mfd.prc.avi.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * AviMenuDTO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */

@Data
public class AviMenuDTO {

    /**
     * 路由名字
     */
    private String routerName = StringUtils.EMPTY;

    /**
     * 节点名字
     */
    private String actionName = StringUtils.EMPTY;
}
