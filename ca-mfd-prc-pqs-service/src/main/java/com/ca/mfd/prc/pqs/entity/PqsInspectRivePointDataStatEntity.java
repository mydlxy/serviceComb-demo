package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 *
 * @Description: SPR铆点数据统计表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "SPR铆点数据统计表")
@TableName("PRC_PQS_INSPECT_RIVE_POINT_DATA_STAT")
public class PqsInspectRivePointDataStatEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_RIVE_POINT_DATA_STAT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;


    /**
     * SPR铆点总数
     */
    @Schema(title = "SPR铆点总数")
    @TableField("TOTAL_NUMBER_RIVE_POINTS")
    private Integer totalNumberRivePoints = 0;


    /**
     * 关重SPR铆点点数
     */
    @Schema(title = "关重SPR铆点点数")
    @TableField("NUMBER_SPR_RIVE_POINTS")
    private Integer numberSprRivePoints = 0;


    /**
     * SPR铆点不合格数
     */
    @Schema(title = "SPR铆点不合格数")
    @TableField("NUMBER_SPR_RIVE_POINTS_THAT_ARE_NOT_QUAL")
    private Integer numberSprRivePointsThatAreNotQual = 0;


    /**
     * 关重SPR铆点不合格数
     */
    @Schema(title = "关重SPR铆点不合格数")
    @TableField("NUMBER_NON_CONF_SPR_RIVE_POINTS")
    private Integer numberNonConfSprRivePoints = 0;


    /**
     * 不合格SPR铆点分布比率
     */
    @Schema(title = "不合格SPR铆点分布比率")
    @TableField("DIST_RATIO_UNQU_SPR_RIVE_POINTS")
    private BigDecimal distRatioUnquSprRivePoints = BigDecimal.valueOf(0);


    /**
     * 关重不合格SPR铆点分布比率
     */
    @Schema(title = "关重不合格SPR铆点分布比率")
    @TableField("DIST_RATIO_SPR_RIVE_POINTS_WITH_UNQU_WEIG")
    private BigDecimal distRatioSprRivePointsWithUnquWeig = BigDecimal.valueOf(0);


    /**
     * 不合格铆点编号
     */
    @Schema(title = "不合格铆点编号")
    @TableField("UNQU_RIVET_POINT_NUMBER")
    private String unquRivetPointNumber = StringUtils.EMPTY;


    /**
     * 不合格铆点检测图片
     */
    @Schema(title = "不合格铆点检测图片")
    @TableField("UNQU_RIVET_POINT_DETEC_IMAGE")
    private String unquRivetPointDetecImage = StringUtils.EMPTY;


    /**
     * 左互锁值
     */
    @Schema(title = "左互锁值")
    @TableField("LEFT_INTERLOCK_VALUE")
    private String leftInterlockValue = StringUtils.EMPTY;


    /**
     * 右互锁值
     */
    @Schema(title = "右互锁值")
    @TableField("RIGHT_INTERLOCK_VALUE")
    private String rightInterlockValue = StringUtils.EMPTY;


    /**
     * 底层板剩余材料厚度
     */
    @Schema(title = "底层板剩余材料厚度")
    @TableField("REMA_MATE_THIC_BOTT_PLATE")
    private String remaMateThicBottPlate = StringUtils.EMPTY;


    /**
     * 铆钉是否断裂
     */
    @Schema(title = "铆钉是否断裂")
    @TableField("RIVET_BROKEN_FLAG")
    private Boolean rivetBrokenFlag = false;


    /**
     * 铆钉是否屈服
     */
    @Schema(title = "铆钉是否屈服")
    @TableField("DOES_RIVET_YIELD_FLAG")
    private Boolean doesRivetYieldFlag = false;


    /**
     * 纽扣裂纹是否贯穿底板
     */
    @Schema(title = "纽扣裂纹是否贯穿底板")
    @TableField("DOES_BUTTON_CRACK_PENETRATE_BOTTOM_PLATE_FLAG")
    private Boolean doesButtonCrackPenetrateBottomPlateFlag = false;


    /**
     * 是否出现板材内部裂纹
     */
    @Schema(title = "是否出现板材内部裂纹")
    @TableField("THERE_ANY_INTERNAL_CRACK_BOARD_FLAG")
    private Boolean thereAnyInternalCrackBoardFlag = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}