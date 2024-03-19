package com.ca.mfd.prc.pm.dto;


import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
     * 工序
     */
    @Schema(title = "工序")
    private Integer processNo = 0;

    /**
     * 工序描述
     */
    @Schema(title = "工序描述")
    private String processRemark = StringUtils.EMPTY;

    /**
     * 作业步骤
     */
    @Schema(title = "作业步骤")
    private String processStep = StringUtils.EMPTY;

    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    private Integer processType = 0;


    /**
     * 操作编码
     */
    @Schema(title = "操作编码")
    private String woCode = StringUtils.EMPTY;

    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    private Integer woOperType = 0;

    /**
     * 工艺顺序
     */
    @Schema(title = "工艺顺序")
    private Integer woDisplayNo = 0;


    /**
     * 异常代码
     */
    @Schema(title = "异常代码")
    private String woQmDefectAnomalyCode = StringUtils.EMPTY;

    /**
     * 异常代码描述
     */
    @Schema(title = "异常代码描述")
    private String woQmDefectAnomalyDescription = StringUtils.EMPTY;

    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    private String woQmDefectComponentDescription = StringUtils.EMPTY;

    /**
     * 组件描述
     */
    @Schema(title = "组件代码")
    private String woQmDefectComponentCode = StringUtils.EMPTY;

    /**
     * 批量追溯
     */
    @Schema(title = "批量追溯")
    private Boolean woTrcByGroup = false;

    /**
     * 分组
     */
    @Schema(title = "分组")
    private String woGroupName = StringUtils.EMPTY;

    /**
     * 动作描述
     */
    @Schema(title = "动作描述")
    private String actionRemark = StringUtils.EMPTY;


    /**
     * 对象
     */
    @Schema(title = "作业对象")
    private String processObjects = StringUtils.EMPTY;


    /**
     * 零件编号
     */
    @Schema(title = "零件编号")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 零件名称
     */
    @Schema(title = "零件名称")
    private String materialName = StringUtils.EMPTY;


    /**
     * 零件用量
     */
    @Schema(title = "零件用量")
    private Integer materialQuantity = 0;

    /**
     * 设备编号
     */
    @Schema(title = "设备编号")
    private String equipmentCode = StringUtils.EMPTY;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    private String equipmentName = StringUtils.EMPTY;

    /**
     * 工装编号
     */
    @Schema(title = "工装编号")
    private String frockCode = StringUtils.EMPTY;

    /**
     * 工具编码
     */
    @Schema(title = "工具编号")
    private String toolCode = StringUtils.EMPTY;

    /**
     * 工具名称
     */
    @Schema(title = "工具名称")
    private String toolName = StringUtils.EMPTY;

    /**
     * 工具类型
     */
    @Schema(title = "工具类型")
    private Integer toolType = 0;

    /**
     * 工具品牌
     */
    @Schema(title = "工具品牌")
    private String toolBrand = StringUtils.EMPTY;

    /**
     * 类型(1.NETWORK
     */
    @Schema(title = "类型")
    private Integer toolNetType = 0;

    /**
     * 工具ip
     */
    @Schema(title = "工具ip")
    private String toolIp = StringUtils.EMPTY;

    /**
     * 端口号
     */
    @Schema(title = "端口号")
    private String toolPort = StringUtils.EMPTY;


    /**
     * JOB号
     */
    @Schema(title = "JOB号")
    private String job = StringUtils.EMPTY;

    /**
     * 操作图册文件名
     */
    @Schema(title = "操作图册文件名")
    private String actionImage = StringUtils.EMPTY;

    /**
     * 零件图册文件名
     */
    @Schema(title = "零件图册文件名")
    private String materialImage = StringUtils.EMPTY;


    /**
     * 时序开始时间
     */
    @Schema(title = "时序开始时间")
    private Integer beginTime = 0;

    /**
     * 时序开始时间
     */
    @Schema(title = "时序结束时间")
    private Integer endTime = 0;

    /**
     * 作业次数
     */
    @Schema(title = "作业次数")
    private Integer processCount = 0;


    /**
     * 特殊要求
     */
    @Schema(title = "特殊要求")
    private String specialRequest = StringUtils.EMPTY;



    /**
     * PFMEA文件
     */
    @Schema(title = "PFMEA文件")
    private String pfmeaFile = StringUtils.EMPTY;


    /**
     * 控制计划
     */
    @Schema(title = "控制计划")
    private String controlFile = StringUtils.EMPTY;


    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    private String featureCode = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * mom bop id 通过该字段回传给通用工厂
     */
    @Schema(title = "mom bop id 通过该字段回传给通用工厂")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long sourceId = Constant.DEFAULT_ID;

    private Integer source = 1;

}