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
 * @Description: 焊装车间备件运输跟踪日志实体
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "焊装车间备件运输跟踪日志")
@TableName("PRC_EPS_BODY_SPARE_PART_TRACK_LOG")
public class EpsBodySparePartTrackLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_BODY_SPARE_PART_TRACK_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 外键主键
     */
    @Schema(title = "外键主键")
    @TableField("PRC_EPS_BODY_SPARE_PART_TRACK_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsBodySparePartTrackId = Constant.DEFAULT_ID;


    /**
     * 虚拟VIN号
     */
    @Schema(title = "虚拟VIN号")
    @TableField("VIRTUAL_VIN")
    private String virtualVin = StringUtils.EMPTY;


    /**
     * 1 等待下发 2 已下发 3 已绑定备件 4 放撬成功 5 完成生产
     */
    @Schema(title = "1 等待下发 2 已下发 3 已绑定备件 4 放撬成功 5 完成生产")
    @TableField("TRACK_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer trackStatus = 0;


    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @TableField("OP_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date opTime;


}