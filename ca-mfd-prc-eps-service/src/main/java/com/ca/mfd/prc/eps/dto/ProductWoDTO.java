package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 产品工艺信息父类
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ProductWoDTO")
public class ProductWoDTO {

    /**
     * 操作编号
     */
    @Schema(title = "操作编号")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;
    private Long woId = Constant.DEFAULT_ID;
    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long pmWoId = Constant.DEFAULT_ID;
    /**
     * 工艺编码
     */
    @Schema(title = "工艺编码")
    private String woCode = StringUtils.EMPTY;

    /**
     * 工艺名称
     */
    @Schema(title = "工艺名称")
    private String woName = StringUtils.EMPTY;

    /**
     * 工艺状态
     */
    @Schema(title = "工艺状态")
    private Integer woStatus = 0;

    /**
     * 工艺对应的缺陷编号
     */
    @Schema(title = "工艺对应的缺陷编号")
    private String woDefectAnomalyId = StringUtils.EMPTY;

    /**
     * workplaceName
     */
    @Schema(title = "workplaceName")
    private String workplaceName = StringUtils.EMPTY;
}