package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 *
 * @Description: 预成组请求入箱记录实体
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "预成组请求入箱记录")
@TableName("PRC_EPS_MODULE_REQUEST_INBOX_LOG")
public class EpsModuleRequestInboxLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_REQUEST_INBOX_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 涂胶日志外键
     */
    @Schema(title = "涂胶日志外键")
    @TableField("PRC_EPS_MODULE_GELATINIZE_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcEpsModuleGelatinizeLogId = Constant.DEFAULT_ID;


    /**
     * 电池RIN码
     */
    @Schema(title = "电池RIN码")
    @TableField("RIN")
    private String rin = StringUtils.EMPTY;


    /**
     * 涂胶工位
     */
    @Schema(title = "涂胶工位")
    @TableField("GELATINIZE_STATION_CODE")
    private String gelatinizeStationCode = StringUtils.EMPTY;


    /**
     * 涂胶时间
     */
    @Schema(title = "涂胶时间")
    @TableField("GELATINIZE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date gelatinizeTime = new Date();


    /**
     * 入箱工位
     */
    @Schema(title = "入箱工位")
    @TableField("INBOX_STATION")
    private String inboxStation = StringUtils.EMPTY;


    /**
     * 入箱时间
     */
    @Schema(title = "入箱时间")
    @TableField("INBOX_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date inboxTime = new Date();


    /**
     * 有效间隔时间
     */
    @Schema(title = "有效间隔时间")
    @TableField("VALID_INTERVAL_TIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer validIntervalTime;


    /**
     * 实际间隔时间
     */
    @Schema(title = "实际间隔时间")
    @TableField("ACTUAL_INTERVAL_TIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer actualIntervalTime;


    /**
     * 是否超时
     */
    @Schema(title = "是否超时")
    @TableField("IS_OVERTIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOvertime = false;


}