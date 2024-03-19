package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class RiskProductFilterDto {

    private String orderNo = StringUtils.EMPTY;

    /**
     * 唯一码
     */
    private String sn = StringUtils.EMPTY;

    /**
     * 条码
     */
    private String barcode = StringUtils.EMPTY;

    /**
     * 物料编号
     */
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    private String materialCn = StringUtils.EMPTY;

    /**
     * 报工类型;1 合格  2 待检 3 不合格 4 待质检
     */
    private Integer reportType;

    /**
     * 产品编码
     */
    private String productCode = StringUtils.EMPTY;

    /**
     * 型号
     */
    private String model;

    /**
     * 特征1
     */
    private String characteristic1;

    /**
     * 特征2
     */
    private String characteristic2;

    /**
     * 特征3
     */
    private String characteristic3;
    /**
     * 特征4
     */
    private String characteristic4;

    /**
     * 特征5
     */
    private String characteristic5;

    /**
     * 特征6
     */
    private String characteristic6;

    /**
     * 特征7
     */
    private String characteristic7;

    /**
     * 特征8
     */
    private String characteristic8;

    /**
     * 特征9
     */
    private String characteristic9;

    /**
     * 特征10
     */
    private String characteristic10;

}
