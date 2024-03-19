package com.ca.mfd.prc.avi.communication.entity;

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
 * @author inkelink
 * @Description: 4EP数据接口记录实体
 * @date 2023年12月27日
 * @变更说明 BY inkelink At 2023年12月27日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "EP数据接口记录")
@TableName("PRC_MID_DJ_EP_SEND")
public class MidDjEpSendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_DJ_EP_SEND_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * VIN号
     */
    @Schema(title = "VIN号")
    @TableField("VIN_CODE")
    private String vinCode = StringUtils.EMPTY;


    /**
     * ECU类型
     */
    @Schema(title = "控制器类型")
    @TableField("ECU_TYPE_CODE")
    private String ecuTypeCode = StringUtils.EMPTY;


    /**
     * 条码信息
     */
    @Schema(title = "条码信息")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 零件物料号
     */
    @Schema(title = "零件物料号")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * ECU唯一识别号
     */
    @Schema(title = "ECU唯一识别号")
    @TableField("ECU_TUID")
    private String ecuTuid = StringUtils.EMPTY;


    /**
     * 参数1
     */
    @Schema(title = "参数1")
    @TableField("PARAMETER1")
    private String parameter1 = StringUtils.EMPTY;

    /**
     * 参数2
     */
    @Schema(title = "参数2")
    @TableField("PARAMETER2")
    private String parameter2 = StringUtils.EMPTY;

    /**
     * 参数3
     */
    @Schema(title = "参数3")
    @TableField("PARAMETER3")
    private String parameter3 = StringUtils.EMPTY;

    /**
     * 参数4
     */
    @Schema(title = "参数4")
    @TableField("PARAMETER4")
    private String parameter4 = StringUtils.EMPTY;

    /**
     * 参数5
     */
    @Schema(title = "参数5")
    @TableField("PARAMETER5")
    private String parameter5 = StringUtils.EMPTY;

    /**
     * 参数6
     */
    @Schema(title = "参数6")
    @TableField("PARAMETER6")
    private String parameter6 = StringUtils.EMPTY;

}