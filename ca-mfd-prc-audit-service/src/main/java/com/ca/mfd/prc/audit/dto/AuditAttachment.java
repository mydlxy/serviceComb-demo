package com.ca.mfd.prc.audit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 附件信息
 *
 * @Author: joel
 * @Date: 2023-08-20-14:30
 * @Description:
 */
@Data
@Schema(description = "附件信息")
public class AuditAttachment {
    /**
     * 附件ID
     */
    @Schema(title = "附件ID")
    private String id = StringUtils.EMPTY;

    /**
     * 附件名
     */
    @Schema(title = "附件名")
    private String name;

    /**
     * 附件路径
     */
    @Schema(title = "附件路径")
    private String path;

    /**
     * 是否可用, 0表示不可用，1表示可用
     */
    @Schema(title = "是否可用, 0表示不可用，1表示可用")
    private String flags;

}
