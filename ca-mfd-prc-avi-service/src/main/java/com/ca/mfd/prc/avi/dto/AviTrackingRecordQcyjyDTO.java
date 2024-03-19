package com.ca.mfd.prc.avi.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: changan 202306215@any3.com
 * @description:车辆过点信息-汽车研究院查询
 * @date: 2024-02-20 13:17
 */
@Data
@Schema(description = "车辆过点信息-汽车研究院查询")
public class AviTrackingRecordQcyjyDTO {
    @Schema(title = "条码")
    private String vinCode = StringUtils.EMPTY;

    // prc_pps_order表的最后更新时间
    @Schema(title = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date lastUpdateDate;

    @Schema(title = "点位")
    private String siteDesc = StringUtils.EMPTY;

    @Schema(title = "点位代码")
    private String siteCode = StringUtils.EMPTY;
}
