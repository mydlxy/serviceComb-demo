package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmUserVO", description = "PrmUserVO")
@Data
public class PrmUserViewVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "主键")
    private String id = UUIDUtils.getEmpty();

    /**
     * 用户
     */
    @Schema(title = "用户")
    private String userName;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    private String nickName;

    /**
     * 中文名称
     */
    @Schema(title = "中文名称")
    private String cnName;

    /**
     * 英文名称
     */
    @Schema(title = "英文名称")
    private String enName;

    /**
     * 扩展编号
     */
    @Schema(title = "扩展编号")
    private String no;

    /**
     * 工号
     */
    @Schema(title = "工号")
    private String code;

    /**
     * 分组
     */
    @Schema(title = "分组")
    private String groupName;

    /**
     * 身份证
     */
    @Schema(title = "身份证")
    private String idCard;

    /**
     * 联系电话
     */
    @Schema(title = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(title = "邮箱")
    private String email;

    /**
     * 中文分组
     */
    @Schema(title = "中文分组")
    private String enGroupName;

    /**
     * 英文分组
     */
    @Schema(title = "英文分组")
    private String cnGroupName;

    /**
     * 全名
     */
    @Schema(title = "全名")
    private String fullName;

    /**
     * 认证到期时间
     */
    @Schema(title = "认证到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passExpireDt;

    /**
     * 是否激活
     */
    @Schema(title = "是否激活")
    private Boolean isActive = false;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * 用户类型
     */
    @Schema(title = "用户类型")
    private Integer userType = 0;

    /**
     * 冻结时间
     */
    @Schema(title = "冻结时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date frozenDt;

    /**
     * 用户状态
     */
    @Schema(title = "用户状态")
    private Integer status = 0;

    /**
     * 编辑状态:1、可编辑,0、不可编辑
     */
    @Schema(title = "编辑状态:1、可编辑,0、不可编辑")
    private Boolean isEdit = false;

    /**
     * 过期时间
     */
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date expiredDt;

    @Schema(title = "用户角色")
    private List<PrmRoleEntity> roles = new ArrayList<>();

    @Schema(title = "用户权限")
    private List<PrmPermissionEntity> permissions;

    @Schema(title = "角色名称")
    private String roleName;

    @Schema(title = "角色名称")
    private String departmentName = StringUtils.EMPTY;

    @Schema(title = "角色名称")
    private String departmentCode = StringUtils.EMPTY;

    @Schema(title = "角色名称")
    private String roleGroupName;

    @Schema(title = "角色名称")
    private String extData;

    @Schema(title = "部门ID")
    private String departmentId = UUIDUtils.getEmpty();

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date creationDate = new Date();

    /**
     * 创建人名
     */
    @Schema(title = "创建人名")
    private String createdBy = StringUtils.EMPTY;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date lastUpdatedDate = new Date();

    /**
     * 更新人名
     */
    @Schema(title = "更新人名")
    private String lastUpdatedBy = StringUtils.EMPTY;


}

