package com.ca.mfd.prc.pm.communication.entity;

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
 * @Description: 国家代码中间表实体
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "国家代码中间表")
@TableName("PRC_MID_PM_COUNTRY")
public class MidPmCountryEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_PM_COUNTRY_ID", type = IdType.INPUT)
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
     * 代码
     */
    @Schema(title = "代码")
    @TableField("NATION_CODE")
    private String nationCode = StringUtils.EMPTY;


    /**
     * 国家名称
     */
    @Schema(title = "国家名称")
    @TableField("NATION_NAME")
    private String nationName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("NATION_REMARK")
    private String nationRemark = StringUtils.EMPTY;


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
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @TableField("SUB_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @TableField("OP_CODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer opCode = 0;


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