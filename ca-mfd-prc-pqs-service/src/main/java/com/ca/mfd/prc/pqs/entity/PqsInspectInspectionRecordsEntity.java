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
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 抽检记录表(离线/在线)实体
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "抽检记录表(离线/在线)")
@TableName("PRC_PQS_INSPECT_INSPECTION_RECORDS")
public class PqsInspectInspectionRecordsEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_INSPECTION_RECORDS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检验类型（1、在线，2、离线）
     */
    @Schema(title = "检验类型（1、在线，2、离线）")
    @TableField("INSPECTION_TYPE")
    private String inspectionType = StringUtils.EMPTY;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("SAMPLING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date samplingDate;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 零件
     */
    @Schema(title = "零件")
    @TableField("MATERINAL_NO")
    private String materinalNo = StringUtils.EMPTY;


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("WORKSHOP_SHIFT")
    private String workshopShift = StringUtils.EMPTY;


    /**
     * 抽件序号
     */
    @Schema(title = "抽件序号")
    @TableField("SAMPLE_NUMBER")
    private Integer sampleNumber = 0;


    /**
     * 开裂
     */
    @Schema(title = "开裂")
    @TableField("DEHISCENCE")
    private String dehiscence = StringUtils.EMPTY;


    /**
     * 缩颈
     */
    @Schema(title = "缩颈")
    @TableField("NECKING_DOWN")
    private String neckingDown = StringUtils.EMPTY;


    /**
     * 起皱
     */
    @Schema(title = "起皱")
    @TableField("WRINKLING")
    private String wrinkling = StringUtils.EMPTY;


    /**
     * 凹凸伤
     */
    @Schema(title = "凹凸伤")
    @TableField("CONVEX_CONCAVE_INJURY")
    private String convexConcaveInjury = StringUtils.EMPTY;


    /**
     * 凹凸点
     */
    @Schema(title = "凹凸点")
    @TableField("CONVEX_CONCAVE_POINT")
    private String convexConcavePoint = StringUtils.EMPTY;


    /**
     * 拉伤
     */
    @Schema(title = "拉伤")
    @TableField("STRAIN")
    private String strain = StringUtils.EMPTY;


    /**
     * 压伤
     */
    @Schema(title = "压伤")
    @TableField("PRESSURE_INJURY")
    private String pressureInjury = StringUtils.EMPTY;


    /**
     * 划伤
     */
    @Schema(title = "划伤")
    @TableField("SCRATCHES")
    private String scratches = StringUtils.EMPTY;


    /**
     * 波纹
     */
    @Schema(title = "波纹")
    @TableField("CORRUGATION")
    private String corrugation = StringUtils.EMPTY;


    /**
     * 滑移线
     */
    @Schema(title = "滑移线")
    @TableField("SLIP_LINE")
    private String slipLine = StringUtils.EMPTY;


    /**
     * R不顺
     */
    @Schema(title = "R不顺")
    @TableField("R_NOT_SMOOTH")
    private String rNotSmooth = StringUtils.EMPTY;


    /**
     * 毛刺
     */
    @Schema(title = "毛刺")
    @TableField("BURR")
    private BigDecimal burr = BigDecimal.valueOf(0);


    /**
     * 脱锌
     */
    @Schema(title = "脱锌")
    @TableField("DEZINCIFICATION")
    private String dezincification = StringUtils.EMPTY;


    /**
     * 孔数
     */
    @Schema(title = "孔数")
    @TableField("NUMBER_OF_HOLES")
    private Integer numberOfHoles = 0;


    /**
     * 其他
     */
    @Schema(title = "其他")
    @TableField("OTHER")
    private String other = StringUtils.EMPTY;


    /**
     * 抽检人
     */
    @Schema(title = "抽检人")
    @TableField("SAMPLING_PERSON")
    private String samplingPerson = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}