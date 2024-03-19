package com.ca.mfd.prc.eps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 熔化炉投料记录实体
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "熔化炉投料记录")
@TableName("PRC_EPS_FURNACE_PUT_LOG")
public class EpsFurnacePutLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_FURNACE_PUT_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 熔炉编号
     */
    @Schema(title = "熔炉编号")
    @TableField("FURNACE_NO")
    private String furnaceNo = StringUtils.EMPTY;


    /**
     * 班次ID
     */
    @Schema(title = "班次ID")
    @TableField("SHIFT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long shiftId = Constant.DEFAULT_ID;


    /**
     * 班次名称
     */
    @Schema(title = "班次名称")
    @TableField("SHIFT_NAME")
    private String shiftName = StringUtils.EMPTY;


    /**
     * 批次;从LMS获取
     */
    @Schema(title = "批次;从LMS获取")
    @TableField("LOT_NO")
    private String lotNo = StringUtils.EMPTY;


    /**
     * 投料类型;1、新料 2、废料
     */
    @Schema(title = "投料类型;1、新料 2、废料")
    @TableField("PUT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer putType = 0;


    /**
     * 重量
     */
    @Schema(title = "重量")
    @TableField("WEIGHT")
    private BigDecimal weight = BigDecimal.valueOf(0);


    /**
     * 来源;1、手动 2自动
     */
    @Schema(title = "来源;1、手动 2自动")
    @TableField("SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;


    /**
     * 记录标识;自动模式下记录下位请求标识
     */
    @Schema(title = "记录标识;自动模式下记录下位请求标识")
    @TableField("REQUEST_FLAG")
    private String requestFlag = StringUtils.EMPTY;


}