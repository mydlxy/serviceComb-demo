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
     * 反馈类型(1：一般通过、2：车辆报废、4：车辆HOLD、5：车辆UNHOLD、6：车辆SETIN、7：车辆SETOUT)
     */
    @Schema(title = "反馈类型(1：一般通过、2：车辆报废、4：车辆HOLD、5：车辆UNHOLD、6：车辆SETIN、7：车辆SETOUT)")
    private String feedBackType = StringUtils.EMPTY;

    /**
     * 触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)
     */
    @Schema(title = "触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)")
    private String scanType = StringUtils.EMPTY;

    /**
     * 扫描用户
     */
    @Schema(title = "扫描用户")
    private String scanUser = StringUtils.EMPTY;

    /**
     * 通过次数
     */
    @Schema(title = "通过次数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer passTimes = 0;

    /**
     * 通过工厂代码
     */
    @Schema(title = "通过工厂代码")
    private String actualPlantCode = StringUtils.EMPTY;
    /**
     * 通过车间代码
     */
    @Schema(title = "通过车间代码")
    private String actualWorkShop = StringUtils.EMPTY;
    /**
     * 通过产线代码
     */
    @Schema(title = "通过产线代码")
    private String actualLineCode = StringUtils.EMPTY;
    /**
     * 通过工位代码
     */
    @Schema(title = "通过工位代码")
    private String actualWorkStation = StringUtils.EMPTY;

    /**
     * 通过日(yyyy-MM-dd)
     */
    @Schema(title = "通过日(yyyy-MM-dd)")
    private String actualDate = StringUtils.EMPTY;

    /**
     * 通过班次
     */
    @Schema(title = "通过班次")
    private String actualShift = StringUtils.EMPTY;

    /**
     * 通过时间
     */
    @Schema(title = "通过时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualTIme = new Date();

}