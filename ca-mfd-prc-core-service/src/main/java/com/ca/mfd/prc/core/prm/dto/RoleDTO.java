package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * @author eric.zhou
 */
public class RoleDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "RoleSaveModel", description = "角色")
    @Data
    public static class RoleSaveModel extends BasicServiceModel<PrmRoleEntity> {
        private static final long serialVersionUID = 1L;
        @Schema(description = "账户主键")
        private Long id;
        @Schema(description = "角色名称")
        private String name;
        @Schema(description = "工号")
        private String code;
        @Schema(description = "组名称")
        private String groupName;
        @Schema(description = "分组")
        private String description;
        @Schema(description = "权限")
        private List<PrmPermissionEntity> permissions;

        public RoleSaveModel() {
            setService(new PrmRoleEntity());
        }

        public Long getId() {
            return getService().getId();
        }

        public void setId(Long value) {
            this.id = value;
            getService().setId(value);
        }

        public String getName() {
            return getService().getRoleName();
        }

        public void setName(String value) {
            this.name = value;
            getService().setRoleName(value);
        }

        public String getCode() {
            return getService().getRoleCode();
        }

        public void setCode(String value) {
            this.code = value;
            getService().setRoleCode(value);
        }

        public String getGroupName() {
            return getService().getGroupName();
        }

        public void setGroupName(String value) {
            this.groupName = value;
            getService().setGroupName(value);
        }

        public String getDescription() {
            return getService().getRemark();
        }

        public void setDescription(String value) {
            this.description = value;
            getService().setRemark(value);
        }

        public List<PrmPermissionEntity> getPermissions() {
            return getService().getPermissions();
        }

        public void setPermissions(List<PrmPermissionEntity> value) {
            this.permissions = value;
            getService().setPermissions(value);
        }


    }
}


