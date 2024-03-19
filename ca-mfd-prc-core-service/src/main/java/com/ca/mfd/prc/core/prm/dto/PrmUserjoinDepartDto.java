package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmUserjoinDepartDto", description = "用户部门")
@Data
public class PrmUserjoinDepartDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "departJoinId")
    private String departJoinId = UUIDUtils.getEmpty();

    @Schema(description = "departId")
    private String departId = UUIDUtils.getEmpty();

    @Schema(description = "No")
    private String no = StringUtils.EMPTY;
    @Schema(description = "用户名")
    private String userName = StringUtils.EMPTY;
    @Schema(description = "工号")
    private String code = StringUtils.EMPTY;
    @Schema(description = "昵称")
    private String nickName = StringUtils.EMPTY;
    @Schema(description = "全程")
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

    public String getFullName() {
        return this.fullName + "/" + this.userName;
    }

    public void setFullName(String value) {
        this.fullName = value;
    }
}

