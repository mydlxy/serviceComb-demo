package com.ca.mfd.prc.pmc.remote.app.core.sys.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;

/**
 * @author inkelink
 */
@Data
public class MenuPremissmsInfo {
    private String id = UUIDUtils.getEmpty();
    private String key;

    private String tempId;
    private String tempKey;
    private String name;
    private String icon;
    private String url;

    private Integer openType;
    private String position;
    private Boolean isHide;

}
