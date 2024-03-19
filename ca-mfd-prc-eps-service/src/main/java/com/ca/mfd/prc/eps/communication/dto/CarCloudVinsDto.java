package com.ca.mfd.prc.eps.communication.dto;

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
 *
 * @Description: 终检完成数据中间表（车云）实体
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description= "vin号列表")
public class CarCloudVinsDto {

    /**
     * 车辆VIN
     */
    @Schema(title = "车辆VIN")
    private List<String> vins = new ArrayList<>();




}