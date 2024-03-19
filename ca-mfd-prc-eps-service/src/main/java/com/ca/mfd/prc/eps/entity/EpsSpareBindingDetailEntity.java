package com.ca.mfd.prc.eps.entity;

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

/**
 * @author inkelink
 * @Description: 备件绑定明细实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "备件绑定明细")
@TableName("PRC_EPS_SPARE_BINDING_DETAIL")
public class EpsSpareBindingDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_SPARE_BINDING_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 外键
     */
    @Schema(title = "外键")
    @TableField("PRC_EPS_BODY_SPARE_PART_TRACK_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsBodySparePartTrackId = Constant.DEFAULT_ID;


    /**
     * 运输撬的虚拟VIN号
     */
    @Schema(title = "运输撬的虚拟VIN号")
    @TableField("PART_VIRTUAL_VIN")
    private String partVirtualVin = StringUtils.EMPTY;


    /**
     * 备件VIN号
     */
    @Schema(title = "备件VIN号")
    @TableField("SPARE_VIN")
    private String spareVin = StringUtils.EMPTY;


    /**
     * 备件物料名称
     */
    @Schema(title = "备件物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 备件物料编号
     */
    @Schema(title = "备件物料编号")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 队列订阅码
     */
    @Schema(title = "队列订阅码")
    @TableField("AVI_QUEUE_CODE")
    private String aviQueueCode = StringUtils.EMPTY;


}