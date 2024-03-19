package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 参数预警记录实体
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "参数预警记录")
@TableName("PRC_PQS_TECHNOLOGY_ALARM_RECORD")
public class PqsTechnologyAlarmRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_TECHNOLOGY_ALARM_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 策略编号
     */
    @Schema(title = "策略编号")
    @TableField("POLICY_NO")
    private String policyNo = StringUtils.EMPTY;


    /**
     * 控制特征
     */
    @Schema(title = "控制特征")
    @TableField("CONTROL_PROPERTY")
    private String controlProperty = StringUtils.EMPTY;


    /**
     * 区域
     */
    @Schema(title = "区域")
    @TableField("AERA")
    private String aera = StringUtils.EMPTY;


    /**
     * 点位代码
     */
    @Schema(title = "点位代码")
    @TableField("POINT_CODE")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long pointCode = Constant.DEFAULT_ID;


    /**
     * 严重度（S)
     */
    @Schema(title = "严重度（S)")
    @TableField("YZD")
    private String yzd = StringUtils.EMPTY;


    /**
     * 频度预估（O）
     */
    @Schema(title = "频度预估（O）")
    @TableField("PDYG")
    private String pdyg = StringUtils.EMPTY;


    /**
     * 探测度（D）
     */
    @Schema(title = "探测度（D）")
    @TableField("TCD")
    private String tcd = StringUtils.EMPTY;


    /**
     * RPN
     */
    @Schema(title = "RPN")
    @TableField("RPN")
    private String rpn = StringUtils.EMPTY;


    /**
     * 是否超差
     */
    @Schema(title = "是否超差")
    @TableField("IS_OVERPROOF")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOverproof = false;


    /**
     * 是否停机
     */
    @Schema(title = "是否停机")
    @TableField("IS_STOP")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long isStop = Constant.DEFAULT_ID;


    /**
     * 次数
     */
    @Schema(title = "次数")
    @TableField("COUNTER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer counter = 0;


    /**
     * 处理方式
     */
    @Schema(title = "处理方式")
    @TableField("HANDLE_METHOD")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean handleMethod = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 结果
     */
    @Schema(title = "结果")
    @TableField("RESULT_VALUE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean resultValue = false;


    /**
     * 上限
     */
    @Schema(title = "上限")
    @TableField("UPPER_LIMIT")
    private String upperLimit = StringUtils.EMPTY;


    /**
     * 下限
     */
    @Schema(title = "下限")
    @TableField("LOWER_LIMIT")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long lowerLimit = Constant.DEFAULT_ID;


    /**
     * 报警时间
     */
    @Schema(title = "报警时间")
    @TableField("ALARM_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date alarmDt = new Date();


    /**
     * 是否发送QMS
     */
    @Schema(title = "是否发送QMS")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @TableField("SEND_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date sendDate = new Date();


}