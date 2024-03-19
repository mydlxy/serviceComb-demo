package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class RiskProductListFilterInfo {

    /**
     * 型号
     */
    private String model;

    /**
     * AVI站点 ==默认下线点
     */
    private String aviCode;

    /**
     * 开始时间 默认3天
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDt = DateUtils.addDateDays(new Date(), -3);

    /**
     * 结束时间
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date endDt = new Date();

    /**
     * 类别
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 1;

    /**
     * 零件号
     */
    private String materialNo = StringUtils.EMPTY;
}
