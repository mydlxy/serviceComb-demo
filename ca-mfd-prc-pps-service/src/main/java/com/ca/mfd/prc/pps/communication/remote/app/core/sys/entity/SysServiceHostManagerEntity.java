package com.ca.mfd.prc.pps.communication.remote.app.core.sys.entity;

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
 * @Description: 服务寄宿管理
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "服务寄宿管理")
@TableName("PRC_SYS_SERVICE_HOST_MANAGER")
public class SysServiceHostManagerEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SERVICE_HOST_MANAGER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 服务名称
     */
    @Schema(title = "服务名称")
    @TableField("NAME")
    private String name = StringUtils.EMPTY;

    /**
     * 服务寄宿IP（高可用浮动IP）
     */
    @Schema(title = "服务寄宿IP（高可用浮动IP）")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;

    /**
     * 服务寄宿类型(0,双活，1单活)
     */
    @Schema(title = "服务寄宿类型(0,双活，1单活)")
    @TableField("SERVICE_HOST_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serviceHostType = 1;

    /**
     * 状态（服务运行状态）
     */
    @Schema(title = "状态（服务运行状态）")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 在运行中的数量(服务运行的数量)
     */
    @Schema(title = "在运行中的数量(服务运行的数量)")
    @TableField("SERVICE_RUN_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serviceRunCount = 0;

    /**
     * 服务安装数量（服务已安装机器数量）
     */
    @Schema(title = "服务安装数量（服务已安装机器数量）")
    @TableField("SERVICE_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serviceCount = 0;

    /**
     * 最新版本
     */
    @Schema(title = "最新版本")
    @TableField("NEW_VERSION")
    private String newVersion = StringUtils.EMPTY;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("HOST_DESCRIPTION")
    private String hostDescription = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 排序号
     */
    @Schema(title = "排序号")
    @TableField("SORT_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sortNo = 0;

    @Schema(title = "运行路径")
    @TableField(exist = false)
    private String runPath = StringUtils.EMPTY;

    @Schema(title = "运行ip")
    @TableField(exist = false)
    private String runIp = StringUtils.EMPTY;

}
