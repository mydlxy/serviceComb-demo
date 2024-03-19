package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class CreateQgAuditInfo {

    /**
     * 操作工位
     */
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 条码
     */
    private String barcode = StringUtils.EMPTY;

    /**
     * 物料
     */
    private String materialNo = StringUtils.EMPTY;

    /**
     *
     */
    private BigDecimal qty = BigDecimal.valueOf(0);

    /**
     * 处置方式 -1不适用 1让步接收 2返工返修 3工废 4料废
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int handleWay = 3;

    /**
     * 备注
     */
    private String remark = StringUtils.EMPTY;
}
