package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 生产计划-零部件DTO
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年11月10日
 */
@Data
public class PpsPlanPartsDto {

    /**
     * 计划编号
     */
    private String planNo = StringUtils.EMPTY;

    /**
     * 物料编号
     */
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    private String materialCn = StringUtils.EMPTY;

    /**
     * 订单大类;批次件：5：冲压  6：电池上盖
     */
    private Integer orderCategory = 0;

}