package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: AS批次件进度反馈
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "AS批次件进度反馈")
public class AsBatchPiecesDto {

    @Schema(title = "工厂组织代码")
    private String orgCode = StringUtils.EMPTY;

    @Schema(title = "车间代码")
    private String shopCode = StringUtils.EMPTY;

    @Schema(title = "生产线代码")
    private String lineCode = StringUtils.EMPTY;

    @Schema(title = "工位号")
    private String wsCode = StringUtils.EMPTY;

    @Schema(title = "上下线标识(0：上线，1：下线)")
    private String wsFlag = StringUtils.EMPTY;

    @Schema(title = "工序任务号")
    private String taskCode = StringUtils.EMPTY;

    @Schema(title = "合格数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer yieldQuantity = 0;

    @Schema(title = "不合格数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer defectQuantity = 0;

    @Schema(title = "批次状态（0:已开工; 1:制造中; 2:已完成）")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer lotStatus = 0;

    @Schema(title = "实际开始时间")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    private Date actualStartTime;

    @Schema(title = "实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualEndTime;

    @Schema(title = "弹性域1")
    private String attr1 = StringUtils.EMPTY;

    @Schema(title = "弹性域2")
    private String attr2 = StringUtils.EMPTY;

    @Schema(title = "弹性域3")
    private String attr3 = StringUtils.EMPTY;

    @Schema(title = "弹性域4")
    private String attr4 = StringUtils.EMPTY;
}
