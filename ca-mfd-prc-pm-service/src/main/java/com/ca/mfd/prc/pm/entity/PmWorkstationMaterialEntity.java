package com.ca.mfd.prc.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    private String workshopCode = StringUtils.EMPTY;


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
    private String lineCode = StringUtils.EMPTY;


    /**
     * 工位ID
     */
    @Schema(title = "工位ID")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;

    /**
     * MBOM行号
     */
    @Schema(title = "MBOM行号和物料号中间用‘|’分割")
    @TableField("ROW_NUM_AND_MATERIAL_NO")
    private String rowNumAndMaterialNo = StringUtils.EMPTY;


    /**
     * 工位编码
     */
    @Schema(title = "工位编码")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


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
    private Double materialNum = 0D;

    /**
     * 物料单位
     */
    @Schema(title = "物料单位")
    @TableField("MATERIAL_UNIT")
    private String materialUnit = StringUtils.EMPTY;

    /**
     * 是否定编
     */
    @Schema(title = "是否定编")
    @TableField("CUSTOMIZED_FLAG")
    private Boolean customizedFlag = false;


    /**
     * 供货状态
     */
    @Schema(title = "供货状态")
    @TableField("SUPPLY_STATUS")
    private Integer supplyStatus = 0;


    /**
     * 是否切换件
     */
    @Schema(title = "是否切换件")
    @TableField("SWITCH_PARTS_FLAG")
    private Boolean switchPartsFlag = false;


    /**
     * 是否确认
     */
    @Schema(title = "是否确认")
    @TableField("CONFIRM_FLAG")
    private Boolean confirmFlag = false;

    /**
     * 物料使用关系 0：多个工位使用数量相同，如A工位使用或B工位使用
     * 1:一个工位分摊全部物料
     * 2:多个工位分摊物料
     */
    @Schema(title = "物料使用关系")
    @TableField("USE_TYPE")
    private int useType = 1;


    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;


    /**
     * attribute4:辅料卷积模式（定额/实际用量）
     * attribute5:辅料实际用量
     * attribute6:辅料定额用量
     */


}