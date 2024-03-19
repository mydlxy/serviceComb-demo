package com.ca.mfd.prc.core.dc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 */
@Data
public class DcDTO {
    @Schema(title = "PrmModel", description = "PrmModel")
    @Data
    public static class PrmModel implements Serializable {
        private static final long serialVersionUID = 1L;
        private String code;
        private List<ItemModel> datas;
    }

    @Schema(title = "ItemModel", description = "ItemModel")
    @Data
    public static class ItemModel implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private Integer type = 0;
    }

    @Schema(title = "DelModel", description = "DelModel")
    @Data
    public static class DelModel implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer type;
        private List<String> ids;
    }
}
