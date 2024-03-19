package com.ca.mfd.prc.core.main.entity;

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
 * @Description: 系统网络设备监控
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统网络设备监控")
@TableName("PRC_SYS_NETWORK_MONITOR")
public class SysNetworkMonitorEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_NETWORK_MONITOR_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("NAME")
    private String name = StringUtils.EMPTY;

    /**
     * IP
     */
    @Schema(title = "IP")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;

    /**
     * 监控类型
     */
    @Schema(title = "监控类型")
    @TableField("MONITOR_TYPE")
    private String monitorType = "ping";

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("`DESCRIBE`")
    private String describe = StringUtils.EMPTY;

    /**
     * 组名
     */
    @Schema(title = "组名")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("RENARK")
    private String renark = StringUtils.EMPTY;

    /**
     * 排序
     */
    @Schema(title = "排序")
    @TableField("ORDERNUM")
    private Integer ordernum = 0;

    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("CODE")
    private String code = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    private Boolean isEnabled = true;

    /**
     * 区域地质
     */
    @Schema(title = "区域地质")
    @TableField("ADDRESS")
    private String address = StringUtils.EMPTY;

}
