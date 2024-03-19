package com.ca.mfd.prc.pm.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
public class PmVersionDTO implements Serializable {

    @NotNull(message = "id不能为空")
    @Schema(title = "车间外键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;

    @Schema(title = "车间代码")
    private String shopCode = StringUtils.EMPTY;

    @Schema(title = "发布备注")
    private String remark = StringUtils.EMPTY;
    @Schema(title = "版本ID")
    private Long id;
}
