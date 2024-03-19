package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgGateVarificationResultDto {

    /**
     * 结果 true 通过 false 不通过
     */
    private boolean result;

    /**
     * 是否允许强制跳过
     */
    private boolean allowByPass;

}
