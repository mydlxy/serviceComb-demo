package com.ca.mfd.prc.pmc.remote.app.pm.dto;

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
 * 线体对象
 *
 * @author jay.he
 * @since 1.0.0 2023-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "线体对象")
public class LineVO {

   /* @Schema(title = "线体ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmLineId;

    @Schema(title = "车间ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmShopId;

    @Schema(title = "线体代码")
    private String pmLineCode;

    @Schema(title = "线体名称")
    private String pmLineName;*/
    /**
     *
     */
    @Schema(title = "")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 版本号
     */
    @Schema(title = "版本号")
    private Integer version = 0;


    /**
     *
     */
    @Schema(title = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;

    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    private String workshopName = StringUtils.EMPTY;


    /**
     * 代码
     */
    @Schema(title = "代码")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    private String lineName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    private String lineRemark = StringUtils.EMPTY;
    /**
     * 生产时间
     */
    @Schema(title = "生产时间")
    private Integer productTime = 0;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer lineDisplayNo = 0;


    /**
     * 工位数量
     */
    @Schema(title = "工位数量")
    private Integer stationCount = 0;


    /**
     * 线体开始距离
     */
    @Schema(title = "线体开始距离")
    private Integer beginDistance = 0;


    /**
     * 线体结束距离
     */
    @Schema(title = "线体结束距离")
    private Integer endDistance = 0;


    /**
     * 线体类型(1.主线；2.辅线)
     */
    @Schema(title = "线体类型(1.主线；2.辅线)")
    private Integer lineType = 0;


    /**
     * 队列DB
     */
    @Schema(title = "队列DB")
    private String queueDb = StringUtils.EMPTY;


    /**
     * 备用DB
     */
    @Schema(title = "备用DB")
    private String plcDb3 = StringUtils.EMPTY;


    /**
     * 安灯DB
     */
    @Schema(title = "安灯DB")
    private String andonDb = StringUtils.EMPTY;


    /**
     * 线体链接
     */
    @Schema(title = "线体链接")
    private String opcConnect = StringUtils.EMPTY;


    /**
     * 前缓存上限
     */
    @Schema(title = "前缓存上限")
    private Integer frontBufferUpLimit = 0;


    /**
     * 前缓存下限
     */
    @Schema(title = "前缓存下限")
    private Integer frontBufferLowLimit = 0;


    /**
     * 后缓存上限
     */
    @Schema(title = "后缓存上限")
    private Integer rearBufferUpLimit = 0;


    /**
     * 后缓存下限
     */
    @Schema(title = "后缓存下限")
    private Integer rearBufferLowLimit = 0;


    /**
     * 工位长度
     */
    @Schema(title = "工位长度")
    private Integer stationLength = 0;


    /**
     * 区域类型(1.CONTINUE;2.STOP&GO)
     */
    @Schema(title = "区域类型(1.CONTINUE;2.STOP&GO)")
    private Integer runType = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    private Boolean isEnable = false;


    /**
     * JPH
     */
    @Schema(title = "JPH")
    private Integer lineDesignJph = 0;


    /**
     * 接收线体代码
     */
    @Schema(title = "接收线体代码")
    private String receiveWorkCenterCode = StringUtils.EMPTY;


    /**
     * 岗位DB
     */
    @Schema(title = "岗位DB")
    private String stationDb = StringUtils.EMPTY;


    /**
     * 安灯链接
     */
    @Schema(title = "安灯链接")
    private String andonOpcConnect = StringUtils.EMPTY;


    /**
     * 工单DB
     */
    @Schema(title = "工单DB")
    private String entryDb = StringUtils.EMPTY;


}
