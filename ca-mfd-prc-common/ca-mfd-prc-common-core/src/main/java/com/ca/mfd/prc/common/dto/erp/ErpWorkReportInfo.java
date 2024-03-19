package com.ca.mfd.prc.common.dto.erp;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * ErpWorkReportInfo
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ErpWorkReportInfo")
public class ErpWorkReportInfo {

    @Schema(title = "底盘号")
    @JsonProperty("CLOT")
    private String sn = StringUtils.EMPTY;

    @Schema(title = "AVI代码对应的工序代码")
    @JsonProperty("OPNO")
    private Integer opNo = 0;

    @Schema(title = "工序采集时间")
    @JsonProperty("PDTE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date collectTime;

    @Schema(title = "备用1")
    @JsonProperty("BZ1")
    private String spare1 = StringUtils.EMPTY;

    @Schema(title = "备用2")
    @JsonProperty("BZ2")
    private String spare2 = StringUtils.EMPTY;

    @Schema(title = "备用3")
    @JsonProperty("BZ3")
    private String spare3 = StringUtils.EMPTY;

}
