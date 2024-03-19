package com.ca.mfd.prc.pqs.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class WpAnomalyWorkstationParaInfo extends PageFilterBase {
    private String workstationName = StringUtils.EMPTY;

    private boolean showAllWorkstation = true;
}
