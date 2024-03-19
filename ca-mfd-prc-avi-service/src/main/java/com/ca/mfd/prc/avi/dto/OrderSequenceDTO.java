package com.ca.mfd.prc.avi.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * OperWosCacheDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OperWosCacheDTO")
public class OrderSequenceDTO {

    /**
     * ID
     */
    @Schema(title = "ID")
    private String id = StringUtils.EMPTY;

    /**
     * 订阅厂商代码
     */
    @Schema(title = "订阅厂商代码")
    private String code = StringUtils.EMPTY;

    /**
     * 产品标识码
     */
    @Schema(title = "产品标识码")
    private String productNo = StringUtils.EMPTY;

    /**
     * Vin号
     */
    @Schema(title = "Vin号")
    private String vin = StringUtils.EMPTY;

    /**
     * 订单编号
     */
    @Schema(title = "订单编号")
    private String orderNo = StringUtils.EMPTY;

    /**
     * Avi 站点 CODE
     */
    @Schema(title = "Avi 站点 CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * Avi站点名称
     */
    @Schema(title = "Avi站点名称")
    private String aviName = StringUtils.EMPTY;

    /**
     * 整车编码
     */
    @Schema(title = "整车编码")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 车型
     */
    @Schema(title = "车型")
    private String carType = StringUtils.EMPTY;

    /**
     * 车型描述
     */
    @Schema(title = "车型描述")
    private String carTypeDescription = StringUtils.EMPTY;

    /**
     * 内饰色
     */
    @Schema(title = "内饰色")
    private String interiorColor = StringUtils.EMPTY;

    /**
     * 内饰色描述
     */
    @Schema(title = "内饰色描述")
    private String interiorColorDescription = StringUtils.EMPTY;

    /**
     * 车身颜色
     */
    @Schema(title = "车身颜色")
    private String bodyColor = StringUtils.EMPTY;

    /**
     * 车身颜色描述
     */
    @Schema(title = "车身颜色描述")
    private String bodyColorDescription = StringUtils.EMPTY;

    /**
     * 配置
     */
    @Schema(title = "配置")
    private String ver = StringUtils.EMPTY;

    /**
     * 配置描述
     */
    @Schema(title = "配置描述")
    private String verDescription = StringUtils.EMPTY;

    /**
     * 选装包
     */
    @Schema(title = "选装包")
    @JsonProperty("package")
    private String packAge = StringUtils.EMPTY;

    /**
     * 选装包描述
     */
    @Schema(title = "选装包描述")
    private String packageDescription = StringUtils.EMPTY;

    /**
     * 电池型号
     */
    @Schema(title = "电池型号")
    private String rinModel = StringUtils.EMPTY;

    /**
     * 电池型号描述
     */
    @Schema(title = "电池型号描述")
    private String rinModelDescription = StringUtils.EMPTY;
    /**
     * 电机型号
     */
    @Schema(title = "电机型号")
    private String powerModel = StringUtils.EMPTY;

    /**
     * 电机型号描述
     */
    @Schema(title = "电机型号描述")
    private String powerModelDescription = StringUtils.EMPTY;

    /**
     * 发动机型号
     */
    @Schema(title = "发动机型号")
    private String engine = StringUtils.EMPTY;

    /**
     * 发动机型号描述
     */
    @Schema(title = "发动机型号描述")
    private String engineDescription = StringUtils.EMPTY;

    /**
     * 变速箱型号
     */
    @Schema(title = "变速箱型号")
    private String gearbox = StringUtils.EMPTY;

    /**
     * 变速箱型号描述
     */
    @Schema(title = "变速箱型号描述")
    private String gearBoxDescription = StringUtils.EMPTY;

    /**
     * 过点时间 yyyy-MM-dd HH:mm:ss.fff
     */
    @Schema(title = "过点时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passDt;

    /**
     * 发送标志
     */
    @Schema(title = "发送标志")
    private Boolean send;
}