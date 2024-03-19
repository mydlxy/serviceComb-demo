package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class SubmitCheckItemInfo {

    /**
     * 产品唯一码
     */
    private String sn;

    /**
     * 操作工位
     */
    private String workstationCode;

    /**
     * 检验清单
     */
    private List<QgCheckItemInfo> chcekList;
}
