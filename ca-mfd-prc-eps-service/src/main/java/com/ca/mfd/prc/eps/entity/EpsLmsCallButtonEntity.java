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
 * @Description: 物流拉动按钮配置实体
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "物流拉动按钮配置")
@TableName("PRC_EPS_LMS_CALL_BUTTON")
public class EpsLmsCallButtonEntity extends BaseEntity {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @TableId(value = "PRC_EPS_LMS_CALL_BUTTON_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间
     */
    @Schema(title = "车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 绑定工位
     */
    @Schema(title = "绑定工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 拉动代码
     */
    @Schema(title = "拉动代码")
    @TableField("ROUTE_CODE")
    private String routeCode = StringUtils.EMPTY;


    /**
     * 拉动名称
     */
    @Schema(title = "拉动名称")
    @TableField("ROUTE_NAME")
    private String routeName = StringUtils.EMPTY;


    /**
     * 拉动类型;1 拉入 2 拉出
     */
    @Schema(title = "拉动类型;1 拉入 2 拉出")
    @TableField("CALL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer callType = 0;


    /**
     * PLC类型;0 软按钮 1 西门子 2 汇川
     */
    @Schema(title = "PLC类型;0 软按钮 1 西门子 2 汇川")
    @TableField("PLC_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer plcType = 0;


    /**
     * PLC链接
     */
    @Schema(title = "PLC链接")
    @TableField("PLC_CONNECTION")
    private String plcConnection = StringUtils.EMPTY;


    /**
     * DB地址
     */
    @Schema(title = "DB地址")
    @TableField("DB")
    private String db = StringUtils.EMPTY;


    /**
     * 目标系统;1、LMS 2、AGV
     */
    @Schema(title = "目标系统;1、LMS 2、AGV")
    @TableField("TARGET_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer targetType = 0;


    /**
     * 是否发送第三方系统
     */
    @Schema(title = "是否发送第三方系统")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


}