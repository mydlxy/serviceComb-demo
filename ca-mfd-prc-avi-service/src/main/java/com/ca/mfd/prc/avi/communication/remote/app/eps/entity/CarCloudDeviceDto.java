package com.ca.mfd.prc.avi.communication.remote.app.eps.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆设备中间表（车云）实体
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description = "车辆设备中间表（车云）")
public class CarCloudDeviceDto {


    /**
     * 车辆VIN
     */
    @Schema(title = "车辆VIN")
    private String vinCode = StringUtils.EMPTY;

    @Schema(title = "ecu信息")
    private List<CarCloudDeviceDatailDto> ecuList = new ArrayList<>();

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
    private Date lastUpdateDate=new Date();


}