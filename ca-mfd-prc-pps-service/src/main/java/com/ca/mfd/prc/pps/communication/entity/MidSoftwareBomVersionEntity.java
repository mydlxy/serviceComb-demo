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
 * @Description: 单车软件清单版本实体
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "单车软件清单版本")
@TableName("PRC_MID_SOFTWARE_BOM_VERSION")
public class MidSoftwareBomVersionEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_SOFTWARE_BOM_VERSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;

    /**
     * 整车大版本发布单号
     */
    @Schema(title = "整车大版本发布单号")
    @TableField("PUBLISH_CHANGE_CODE")
    private String publishChangeCode = StringUtils.EMPTY;


    /**
     * 产品物料号
     */
    @Schema(title = "整车物料号")
    @TableField("VEHICLE_MATERIAL_NUMBER")
    private String vehicleMaterialNumber = StringUtils.EMPTY;


    /**
     * 校验码
     */
    @Schema(title = "校验码")
    @TableField("CHECK_CODE")
    private String checkCode = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;
}