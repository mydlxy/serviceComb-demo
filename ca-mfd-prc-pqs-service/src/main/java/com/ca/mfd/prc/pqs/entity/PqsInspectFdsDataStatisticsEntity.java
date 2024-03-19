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
 * @Description: FDS数据统计表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "FDS数据统计表")
@TableName("PRC_PQS_INSPECT_FDS_DATA_STATISTICS")
public class PqsInspectFdsDataStatisticsEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_FDS_DATA_STATISTICS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;


    /**
     * 流钻攻丝铆点（FDS）点总数
     */
    @Schema(title = "流钻攻丝铆点（FDS）点总数")
    @TableField("TOTAL_NUMBER_RIVETING_POINTS")
    private Integer totalNumberRivetingPoints = 0;


    /**
     * 关键自穿刺铆接（FDS）点数
     */
    @Schema(title = "关键自穿刺铆接（FDS）点数")
    @TableField("NUMBER_KEY_SELF_PIER_FDS_POINTS")
    private Integer numberKeySelfPierFdsPoints = 0;


    /**
     * 流钻攻丝铆点（FDS）不合格数
     */
    @Schema(title = "流钻攻丝铆点（FDS）不合格数")
    @TableField("NUMBER_DEFE_FLOW_DRIL_TAPP_RIVE_POINTS")
    private Integer numberDefeFlowDrilTappRivePoints = 0;


    /**
     * 关键流钻攻丝铆点（FDS）不合格数
     */
    @Schema(title = "关键流钻攻丝铆点（FDS）不合格数")
    @TableField("NUMBER_KEY_DEFE_FLOW_DRIL_TAPP_RIVE_POINTS")
    private Integer numberKeyDefeFlowDrilTappRivePoints = 0;


    /**
     * 不合格流钻攻丝铆点（FDS）分布比率
     */
    @Schema(title = "不合格流钻攻丝铆点（FDS）分布比率")
    @TableField("DIST_RATIO_UNQU_FLOW_DRIL_TAPP_RIVET_POINTS")
    private BigDecimal distRatioUnquFlowDrilTappRivetPoints = BigDecimal.valueOf(0);


    /**
     * 关键不合格流钻攻丝铆点（FDS）分布比率
     */
    @Schema(title = "关键不合格流钻攻丝铆点（FDS）分布比率")
    @TableField("DIST_RATIO_CRIT_CONF_FLOW_DRILL_TAPP_RIVE_POINTS")
    private BigDecimal distRatioCritConfFlowDrillTappRivePoints = BigDecimal.valueOf(0);


    /**
     * 不合格铆点编号
     */
    @Schema(title = "不合格铆点编号")
    @TableField("UNQUALIFIED_RIVET_POINT_NO")
    private String unqualifiedRivetPointNo = StringUtils.EMPTY;


    /**
     * 不合格铆点检测图片
     */
    @Schema(title = "不合格铆点检测图片")
    @TableField("DATA_UNQUALIFIED_RIVET_POINT_IMG")
    private String dataUnqualifiedRivetPointImg = StringUtils.EMPTY;


    /**
     * 钉头间隙G[mm]
     */
    @Schema(title = "钉头间隙G[mm]")
    @TableField("GAP_G_BETWEEN_NAIL_HEADS")
    private String gapGBetweenNailHeads = StringUtils.EMPTY;


}