package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AnomalyActivity
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AnomalyActivity")
public class AnomalyActivity {

    /**
     * dataId
     */
    @Schema(title = "dataId")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long dataId = Constant.DEFAULT_ID;

    /**
     * status
     */
    @Schema(title = "status")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * repairActivity
     */
    @Schema(title = "repairActivity")
    private RepairActivity repairActivity;

    /**
     * remark
     */
    @Schema(title = "remark")
    private String remark;

}
