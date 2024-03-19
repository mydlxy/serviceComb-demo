package com.ca.mfd.prc.pps.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 配置字详情实体
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "配置字详情")
@TableName("PRC_MID_SOFTWARE_CONFIG_DETAIL")
public class MidSoftwareConfigDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_SOFTWARE_CONFIG_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 配置字ID
     */
    @Schema(title = "配置字ID")
    @TableField("PRC_MID_SOFTWARE_BOM_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidSoftwareBomConfigId = Constant.DEFAULT_ID;


    /**
     * DID
     */
    @Schema(title = "DID")
    @TableField("DID")
    private String did = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("DESCRIPTION")
    private String description = StringUtils.EMPTY;


    /**
     * 配置码
     */
    @Schema(title = "配置码")
    @TableField("ANALYSIS_VALUE")
    private String analysisValue = StringUtils.EMPTY;


}