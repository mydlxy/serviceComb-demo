package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description= "下发整车信息dto")
public class CarInfoDto {

    @Schema(title = "VIN号")
    private String vinCode = StringUtils.EMPTY;
    @Schema(title = "车型系列代码")
    private String typeCode = StringUtils.EMPTY;
    @Schema(title = "基础车型")
    private String basicVehicleModel = StringUtils.EMPTY;
    @Schema(title = "车型状态号")
    private String carCode = StringUtils.EMPTY;
    @Schema(title = "选装件")
    private String optionCode = StringUtils.EMPTY;
    @Schema(title = "定制编码")
    private String attributeCode = StringUtils.EMPTY;
    @Schema(title = "颜色编码")
    private String colorCode = StringUtils.EMPTY;
    @Schema(title = "生产基地信息")
    private String orgCode = StringUtils.EMPTY;
    @Schema(title = "整车物料号")
    private String vehicleMaterialNumber = StringUtils.EMPTY;
    @Schema(title = "站点信息")
    private String siteInformation = StringUtils.EMPTY;
    @Schema(title = "整车特性信息")
    private String featureCode = StringUtils.EMPTY;
    @Schema(title = "数据创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();
    @Schema(title = "总装上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date faDate ;
    @Schema(title = "数据更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate ;

}
