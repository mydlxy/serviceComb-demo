package com.ca.mfd.prc.avi.remote.app.core.sys.dto;

import com.ca.mfd.prc.avi.remote.app.core.sys.entity.SysMenuItemEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 */
@Data
public class MenuInfo {
    private Serializable id = Constant.DEFAULT_ID;
    private String position;
    private SysMenuItemEntity data;
    private List<MenuInfo> children;
}
