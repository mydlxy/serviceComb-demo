package com.ca.mfd.prc.pmc.remote.app.core.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author inkelink
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DefalutRequestModel")
public class DefalutRequestModel {

    @Schema(title = "请求key")
    private String requestKey;
    @Schema(title = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date requestTime;
    @Schema(title = "请求地址")
    private String requestUrl;
    @Schema(title = "请求参数")
    private String requestPar;
    @Schema(title = "请求源地址")
    private String requestSourceApp;
    @Schema(title = "返回值")
    private String responseBody;
    @Schema(title = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date requestDateTime = new Date();
    @Schema(title = "错误信息")
    private String errorMessage;
}
