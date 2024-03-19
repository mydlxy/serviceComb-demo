package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description= "下发过点信息dto")
public class SiteInfoDto {

    @Schema(title = "VIN号")
    private String vinCode = StringUtils.EMPTY;
    @Schema(title = "车型系列代码")
    private String typeCode = StringUtils.EMPTY;
    @Schema(title = "车型状态号")
    private String carCode = StringUtils.EMPTY;
    @Schema(title = "选装件")
    private String optionCode = StringUtils.EMPTY;
    @Schema(title = "定制编码")
    private String attributeCode = StringUtils.EMPTY;
    @Schema(title = "颜色编码")
    private String colorCode = StringUtils.EMPTY;
    @Schema(title = "站点信息")
    private String siteInformation = StringUtils.EMPTY;

    @Schema(title = "数据创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();
    @Schema(title = "过点时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date faDate ;
    @Schema(title = "数据更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate = new Date();

}
