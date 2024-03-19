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
 * @ClassName PmWorkUnit
 * @description: 工作单元
 * @date 2023年07月28日
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(of = "sourceId")
@Schema(description = "工作单元")
public class CmcPmWorkUnitVo {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkUnitId = Constant.DEFAULT_ID;


    /**
     * 工作区域ID
     */
    @Schema(title = "工作区域ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmAreaId = Constant.DEFAULT_ID;


    /**
     * 工作中心ID
     */
    @Schema(title = "工作中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkCenterId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    private String workUnitCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    private String workUnitName = StringUtils.EMPTY;


    /**
     * 开始距离
     */
    @Schema(title = "开始距离")
    private Integer beginDistance = 0;


    /**
     * 结束距离
     */
    @Schema(title = "结束距离")
    private Integer endDistance = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    private Boolean isEnable = false;


    /**
     * avi_typeavil类型（1:正常通过；2：再投入；3：跳出；x:完成通过；s:开工）
     */
    @Schema(title = "avi_typeavil类型（1:正常通过；2：再投入；3：跳出；x:完成通过；s:开工）")
    private String aviType = StringUtils.EMPTY;

    /**
     * 工位类型
     */
    @Schema(title = "工位类型（1.生产岗.2.质量门.3.返修岗)")
    private Integer workstationType = 0;

    /**
     * 生产l/t
     */
    @Schema(title = "生产l/t")
    private Integer productTime = 0;


    /**
     * 预警距离
     */
    @Schema(title = "预警距离")
    private Integer alarmDistance = 0;


    /**
     * 方位
     */
    @Schema(title = "方位")
    private Integer direction = 0;


    /**
     * 类型（1.工位；2.avi；3.库位）
     */
    @Schema(title = "类型（1.工位；2.avi；3.库位）")
    private Integer workUnitType = 0;


    /**
     * 工位号
     */
    @Schema(title = "工位号")
    private Integer workstationNo = 0;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer displayNo = 0;


    /**
     * 班组
     */
    @Schema(title = "班组")
    private String teamNo = StringUtils.EMPTY;

    private Integer source = 1;


    /**
     * 来源ID
     */
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;


}
