package com.ca.mfd.prc.avi.remote.app.core.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author eric.zhou
 */
public class SysConfigurationDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "GetModel", description = "GetModel")
    @Data
    public static class GetModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "集合")
        private List<String> category = new ArrayList<>();
    }

}


