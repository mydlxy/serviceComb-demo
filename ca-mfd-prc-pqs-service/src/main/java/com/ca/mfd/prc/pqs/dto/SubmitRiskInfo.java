package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class SubmitRiskInfo {

    /**
     * 型号
     */
    private String model = StringUtils.EMPTY;

    /**
     * 唯一码
     */
    private List<String> snList = Lists.newArrayList();

    /**
     * 组合代码
     */
    private String defectAnomalyCode = StringUtils.EMPTY;

    /**
     * 问题描述
     */
    private String defectAnomalyDescription;

    /**
     * 备注
     */
    private String remark = StringUtils.EMPTY;

    /**
     * 类别 1 整车 2压铸 3机加 4冲压
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int category = 1;
}
