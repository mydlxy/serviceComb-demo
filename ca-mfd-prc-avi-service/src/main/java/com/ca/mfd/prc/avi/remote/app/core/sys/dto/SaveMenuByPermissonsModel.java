package com.ca.mfd.prc.avi.remote.app.core.sys.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 */
@Data
public class SaveMenuByPermissonsModel {
    private Serializable menuItemId = UUIDUtils.getEmpty();
    private List<Serializable> permissionIds;
}
