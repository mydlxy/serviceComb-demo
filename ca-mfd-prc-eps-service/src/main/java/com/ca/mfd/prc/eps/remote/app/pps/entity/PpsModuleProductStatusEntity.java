package com.ca.mfd.prc.eps.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 模组相关产品状态实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "模组相关产品状态")
@TableName("PRC_PPS_MODULE_PRODUCT_STATUS")
public class PpsModuleProductStatusEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_PRODUCT_STATUS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 产品条码
     */
    @Schema(title = "产品条码")
    @TableField("PRODUCT_BARCODE")
    private String productBarcode = StringUtils.EMPTY;


    /**
     * 产品状态 1 正常 2 异常 3 报废
     */
    @Schema(title = "产品状态 1 正常 2 异常 3 报废")
    @TableField("PRODUCT_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer productStatus = 1;


}