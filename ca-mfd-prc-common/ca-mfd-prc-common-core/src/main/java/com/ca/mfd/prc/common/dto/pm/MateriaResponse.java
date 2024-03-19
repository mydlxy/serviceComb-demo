package com.ca.mfd.prc.common.dto.pm;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;

/**
 * 获取扩展对象
 *
 * @author inkelink
 * @date 2023年4月4日
 * @变更说明 BY eric.zhou At 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "MateriaResponse")
public class MateriaResponse {

    @Schema(title = "物料代码")
    @JsonProperty("WLBM")
    private String materialNo = Strings.EMPTY;

    @Schema(title = "物料说明")
    @JsonProperty("wlsm")
    private String chinese = Strings.EMPTY;

    @Schema(title = "传入时间")
    @JsonProperty("crsj")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date requestTime = new Date();

    @Schema(title = "传输标记")
    @JsonProperty("csbj")
    private String tag = Strings.EMPTY;

    @Schema(title = "物料组")
    @JsonProperty("wlz")
    private String partGroup = Strings.EMPTY;

    @Schema(title = "物料类型 1 采购 2 制造")
    @JsonProperty("wllx")
    private String materialType = "1";

    @Schema(title = "物料单位")
    @JsonProperty("dw")
    private String unit = Strings.EMPTY;

    @Schema(title = "公告好")
    @JsonProperty("GGH")
    private String ggh = Strings.EMPTY;

}
