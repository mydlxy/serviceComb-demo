package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class RiskDetailActiveInfo {

    /**
     * 围堵明细ID
     */
    private List<Long> ids;

    /**
     * 操作工位
     */
    private String workstationCode;
}
