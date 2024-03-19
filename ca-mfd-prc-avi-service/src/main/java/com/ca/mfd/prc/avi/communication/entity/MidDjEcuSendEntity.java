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
 *
 * @Description: 电检软件信息下发记录实体
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电检软件信息下发记录")
@TableName("PRC_MID_DJ_ECU_SEND")
public class MidDjEcuSendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_DJ_ECU_SEND_ID", type = IdType.INPUT)
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
     * 车型系列代码
     */
    @Schema(title = "车型系列代码")
    @TableField("TYPE_CODE")
    private String typeCode = StringUtils.EMPTY;


    /**
     * 车型状态号
     */
    @Schema(title = "车型状态号")
    @TableField("CAR_CODE")
    private String carCode = StringUtils.EMPTY;


    /**
     * 选装件
     */
    @Schema(title = "选装件")
    @TableField("OPTION_CODE")
    private String optionCode = StringUtils.EMPTY;


    /**
     * 定制编码
     */
    @Schema(title = "定制编码")
    @TableField("ATTRIBUTE_CODE")
    private String attributeCode = StringUtils.EMPTY;


    /**
     * 软件清单
     */
    @Schema(title = "软件清单")
    @TableField("ECU_LIST")
    private String ecuList = StringUtils.EMPTY;


}