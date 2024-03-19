package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.pps.communication.entity.MidAsShopPlanEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsVehicleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS整车计划和车间计划
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "AS整车计划和车间计划")
public class AsVehicleShopPlanDto {


    /**
     * 车间计划
     */
    @Schema(title = "车间计划")
    private List<MidAsShopPlanEntity> shopPlans = new ArrayList<>();


    /**
     * 整车
     */
    @Schema(title = "整车")
    private List<MidAsVehicleEntity> vehicles = new ArrayList<>();


}