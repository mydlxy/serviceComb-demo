package com.ca.mfd.prc.avi.communication.remote.app.eps.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description = "EP数据接口信息dto")
public class EpInfoDto {
    @Schema(title = "VIN号")
    private String vinCode = StringUtils.EMPTY;
    @Schema(title = "控制器类型")
    private String ecuTypeCode = StringUtils.EMPTY;
    @Schema(title = "条码信息")
    private String barcode = StringUtils.EMPTY;
    @Schema(title = "零件物料号")
    private String materialCode = StringUtils.EMPTY;
    @Schema(title = "数据更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate = new Date();
}
