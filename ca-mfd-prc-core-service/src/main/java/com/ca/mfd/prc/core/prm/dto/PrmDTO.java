package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author eric.zhou
 */
public class PrmDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "UserPrmModel", description = "UserPrmModel")
    @Data
    public static class UserPrmModel implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(description = "集合")
        public List<UserPrmDataModel> datas = new ArrayList<>();
        @Schema(description = "账户主键")
        private String id = UUIDUtils.getEmpty();
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "UserPrmDataModel", description = "UserPrmDataModel")
    @Data
    public static class UserPrmDataModel implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(description = "回收时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
        @JsonDeserialize(using = JsonDeserializeDate.class)
        public Date recycleDt;
        @Schema(description = "用户外键")
        private String prmId = UUIDUtils.getEmpty();
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "PermissionModel", description = "授权模型")
    @Data
    public static class PermissionModel extends BasicServiceModel<PrmPermissionEntity> {
        private static final long serialVersionUID = 1L;
        @Schema(description = "账户主键")
        private String id = Constant.DEFAULT_ID.toString();
        @Schema(description = "代码")
        private String permissionCode;
        @Schema(description = "名称")
        private String permissionName;
        @Schema(description = "描述")
        private String description;
        @Schema(description = "模块")
        private String model;

        public PermissionModel() {
            setService(new PrmPermissionEntity());
        }

        public String getId() {
            return getService().getId() == null ? Constant.DEFAULT_ID.toString() : getService().getId().toString();
        }

        public void setId(String value) {
            this.id = value;
            getService().setId(Long.valueOf(value));
        }

        public String getPermissionCode() {
            return getService().getPermissionCode();
        }

        public void setPermissionCode(String value) {
            this.permissionCode = value;
            getService().setPermissionCode(value);
        }

        public String getPermissionName() {
            return getService().getPermissionName();
        }

        public void setPermissionName(String value) {
            this.permissionName = value;
            getService().setPermissionName(value);
        }

        public String getDescription() {
            return getService().getRemark();
        }

        public void setDescription(String value) {
            this.description = value;
            getService().setRemark(value);
        }

        public String getModel() {
            return getService().getModel();
        }

        public void setModel(String value) {
            this.model = value;
            getService().setModel(value);
        }

    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "RolePrmModel", description = "授权模型")
    @Data
    public static class RolePrmModel extends BasicServiceModel<PrmRoleEntity> {
        private static final long serialVersionUID = 1L;
        @Schema(description = "账户主键")
        private Serializable id = Constant.DEFAULT_ID;
        @Schema(description = "代码")
        private String code;
        @Schema(description = "名称")
        private String roleName;
        @Schema(description = "描述")
        private String description;
        @Schema(description = "分组")
        private String groupName;
        @Schema(description = "角色菜单")
        private List<Serializable> permissions = new ArrayList<>();

        public RolePrmModel() {
            setService(new PrmRoleEntity());
        }

        public String getId() {
            return getService().getId() == null ? Constant.DEFAULT_ID.toString() : getService().getId().toString();
        }

        public void setId(Serializable value) {
            this.id = value;
            getService().setId(Long.parseLong(value.toString()));
        }

        public String getCode() {
            return getService().getRoleCode();
        }

        public void setCode(String value) {
            this.code = value;
            getService().setRoleCode(value);
        }

        public String getRoleName() {
            return getService().getRoleName();
        }

        public void setRoleName(String value) {
            this.roleName = value;
            getService().setRoleName(value);
        }

        public String getDescription() {
            return getService().getRemark();
        }

        public void setDescription(String value) {
            this.description = value;
            getService().setRemark(value);
        }

        public String getGroupName() {
            return getService().getGroupName();
        }

        public void setGroupName(String value) {
            this.groupName = value;
            getService().setGroupName(value);
        }
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "令牌授权模型", description = "令牌授权模型")
    @Data
    public static class TokenPrmModel implements Serializable {

        private static final long serialVersionUID = 1L;
        @Schema(description = "令牌外键集合")
        public List<String> datas = new ArrayList<>();
        @Schema(description = "权限Id")
        private String id = UUIDUtils.getEmpty();
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "菜单授权模型", description = "菜单授权模型")
    @Data
    public static class MenuPrmModel implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(description = "菜单项外键集合")
        public List<Serializable> datas = new ArrayList<>();
        @Schema(description = "权限Id")
        private String id = UUIDUtils.getEmpty();
        @Schema(description = "菜单Id")
        private String menuId = UUIDUtils.getEmpty();
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "接口授权模型", description = "接口授权模型")
    @Data
    public static class InterfacePrmModel implements Serializable {
        private static final long serialVersionUID = 1L;
        @Schema(description = "接口权限集合")
        public List<String> datas = new ArrayList<>();
        @Schema(description = "权限Id")
        private String id = UUIDUtils.getEmpty();

    }
}


