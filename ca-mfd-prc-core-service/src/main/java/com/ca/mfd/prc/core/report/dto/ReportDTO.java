package com.ca.mfd.prc.core.report.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author banny.luo
 */
public class ReportDTO {
    @Override
    public String toString() {
        return "ReportDTO";
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "IdModel", description = "IdModel")
    @Data
    public static class IdModel implements Serializable {
        @Schema(title = "主键")
        @JsonAlias(value = {"id", "Id"})
        private long id;
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "IdsModel", description = "IdsModel")
    @Data
    public static class IdsModel implements Serializable {
        @Schema(title = "ips")
        @JsonAlias(value = {"ids", "Ids"})
        private List<String> ids;
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "IpsModel", description = "IpsModel")
    @Data
    public static class IpsModel implements Serializable {
        @Schema(title = "ips")
        @JsonAlias(value = {"ips", "Ips"})
        private List<String> ips;
    }

    @EqualsAndHashCode(callSuper = false)
    @Schema(title = "PrintStatusModel", description = "PrintStatusModel")
    @Data
    public static class PrintStatusModel extends IdModel implements Serializable {
        @Schema(title = "text")
        @JsonAlias(value = {"text", "Text"})
        private String text;
    }
}
