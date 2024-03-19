package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PrmUserDto
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Schema(title = "PrmUserDto", description = "PrmUserDto")
@Data
public class PrmUserDto implements Serializable {

    @Schema(description = "扩展编号")
    private String no = StringUtils.EMPTY;

    @Schema(description = "用户名")
    private String userName = StringUtils.EMPTY;

    @Schema(description = "工号")
    private String code = StringUtils.EMPTY;

    @Schema(description = "名称")
    private String nickName = StringUtils.EMPTY;

    @Schema(description = "英文名称")
    private String enName = StringUtils.EMPTY;

    @Schema(description = "中文名称")
    private String cnName = StringUtils.EMPTY;

    @Schema(description = "密码")
    private String password = StringUtils.EMPTY;

    @Schema(description = "身份证")
    private String idCard = StringUtils.EMPTY;

    @Schema(description = "电话")
    private String phone = StringUtils.EMPTY;

    @Schema(description = "邮件地址")
    private String email = StringUtils.EMPTY;

    @Schema(description = "分组-EN")
    private String enGroupName = StringUtils.EMPTY;

    @Schema(description = "分组-CN")
    private String cnGroupName = StringUtils.EMPTY;

    @Schema(description = "分组-EN")
    private String groupName = StringUtils.EMPTY;

    @Schema(description = "备注")
    private String remark = StringUtils.EMPTY;

    @Schema(description = "部门名称")
    private String departmentName = StringUtils.EMPTY;

    @Schema(description = "角色")
    private List<PrmRoleDto> roles = new ArrayList<>();
}
