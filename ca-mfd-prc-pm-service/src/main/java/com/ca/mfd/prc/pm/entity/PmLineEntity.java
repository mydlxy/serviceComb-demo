package com.ca.mfd.prc.pm.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author inkelink
 * @Description: 生产线实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生产线")
@TableName("PRC_PM_LINE")
public class PmLineEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_LINE_ID", type = IdType.INPUT)
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
     * 代码
     */
    @Schema(title = "代码")
    @TableField("LINE_CODE")
    @NotBlank(message = "线体代码不能为空")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("LINE_NAME")
    @NotBlank(message = "线体名称不能为空")
    private String lineName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("LINE_REMARK")
    private String lineRemark = StringUtils.EMPTY;
    /**
     * 生产时间
     */
    @Schema(title = "生产时间")
    @TableField("PRODUCT_TIME")
    @NotNull(message = "生产时间不能为空")
    private Integer productTime = 0;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("LINE_DISPLAY_NO")
    @NotNull(message = "顺序号不能为空")
    private Integer lineDisplayNo = 0;


    /**
     * 工位数量
     */
    @Schema(title = "工位数量")
    @TableField("STATION_COUNT")
    @NotNull(message = "工位数量不能为空")
    private Integer stationCount = 0;


    /**
     * 线体开始距离
     */
    @Schema(title = "线体开始距离")
    @TableField("BEGIN_DISTANCE")
    @NotNull(message = "开始距离不能为空")
    private Integer beginDistance = 0;


    /**
     * 线体结束距离
     */
    @Schema(title = "线体结束距离")
    @TableField("END_DISTANCE")
    @NotNull(message = "结束距离不能为空")
    private Integer endDistance = 0;


    /**
     * 线体类型(1.主线；2.辅线)
     */
    @Schema(title = "线体类型(1.主线；2.辅线)")
    @TableField("LINE_TYPE")
    @NotNull(message = "线体类型不能为空")
    private Integer lineType = 0;


    /**
     * 队列DB
     */
    @Schema(title = "队列DB")
    @TableField("QUEUE_DB")
    private String queueDb = StringUtils.EMPTY;


    /**
     * 备用DB
     */
    @Schema(title = "备用DB")
    @TableField("PLC_DB3")
    private String plcDb3 = StringUtils.EMPTY;


    /**
     * 安灯DB
     */
    @Schema(title = "安灯DB")
    @TableField("ANDON_DB")
    private String andonDb = StringUtils.EMPTY;


    /**
     * 线体链接
     */
    @Schema(title = "线体链接")
    @TableField("OPC_CONNECT")
    private String opcConnect = StringUtils.EMPTY;


    /**
     * 前缓存上限
     */
    @Schema(title = "前缓存上限")
    @TableField("FRONT_BUFFER_UP_LIMIT")
    private Integer frontBufferUpLimit = 0;


    /**
     * 前缓存下限
     */
    @Schema(title = "前缓存下限")
    @TableField("FRONT_BUFFER_LOW_LIMIT")
    private Integer frontBufferLowLimit = 0;


    /**
     * 后缓存上限
     */
    @Schema(title = "后缓存上限")
    @TableField("REAR_BUFFER_UP_LIMIT")
    private Integer rearBufferUpLimit = 0;


    /**
     * 后缓存下限
     */
    @Schema(title = "后缓存下限")
    @TableField("REAR_BUFFER_LOW_LIMIT")
    private Integer rearBufferLowLimit = 0;


    /**
     * 工位长度
     */
    @Schema(title = "工位长度")
    @TableField("STATION_LENGTH")
    @NotNull(message = "工位长度不能为空")
    private Integer stationLength = 0;


    /**
     * 区域类型(1.CONTINUE;2.STOP&GO)
     */
    @Schema(title = "区域类型(0.STOP&GO 1.CONTINUE,2.AGV,3.Discrete,4.other,5.Distribution)")
    @TableField("RUN_TYPE")
    @NotNull(message = "区域类型不能为空")
    private Integer runType = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * JPH
     */
    @Schema(title = "JPH")
    @TableField("LINE_DESIGN_JPH")
    @NotNull(message = "JPH不能为空")
    private Integer lineDesignJph = 0;


    /**
     * 接收线体代码
     */
    @Schema(title = "接收线体代码")
    @TableField("RECEIVE_WORK_CENTER_CODE")
    private String receiveWorkCenterCode = StringUtils.EMPTY;


    /**
     * 岗位DB
     */
    @Schema(title = "岗位DB")
    @TableField("STATION_DB")
    private String stationDb = StringUtils.EMPTY;


    /**
     * 安灯链接
     */
    @Schema(title = "安灯链接")
    @TableField("ANDON_OPC_CONNECT")
    private String andonOpcConnect = StringUtils.EMPTY;


    /**
     * 工单DB
     */
    @Schema(title = "工单DB")
    @TableField("ENTRY_DB")
    private String entryDb = StringUtils.EMPTY;


    /**
     * 工单OPC链接
     */
    @Schema(title = "工单OPC链接")
    @TableField("ENTRY_OPC_CONNECT")
    private String entryOpcConnect = StringUtils.EMPTY;


    /**
     * 工区
     */
    @Schema(title = "工区")
    @TableField("MERGE_LINE")
    private String mergeLine = StringUtils.EMPTY;

    /**
     * TP上线点
     */
    @Schema(title = "TP上线点")
    @TableField("TP_ONLINE_POINT")
    private String tpOnlinePoint = StringUtils.EMPTY;

    /**
     * TP下线点
     */
    @Schema(title = "TP下线点")
    @TableField("TP_OFFLINE_POINT")
    private String tpOfflinePoint = StringUtils.EMPTY;


    /**
     * 是否同步到能力中心
     */
    @Schema(title = "是否同步到能力中心")
    @TableField("IS_SYNC")
    private Boolean isSync = Boolean.TRUE;

    @TableField(exist = false)
    @JSONField(name = "PmWorkStationEntity")
    private List<PmWorkStationEntity> pmWorkStationEntity;

    @TableField(exist = false)
    @JSONField(name = "PmAviEntity")
    private List<PmAviEntity> pmAviEntity;




    @TableField(exist = false)
    private String workshopCode;

    @TableField(exist = false)
    private String lineDeleteFlag;

    @TableField(exist = false)
    private String lineEnableFlag;


}