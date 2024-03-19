package com.ca.mfd.prc.eps.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 车辆数据集合
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class GetProductDataListDTO {

    /**
     * Sn
     */
    private String sn = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    private Integer displayNo = 0;

    /**
     * 总装流水号
     */
    private String sequenceNo = StringUtils.EMPTY;

    /**
     * Barcode
     */
    private String barcode = StringUtils.EMPTY;

    /**
     * 产品类型
     */
    private String model = StringUtils.EMPTY;

}
