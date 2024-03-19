package com.ca.mfd.prc.avi.remote.app.pm.dto;


import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: BOP实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(of = "sourceId")
public class CmcPmBopVo {

    /**
     * 主键
     */
    @Schema(title = "主键")
    private Long cmcPmBopId;


    /**
     * 车间外键
     */
    @Schema(title = "车间外键")
    private Long cmcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    private String cmcPmWorkshopCode = StringUtils.EMPTY;


    /**
     * 生产线
     */
    @Schema(title = "生产线")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmLineId = Constant.DEFAULT_ID;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    private String cmcPmLineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    private String cmcPmLineName = StringUtils.EMPTY;


    /**
     * 工位
     */
    @Schema(title = "工位")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    private String cmcPmWorkstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    private String cmcPmWorkstationName = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer displayNo = 0;


    /**
     * 动作
     */
    @Schema(title = "动作")
    private String actions = StringUtils.EMPTY;


    /**
     * 对象
     */
    @Schema(title = "对象")
    private String objects = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料号名称
     */
    @Schema(title = "物料号名称")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 物料数量
     */
    @Schema(title = "物料数量")
    private Integer materialQuantity = 0;


    /**
     * 工具
     */
    @Schema(title = "工具")
    private String tool = StringUtils.EMPTY;


    /**
     * 套筒/枪头
     */
    @Schema(title = "套筒/枪头")
    private String subTool = StringUtils.EMPTY;


    /**
     * 工装
     */
    @Schema(title = "工装")
    private String frock = StringUtils.EMPTY;


    /**
     * JOB号
     */
    @Schema(title = "JOB号")
    private String job = StringUtils.EMPTY;


    /**
     * 设备
     */
    @Schema(title = "设备")
    private String equipment = StringUtils.EMPTY;


    /**
     * 色标
     */
    @Schema(title = "色标")
    private String color = StringUtils.EMPTY;


    /**
     * 特殊特性
     */
    @Schema(title = "特殊特性")
    private String characters = StringUtils.EMPTY;


    /**
     * 特殊要求
     */
    @Schema(title = "特殊要求")
    private String requires = StringUtils.EMPTY;


    /**
     * 操作部位
     */
    @Schema(title = "操作部位")
    private String operatePart = StringUtils.EMPTY;


    /**
     * 操作图片
     */
    @Schema(title = "操作图片")
    private String operateImage = StringUtils.EMPTY;


    /**
     * PFMEA编号
     */
    @Schema(title = "PFMEA编号")
    private String pfmeaCode = StringUtils.EMPTY;

    /**
     * PFMEA文件
     */
    @Schema(title = "PFMEA文件")
    private String pfmeaFile = StringUtils.EMPTY;


    /**
     * 控制特性-产品
     */
    @Schema(title = "控制特性-产品")
    private String pfmeaAttributeProduct = StringUtils.EMPTY;


    /**
     * 控制特性-过程
     */
    @Schema(title = "控制特性-过程")
    private String pfmeaAttributeProcess = StringUtils.EMPTY;


    /**
     * 产品/过程规范/公差
     */
    @Schema(title = "产品/过程规范/公差")
    private String pfmeaProduct = StringUtils.EMPTY;


    /**
     * 样本-容量
     */
    @Schema(title = "样本-容量")
    private String pfmeaCapacity = StringUtils.EMPTY;


    /**
     * 样本-频率
     */
    @Schema(title = "样本-频率")
    private String pfmeaFrequency = StringUtils.EMPTY;


    /**
     * 控制方法
     */
    @Schema(title = "控制方法")
    private String control = StringUtils.EMPTY;


    /**
     * 反应计划编号
     */
    @Schema(title = "反应计划编号")
    private String planCode = StringUtils.EMPTY;

    /**
     * 反应计划文件
     */
    @Schema(title = "反应计划文件")
    private String planFile = StringUtils.EMPTY;


    /**
     * 特征配置
     */
    @Schema(title = "特征配置")
    private String featureCode = StringUtils.EMPTY;


    /**
     * 图片
     */
    @Schema(title = "图片")
    private String image = StringUtils.EMPTY;

    private Integer source = 1;


    /**
     * 来源ID
     */
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;

}