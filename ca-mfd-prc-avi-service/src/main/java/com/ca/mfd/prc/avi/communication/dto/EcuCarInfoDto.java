package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Schema(description= "下发软件信息dto")
public class EcuCarInfoDto {

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

    //软件清单
    private List<EcuSoftwareListDto> ecuList = new ArrayList<>();



    @Schema(title = "数据创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();
    @Schema(title = "数据更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate ;

}
