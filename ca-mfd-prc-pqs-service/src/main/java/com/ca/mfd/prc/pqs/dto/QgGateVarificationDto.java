package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgGateVarificationDto {

    /**
     * 检查项
     */
    private String checkItem;

    /**
     * 结果
     */
    private boolean result;
}
