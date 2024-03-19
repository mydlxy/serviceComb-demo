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
 * @Description: 拧紧指示配置实体
 * @author inkelink
 * @date 2024年01月24日
 * @变更说明 BY inkelink At 2024年01月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "拧紧指示配置")
@TableName("PRC_PM_SCR_CONFIG")
public class PmScrConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_SCR_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * job号
     */
    @Schema(title = "JOB号")
    @TableField("JOB_NO")
    private String jobNo = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工艺编码")
    @TableField("PM_WO_CODE")
    private String pmWoCode = StringUtils.EMPTY;


    /**
     * CONFIG_JSON
     */
    @Schema(title = "CONFIG_JSON")
    @TableField("CONFIG_JSON")
    private String configJson = StringUtils.EMPTY;

    /**
     * pmWoId
     */
    @Schema(title = "pmWoId")
    @TableField(exist = false)
    private String pmWoId = StringUtils.EMPTY;





}