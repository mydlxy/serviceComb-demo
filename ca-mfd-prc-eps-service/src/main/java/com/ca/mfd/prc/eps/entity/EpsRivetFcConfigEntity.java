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
 * @Description: 铆钉防错配置实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "铆钉防错配置")
@TableName("PRC_EPS_RIVET_FC_CONFIG")
public class EpsRivetFcConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_RIVET_FC_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 网络地址
     */
    @Schema(title = "网络地址")
    @TableField("ADDRESS_IP")
    private String addressIp = StringUtils.EMPTY;


    /**
     * 数据DB
     */
    @Schema(title = "数据DB")
    @TableField("DATA_DB")
    private String dataDb = StringUtils.EMPTY;


    /**
     * 料口条码
     */
    @Schema(title = "料口条码")
    @TableField("ORIFICE_BARCODE")
    private String orificeBarcode = StringUtils.EMPTY;


    /**
     * 物料名称
     */
    @Schema(title = "物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 料口位置
     */
    @Schema(title = "料口位置")
    @TableField("ORIFICE_LOCATION")
    private String orificeLocation = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("DESCRIPTION")
    private String description = StringUtils.EMPTY;


}