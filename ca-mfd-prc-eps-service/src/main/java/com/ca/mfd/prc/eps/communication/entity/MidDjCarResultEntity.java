package com.ca.mfd.prc.eps.communication.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 电检整车结果数据实体
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "电检整车结果数据")
@TableName("PRC_MID_DJ_CAR_RESULT")
public class MidDjCarResultEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_DJ_CAR_RESULT_ID", type = IdType.INPUT)
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
     * VIN号
     */
    @Schema(title = "VIN号")
    @TableField("VIN_CODE")
    private String vinCode = StringUtils.EMPTY;

    /**
     * 开始检测的时间
     */
    @Schema(title = "开始检测的时间")
    @TableField("START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDate= new Date();

    /**
     * 胎压传感器编码读取结果
     */
    @Schema(title = "胎压传感器编码读取结果")
    @TableField("EVAL_TIRE_PRESSURE_FLAG")
    private String evalTirePressureFlag = StringUtils.EMPTY;

    /**
     * 胎压传感器编码读取时间
     */
    @Schema(title = "胎压传感器编码读取时间")
    @TableField("TIRE_PRESSURE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date tirePressureTime = new Date();

    /**
     * 总装生产线安规检测结果
     */
    @Schema(title = "总装生产线安规检测结果")
    @TableField("EVAL_DCLOAD_SAFETY_TEST_FLAG")
    private String evalDcloadSafetyTestFlag = StringUtils.EMPTY;

    /**
     * 总装生产线安规检测时间
     */
    @Schema(title = "总装生产线安规检测时间")
    @TableField("DCLOAD_SAFETY_TEST_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date dcloadSafetyTestTime = new Date();

    /**
     * 电器程序刷写结果
     */
    @Schema(title = "电器程序刷写结果")
    @TableField("EVAL_FLASH_FLAG")
    private String evalFlashFlag = StringUtils.EMPTY;

    /**
     * 电器程序刷写时间
     */
    @Schema(title = "电器程序刷写时间")
    @TableField("FLASH_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date flashTime = new Date();

    /**
     * 电器参数配置结果
     */
    @Schema(title = "电器参数配置结果")
    @TableField("EVAL_CONFIG_FLAG")
    private String evalConfigFlag = StringUtils.EMPTY;

    /**
     * 电器参数配置时间
     */
    @Schema(title = "电器参数配置时间")
    @TableField("CONFIG_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date configTime = new Date();


    /**
     * 四轮定位结果
     */
    @Schema(title = "四轮定位结果")
    @TableField("EVAL_FWA_FLAG")
    private String evalFwaFlag = StringUtils.EMPTY;

    /**
     * 四轮定位时间
     */
    @Schema(title = "四轮定位时间")
    @TableField("FWA_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date fwaTime = new Date();

    /**
     * 大灯检测结果
     */
    @Schema(title = "大灯检测结果")
    @TableField("EVAL_LAMPLIGHT_FLAG")
    private String evalLamplightFlag = StringUtils.EMPTY;

    /**
     * 大灯检测时间
     */
    @Schema(title = "大灯检测时间")
    @TableField("LAMPLIGHT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lamplightTime = new Date();

    /**
     * 转角检测结果
     */
    @Schema(title = "转角检测结果")
    @TableField("EVAL_LOCKTOLOCK_FLAG")
    private String evalLocktolockFlag = StringUtils.EMPTY;

    /**
     * 转角检测时间
     */
    @Schema(title = "转角检测时间")
    @TableField("LOCKTOLOCK_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date locktolockTime = new Date();

    /**
     * 侧滑检测时间
     */
    @Schema(title = "侧滑检测时间")
    @TableField("SLIDESLIP_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date slideslipTime = new Date();

    /**
     * 侧滑检测结果
     */
    @Schema(title = "侧滑检测结果")
    @TableField("EVAL_SLIDESLIP_FLAG")
    private String evalSlideslipFlag = StringUtils.EMPTY;

    /**
     * 制动结果
     */
    @Schema(title = "制动结果")
    @TableField("EVAL_BRAKE_FLAG")
    private String evalBrakeFlag = StringUtils.EMPTY;

    /**
     * 制动时间
     */
    @Schema(title = "制动时间")
    @TableField("BRAKE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date brakeTime = new Date();

    /**
     * 转毂结果
     */
    @Schema(title = "转毂结果")
    @TableField("EVL_DRUM_FLAG")
    private String evlDrumFlag = StringUtils.EMPTY;

    /**
     * 转毂时间
     */
    @Schema(title = "转毂时间")
    @TableField("DRUM_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date drumTime = new Date();

    /**
     * 空调出风口温度检测结果
     */
    @Schema(title = "空调出风口温度检测结果")
    @TableField("EVAL_AIRCONDITION_FLAG")
    private String evalAirconditionFlag = StringUtils.EMPTY;
    ;

    /**
     * 空调出风口温度检测时间
     */
    @Schema(title = "空调出风口温度检测时间")
    @TableField("AIR_CONDITION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date airConditionTime = new Date();

    /**
     * 标定结果
     */
    @Schema(title = "标定结果")
    @TableField("EVAL_CALIBRATION_FLAG")
    private String evalCalibrationFlag = StringUtils.EMPTY;

    /**
     * 标定时间
     */
    @Schema(title = "标定时间")
    @TableField("CALIBRATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date calibrationTime = new Date();

    /**
     * 电喷结果
     */
    @Schema(title = "电喷结果")
    @TableField("EVAL_EI_FLAG")
    private String evalEiFlag = StringUtils.EMPTY;

    /**
     * 电喷时间
     */
    @Schema(title = "电喷时间")
    @TableField("EI_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date eiTime = new Date();

    /**
     * 检测线安规检测结果
     */
    @Schema(title = "检测线安规检测结果")
    @TableField("EVAL_CPSAFETY_TEST_FLAG")
    private String evalCPsafetyTestFlag = StringUtils.EMPTY;

    /**
     * 检测线安规检测时间
     */
    @Schema(title = "检测线安规检测时间")
    @TableField("CP_SAFETYTEST_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date cpSafetytestTime = new Date();

    /**
     * 路试后电检结果
     */
    @Schema(title = "路试后电检结果")
    @TableField("EVAL_ROAD_TEST_FLAG")
    private String evalRoadTestFlag = StringUtils.EMPTY;

    /**
     * 路试后电检时间
     */
    @Schema(title = "路试后电检时间")
    @TableField("ROADTEST_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date roadtestTime = new Date();


    /**
     * 出厂日期，所有检测全部合格日期
     */
    @Schema(title = "出厂日期，所有检测全部合格日期")
    @TableField("A_OUT_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date aOutTime = new Date();


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @TableField("OP_CODE")
    private Integer opCode = 0;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date exeTime;
}