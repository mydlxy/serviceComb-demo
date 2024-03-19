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

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author inkelink ${email}
 * @Description: 权限
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限")
@TableName("PRC_PRM_PERMISSION")
public class PrmPermissionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_PERMISSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("PERMISSION_CODE")
    private String permissionCode = StringUtils.EMPTY;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("PERMISSION_NAME")
    private String permissionName = StringUtils.EMPTY;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 模块
     */
    @Schema(title = "模块")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    @Schema(title = "回收时间")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date recycleDt = new GregorianCalendar(1, 1, 1).getTime();
    ;
    @Schema(title = "角色名字")
    @TableField(exist = false)
    private String roleName;
    @Schema(title = "分组名称")
    @TableField(exist = false)
    private String groupName;
    @Schema(title = "userName")
    @TableField(exist = false)
    private String userName;
    @Schema(title = "userNo")
    @TableField(exist = false)
    private String userNo;
    @Schema(title = "prmUserId")
    @TableField(exist = false)
    private Long prmUserId = Constant.DEFAULT_ID;
    @Schema(title = "permissionModel")
    @TableField(exist = false)
    private String permissionModel;

    public Date getRecycleDt() {
        return this.recycleDt;
    }

    public void setRecycleDt(Date value) {
        if (value.getTime() > new GregorianCalendar(9999, 1, 1).getTime().getTime()) {
            this.recycleDt = null;
        } else {
            this.recycleDt = value;
        }
    }
}
