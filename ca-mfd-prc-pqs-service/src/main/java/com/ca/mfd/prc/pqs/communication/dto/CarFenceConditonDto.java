package com.ca.mfd.prc.pqs.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: GPS寻车返回字段
 * @author mason
 * @date 2024年1月22日
 */
@Data
public class CarFenceConditonDto {

    @Schema(title = "车系")
    private String seriesCode = StringUtils.EMPTY;

    @Schema(title = "车身颜色")
    private String color = StringUtils.EMPTY;

    @Schema(title = "位置经度")
    private String lng = StringUtils.EMPTY;

    @Schema(title = "车型编码")
    private String modelCode = StringUtils.EMPTY;

    @Schema(title = "车身颜色编码")
    private String colorCode = StringUtils.EMPTY;

    @Schema(title = "车辆gps状态")
    private String vehicleGpsStatus = StringUtils.EMPTY;

    @Schema(title = "车辆vin码")
    private String vin = StringUtils.EMPTY;

    @Schema(title = "位置时间")
    private String gpsTime = StringUtils.EMPTY;

    @Schema(title = "位置纬度")
    private String lat = StringUtils.EMPTY;
}
