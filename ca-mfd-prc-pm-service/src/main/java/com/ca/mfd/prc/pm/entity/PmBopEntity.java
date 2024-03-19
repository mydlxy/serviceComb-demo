package com.ca.mfd.prc.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    private String workshopCode = StringUtils.EMPTY;


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
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("PRC_PM_LINE_NAME")
    private String lineName = StringUtils.EMPTY;


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
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("PRC_PM_WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 工序
     */
    @Schema(title = "工序")
    @TableField("PROCESS_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer processNo = 0;

    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 工序描述
     */
    @Schema(title = "工序描述")
    @TableField("PROCESS_REMARK")
    private String processRemark = StringUtils.EMPTY;

    /**
     * 分组编码
     */
    @Schema(title = "分组编码")
    @TableField("GROUP_CODE")
    private String groupCode = StringUtils.EMPTY;

    /**
     * 工序简图
     */
    @Schema(title = "工序简图")
    @TableField("PROCESS_IMG")
    private String processImg = StringUtils.EMPTY;

    /**
     * 作业步骤
     */
    @Schema(title = "作业步骤")
    @TableField("PROCESS_STEP")
    private String processStep = StringUtils.EMPTY;

    /**
     * 作业类型
     */
    @Schema(title = "作业类型")
    @TableField("ASSIGNMENT_TYPE")
    private String assignmentType = StringUtils.EMPTY;

    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    @TableField("PROCESS_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer processType = 0;


    /**
     * 操作编码
     */
    @Schema(title = "操作编码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    @TableField("WO_OPER_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer woOperType = 0;

    /**
     * 工艺顺序
     */
    @Schema(title = "工艺顺序")
    @TableField("WO_DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer woDisplayNo = 0;


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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
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
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 零件用量
     */
    @Schema(title = "零件用量")
    @TableField("MATERIAL_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String materialQuantity = StringUtils.EMPTY;


    /**
     * 零件单位
     */
    @Schema(title = "零件单位")
    @TableField("MATERIAL_UNIT")
    private String materialUnit = StringUtils.EMPTY;


    /**
     * 设备编号
     */
    @Schema(title = "设备编号")
    @TableField("EQUIPMENT_CODE")
    private String equipmentCode = StringUtils.EMPTY;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    @TableField("EQUIPMENT_NAME")
    private String equipmentName = StringUtils.EMPTY;

    /**
     * 工装编号
     */
    @Schema(title = "工装编号")
    @TableField("FROCK_CODE")
    private String frockCode = StringUtils.EMPTY;

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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer toolType = 0;

    /**
     * 工具品牌
     */
    @Schema(title = "工具品牌")
    @TableField("TOOL_BRAND")
    private String toolBrand = StringUtils.EMPTY;

    /**
     * 交付方式(1.JOB_OPC;2.JOB_PLC;3.JOB_HTTP)
     */
    @Schema(title = "交付方式(1.JOB_OPC;2.JOB_PLC;3.JOB_HTTP)")
    @TableField("TOOL_NET_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
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
     * 时序开始时间
     */
    @Schema(title = "时序开始时间")
    @TableField("BEGIN_TIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer beginTime = 0;

    /**
     * 时序开始时间
     */
    @Schema(title = "时序结束时间")
    @TableField("END_TIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer endTime = 0;

    /**
     * 作业次数
     */
    @Schema(title = "作业次数")
    @TableField("PROCESS_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer processCount = 0;


    /**
     * 特殊要求
     */
    @Schema(title = "特殊要求")
    @TableField("SPECIAL_REQUEST")
    private String specialRequest = StringUtils.EMPTY;


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

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnable = true;

    /**
     * 模型类型:wo,material,tool,job,other
     */
    @Schema(title = "类型")
    @TableField(exist = false)
    private String modelType = StringUtils.EMPTY;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;

    @TableField(exist = false)
    private Long qmDefectComponentId = Constant.DEFAULT_ID;

}