package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * @author eric.zhou
 */
public class TokenDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "TokenSaveModel", description = "Token")
    @Data
    public static class TokenSaveModel extends BasicServiceModel<PrmTokenEntity> {
        private static final long serialVersionUID = 1L;
        @Schema(description = "账户主键")
        private String id;
        @Schema(description = "令牌名称")
        private String tokenName;
        @Schema(description = "令牌")
        private String token;
        @Schema(description = "组名称")
        private String groupName;
        @Schema(description = "过期时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
        @JsonDeserialize(using = JsonDeserializeDate.class)
        private Date expireDt;
        @Schema(description = "备注")
        private String remark;
        @Schema(description = "是否启用Token")
        private Boolean tokenEnable;
        @Schema(description = "权限")
        private List<PrmPermissionEntity> permissions;

        public TokenSaveModel() {
            setService(new PrmTokenEntity());
        }

        public String getId() {
            return getService().getId().toString();
        }

        public void setId(String value) {
            this.id = value;
            getService().setId(Long.valueOf(value));
        }

        public String getTokenName() {
            return getService().getTokenName();
        }

        public void setTokenName(String value) {
            this.tokenName = value;
            getService().setTokenName(value);
        }

        public String getToken() {
            return getService().getToken();
        }

        public void setToken(String value) {
            this.token = value;
            getService().setToken(value);
        }

        public String getGroupName() {
            return getService().getGroupName();
        }

        public void setGroupName(String value) {
            this.groupName = value;
            getService().setGroupName(value);
        }

        public Date getExpireDt() {
            return getService().getExpireDt();
        }

        public void setExpireDt(Date value) {
            if (value == null) {
                value = DateUtils.addDateMonths(new Date(), 3);
            }
            this.expireDt = value;
            getService().setExpireDt(value);
        }

        public String getRemark() {
            return getService().getRemark();
        }

        public void setRemark(String value) {
            this.remark = value;
            getService().setRemark(value);
        }

        public Boolean getTokenEnable() {
            return getService().getTokenEnable();
        }

        public void setTokenEnable(Boolean value) {
            this.tokenEnable = value;
            getService().setTokenEnable(value);
        }

        public List<PrmPermissionEntity> getPermissions() {
            return getService().getPermissions();
        }

        public void setPermissions(List<PrmPermissionEntity> value) {
            this.permissions = value;
            getService().setPermissions(value);
        }


    }

    @Schema(title = "GetTokenModel", description = "Token")
    @Data
    public static class GetTokenModel {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
        @JsonDeserialize(using = JsonDeserializeDate.class)
        private Date nowTime;

        private String appId;

        private int rememberType = 8760;
    }
}


