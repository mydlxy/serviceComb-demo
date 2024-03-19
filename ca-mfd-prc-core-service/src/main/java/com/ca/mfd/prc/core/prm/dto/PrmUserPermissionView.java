package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 */
@Data
public class PrmUserPermissionView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "账户主键")
    private String code;

    /**
     * 回收时间
     */
    @Schema(title = "回收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date recycleDt;

}
