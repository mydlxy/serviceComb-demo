package com.ca.mfd.prc.core.fs.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(title = "服务文件信息")
public class ServiceFileInfo {
    @Schema(title = "服务名")
    private String serviceName;
    @Schema(title = "最后更新时间")
    private Date lastUpdateTime;
    @Schema(title = "版本信息名称")
    private String versionInfoName;
    @Schema(title = "备注")
    private String remark;
}
