package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽检模型
 *
 * @Author: joel
 * @Date: 2023-04-19-11:23
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "抽检模型")
public class PqsEntryCjDto {
    /**
     *
     */
    @Schema(title = "")
    private String barcode;

    /**
     * 区域
     */
    @Schema(title = "区域")
    private String areaCode;

    /**
     * 批次
     */
    @Schema(title = "批次")
    private String lotNo;

    /**
     * 图片
     */
    @Schema(title = "图片")
    private String productImg;

    /**
     * 工单ID
     */
    @Schema(title = "工单ID")
    private Long id = Constant.DEFAULT_ID;

    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    private String inspectionNo;

    /**
     * 抽检单类型
     */
    @Schema(title = "抽检单类型")
    private String entryType;

    /**
     * 状态
     */
    @Schema(title = "状态")
    private Integer status = 0;

    /**
     * 状态描述
     */
    @Schema(title = "状态描述")
    private String statusDescription;

    /**
     * 检验人
     */
    @Schema(title = "检验人")
    private String qcUser;

    /**
     * 检验时间
     */
    @Schema(title = "检验时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date qcDt;

    /**
     * 质检结论
     */
    @Schema(title = "质检结论")
    private Integer result = 0;

    /**
     * 质量结论描述
     */
    @Schema(title = "质量结论描述")
    private String resultDescription;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 物料号
     */
    @Schema(title = "物料号")
    private String materialNo;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    private String materialDescription;

    /**
     * 物料图号
     */
    @Schema(title = "物料图号")
    private String materialDrawingNo;

    /**
     * 数量
     */
    @Schema(title = "数量")
    private BigDecimal qty = BigDecimal.valueOf(0);

    /**
     * 抽检数量
     */
    @Schema(title = "抽检数量")
    private BigDecimal checkQty = BigDecimal.valueOf(0);

    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    private BigDecimal unpassQty = BigDecimal.valueOf(0);

    /**
     * 合格数量
     */
    @Schema(title = "合格数量")
    private BigDecimal acceptQty = BigDecimal.valueOf(0);

    /**
     *
     */
    @Schema(title = "")
    private BigDecimal passQty = BigDecimal.valueOf(0);
}
