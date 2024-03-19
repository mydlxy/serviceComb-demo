package com.ca.mfd.prc.pqs.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 超级BOP实体
 * @date 2023年08月30日
 * @变更说明 BY inkelink At 2023年08月30日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "超级BOP")
@TableName("PRC_PM_BOP")
public class PmBopEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_BOP_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间外键
     */
    @Schema(title = "车间外键")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("PRC_PM_WORKSHOP_CODE")
    private String prcPmWorkshopCode = StringUtils.EMPTY;


    /**
     * 生产线
     */
    @Schema(title = "生产线")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("PRC_PM_LINE_CODE")
    private String prcPmLineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("PRC_PM_LINE_NAME")
    private String prcPmLineName = StringUtils.EMPTY;


    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String prcPmWorkstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("PRC_PM_WORKSTATION_NAME")
    private String prcPmWorkstationName = StringUtils.EMPTY;


    /**
     * 工序
     */
    @Schema(title = "工序")
    @TableField("PROCESS_NO")
    private Integer processNo = 0;

    /**
     * 工序描述
     */
    @Schema(title = "工序描述")
    @TableField("PROCESS_REMARK")
    private String processRemark = StringUtils.EMPTY;

    /**
     * 作业步骤
     */
    @Schema(title = "作业步骤")
    @TableField("PROCESS_STEP")
    private String processStep = StringUtils.EMPTY;


    /**
     * 操作编码
     */
    @Schema(title = "操作编码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;


    /**
     * 异常代码
     */
    @Schema(title = "异常代码")
    @TableField("WO_QM_DEFECT_ANOMALY_CODE")
    private String woQmDefectAnomalyCode = StringUtils.EMPTY;

    /**
     * 异常代码描述
     */
    @Schema(title = "异常代码描述")
    @TableField("WO_QM_DEFECT_ANOMALY_DESCRIPTION")
    private String woQmDefectAnomalyDescription = StringUtils.EMPTY;

    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    @TableField("WO_QM_DEFECT_COMPONENT_DESCRIPTION")
    private String woQmDefectComponentDescription = StringUtils.EMPTY;

    /**
     * 组件描述
     */
    @Schema(title = "组件代码")
    @TableField("WO_QM_DEFECT_COMPONENT_CODE")
    private String woQmDefectComponentCode = StringUtils.EMPTY;

    /**
     * 批量追溯
     */
    @Schema(title = "批量追溯")
    @TableField("WO_TRC_BY_GROUP")
    private Boolean woTrcByGroup = false;

    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("WO_GROUP_NAME")
    private String woGroupName = StringUtils.EMPTY;

    /**
     * 动作描述
     */
    @Schema(title = "动作描述")
    @TableField("ACTION_REMARK")
    private String actionRemark = StringUtils.EMPTY;


    /**
     * 对象
     */
    @Schema(title = "作业对象")
    @TableField("PROCESS_OBJECTS")
    private String processObjects = StringUtils.EMPTY;


    /**
     * 零件编号
     */
    @Schema(title = "零件编号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 零件名称
     */
    @Schema(title = "零件名称")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 零件用量
     */
    @Schema(title = "零件用量")
    @TableField("MATERIAL_QUANTITY")
    private Integer materialQuantity = 0;

    /**
     * 设备编号
     */
    @Schema(title = "设备编号")
    @TableField("EQUIPMENT_CODE")
    private String equipmentCode = StringUtils.EMPTY;


    /**
     * 工具编码
     */
    @Schema(title = "工具编号")
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
    @TableField("TOOL_TYPE")
    private Integer toolType = 0;

    /**
     * 工具品牌
     */
    @Schema(title = "工具品牌")
    @TableField("TOOL_BRAND")
    private String toolBrand = StringUtils.EMPTY;

    /**
     * 类型(1.NETWORK
     */
    @Schema(title = "类型")
    @TableField("TOOL_NET_TYPE")
    private Integer toolNetType = 0;

    /**
     * 工具ip
     */
    @Schema(title = "工具ip")
    @TableField("TOOL_IP")
    private String toolIp = StringUtils.EMPTY;

    /**
     * 端口号
     */
    @Schema(title = "端口号")
    @TableField("TOOL_PORT")
    private String toolPort = StringUtils.EMPTY;

    /**
     * 工装顺序
     */
    @Schema(title = "工装顺序")
    @TableField("FROCK_ORDER")
    private Integer frockOrder = 0;


    /**
     * JOB号
     */
    @Schema(title = "JOB号")
    @TableField("JOB")
    private String job = StringUtils.EMPTY;

    /**
     * 操作图册文件名
     */
    @Schema(title = "操作图册文件名")
    @TableField("ACTION_IMAGE")
    private String actionImage = StringUtils.EMPTY;

    /**
     * 零件图册文件名
     */
    @Schema(title = "零件图册文件名")
    @TableField("MATERIAL_IMAGE")
    private String materialImage = StringUtils.EMPTY;

    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    @TableField("processType")
    private Integer processType = 0;

    /**
     * 时序开始时间
     */
    @Schema(title = "时序开始时间")
    @TableField("BEGIN_TIME")
    private Integer beginTime = 0;

    /**
     * 时序开始时间
     */
    @Schema(title = "时序结束时间")
    @TableField("END_TIME")
    private Integer endTime = 0;

    /**
     * 作业次数
     */
    @Schema(title = "作业次数")
    @TableField("PROCESS_COUNT")
    private Integer processCount = 0;


    /**
     * 特殊要求
     */
    @Schema(title = "特殊要求")
    @TableField("SPECIAL_REQUEST")
    private String specialRequest = StringUtils.EMPTY;


    /**
     * 色标
     */
    @Schema(title = "色标")
    @TableField("COLOR")
    private String color = StringUtils.EMPTY;


    /**
     * 特殊特性
     */
    @Schema(title = "特殊特性")
    @TableField("SPECIAL_CHARACTER")
    private String specialCharacter = StringUtils.EMPTY;



    /**
     * PFMEA文件
     */
    @Schema(title = "PFMEA文件")
    @TableField("PFMEA_FILE")
    private String pfmeaFile = StringUtils.EMPTY;


    /**
     * 控制计划
     */
    @Schema(title = "控制计划")
    @TableField("CONTROL_FILE")
    private String controlFile = StringUtils.EMPTY;


    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;


}