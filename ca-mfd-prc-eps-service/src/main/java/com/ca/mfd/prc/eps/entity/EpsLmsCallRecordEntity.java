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

import java.util.Date;

/**
 *
 * @Description: 物流拉动呼叫记录实体
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "物流拉动呼叫记录")
@TableName("PRC_EPS_LMS_CALL_RECORD")
public class EpsLmsCallRecordEntity extends BaseEntity {

    /**
     * 主键号
     */
    @Schema(title = "主键号")
    @TableId(value = "PRC_EPS_LMS_CALL_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 目标系统;1、LMS 2、AGV
     */
    @Schema(title = "目标系统;1、LMS 2、AGV")
    @TableField("TARGET_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer targetType = 0;


    /**
     * 拉动类型;1 拉入 2 拉出
     */
    @Schema(title = "拉动类型;1 拉入 2 拉出")
    @TableField("CALL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer callType = 0;


    /**
     * 调度代码
     */
    @Schema(title = "调度代码")
    @TableField("ROUTE_CODE")
    private String routeCode = StringUtils.EMPTY;


    /**
     * 调度名称
     */
    @Schema(title = "调度名称")
    @TableField("ROUTE_NAME")
    private String routeName = StringUtils.EMPTY;


    /**
     * 呼叫时间
     */
    @Schema(title = "呼叫时间")
    @TableField("CALL_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date callDt = new Date();


    /**
     * 是否发送第三方系统
     */
    @Schema(title = "是否发送第三方系统")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


    /**
     * 推送时间
     */
    @Schema(title = "推送时间")
    @TableField("SEND_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date sendDt = new Date();


    /**
     * 来源;1 设备触发 2人工触发
     */
    @Schema(title = "来源;1 设备触发 2人工触发")
    @TableField("SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;


}