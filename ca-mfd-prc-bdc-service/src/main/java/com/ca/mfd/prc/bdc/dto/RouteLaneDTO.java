package com.ca.mfd.prc.bdc.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

public class RouteLaneDTO {
    @Override
    public String toString() {
        return "RouteLaneDTO";
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "GenerateModel", description = "GenerateModel")
    @Data
    public static class GenerateModel implements Serializable {
        @Schema(title = "区域外键")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = JsonDeserializeLong.class)
        @JsonAlias(value = {"areaId", "areaId"})
        private Long areaId;
    }
}
