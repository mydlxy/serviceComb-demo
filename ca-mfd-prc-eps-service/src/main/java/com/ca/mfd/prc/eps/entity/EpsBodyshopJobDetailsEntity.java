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
 * @Description: 焊装车间执行码详情
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "焊装车间执行码详情")
@TableName("PRC_EPS_BODYSHOP_JOB_DETAILS")
public class EpsBodyshopJobDetailsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_BODYSHOP_JOB_DETAILS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 配置外键
     */
    @Schema(title = "配置外键")
    @TableField(value = "PRC_EPS_BODYSHOP_JOB_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long epsBodyshopJobConfigId = Constant.DEFAULT_ID;


    /**
     * 默认的job
     */
    @Schema(title = "默认的job")
    @TableField("JOB_VALUE")
    private String jobValue = StringUtils.EMPTY;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


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


//    /**
//     * 状态码
//     */
//    @Schema(title = "状态码")
//    @TableField("STATE_CODE")
//    private String stateCode = StringUtils.EMPTY;


//    /**
//     * PLC执行码
//     */
//    @Schema(title = "PLC执行码")
//    @TableField("JOB_NO")
//    private String jobNo = StringUtils.EMPTY;


}