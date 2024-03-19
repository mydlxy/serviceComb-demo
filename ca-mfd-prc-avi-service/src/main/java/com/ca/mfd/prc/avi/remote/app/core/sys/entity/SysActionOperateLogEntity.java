package com.ca.mfd.prc.avi.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 系统请求操作日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统请求操作日志")
@TableName("PRC_SYS_ACTION_OPERATE_LOG")
public class SysActionOperateLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_ACTION_OPERATE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 菜单
     */
    @Schema(title = "菜单")
    @TableField("MODULE")
    private String module = StringUtils.EMPTY;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ACTIONNAME")
    private String actionname = StringUtils.EMPTY;

    /**
     * URL
     */
    @Schema(title = "URL")
    @TableField("URLPATH")
    private String urlpath = StringUtils.EMPTY;

    /**
     * 备注内容
     */
    @Schema(title = "备注内容")
    @TableField("COMMENT")
    private String comment = StringUtils.EMPTY;

    /**
     * key
     */
    @Schema(title = "key")
    @TableField("DICKEY")
    private String dickey = StringUtils.EMPTY;

    /**
     * 是否隐藏
     */
    @Schema(title = "是否隐藏")
    @TableField("IS_ENABLE")
    private Boolean isEnable;

    /**
     * 模板
     */
    @Schema(title = "模板")
    @TableField("TEMPLATE")
    private String template = StringUtils.EMPTY;

}
