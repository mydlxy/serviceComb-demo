package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmTemporaryPermissionsVO", description = "临时权限")
@Data
public class PrmTemporaryPermissionsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "模型")
    private String model;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String permissionModel;

    @Schema(description = "编码")
    private String permissionName;

    @Schema(description = "编码")
    private String userName;

    @Schema(description = "编码")
    private String userNo;

    @Schema(description = "回收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date recycleDt;

}

