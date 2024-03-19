package com.ca.mfd.prc.pps.remote.app.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName PmWorkCenter
 * @description: 工作中心
 * @date 2023年07月28日
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(of = "sourceId")
@Schema(description = "工作中心")
public class CmcPmWorkCenterVo {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkCenterId = Constant.DEFAULT_ID;


    /**
     * 工作区域外键
     */
    @Schema(title = "工作区域外键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmAreaId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    private String workCenterCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    private String workCenterName = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer displayNo = 0;


    /**
     * 工位数量
     */
    @Schema(title = "工位数量")
    private Integer stationCount = 0;


    /**
     * 线体开始距离
     */
    @Schema(title = "线体开始距离")
    private Integer beginDistance = 0;


    /**
     * 线体结束距离
     */
    @Schema(title = "线体结束距离")
    private Integer endDistance = 0;


    /**
     * 线体类型(1.主线；2.辅线)
     */
    @Schema(title = "线体类型(1.主线；2.辅线)")
    private Integer lineType = 0;


    /**
     * 工位长度
     */
    @Schema(title = "工位长度")
    private Integer stationLength = 0;


    /**
     * 运行方式(1.CONTINUE;2.STOP&GO)
     */
    @Schema(title = "运行方式(1.CONTINUE;2.STOP&GO)")
    private Integer runType = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    private Boolean isEnable = false;


    /**
     * JPH
     */
    @Schema(title = "JPH")
    private Integer workCenterDesignJph = 0;

    private Integer source = 1;


    /**
     * 来源ID
     */
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;

}
