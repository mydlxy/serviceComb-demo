package com.ca.mfd.prc.avi.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 工位物料清单实体
 * @date 2023年09月26日
 * @变更说明 BY inkelink At 2023年09月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工位物料清单")
@TableName("PRC_PM_WORKSTATION_MATERIAL")
public class PmWorkstationMaterialEntity extends PmBaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_WORKSTATION_MATERIAL_ID", type = IdType.INPUT)
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
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("PRC_PM_WORKSHOP_CODE")
    private String prcPmWorkshopCode = StringUtils.EMPTY;


    /**
     * 线体ID
     */
    @Schema(title = "线体ID")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("PRC_PM_LINE_CODE")
    private String prcPmLineCode = StringUtils.EMPTY;


    /**
     * 工位ID
     */
    @Schema(title = "工位ID")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位编码
     */
    @Schema(title = "工位编码")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String prcPmWorkstationCode = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MASTER_CHINESE")
    private String masterChinese = StringUtils.EMPTY;


    /**
     * 物料描述英文
     */
    @Schema(title = "物料描述英文")
    @TableField("MASTER_ENGLISH")
    private String masterEnglish = StringUtils.EMPTY;


    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;


    /**
     * 物料数量
     */
    @Schema(title = "物料数量")
    @TableField("MATERIAL_NUM")
    private Integer materialNum = 0;


}