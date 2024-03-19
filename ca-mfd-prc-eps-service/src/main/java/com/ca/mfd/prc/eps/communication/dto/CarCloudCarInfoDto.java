package com.ca.mfd.prc.eps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
@Schema(description= "车辆基础数据中间表（车云）")
public class CarCloudCarInfoDto {


    /**
     * 车辆VIN
     */
    @Schema(title = "车辆VIN")
    private String vinCode = StringUtils.EMPTY;

    /**
     * 品牌名称
     */
    @Schema(title = "品牌名称")
    private String clpp = StringUtils.EMPTY;


    /**
     * 车系编码
     */
    @Schema(title = "车系编码")
    private String bomRoom = StringUtils.EMPTY;



    /**
     * 车型编码
     */
    @Schema(title = "车型编码")
    private String vehicleModelNumber = StringUtils.EMPTY;


    /**
     * 基础车型
     */
    @Schema(title = "基础车型")
    private String basicVehicleModel = StringUtils.EMPTY;


    /**
     * 配置编码
     */
    @Schema(title = "配置编码")
    private String vehicleMaterialNumber = StringUtils.EMPTY;


    /**
     * 配置编码名称
     */
    @Schema(title = "配置编码名称")
    private String vehicleDesc = StringUtils.EMPTY;


    /**
     * 车身颜色
     */
    @Schema(title = "车身颜色")
    private String bodyColorName = StringUtils.EMPTY;


    /**
     * 车身颜色编码
     */
    @Schema(title = "车身颜色编码")
    private String bodyColorCode = StringUtils.EMPTY;


    /**
     * 载客量
     */
    @Schema(title = "载客量")
    private String edzk = StringUtils.EMPTY;


    /**
     * 驾驶室准乘人数(人)
     */
    @Schema(title = "驾驶室准乘人数(人)")
    private String jsszcrs = StringUtils.EMPTY;


    /**
     * 驱电电机
     */
    @Schema(title = "驱电电机")
    private CarCloudCarInfoQddjDto qddj=new CarCloudCarInfoQddjDto();


    /**
     * 电池编码
     */
    @Schema(title = "电池编码")
    private String rinCode = StringUtils.EMPTY;


    /**
     * 发动机号（可选）
     */
    @Schema(title = "发动机号（可选）")
    private String engineNo = StringUtils.EMPTY;


    /**
     * 燃料种类
     */
    @Schema(title = "燃料种类")
    private String rlzl = StringUtils.EMPTY;


    /**
     * 整车大版本号(整车大版本发布单号,唯一标识)
     */
    @Schema(title = "整车大版本号(整车大版本发布单号,唯一标识)")
    private String publishChangeCode = StringUtils.EMPTY;


    /**
     * 整车大版本号
     */
    @Schema(title = "整车大版本号")
    private String lastVehicleVersionCode = StringUtils.EMPTY;


    /**
     * 整车配置版本号
     */
    @Schema(title = "整车配置版本号")
    private String changeCode = StringUtils.EMPTY;



    /**
     * 车辆出厂时间(合格证上的发证日期)
     */
    @Schema(title = "车辆出厂时间(合格证上的发证日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date fzrq = new Date();


    /**
     * 车辆出厂合格标识(打印前为false,打印后为true)
     */
    @Schema(title = "车辆出厂合格标识(打印前为false,打印后为true)")
    private Boolean isPrintCertificate =Boolean.FALSE;


    /**
     * 合格证书编号
     */
    @Schema(title = "合格证书编号")
    private String certificateNo = StringUtils.EMPTY;


    /**
     * 工厂编号
     */
    @Schema(title = "工厂编号")
    private String organizationCode = StringUtils.EMPTY;


    /**
     * 工厂名称
     */
    @Schema(title = "工厂名称")
    private String organizationName = StringUtils.EMPTY;


    /**
     * 车辆名称
     */
    @Schema(title = "车辆名称")
    private String clmc = StringUtils.EMPTY;


    /**
     * 车辆类型
     */
    @Schema(title = "车辆类型")
    private String cllx = StringUtils.EMPTY;


    /**
     * 产地/最终阶段车辆制造国
     */
    @Schema(title = "产地/最终阶段车辆制造国")
    private String zzjdclzzg = StringUtils.EMPTY;


    /**
     * 道路机动车辆生产企业名称/车辆生产企业名称
     */
    @Schema(title = "道路机动车辆生产企业名称/车辆生产企业名称")
    private String clscqymc = StringUtils.EMPTY;


    /**
     * 道路机动车辆生产企业名称/车辆生产企业名称
     */
    @Schema(title = "最终制造阶段的制造商名称/制造商名称")
    private String zzsmc = StringUtils.EMPTY;


    /**
     * 储能装置电池包(箱)型号
     */
    @Schema(title = "储能装置电池包(箱)型号")
    private String cnzzXhScc = StringUtils.EMPTY;

    /**
     * 内饰颜色编码
     */
    @Schema(title = "内饰颜色编码")
    private String interiorColorCode = StringUtils.EMPTY;

    /**
     * 内饰颜色
     */
    @Schema(title = "内饰颜色")
    private String interiorColorDesc = StringUtils.EMPTY;

    /**
     * 销售渠道(车辆生产需求方)
     */
    @Schema(title = "销售渠道(车辆生产需求方)")
    private String demandOrigin = StringUtils.EMPTY;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();


    /**
     * 最后修改时间
     */
    @Schema(title = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate = new Date();


}