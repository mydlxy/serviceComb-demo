package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 总装车间放撬下发日志实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "总装车间放撬下发日志")
@TableName("PRC_EPS_ASSEMBLY_PRIZE_CONTROL_DOWN_LOG")
public class EpsAssemblyPrizeControlDownLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_ASSEMBLY_PRIZE_CONTROL_DOWN_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 控制点名称
     */
    @Schema(title = "控制点名称")
    @TableField("CONTROL_POINT_NAME")
    private String controlPointName = StringUtils.EMPTY;

    /**
     * PLC地址
     */
    @Schema(title = "PLC地址")
    @TableField("PLC_ADDRESS")
    private String plcAddress = StringUtils.EMPTY;

    /**
     * 点位地址
     */
    @Schema(title = "点位地址")
    @TableField("DB_POINT")
    private String dbPoint = StringUtils.EMPTY;

    /**
     * 报错信息
     */
    @Schema(title = "报错信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage = StringUtils.EMPTY;

    /**
     * 空撬数量
     */
    @Schema(title = "空撬数量")
    @TableField("EMPTY_PRIZE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer emptyPrizeNum = 0;


    /**
     * 下发时间
     */
    @Schema(title = "下发时间")
    @TableField("DOWN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date downTime;


}