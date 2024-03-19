package com.ca.mfd.prc.avi.communication.remote.app.eps.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 终检完成数据中间表（车云）实体
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description = "终检完成数据中间表（车云）")
public class CarCloudCheckDto {

    /**
     * 车辆VIN
     */
    @Schema(title = "车辆VIN")
    private String vinCode = StringUtils.EMPTY;


    /**
     * 车辆下线标志
     */
    @Schema(title = "车辆下线标志")
    private String wsFlag = StringUtils.EMPTY;


    /**
     * 下线时间
     */
    @Schema(title = "下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualEndDt;


}