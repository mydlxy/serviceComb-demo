package com.ca.mfd.prc.pps.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 服务管理
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "服务管理")
@TableName("PRC_SYS_SERVICE_MANAGER")
public class SysServiceManagerEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SERVICE_MANAGER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 服务
     */
    @Schema(title = "服务")
    @TableField("NAME")
    private String name = StringUtils.EMPTY;

    /**
     * 服务地址
     */
    @Schema(title = "服务地址")
    @TableField("PATH")
    private String path = StringUtils.EMPTY;

    /**
     * IP
     */
    @Schema(title = "IP")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;

    /**
     * {t:"状态",d:["0.未安装;1.停止;4.运行"]}
     */
    @Schema(title = "{t:\"状态\",d:[\"0.未安装;1.停止;4.运行\"]}")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * {t:"目标状态",d:["0.未安装;1.停止;4.运行"]}
     */
    @Schema(title = "{t:\"目标状态\",d:[\"0.未安装;1.停止;4.运行\"]}")
    @TableField("TARGET_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer targetStatus = 0;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("SERVICE_DESCRIPTION")
    private String serviceDescription = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 是否关联车间
     */
    @Schema(title = "是否关联车间")
    @TableField("FACTORY_REALTION")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer factoryRealtion = 1;

    /**
     * 关联的车间
     */
    @Schema(title = "关联的车间")
    @TableField("SHOP_CODE")
    private String shopCode = StringUtils.EMPTY;

    /**
     * 当前版本
     */
    @Schema(title = "当前版本")
    @TableField("NOW_VERSION")
    private String nowVersion = StringUtils.EMPTY;

    /**
     * 最新版本
     */
    @Schema(title = "最新版本")
    @TableField("NEW_VERSION")
    private String newVersion = StringUtils.EMPTY;

    /**
     * 最新版本：0,表示不更新，1 表示更新程序,2 表示正在更新，3更新失败
     */
    @Schema(title = "最新版本：0,表示不更新，1表示更新程序,2表示正在更新，3更新失败")
    @TableField("IS_UPDATE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer isUpdate = 0;

    /**
     * 服务类型：windows Service 还是iis。0 windows，1 iis
     */
    @Schema(title = "服务类型：windowsService还是iis。0windows，1iis")
    @TableField("SERVICE_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serviceType = 1;

    /**
     * 服务端口
     */
    @Schema(title = "服务端口")
    @TableField("URL_PORT")
    private String urlPort = StringUtils.EMPTY;

    /**
     * 服务类型物理运行环境：0,表示双活，1表示单活
     */
    @Schema(title = "服务类型物理运行环境：0,表示双活，1表示单活")
    @TableField("PHYSICAL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer physicalType = 1;

    /**
     * 服务宿主ID
     */
    @Schema(title = "服务宿主ID")
    @TableField("SERVICE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long serviceId = Constant.DEFAULT_ID;

}
