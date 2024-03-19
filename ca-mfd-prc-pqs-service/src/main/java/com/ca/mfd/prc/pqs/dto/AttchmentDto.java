package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 附件
 *
 * @Author: joel
 * @Date: 2023-04-13-9:50
 * @Description:
 */
@Data
@Schema(description = "附件")
public class AttchmentDto {

    /**
     * 操作ID
     */
    @Schema(title = "操作ID")
    private Long id = Constant.DEFAULT_ID;
    /**
     * 质检单
     */
    @Schema(title = "质检单")
    private String inspectionNo;
    /**
     * 附件名称
     */
    @Schema(title = "附件名称")
    private String attachName;
    /**
     * 附件地址
     */
    @Schema(title = "附件地址")
    private String address;
    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    public AttchmentDto() {
        id = Constant.DEFAULT_ID;
        inspectionNo = StringUtils.EMPTY;
        attachName = StringUtils.EMPTY;
        address = StringUtils.EMPTY;
        remark = StringUtils.EMPTY;
    }
}
