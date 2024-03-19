package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class SubmitBatchRiskInfo {

    /**
     * 计划单号
     */
    private String planNo = StringUtils.EMPTY;


    /**
     * 工单号
     */
    private String entryNo = StringUtils.EMPTY;

    /**
     * 风险问题补充说明
     */
    private String remark = StringUtils.EMPTY;

    /**
     * 是否发送LMS
     */
    private Boolean isSendLms = false;
}
