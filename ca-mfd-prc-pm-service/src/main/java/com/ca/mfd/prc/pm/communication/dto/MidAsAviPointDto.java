package com.ca.mfd.prc.pm.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 * @Description: AS车辆实际过点实体
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "AS车辆实际过点")
public class MidAsAviPointDto implements Serializable {


    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    private String orgCode = StringUtils.EMPTY;

    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    private String vrn = StringUtils.EMPTY;

    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    private String vin = StringUtils.EMPTY;

    /**
     * 触发工位(AVI代码)
     */
    @Schema(title = "触发工位(AVI代码)")
    private String ulocNo = StringUtils.EMPTY;

    /**
     * 触发时间
     */
    @Schema(title = "触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date scanTime = new Date();

    /**
     * 触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)
     */
    @Schema(title = "触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)")
    private String scanType = StringUtils.EMPTY;


}