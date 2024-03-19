package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 * @Description: UpdateOrderBomPara
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@Data
@Schema(title = "UpdateOrderBomPara", description = "")
public class UpdateOrderBomPara {

    @Schema(description = "datas")
    private List<OrderBomInfos> datas = new ArrayList<>();

    public UpdateOrderBomPara() {
        this.datas = new ArrayList<>();
    }

}
