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
 * @Description: 车间实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "车间")
@TableName("PRC_PM_WORKSHOP")
public class PmWorkShopEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_WORKSHOP_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_ORGANIZATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmOrganizationId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("WORKSHOP_CODE")
    @NotBlank(message = "车间代码不能为空")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("WORKSHOP_NAME")
    @NotBlank(message = "车间名称不能为空")
    private String workshopName = StringUtils.EMPTY;

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
    @TableField("DISPLAY_NO")
    @NotNull(message = "顺序号不能为空")
    private Integer displayNo = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 标准在制
     */
    @Schema(title = "标准在制")
    @TableField("VEHICLE_COUNT")
    private Integer vehicleCount = 0;


    /**
     * 缓存在制
     */
    @Schema(title = "缓存在制")
    @TableField("BUFFER_COUNT")
    private Integer bufferCount = 0;


    /**
     * 缓存下限
     */
    @Schema(title = "缓存下限")
    @TableField("BUFFER_COUNT_LOW_LIMT")
    private Integer bufferCountLowLimt = 0;


    /**
     * JPH
     */
    @Schema(title = "JPH")
    @TableField("WORKSHOP_DESIGN_JPH")
    @NotNull(message = "JPH不能为空")
    private Integer workshopDesignJph = 0;


    /**
     * 最低在制
     */
    @Schema(title = "最低在制")
    @TableField("VEHICLE_MIN")
    private Integer vehicleMin = 0;


    /**
     * 最高在制
     */
    @Schema(title = "最高在制")
    @TableField("VEHICLE_MAX")
    private Integer vehicleMax = 0;

    /**
     * 是否同步到能力中心
     */
    @Schema(title = "是否同步到能力中心")
    @TableField("IS_SYNC")
    private Boolean isSync = Boolean.TRUE;

    @TableField(exist = false)
    @JSONField(name = "PmLineEntity")
    private List<PmLineEntity> pmLineEntity;


}