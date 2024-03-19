package com.ca.mfd.prc.core.communication.remote.app.pm.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: 工具实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工具")
@TableName("PRC_PM_TOOL")
public class PmToolEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_TOOL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工具ID
     */
    @Schema(title = "工具ID")
    @TableField("TOOL_CODE")
    @NotBlank(message = "工具ID不能为空")
    private String toolCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("TOOL_NAME")
    @NotBlank(message = "工具名称不能为空")
    private String toolName = StringUtils.EMPTY;


    /**
     * 类型（1. 拧紧枪;2. 加注工具;3.喷枪）
     */
    @Schema(title = "类型（1. 拧紧枪;2. 加注工具;3.喷枪）")
    @TableField("TOOL_TYPE")
    @NotNull(message = "工具类型不能为空")
    private Integer toolType = 0;


    /**
     * 品牌
     */
    @Schema(title = "品牌")
    @TableField("BRAND")
    @NotBlank(message = "工具品牌不能为空")
    private String brand = StringUtils.EMPTY;


    /**
     * 类型(1.NETWORK;2.PROFINET;3.IO)
     */
    @Schema(title = "类型(1.NETWORK;2.PROFINET;3.IO)")
    @TableField("NET_TYPE")
    @NotNull(message = "工具类型不能为空")
    private Integer netType = 0;


    /**
     * IP
     */
    @Schema(title = "IP")
    @TableField("IP")
    @NotBlank(message = "工具IP地址不能为空")
    private String ip = StringUtils.EMPTY;


    /**
     * 端口号
     */
    @Schema(title = "端口号")
    @TableField("PORT")
    @NotBlank(message = "工具端口号不能为空")
    private String port = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @NotNull(message = "工具顺序号为空")
    private Integer displayNo = 0;

    @TableField(exist = false)
    @JSONField(name = "PmToolJobEntity")
    private List<PmToolJobEntity> pmToolJobEntity = new ArrayList<>();

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;

    @TableField(exist = false)
    @JsonIgnore
    private String workstationCode;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;
}