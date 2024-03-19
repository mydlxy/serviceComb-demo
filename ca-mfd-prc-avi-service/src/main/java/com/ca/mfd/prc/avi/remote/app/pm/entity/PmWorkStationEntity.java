package com.ca.mfd.prc.avi.remote.app.pm.entity;

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
import java.util.List;

/**
 * @author inkelink
 * @Description: 岗位实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位")
@TableName("PRC_PM_WORK_STATION")
public class PmWorkStationEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_WORK_STATION_ID", type = IdType.INPUT)
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
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    @NotBlank(message = "工位代码不能为空")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("WORKSTATION_NAME")
    @NotBlank(message = "工位名称不能为空")
    private String workstationName = StringUtils.EMPTY;
    /**
     * 班组
     */
    @Schema(title = "班组")
    @TableField("TEAM_NO")
    private String teamNo = StringUtils.EMPTY;
    /**
     * 生产时间
     */
    @Schema(title = "生产时间")
    @TableField("PRODUCT_TIME")
    @NotNull(message = "生产时间不能为空")
    private Integer productTime = 0;

    /**
     * 工位号
     */
    @Schema(title = "工位号")
    @TableField("WORKSTATION_NO")
    @NotNull(message = "工位号不能为空")
    private Integer workstationNo = 0;


    /**
     * 类型(1.生产岗.2.质量门.3.返修岗)
     */
    @Schema(title = "类型(1.生产岗.2.质量门.3.返修岗)")
    @TableField("WORKSTATION_TYPE")
    @NotNull(message = "类型不能为空")
    private Integer workstationType = 1;


    /**
     * 工位显示顺序号
     */
    @Schema(title = "工位显示顺序号")
    @TableField(exist = false)
    private Integer workstationDisplayNo = 0;


    /**
     * 方位
     */
    @Schema(title = "方位")
    @TableField("DIRECTION")
    @NotNull(message = "方位不能为空")
    private Integer direction = 1;


    /**
     * 开始距离
     */
    @Schema(title = "开始距离")
    @TableField("BEGIN_DISTANCE")
    @NotNull(message = "开始距离不能为空")
    private Integer beginDistance = 0;


    /**
     * 预警距离
     */
    @Schema(title = "预警距离")
    @NotNull(message = "预警距离不能为空")
    @TableField("ALARM_DISTANCE")
    private Integer alarmDistance = 0;


    /**
     * 结束距离
     */
    @Schema(title = "结束距离")
    @TableField("END_DISTANCE")
    @NotNull(message = "结束距离不能为空")
    private Integer endDistance = 0;


    /**
     * 过点去向
     */
    @Schema(title = "过点去向")
    @TableField("ROUTE_PATH")
    private String routePath = StringUtils.EMPTY;


    /**
     * 过点确认
     */
    @Schema(title = "过点确认")
    @TableField("ROUTE_CHECK")
    private String routeCheck = StringUtils.EMPTY;


    /**
     * 工位关联质量门
     */
    @Schema(title = "工位关联质量门")
    @TableField("STATIONS")
    private String stations = StringUtils.EMPTY;


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
     * 虚拟AVI
     */
    @Schema(title = "虚拟AVI")
    @TableField("IS_AVI")
    private Boolean isAvi = false;


    /**
     * 是否同步到能力中心
     */
    @Schema(title = "是否同步到能力中心")
    @TableField("IS_SYNC")
    private Boolean isSync = Boolean.TRUE;


    @TableField(exist = false)
    @JSONField(name = "PmWoEntity")
    private List<PmWoEntity> pmWoEntity;
    @TableField(exist = false)
    @JSONField(name = "PmOtEntity")
    private List<PmOtEntity> pmOtEntity;
    @TableField(exist = false)
    @JSONField(name = "PmPullCordEntity")
    private List<PmPullCordEntity> pmPullCordEntity;
    @TableField(exist = false)
    @JSONField(name = "PmToolEntity")
    private List<PmToolEntity> pmToolEntity;
    @TableField(exist = false)
    @JSONField(name = "PmBopEntity")
    private List<PmBopEntity> pmBopEntity;
    @TableField(exist = false)
    @JSONField(name = "PmWorkstationMaterialEntity")
    private List<PmWorkstationMaterialEntity> pmWorkstationMaterialEntity;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;


}