package com.ca.mfd.prc.pmc.remote.app.core.sys.entity;

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
 * @Description: 系统操作日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统操作日志")
@TableName("PRC_SYS_LOG_OPERATE")
public class SysLogOperateEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_LOG_OPERATE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 来源
     */
    @Schema(title = "来源")
    @TableField("APP")
    private String app = StringUtils.EMPTY;

    /**
     * 用户外键
     */
    @Schema(title = "用户外键")
    @TableField("USER_ID")
    private String userId = StringUtils.EMPTY;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @TableField("USER_NAME")
    private String userName = StringUtils.EMPTY;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    @TableField("NICK_NAME")
    private String nickName = StringUtils.EMPTY;

    /**
     * 会话ID
     */
    @Schema(title = "会话ID")
    @TableField("SESSION_ID")
    private String sessionId = StringUtils.EMPTY;

    /**
     * 路径
     */
    @Schema(title = "路径")
    @TableField("PATH")
    private String path = StringUtils.EMPTY;

    /**
     * MESSAGE
     */
    @Schema(title = "MESSAGE")
    @TableField("MESSAGE")
    private String message = StringUtils.EMPTY;

    /**
     * 请求方法
     */
    @Schema(title = "请求方法")
    @TableField("METHOD")
    private String method = StringUtils.EMPTY;

    /**
     * 请求内容
     */
    @Schema(title = "请求内容")
    @TableField("REQUEST")
    private String request = StringUtils.EMPTY;

    /**
     * 响应内容
     */
    @Schema(title = "响应内容")
    @TableField("RESPONSE")
    private String response = StringUtils.EMPTY;

    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS_CODE")
    private Integer statusCode;

    /**
     * IP地址
     */
    @Schema(title = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress = StringUtils.EMPTY;

    /**
     * 端口
     */
    @Schema(title = "端口")
    @TableField("PORT")
    private Integer port;

    /**
     * 用时
     */
    @Schema(title = "用时")
    @TableField("ELAPSED")
    private String elapsed = StringUtils.EMPTY;

    /**
     * 菜单分类模块
     */
    @Schema(title = "菜单分类模块")
    @TableField("MODULE")
    private String module = StringUtils.EMPTY;

    /**
     * 执行动作
     */
    @Schema(title = "执行动作")
    @TableField("ACTIONNAME")
    private String actionname = StringUtils.EMPTY;

    /**
     * 数据展示模板
     */
    @Schema(title = "数据展示模板")
    @TableField("TEMPLATE")
    private String template = StringUtils.EMPTY;

    /**
     * 原始数据
     */
    @Schema(title = "原始数据")
    @TableField("OLDDATA")
    private String olddata = StringUtils.EMPTY;

}
