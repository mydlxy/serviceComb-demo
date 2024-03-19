package com.ca.mfd.prc.core.prm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 用户表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户表")
@TableName("PRC_PRM_USER")
public class PrmUserEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_USER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户
     */
    @Schema(title = "用户")
    @TableField("USER_NAME")
    private String userName = StringUtils.EMPTY;

    /**
     * 密码
     */
    @Schema(title = "密码")
    @TableField("PASSWORD")
    private String password;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 中文名称
     */
    @Schema(title = "中文名称")
    @TableField("CN_NAME")
    private String cnName;

    /**
     * 英文名称
     */
    @Schema(title = "英文名称")
    @TableField("EN_NAME")
    private String enName;

    /**
     * 扩展编号
     */
    @Schema(title = "扩展编号")
    @TableField("NO")
    private String no = StringUtils.EMPTY;

    /**
     * 工号
     */
    @Schema(title = "工号")
    @TableField("JOB_NO")
    private String jobNo = StringUtils.EMPTY;

    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 身份证
     */
    @Schema(title = "身份证")
    @TableField("ID_CARD")
    private String idCard = StringUtils.EMPTY;

    /**
     * 联系电话
     */
    @Schema(title = "联系电话")
    @TableField("PHONE")
    private String phone = StringUtils.EMPTY;

    /**
     * 邮箱
     */
    @Schema(title = "邮箱")
    @TableField("EMAIL")
    private String email = StringUtils.EMPTY;

    /**
     * 中文分组
     */
    @Schema(title = "中文分组")
    @TableField("EN_GROUP_NAME")
    private String enGroupName = StringUtils.EMPTY;

    /**
     * 英文分组
     */
    @Schema(title = "英文分组")
    @TableField("CN_GROUP_NAME")
    private String cnGroupName = StringUtils.EMPTY;

    /**
     * 全名
     */
    @Schema(title = "全名")
    @TableField("FULL_NAME")
    private String fullName = StringUtils.EMPTY;

    /**
     * 认证到期时间
     */
    @Schema(title = "认证到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("PASS_EXPIRE_DT")
    private Date passExpireDt = new Date();

    /**
     * 是否激活
     */
    @Schema(title = "是否激活")
    @TableField("IS_ACTIVE")
    private Boolean isActive = true;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 用户类型
     */
    @Schema(title = "用户类型")
    @TableField("USER_TYPE")
    private Integer userType = 1;

    /**
     * 冻结时间
     */
    @Schema(title = "冻结时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("FROZEN_DT")
    private Date frozenDt = new Date();

    /**
     * 用户状态
     */
    @Schema(title = "用户状态")
    @TableField("STATUS")
    private Integer status = 1;

    /**
     * 编辑状态:1、可编辑,0、不可编辑
     */
    @Schema(title = "编辑状态:1、可编辑,0、不可编辑")
    @TableField("IS_EDIT")
    private Boolean isEdit = false;

    /**
     * 过期时间
     */
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("EXPIRED_DT")
    private Date expiredDt;

    @Schema(title = "用户角色")
    @TableField(exist = false)
    private List<Serializable> roles = new ArrayList<>();

    @Schema(title = "用户权限")
    @TableField(exist = false)
    private List<PrmPermissionEntity> permissions;

    @Schema(title = "角色名称")
    @TableField(exist = false)
    private String roleName;

    @Schema(title = "部门名称")
    @TableField(exist = false)
    private String departmentName = StringUtils.EMPTY;

    @Schema(title = "部门代码")
    @TableField(exist = false)
    private String departmentCode = StringUtils.EMPTY;

    @Schema(title = "角色名称")
    @TableField(exist = false)
    private String roleGroupName;

    @Schema(title = "角色名称")
    @TableField(exist = false)
    private String extData;

    @Schema(title = "部门ID")
    @TableField(exist = false)
    private String departmentId = StringUtils.EMPTY;

}
