package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * UserData
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Schema(title = "UserData", description = "菜单项配置")
@Data
public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    private String id = StringUtils.EMPTY;
    @Schema(description = "DeparId")
    private String deparId = StringUtils.EMPTY;

    @Schema(description = "No")
    private String no = StringUtils.EMPTY;
    @Schema(description = "用户名")
    private String userName = StringUtils.EMPTY;
    @Schema(description = "工号")
    private String code = StringUtils.EMPTY;
    @Schema(description = "昵称")
    private String nickName = StringUtils.EMPTY;
    @Schema(description = "名称")
    private String fullName = StringUtils.EMPTY;

    @Schema(description = "英文名称")
    private String enName = StringUtils.EMPTY;
    @Schema(description = "中文名称")
    private String cnName = StringUtils.EMPTY;

    @Schema(description = "身份证")
    private String idCard;
    @Schema(description = "电话")
    private String phone;
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "分组英文")
    private String enGroupName = StringUtils.EMPTY;
    @Schema(description = "分组中文")
    private String cnGroupName = StringUtils.EMPTY;
    @Schema(description = "分组")
    private String groupName = StringUtils.EMPTY;

    @Schema(description = "是否激活状态")
    private Boolean isActive = true;
    @Schema(description = "备注")
    private String remark = StringUtils.EMPTY;
    @Schema(description = "用户类型")
    private Integer userType = 1;

    @Schema(description = "冻结时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date frozenDt = new Date();

    @Schema(description = "状态:1、正常,2、锁定")
    private Integer status = 1;
    @Schema(description = "编辑状态:1、可编辑,0、不可编辑")
    private Boolean isEdit = false;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date createDt;

    @Schema(description = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passExpireDt;

    @Schema(description = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date expiredDt;

    @Schema(description = "单位名")
    private String departmentName = StringUtils.EMPTY;
    @Schema(description = "单位编码")
    private String departmentCode = StringUtils.EMPTY;
}
