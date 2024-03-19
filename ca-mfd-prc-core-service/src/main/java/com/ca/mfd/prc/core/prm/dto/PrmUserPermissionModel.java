package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;
import lombok.Data;

import java.util.List;

/**
 * @author inkelink
 */
@Data
public class PrmUserPermissionModel {
    private String id = UUIDUtils.getEmpty();
    private List<PrmUserPermissionEntity> permissions;

}
