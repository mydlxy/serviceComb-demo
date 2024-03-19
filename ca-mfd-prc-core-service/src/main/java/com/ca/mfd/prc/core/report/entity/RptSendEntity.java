package com.ca.mfd.prc.core.report.entity;

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
 * @author inkelink
 * @Description: 报表请求记录实体
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表请求记录")
@TableName("PRC_RPT_SEND")
public class RptSendEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RPT_SEND_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("TARGET_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long targetId = Constant.DEFAULT_ID;


    /**
     * 目标类型
     */
    @Schema(title = "目标类型")
    @TableField("TARGET_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer targetType = 0;


    /**
     * 打印代码
     */
    @Schema(title = "打印代码")
    @TableField("BIZ_CODE")
    private String bizCode = StringUtils.EMPTY;


    /**
     * 传入参数
     */
    @Schema(title = "传入参数")
    @TableField("PARAMETERS")
    private String parameters = StringUtils.EMPTY;


    /**
     * 发送次数
     */
    @Schema(title = "发送次数")
    @TableField("SEND_TIMES")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sendTimes = 0;


    /**
     * {T:"状态",D:["0.未发送;1.发送成功;2.发送失败"]}
     */
    //@Schema(title = "{T:"状态",D:["0.未发送;1.发送成功;2.发送失败"]}")
    @Schema(title = "状态:0.未发送;1.发送成功;2.发送失败")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 打印时间
     */
    @Schema(title = "打印时间")
    @TableField("PRINT_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date printDt = new Date();


    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @TableField("SEND_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date sendDt = new Date();


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 发送备注
     */
    @Schema(title = "发送备注")
    @TableField("SEND_REMARK")
    private String sendRemark = StringUtils.EMPTY;


    /**
     * 打印数量
     */
    @Schema(title = "打印数量")
    @TableField("PRINT_NUMBER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer printNumber;

}