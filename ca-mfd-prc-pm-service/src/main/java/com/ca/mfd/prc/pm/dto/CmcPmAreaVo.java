package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 生产线
 * @date 2023年8月1日
 * @变更说明 BY inkelink At 2023年8月1日
 */
@Data
@EqualsAndHashCode(of = "sourceId")
@Schema(description = "作区域")
public class CmcPmAreaVo {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmAreaId = Constant.DEFAULT_ID;


    /**
     * 工厂
     */
    @Schema(title = "工厂")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmOrganizationId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @JsonSerialize(using = ToStringSerializer.class)
    private String areaCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @JsonSerialize(using = ToStringSerializer.class)
    private String areaName = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer displayNo = 0;


    /**
     * 生产l/t(上线到下线的时间)
     */
    @Schema(title = "生产l/t(上线到下线的时间)")
    private Integer productTime = 0;


    /**
     * JPH
     */
    @Schema(title = "JPH")
    private Integer pmAreaDesignJph = 0;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    private Boolean isEnable = false;

    private List<CmcPmWorkCenterVo> pmWorkCenters = new ArrayList();

    private Integer source = 1;


    /**
     * 来源ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;

}
