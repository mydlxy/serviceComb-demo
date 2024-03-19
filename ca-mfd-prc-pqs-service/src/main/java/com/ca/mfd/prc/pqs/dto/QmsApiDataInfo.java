package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * QMS系统接口对接数据容器
 *
 * @Author: joel
 * @Date: 2023-04-25-15:59
 * @Description:
 */
@Data
@Schema(description = "QMS系统接口对接数据容器")
public class QmsApiDataInfo<T> {
    private QmsApiHeaderInfo header;
    private T data;
}
