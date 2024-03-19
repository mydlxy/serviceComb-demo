package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class EntryParaInfo {
    /**
     * 排除代码
     */
    private String key;

    private String workstationCode;

    private String status;

    private Integer pageSize = 0;

    private Integer pageIndex = 0;
}
