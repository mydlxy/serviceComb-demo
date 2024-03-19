package com.ca.mfd.prc.rc.rcsml.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author banny.luo
 */
public class RcRouteCacheDTO {
    @Override
    public String toString() {
        return "RcRouteCacheDTO";
    }

    @Schema(title = "RouteOutModel", description = "RouteOutModel")
    @Data
    public static class RouteOutModel {
        @Schema(title = "缓存外键")
        @JsonAlias(value = {"id", "Id"})
        private String id;
    }

    @Schema(title = "LaneModel", description = "LaneModel")
    @Data
    public static class LaneModel {
        @Schema(title = "路由区外键")
        @JsonAlias(value = {"rcRouteAreaId", "RcRouteAreaId"})
        private String rcRouteAreaId;
    }
}
