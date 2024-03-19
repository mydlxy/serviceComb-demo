package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: AUDIT评审单实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
public class TemplateCopyDto {

    /**
     * 主键
     */
    @Schema(title = "模板id")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 模板代码
     */
    @Schema(title = "模板代码")
    private String templateCode = StringUtils.EMPTY;
    /**
     * 模板名称
     */
    @Schema(title = "模板名称")
    private String templateName = StringUtils.EMPTY;
    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialNo = StringUtils.EMPTY;


}