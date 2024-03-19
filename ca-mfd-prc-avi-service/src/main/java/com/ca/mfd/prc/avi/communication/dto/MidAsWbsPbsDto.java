package com.ca.mfd.prc.avi.communication.dto;

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
 * @Description: AS车辆过点WbsPbs实体
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "AS车辆过点WbsPbs")
public class MidAsWbsPbsDto implements Serializable {

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
     * 在制类型（WBS/PBS）
     */
    @Schema(title = "在制类型")
    private String wbspbs = StringUtils.EMPTY;

    /**
     * 进入时间
     */
    @Schema(title = "进入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date entryTime = new Date();

    /**
     * 退出时间
     */
    @Schema(title = "退出时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exitTime = new Date();

    /**
     * 停留时间(单位秒)
     */
    @Schema(title = "停留时间(单位秒)")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer stayTime = 0;

}