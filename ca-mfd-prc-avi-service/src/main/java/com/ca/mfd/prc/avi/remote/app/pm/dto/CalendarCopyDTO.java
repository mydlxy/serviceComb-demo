package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 复制日历
 *
 * @author jay.he
 * @since 1.0.0 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "复制日历")
public class CalendarCopyDTO {

    @Schema(title = "源车间编码")
    private String workshopCode;

    @Schema(title = "目标车间编码集合")
    private List<DistWorkShop> distWorkshopCodeList;

    @Schema(title = "源线体编码")
    private String lineCode;

    @Schema(title = "目标线体编码集合")
    private List<DistLine> distLineCodeList;

    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginTime;

    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endTime;

    @Data
    public static class DistWorkShop {
        private String workshopCode;
        private String workshopName;
    }

    @Data
    public static class DistLine {
        private String lineCode;
        private String lineName;
        private String workshopCode;
        private String workshopName;
    }

}


