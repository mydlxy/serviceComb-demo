package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.main.dto.MenuAllInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author eric.zhou
 */
public class PrmDetailDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "PermissionDTO", description = "PermissionDTO")
    @Data
    public static class PermissionDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id = UUIDUtils.getEmpty();

        private String permissionCode;

        private String permissionName;
        private String model;
        private String remark;
    }


    /**
     * @author eric.zhou
     */
    @Schema(title = "InterfacePermissionDTO", description = "InterfacePermissionDTO")
    @Data
    public static class InterfacePermissionDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "ID")
        private String id = UUIDUtils.getEmpty();

        @Schema(description = "Key")
        private String key;
        @Schema(description = "TempId")
        private String tempId;
        @Schema(description = "TempKey")
        private String tempKey;
        @Schema(description = "Name")
        private String name;
        @Schema(description = "Remark")
        private String remark;
        @Schema(description = "Path")
        private String path;
        @Schema(description = "Type")
        private Integer type;

    }


    /**
     * @author eric.zhou
     */
    @Schema(title = "PrmDetailResult", description = "PrmDetailResult")
    @Data
    public static class PrmDetailResult implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "permission")
        private PermissionDTO permission;
        @Schema(description = "menus")
        private List<MenuAllInfo> menus;
        @Schema(description = "interfacePermission")
        private List<InterfacePermissionDTO> interfacePermission;
        @Schema(description = "tokenPermissions")
        private List<TokenPermissionDTO> tokenPermissions;
    }

}


