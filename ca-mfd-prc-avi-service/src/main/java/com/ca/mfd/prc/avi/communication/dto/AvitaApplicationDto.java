package com.ca.mfd.prc.avi.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Schema(description = "同步待申请秘钥车辆接口参数")
public class AvitaApplicationDto {

    @Schema(title = "车系")
    private String vclSer = StringUtils.EMPTY;
    @Schema(title = "公告车型")
    private String carCode = StringUtils.EMPTY;
    @Schema(title = "vin列表")
    private List<String> vinList;
}
