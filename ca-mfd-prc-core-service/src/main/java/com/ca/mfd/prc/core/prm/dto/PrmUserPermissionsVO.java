package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmUserPermissionsVO", description = "用户权限")
@Data
public class PrmUserPermissionsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "编码")
    private String fullName;
    @Schema(description = "编码")
    private String userName;
    @Schema(description = "编码")
    private String nickName;
    @Schema(description = "编码")
    private String groupName;
    @Schema(description = "编码")
    private String enGroupName;
    @Schema(description = "编码")
    private String cnGroupName;
    @Schema(description = "编码")
    private String idCard;

    @Schema(description = "编码")
    private String email;
    @Schema(description = "编码")
    private String phone;
    @Schema(description = "编码")
    private String enName;
    @Schema(description = "编码")
    private String cnName;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "编号")
    private String no;
    @Schema(description = "更新人")
    private String lastUpdatedBy;
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date lastUpdatedDate;
    @Schema(description = "创建人")
    private String createdBy;
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date creationDate = new Date();
    @Schema(description = "角色ID")
    private List<Serializable> roles;
    @Schema(description = "认证到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passExpireDt;

    @Schema(description = "角色分组")
    private String roleGroupName;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "是否激活")
    private Boolean isActive;

    @Schema(description = "描述")
    private String remark;


}

