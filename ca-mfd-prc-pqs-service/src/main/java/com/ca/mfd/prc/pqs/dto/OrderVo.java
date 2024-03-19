package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;

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
@Schema(description = "车辆订单vo")
public class OrderVo {
    /**
     *
     */
    @Schema(title = "条码")
    private String barcode = Strings.EMPTY;
    /**
     *
     */
    @Schema(title = "唯一码")
    private String vin = Strings.EMPTY;

    /**
     * 区域
     */
    @Schema(title = "车型代码")
    private String modelCode = Strings.EMPTY;


    @Schema(title = "制造时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date manufactureDt = new Date();

}
