package com.ca.mfd.prc.avi.remote.app.core.sys.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;

import java.util.List;

/**
 * @author inkelink
 */
@Data
public class MenuAllInfo {
    private String id = UUIDUtils.getEmpty();
    private String key;
    private String name;
    private Integer version;
    private String descrpiton;
    private List<MenuPremissmsInfo> menuPermission;
}
