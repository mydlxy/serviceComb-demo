package com.ca.mfd.prc.eps.entity;

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
 * @author inkelink
 * @Description: 车辆操作执行信息实体
 * @date 2023年09月14日
 * @变更说明 BY inkelink At 2023年09月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "车辆操作执行信息")
@TableName("PRC_EPS_VEHICLE_WO_EXECUTE")
public class EpsVehicleWoExecuteEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "")
    @TableId(value = "PRC_EPS_VEHICLE_WO_EXECUTE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工艺外键
     */
    @Schema(title = "工艺外键")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 工位编码
     */
    @Schema(title = "工位编码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 配置工艺编码
     */
    @Schema(title = "配置工艺编码")
    @TableField("PRC_PM_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcPmWoId = Constant.DEFAULT_ID;

    /**
     * 操作
     */
    @Schema(title = "操作")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 工具编码
     */
    @Schema(title = "工具编码")
    @TableField("PRC_PM_TOOL_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcPmToolId = Constant.DEFAULT_ID;

    /**
     * 工具代码
     */
    @Schema(title = "工具代码")
    @TableField("TOOL_CODE")
    private String toolCode = StringUtils.EMPTY;

    /**
     * 工具名称
     */
    @Schema(title = "工具名称")
    @TableField("TOOL_NAME")
    private String toolName = StringUtils.EMPTY;

    /**
     * 工具类型
     */
    @Schema(title = "工具类型")
    @TableField("TOOL_TOOL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer toolToolType = 0;

    /**
     * 工具类型(通讯协议）
     */
    @Schema(title = "工具类型(通讯协议）")
    @TableField("TOOL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer toolType = 0;

    /**
     * JOB_NO
     */
    @Schema(title = "JOB_NO")
    @TableField("JOB_NO")
    private String jobNo = StringUtils.EMPTY;

    /**
     * 工具IP
     */
    @Schema(title = "工具IP")
    @TableField("TOOL_IP")
    private String toolIp = StringUtils.EMPTY;

    /**
     * 工具端口
     */
    @Schema(title = "工具端口")
    @TableField("JOB_PORT")
    private String jobPort = StringUtils.EMPTY;

    /**
     * 组件编号
     */
    @Schema(title = "组件编号")
    @TableField("TRACE_COMPONENT_CODE")
    private String traceComponentCode = StringUtils.EMPTY;

    /**
     * 工具品牌
     */
    @Schema(title = "工具品牌")
    @TableField("TOOL_BRAND")
    private String toolBrand = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


}