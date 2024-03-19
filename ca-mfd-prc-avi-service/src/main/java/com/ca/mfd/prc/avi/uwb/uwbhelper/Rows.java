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
public class Rows {

    private String userName = StringUtils.EMPTY;

    public String getVin() {
        return getUserName();
    }
}
