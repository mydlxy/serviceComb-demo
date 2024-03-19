package com.ca.mfd.prc.pps.entity;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 电池开工配置实体
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池开工配置")
@TableName("PRC_PPS_PACK_START_WORK_CONFIG")
public class PpsPackStartWorkConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PACK_START_WORK_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 电池类型
     */
    @Schema(title = "电池类型")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


    /**
     * PLC地址
     */
    @Schema(title = "PLC地址")
    @TableField("PLC_ADDRESS")
    private String plcAddress = StringUtils.EMPTY;


    /**
     * DB块
     */
    @Schema(title = "DB块")
    @TableField("PLC_DB")
    private String plcDb = StringUtils.EMPTY;


}