package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @author inkelink ${email}
 * @Description: 队列发布数据表
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "队列发布数据表")
//@TableName("AVI_QUEUE_RELEASE")
@TableName("PRC_AVI_QUEUE_RELEASE")
public class AviQueueReleaseEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_QUEUE_RELEASE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 厂商代码
     */
    //    @Schema(title = "厂商代码")
    //    @TableField("CODE")
    //	private String code = StringUtils.EMPTY;
    @Schema(title = "厂商代码")
    @TableField("QUEUE_CODE")
    private String queueCode = StringUtils.EMPTY;

    /**
     * 厂商名称
     */
    //    @Schema(title = "厂商名称")
    //    @TableField("NAME")
    //    private String name = StringUtils.EMPTY;
    @Schema(title = "厂商名称")
    @TableField("QUEUE_NAME")
    private String queueName = StringUtils.EMPTY;


    /**
     * 产品唯一编码
     */
    //    @Schema(title = "对象编号")
    //    @TableField("SN")
    //    private String sn = StringUtils.EMPTY;
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 对象类型(1、整车，2、SP件，3、PACK)
     */
    //    @Schema(title = "对象类型(1、整车，2、SP件，3、PACK)")
    //    @TableField("ORDER_CATEGORY")
    //    private String orderCategory = StringUtils.EMPTY;
    @Schema(title = "对象类型(1、整车，2、SP件，3、PACK)")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * AVI站点ID
     */
    //    @Schema(title = "AVI站点ID")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    //    @Schema(title = "车间代码")
    //    @TableField("PM_AREA_CODE")
    //    private String pmAreaCode = StringUtils.EMPTY;
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    //    @Schema(title = "线体代码")
    //    @TableField("PM_WORK_CENTER_CODE")
    //    private String pmWorkCenterCode = StringUtils.EMPTY;
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    //    @Schema(title = "AVI代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 接收 PLC
     */
    //    @Schema(title = "接收PLC")
    //    @TableField("PLC_IP")
    //    private String plcIp = StringUtils.EMPTY;
    @Schema(title = "接收 PLC")
    @TableField("PLC_IP")
    private String plcIp = StringUtils.EMPTY;

    /**
     * 接收PLC类型
     */
    //    @Schema(title = "接收PLC类型")
    //    @TableField("PLC_MODE")
    //    private String plcMode = StringUtils.EMPTY;
    @Schema(title = "接收PLC类型")
    @TableField("PLC_MODE")
    private String plcMode = StringUtils.EMPTY;

    /**
     * 接收 DB
     */
    //    @Schema(title = "接收DB")
    //    @TableField("DB_NAME")
    //    private String dbName = StringUtils.EMPTY;
    @Schema(title = "接收 DB")
    @TableField("DB_NAME")
    private String dbName = StringUtils.EMPTY;

    /**
     * PASS TIME
     */
    //    @Schema(title = "Passtime")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //    @JsonDeserialize(using = JsonDeserializeDate.class)
    //    @TableField("INSERT_DT")
    //    private Date insertDt;
    @Schema(title = "PASS TIME")
    @TableField("INSERT_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date insertDt;

    /**
     * 备注
     */
    //    @Schema(title = "备注")
    //    @TableField("REMARK")
    //    private String remark = StringUtils.EMPTY;
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 发送标识
     */
    //    @Schema(title = "发送标识")
    //    @TableField("IS_SEND")
    //    private Boolean isSend = false;
    @Schema(title = "发送标识")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isSend = false;


    /**
     * 队列顺序号
     */
    //    @Schema(title = "队列顺序号")
    //    @TableField("DISPLAY_NO")
    //    private String displayNo = StringUtils.EMPTY;
    @Schema(title = "队列顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;

}
