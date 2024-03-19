package com.ca.mfd.prc.pps.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工厂排班实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工厂排班")
@TableName("PRC_PM_SHC_SHIFT")
public class PmShcShiftEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_SHC_SHIFT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("SHIFT_NAME")
    private String shiftName = StringUtils.EMPTY;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("SHIFT_CODE")
    private Integer shiftCode = 0;


    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @TableField("START_TIME")
    private String startTime = StringUtils.EMPTY;


    /**
     * 开始时间跨天
     */
    @Schema(title = "开始时间跨天")
    @TableField("IS_DAY_BEFORE")
    private Boolean isDayBefore = false;


    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @TableField("END_TIME")
    private String endTime = StringUtils.EMPTY;


    /**
     * 产能
     */
    @Schema(title = "产能")
    @TableField("PRODUCTIVITY")
    private int productivity = 0;


    /**
     * 结束时间跨天
     */
    @Schema(title = "结束时间跨天")
    @TableField("IS_DAY_AFTER")
    private Boolean isDayAfter = false;
    /**
     * 休息时间
     */
    @Schema(title = "休息时间")
    @TableField(exist = false)
    private List<PmShcBreakEntity> pmShcBreakInfos;

}