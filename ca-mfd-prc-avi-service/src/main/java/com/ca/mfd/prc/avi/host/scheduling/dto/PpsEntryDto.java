package com.ca.mfd.prc.avi.host.scheduling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "报工单-零部件")
public class PpsEntryDto implements Serializable {
    /**
     * 主键
     */
    @Schema(title = "主键")
    private Long id;

    /**
     * 报工单号
     */
    @Schema(title = "报工单号")
    private String entryReportNo;

    /**
     * 报工车间
     */
    @Schema(title = "报工车间")
    private String workShopCode;

    /**
     * 报工线体
     */
    @Schema(title = "报工线体")
    private String lineCode;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private Date creationDate;

    /**
     * 订单大类;3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单大类;3：压铸  4：机加   5：冲压  6：电池上盖")
    private Integer orderCategory;
}
