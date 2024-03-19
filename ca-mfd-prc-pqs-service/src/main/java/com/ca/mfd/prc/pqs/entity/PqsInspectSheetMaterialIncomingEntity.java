package com.ca.mfd.prc.pqs.entity;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 板料来料检查表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "板料来料检查表")
@TableName("PRC_PQS_INSPECT_SHEET_MATERIAL_INCOMING")
public class PqsInspectSheetMaterialIncomingEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_SHEET_MATERIAL_INCOMING_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 接收日期
     */
    @Schema(title = "接收日期")
    @TableField("RECEIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date receiveDate;


    /**
     * 零件件号
     */
    @Schema(title = "零件件号")
    @TableField("MATERINAL_NO")
    private String materinalNo = StringUtils.EMPTY;


    /**
     * 捆包号
     */
    @Schema(title = "捆包号")
    @TableField("BUNDLING_NO")
    private String bundlingNo = StringUtils.EMPTY;


    /**
     * 板料牌号
     */
    @Schema(title = "板料牌号")
    @TableField("PLATE_NO")
    private String plateNo = StringUtils.EMPTY;


    /**
     * 外包装标签
     */
    @Schema(title = "外包装标签")
    @TableField("OUTER_PACKAGING_LABEL")
    private String outerPackagingLabel = StringUtils.EMPTY;


    /**
     * 防尘防护
     */
    @Schema(title = "防尘防护")
    @TableField("DUST_PROTECTION")
    private String dustProtection = StringUtils.EMPTY;


    /**
     * 防倾倒防护
     */
    @Schema(title = "防倾倒防护")
    @TableField("ANTI_TIPPING_PROTECTION")
    private String antiTippingProtection = StringUtils.EMPTY;


    /**
     * 送料方向
     */
    @Schema(title = "送料方向")
    @TableField("FEEDING_DIRECTION")
    private String feedingDirection = StringUtils.EMPTY;


    /**
     * 托盘清洁
     */
    @Schema(title = "托盘清洁")
    @TableField("TRAY_CLEANING")
    private String trayCleaning = StringUtils.EMPTY;


    /**
     * 托盘定位
     */
    @Schema(title = "托盘定位")
    @TableField("TRAY_POSITIONING")
    private String trayPositioning = StringUtils.EMPTY;


    /**
     * 卷宽
     */
    @Schema(title = "卷宽")
    @TableField("ROLL_WIDTH")
    private BigDecimal rollWidth = BigDecimal.valueOf(0);


    /**
     * 长度
     */
    @Schema(title = "长度")
    @TableField("ROLL_LENGTH")
    private BigDecimal rollLength = BigDecimal.valueOf(0);


    /**
     * 板厚
     */
    @Schema(title = "板厚")
    @TableField("PLATE_THICKNESS")
    private BigDecimal plateThickness = BigDecimal.valueOf(0);


    /**
     * 灰尘颗粒
     */
    @Schema(title = "灰尘颗粒")
    @TableField("DUST_PARTICLES")
    private String dustParticles = StringUtils.EMPTY;


    /**
     * 板料油污
     */
    @Schema(title = "板料油污")
    @TableField("OIL_STAINS")
    private String oilStains = StringUtils.EMPTY;


    /**
     * 划痕深度
     */
    @Schema(title = "划痕深度")
    @TableField("SCRATCH_DEPTH")
    private String scratchDepth = StringUtils.EMPTY;


    /**
     * 生锈
     */
    @Schema(title = "生锈")
    @TableField("RUST")
    private String rust = StringUtils.EMPTY;


    /**
     * 形状
     */
    @Schema(title = "形状")
    @TableField("SHAPE")
    private String shape = StringUtils.EMPTY;


    /**
     * 毛刺
     */
    @Schema(title = "毛刺")
    @TableField("BURR")
    private BigDecimal burr = BigDecimal.valueOf(0);


    /**
     * 平整度
     */
    @Schema(title = "平整度")
    @TableField("EVENNESS")
    private BigDecimal evenness = BigDecimal.valueOf(0);


    /**
     * 堆垛错位
     */
    @Schema(title = "堆垛错位")
    @TableField("STACKING_MISALIGNMENT")
    private BigDecimal stackingMisalignment = BigDecimal.valueOf(0);


    /**
     * 堆垛倾斜量
     */
    @Schema(title = "堆垛倾斜量")
    @TableField("STACKING_INCLINATION")
    private BigDecimal stackingInclination = BigDecimal.valueOf(0);


    /**
     * 堆垛高度
     */
    @Schema(title = "堆垛高度")
    @TableField("STACKING_HEIGHT")
    private BigDecimal stackingHeight = BigDecimal.valueOf(0);


    /**
     * 堆垛水平度
     */
    @Schema(title = "堆垛水平度")
    @TableField("STACKING_LEVELNESS")
    private String stackingLevelness = StringUtils.EMPTY;


    /**
     * 聚氨酯垫块连接绳
     */
    @Schema(title = "聚氨酯垫块连接绳")
    @TableField("POLYURETHANE_PAD_CONNECTION_ROPE")
    private String polyurethanePadConnectionRope = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}