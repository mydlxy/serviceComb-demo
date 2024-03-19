package com.ca.mfd.prc.core.fs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "文件下载地址DTO")
public class DownFileModelDto {

    @Schema(title = "文件下载地址列表")
    private List<PathInfo> filePaths;

    @Data
    public static class PathInfo {
        @Schema(title = "文件路径")
        private String filePath;
        @Schema(title = "安全策略")
        private String strategy;
    }

}
