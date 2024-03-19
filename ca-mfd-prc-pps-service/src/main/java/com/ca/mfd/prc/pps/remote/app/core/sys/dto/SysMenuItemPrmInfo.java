package com.ca.mfd.prc.pps.remote.app.core.sys.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;

/**
 * @author inkelink
 */
@Data
public class SysMenuItemPrmInfo {
    private String id = UUIDUtils.getEmpty();
    private Long prcPrmMenuItemId = Constant.DEFAULT_ID;
}
