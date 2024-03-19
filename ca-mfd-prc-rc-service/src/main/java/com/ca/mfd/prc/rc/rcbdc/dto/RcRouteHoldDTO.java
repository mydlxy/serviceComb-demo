package com.ca.mfd.prc.rc.rcbdc.dto;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author banny.luo
 */
public class RcRouteHoldDTO {
    @Override
    public String toString() {
        return "RcRouteHoldDTO";
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "AddModel", description = "AddModel")
    @Data
    public static class AddModel implements Serializable {
        @Schema(title = "区域外键")
        @JsonAlias(value = {"areaId", "AreaId"})
        private String areaId;

        @Schema(title = "被暂存车辆信息")
        @JsonAlias(value = {"tpsCodes", "TpsCodes"})
        private List<String> tpsCodes;

        @Schema(title = "原因")
        @JsonAlias(value = {"reason", "Reason"})
        private String reason;
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "RemoveModel", description = "RemoveModel")
    @Data
    public static class RemoveModel extends IdsModel implements Serializable {
        @Schema(title = "原因")
        @JsonAlias(value = {"reason", "Reason"})
        private String reason;
    }
}
