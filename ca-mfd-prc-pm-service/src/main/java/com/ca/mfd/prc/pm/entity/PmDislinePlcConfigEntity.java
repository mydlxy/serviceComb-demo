package com.ca.mfd.prc.pm.entity;

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
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 分布线体PLC配置实体
 * @author inkelink
 * @date 2023年10月28日
 * @变更说明 BY inkelink At 2023年10月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "分布线体PLC配置")
@TableName("PRC_PM_DISLINE_PLC_CONFIG")
public class PmDislinePlcConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_DISLINE_PLC_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间ID
     */
    @Schema(title = "车间ID")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 线体ID
     */
    @Schema(title = "线体ID")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("PRC_PM_LINE_CODE")
    private String prcPmLineCode = StringUtils.EMPTY;


    /**
     * 线体链接
     */
    @Schema(title = "线体链接")
    @TableField("LINE_PLC_CONNECT")
    private String linePlcConnect = StringUtils.EMPTY;


    /**
     * 线体DB
     */
    @Schema(title = "线体DB")
    @TableField("LINE_DB_NAME")
    private String lineDbName = StringUtils.EMPTY;


    /**
     * Andon链接
     */
    @Schema(title = "Andon链接")
    @TableField("ANDON_PLC_CONNECT")
    private String andonPlcConnect = StringUtils.EMPTY;


    /**
     * AndonDB
     */
    @Schema(title = "AndonDB")
    @TableField("ANDON_DB_NAME")
    private String andonDbName = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


}