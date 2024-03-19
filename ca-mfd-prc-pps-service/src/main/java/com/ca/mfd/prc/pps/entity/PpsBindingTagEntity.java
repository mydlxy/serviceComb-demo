package com.ca.mfd.prc.pps.entity;

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
 * @author eric.zhou
 * @Description: 吊牌绑定管理实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "吊牌绑定管理")
@TableName("PRC_PPS_BINDING_TAG")
public class PpsBindingTagEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_BINDING_TAG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 吊牌编码
     */
    @Schema(title = "吊牌编码")
    @TableField("TAG_CODE")
    private String tagCode = StringUtils.EMPTY;

    /**
     * 产品条码
     */
    @Schema(title = "产品条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;

    /**
     * 绑定点类型（1 开工点 2 AVI站点）
     */
    @Schema(title = "绑定点类型（1 开工点 2 AVI站点）")
    @TableField("BINDING_POINT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer bindingPointType = 0;

    /**
     * 绑定介质 1 tag 2 滑橇编号）
     */
    @Schema(title = "绑定介质 1 tag 2 滑橇编号）")
    @TableField("BINDING_MEDIUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer bindingMedium = 1;

    /**
     * 绑定站点编码
     */
    @Schema(title = "绑定站点编码")
    @TableField("BINDING_AVI_CODE")
    private String bindingAviCode = StringUtils.EMPTY;

}