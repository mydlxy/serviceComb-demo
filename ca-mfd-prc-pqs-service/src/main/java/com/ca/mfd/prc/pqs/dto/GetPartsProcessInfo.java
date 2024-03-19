package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class GetPartsProcessInfo {

    /**
     * 订单类型
     */
    private Integer orderCategory = 1;

    /**
     * 条码
     */
    private String barcode = StringUtils.EMPTY;

    /**
     * 工单号
     */
    private String entryNo = StringUtils.EMPTY;

    /**
     * 报工单号
     */
    private String entryReportNo = StringUtils.EMPTY;

    /**
     * 车系/机型
     */
    private String model = StringUtils.EMPTY;

    /**
     * 物料号
     */
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    private String materialCn = StringUtils.EMPTY;

    /**
     * 工序代码
     */
    private String processCode = StringUtils.EMPTY;

    /**
     * 工序名称
     */
    private String processName = StringUtils.EMPTY;

}
