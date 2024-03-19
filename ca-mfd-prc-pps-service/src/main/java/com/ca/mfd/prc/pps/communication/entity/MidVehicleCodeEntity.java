package com.ca.mfd.prc.pps.communication.entity;

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
 * @Description: 车型代码中间表实体
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "车型代码中间表")
@TableName("PRC_MID_VEHICLE_CODE")
public class MidVehicleCodeEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_VEHICLE_CODE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 层级
     */
    @Schema(title = "层级")
    @TableField("LEVEL")
    private String level = StringUtils.EMPTY;


    /**
     * 编码
     */
    @Schema(title = "编码")
    @TableField("NODE_CODE")
    private String nodeCode = StringUtils.EMPTY;


    /**
     * 父编码
     */
    @Schema(title = "父编码")
    @TableField("PARENT_NODE_CODE")
    private String parentNodeCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("NODE_NAME")
    private String nodeName = StringUtils.EMPTY;


    /**
     * 业务级别
     */
    @Schema(title = "业务级别")
    @TableField("BUSINESS_LEVEL")
    private String businessLevel = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("DESCRIPTION")
    private String description = StringUtils.EMPTY;


    /**
     * 是否可构建BOM
     */
    @Schema(title = "是否可构建BOM")
    @TableField("IS_BOM_NODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isBomNode = false;


    /**
     * 是否可配置
     */
    @Schema(title = "是否可配置")
    @TableField("IS_CONFIG_NODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isConfigNode = false;


    /**
     * 车型生命周期状态
     */
    @Schema(title = "车型生命周期状态")
    @TableField("VEHICLE_LIFECYCLE_STATUS")
    private String vehicleLifecycleStatus = StringUtils.EMPTY;


    /**
     * 研发代号
     */
    @Schema(title = "研发代号")
    @TableField("RD_CODE")
    private String rdCode = StringUtils.EMPTY;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @TableField("CHECK_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    @TableField("CHECK_RESULT_DESC")
    private String checkResultDesc = StringUtils.EMPTY;


}