package com.ca.mfd.prc.avi.uwb.uwbhelper;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * UwbResult
 *
 * @author eric.zhou
 * @date 2023/05/18
 */
@Data
public class UwbResultInfo {

    private String result = StringUtils.EMPTY;

    private String reason = StringUtils.EMPTY;

    private String manid = StringUtils.EMPTY;

    public Boolean getSuccess() {
        return "success".equalsIgnoreCase(result);
    }
}
