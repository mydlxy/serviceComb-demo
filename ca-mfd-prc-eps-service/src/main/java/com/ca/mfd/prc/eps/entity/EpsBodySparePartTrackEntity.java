package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 焊装车间备件运输跟踪实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "焊装车间备件运输跟踪")
@TableName("PRC_EPS_BODY_SPARE_PART_TRACK")
public class EpsBodySparePartTrackEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_BODY_SPARE_PART_TRACK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 虚拟VIN号
     */
    @Schema(title = "虚拟VIN号")
    @TableField("VIRTUAL_VIN")
    private String virtualVin = StringUtils.EMPTY;


    /**
     * 绑定工位名称
     */
    @Schema(title = "绑定工位名称")
    @TableField("BINDING_WORKSTATION_NAME")
    private String bindingWorkstationName = StringUtils.EMPTY;


    /**
     * 绑定工位编码
     */
    @Schema(title = "绑定工位编码")
    @TableField("BINDING_WORKSTATION_CODE")
    private String bindingWorkstationCode = StringUtils.EMPTY;

    /**
     * 跟踪状态 1 等待下发 2 已下发  3 放撬成功
     */
    @Schema(title = "跟踪状态 1 等待下发 2 已下发  3 放撬成功")
    @TableField("TRACK_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer trackStatus = 1;

    /**
     * 是否绑定备件
     */
    @Schema(title = "是否绑定备件")
    @TableField("IS_BINDING_SPARE_PART")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isBindingSparePart = false;


    /**
     * 是否绑定备件
     */
    @Schema(title = "是否绑定备件")
    @TableField(exist = false)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnter = false;
}