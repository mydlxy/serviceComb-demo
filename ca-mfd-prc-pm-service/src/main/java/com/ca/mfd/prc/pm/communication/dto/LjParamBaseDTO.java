package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName LJParamBaseDTO
 * @description: 拧紧所需设备参数基类
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamBaseDTO {
    private String msgId = StringUtils.EMPTY;
    private Object data;
}
