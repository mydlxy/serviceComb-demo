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

import java.util.Date;

/**
 *
 * @Description: 离线巡检记录表实体
 * @author bo.yang
 * @date 2024年03月14日
 * @变更说明 BY bo.yang At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "离线巡检记录表")
@TableName("PRC_PQS_INSPECT_OFFLINE_INSPECTION")
public class PqsInspectOfflineInspectionEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_OFFLINE_INSPECTION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检验类型：1、离线巡检，2、离线抽检，3、库房抽检
     */
    @Schema(title = "检验类型：1、离线巡检，2、离线抽检，3、库房抽检")
    @TableField("TYPE")
    private String type = StringUtils.EMPTY;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("TEST_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date testDate;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("VEHICLE_MODEL")
    private String vehicleModel = StringUtils.EMPTY;


    /**
     * 零件号
     */
    @Schema(title = "零件号")
    @TableField("MATERIAl_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("TEAM_NO")
    private String teamNo = StringUtils.EMPTY;


    /**
     * 抽件序号
     */
    @Schema(title = "抽件序号")
    @TableField("SAMPLE_NUMBER")
    private String sampleNumber = StringUtils.EMPTY;


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
    @TableField("R_IS_NOT_SMOOTH")
    private String rIsNotSmooth = StringUtils.EMPTY;


    /**
     * 毛刺
     */
    @Schema(title = "毛刺")
    @TableField("BURR")
    private String burr = StringUtils.EMPTY;


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
    @TableField("NUMBER_HOLES")
    private String numberHoles = StringUtils.EMPTY;


    /**
     * 其他
     */
    @Schema(title = "其他")
    @TableField("OTHERS")
    private String others = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 抽检人
     */
    @Schema(title = "抽检人")
    @TableField("SURVEYOR")
    private String surveyor = StringUtils.EMPTY;


}