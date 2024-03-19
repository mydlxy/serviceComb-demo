package com.ca.mfd.prc.eps.communication.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 车辆基础数据中间表（车云）实体
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "车辆基础数据中间表（车云）")
@TableName("PRC_MID_CAR_CLOUD_CAR_INFO")
public class MidCarCloudCarInfoEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_CAR_CLOUD_CAR_INFO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 车辆VIN
     */
    @Schema(title = "车辆VIN")
    @TableField("VIN_CODE")
    private String vinCode = StringUtils.EMPTY;


    /**
     * 品牌名称
     */
    @Schema(title = "品牌名称")
    @TableField("CLPP")
    private String clpp = StringUtils.EMPTY;


    /**
     * 车系编码
     */
    @Schema(title = "车系编码")
    @TableField("BOM_ROOM")
    private String bomRoom = StringUtils.EMPTY;


    /**
     * 车型编码
     */
    @Schema(title = "车型编码")
    @TableField("VEHICLE_MODEL_NUMBER")
    private String vehicleModelNumber = StringUtils.EMPTY;


    /**
     * 基础车型
     */
    @Schema(title = "基础车型")
    @TableField("BASIC_VEHICLE_MODEL")
    private String basicVehicleModel = StringUtils.EMPTY;

    /**
     * 配置编码
     */
    @Schema(title = "配置编码")
    @TableField("VEHICLE_MATERIAL_NUMBER")
    private String vehicleMaterialNumber = StringUtils.EMPTY;


    /**
     * 配置编码名称
     */
    @Schema(title = "配置编码名称")
    @TableField("VEHICLE_DESC")
    private String vehicleDesc = StringUtils.EMPTY;


    /**
     * 车身颜色
     */
    @Schema(title = "车身颜色")
    @TableField("BODY_COLOR_NAME")
    private String bodyColorName = StringUtils.EMPTY;


    /**
     * 车身颜色编码
     */
    @Schema(title = "车身颜色编码")
    @TableField("BODY_COLOR_CODE")
    private String bodyColorCode = StringUtils.EMPTY;


    /**
     * 载客量
     */
    @Schema(title = "载客量")
    @TableField("EDZK")
    private String edzk = StringUtils.EMPTY;

    /**
     * 驾驶室准乘人数(人)
     */
    @Schema(title = "驾驶室准乘人数(人)")
    @TableField("JSSZCRS")
    private String jsszcrs = StringUtils.EMPTY;


    /**
     * 驱电电机
     */
    @Schema(title = "驱电电机")
    @TableField("QDDJ")
    private String qddj = StringUtils.EMPTY;


    /**
     * 电池编码
     */
    @Schema(title = "电池编码")
    @TableField("RIN_CODE")
    private String rinCode = StringUtils.EMPTY;



    /**
     * 发动机号（可选）
     */
    @Schema(title = "发动机号（可选）")
    @TableField("ENGINE_NO")
    private String engineNo = StringUtils.EMPTY;


    /**
     * 燃料种类
     */
    @Schema(title = "燃料种类")
    @TableField("RLZL")
    private String rlzl = StringUtils.EMPTY;


    /**
     * 整车大版本号(整车大版本发布单号,唯一标识)
     */
    @Schema(title = "整车大版本号(整车大版本发布单号,唯一标识)")
    @TableField("PUBLISH_CHANGE_CODE")
    private String publishChangeCode = StringUtils.EMPTY;

    /**
     * 整车大版本号
     */
    @Schema(title = "整车大版本号")
    @TableField("LAST_VEHICLE_VERSION_CODE")
    private String lastVehicleVersionCode = StringUtils.EMPTY;


    /**
     * 整车配置版本号
     */
    @Schema(title = "整车配置版本号")
    @TableField("CHANGE_CODE")
    private String changeCode = StringUtils.EMPTY;


    /**
     * 车辆出厂时间(合格证上的发证日期)
     */
    @Schema(title = "车辆出厂时间(合格证上的发证日期)")
    @TableField("FZRQ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date fzrq = new Date();


    /**
     * 车辆出厂合格标识(打印前为false,打印后为true)
     */
    @Schema(title = "车辆出厂合格标识(打印前为false,打印后为true)")
    @TableField("IS_PRINT_CERTIFICATE")
    private Boolean isPrintCertificate =Boolean.FALSE;


    /**
     * 合格证书编号
     */
    @Schema(title = "合格证书编号")
    @TableField("CERTIFICATE_NO")
    private String certificateNo = StringUtils.EMPTY;


    /**
     * 工厂编号
     */
    @Schema(title = "工厂编号")
    @TableField("ORGANIZATION_CODE")
    private String organizationCode = StringUtils.EMPTY;


    /**
     * 工厂名称
     */
    @Schema(title = "工厂名称")
    @TableField("ORGANIZATION_NAME")
    private String organizationName = StringUtils.EMPTY;


    /**
     * 车辆名称
     */
    @Schema(title = "车辆名称")
    @TableField("CLMC")
    private String clmc = StringUtils.EMPTY;


    /**
     * 车辆类型
     */
    @Schema(title = "车辆类型")
    @TableField("CLLX")
    private String cllx = StringUtils.EMPTY;


    /**
     * 产地/最终阶段车辆制造国
     */
    @Schema(title = "产地/最终阶段车辆制造国")
    @TableField("ZZJDCLZZG")
    private String zzjdclzzg = StringUtils.EMPTY;


    /**
     * 道路机动车辆生产企业名称/车辆生产企业名称
     */
    @Schema(title = "道路机动车辆生产企业名称/车辆生产企业名称")
    @TableField("CLSCQYMC")
    private String clscqymc = StringUtils.EMPTY;


    /**
     * 道路机动车辆生产企业名称/车辆生产企业名称
     */
    @Schema(title = "最终制造阶段的制造商名称/制造商名称")
    @TableField("ZZSMC")
    private String zzsmc = StringUtils.EMPTY;


    /**
     * 储能装置电池包(箱)型号
     */
    @Schema(title = "储能装置电池包(箱)型号")
    @TableField("CNZZ_XH_SCC")
    private String cnzzXhScc = StringUtils.EMPTY;

    /**
     * 内饰颜色编码
     */
    @Schema(title = "内饰颜色编码")
    @TableField("INTERIOR_COLOR_CODE")
    private String interiorColorCode = StringUtils.EMPTY;

    /**
     * 内饰颜色
     */
    @Schema(title = "内饰颜色")
    @TableField("INTERIOR_COLOR_DESC")
    private String interiorColorDesc = StringUtils.EMPTY;

    /**
     * 销售渠道(车辆生产需求方)
     */
    @Schema(title = "销售渠道(车辆生产需求方)")
    @TableField("DEMAND_ORIGIN")
    private String demandOrigin = StringUtils.EMPTY;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


}