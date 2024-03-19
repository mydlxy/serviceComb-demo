package com.ca.mfd.prc.pqs.dto;


import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建评审工单
 *
 * @Author: joel
 * @Date: 2023-04-13-18:42
 * @Description:
 */
@Data
@Schema(description = "QG工料费信息")
public class CreateMMScrapInfo {

    /**
     * 处置方式;1、融化铝渣 2、后处理铝削、3、加加工铝削、4、综合固废
     */
    @Schema(title = "处置方式;1、融化铝渣 2、后处理铝削、3、加加工铝削、4、综合固废")
    private int scrapType = 1;

    /**
     * 记录时间
     */
    @Schema(title = "记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date recordDt = new Date();

    /**
     * 送料人
     */
    @Schema(title = "送料人")
    private String sendUser = StringUtils.EMPTY;

    /**
     * 接收人
     */
    @Schema(title = "接收人")
    private String recieveUser = StringUtils.EMPTY;

    /**
     * 车牌
     */
    @Schema(title = "车牌")
    private String plateNo = StringUtils.EMPTY;

    /**
     * 空车重量
     */
    @Schema(title = "空车重量")
    private BigDecimal emptyWeight = BigDecimal.valueOf(0);

    /**
     * 最终重量
     */
    @Schema(title = "最终重量")
    private BigDecimal finalWeight = BigDecimal.valueOf(0);

    /**
     * 真实质量
     */
    @Schema(title = "真实质量")
    private BigDecimal realWeight = BigDecimal.valueOf(0);

    /**
     * 单位
     */
    @Schema(title = "单位")
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

}
