package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author eric.zhou
 */
public class SysServiceManagerDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "", description = "单个服务操作")
    @Data
    public static class OperModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "服务外键")
        private String serviceId;

        @Schema(description = "代码")
        @JsonDeserialize(using = JsonDeserializeDefault.class)
        private Integer code = 0;

    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "", description = "单个服务操作")
    @Data
    public static class OpersModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "服务集合")
        private List<OperModel> datas;

    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "", description = "日志分页记录")
    @Data
    public static class LogPageModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "每页数量")
        private Integer pageSize;

        @Schema(description = "第几页")
        private Integer pageIndex;

        @Schema(description = "服务主键")
        private String id;

    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "", description = "单个服务操作")
    @Data
    public static class VsersionModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "服务外键")
        private String serviceId;

        @Schema(description = "版本号")
        private String versionNumber;
    }

}


