package com.ca.mfd.prc.audit.remote.app.pm.entity;

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

import javax.validation.constraints.NotBlank;

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
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;


    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;


    /**
     * 动作
     */
    @Schema(title = "动作")
    @TableField("ACTIONS")
    @NotBlank(message = "动作不能为空")
    private String actions = StringUtils.EMPTY;


    /**
     * 对象
     */
    @Schema(title = "对象")
    @TableField("OBJECTS")
    @NotBlank(message = "对象不能为空")
    private String objects = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料号名称
     */
    @Schema(title = "物料号名称")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 物料数量
     */
    @Schema(title = "物料数量")
    @TableField("MATERIAL_QUANTITY")
    private Integer materialQuantity = 0;


    /**
     * 工具
     */
    @Schema(title = "工具")
    @TableField("TOOL")
    private String tool = StringUtils.EMPTY;


    /**
     * 套筒/枪头
     */
    @Schema(title = "套筒/枪头")
    @TableField("SUB_TOOL")
    private String subTool = StringUtils.EMPTY;


    /**
     * 工装
     */
    @Schema(title = "工装")
    @TableField("FROCK")
    private String frock = StringUtils.EMPTY;


    /**
     * JOB号
     */
    @Schema(title = "JOB号")
    @TableField("JOB")
    private String job = StringUtils.EMPTY;


    /**
     * 设备
     */
    @Schema(title = "设备")
    @TableField("EQUIPMENT")
    private String equipment = StringUtils.EMPTY;


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
    @TableField("CHARACTERS")
    private String characters = StringUtils.EMPTY;


    /**
     * 特殊要求
     */
    @Schema(title = "特殊要求")
    @TableField("REQUIRES")
    private String requires = StringUtils.EMPTY;


    /**
     * 操作部位
     */
    @Schema(title = "操作部位")
    @TableField("OPERATE_PART")
    private String operatePart = StringUtils.EMPTY;


    /**
     * 操作图片
     */
    @Schema(title = "操作图片")
    @TableField("OPERATE_IMAGE")
    private String operateImage = StringUtils.EMPTY;


    /**
     * PFMEA编号
     */
    @Schema(title = "PFMEA编号")
    @TableField("PFMEA_CODE")
    private String pfmeaCode = StringUtils.EMPTY;


    /**
     * 控制特性-产品
     */
    @Schema(title = "控制特性-产品")
    @TableField("PFMEA_ATTRIBUTE_PRODUCT")
    private String pfmeaAttributeProduct = StringUtils.EMPTY;


    /**
     * 控制特性-过程
     */
    @Schema(title = "控制特性-过程")
    @TableField("PFMEA_ATTRIBUTE_PROCESS")
    private String pfmeaAttributeProcess = StringUtils.EMPTY;


    /**
     * 产品/过程规范/公差
     */
    @Schema(title = "产品/过程规范/公差")
    @TableField("PFMEA_PRODUCT")
    private String pfmeaProduct = StringUtils.EMPTY;


    /**
     * 样本-容量
     */
    @Schema(title = "样本-容量")
    @TableField("PFMEA_CAPACITY")
    private String pfmeaCapacity = StringUtils.EMPTY;


    /**
     * 样本-频率
     */
    @Schema(title = "样本-频率")
    @TableField("PFMEA_FREQUENCY")
    private String pfmeaFrequency = StringUtils.EMPTY;


    /**
     * 控制方法
     */
    @Schema(title = "控制方法")
    @TableField("CONTROL")
    private String control = StringUtils.EMPTY;


    /**
     * 反应计划编号
     */
    @Schema(title = "反应计划编号")
    @TableField("PLAN_CODE")
    private String planCode = StringUtils.EMPTY;


    /**
     * 特征配置
     */
    @Schema(title = "特征配置")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;


    /**
     * 图片
     */
    @Schema(title = "图片")
    @TableField("IMAGE")
    private String image = StringUtils.EMPTY;


    /**
     * PFMEA文件
     */
    @Schema(title = "PFMEA文件")
    @TableField("PFMEA_FILE")
    private String pfmeaFile = StringUtils.EMPTY;


    /**
     * 控制计划文件
     */
    @Schema(title = "控制计划文件")
    @TableField("PLAN_FILE")
    private String planFile = StringUtils.EMPTY;

    /**
     * 是否同步到能力中心
     */
    @Schema(title = "是否同步到能力中心")
    @TableField("IS_SYNC")
    private Boolean isSync = Boolean.TRUE;


}