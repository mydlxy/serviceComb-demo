package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class DefectAnomalyParaInfo {
    /**
     * 排除代码
     */
    private String excludeCodes = StringUtils.EMPTY;

    private String key = StringUtils.EMPTY;

    private Integer pageSize = 9999;

    private Integer pageIndex = 1;

    private List<String> defectAnomalyCodes;

    /**
     * 工位代码
     */
    private List<String> workstationCodes;
}
