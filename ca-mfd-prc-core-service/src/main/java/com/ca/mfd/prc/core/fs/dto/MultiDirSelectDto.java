package com.ca.mfd.prc.core.fs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "多目录文件选择DTO")
public class MultiDirSelectDto {

    @Schema(title = "文件路径列表")
    private List<PathInfo> filePaths;
    @Data
    public static class PathInfo {
        @Schema(title = "文件路径")
        private String filePath;
        @Schema(title = "安全策略")
        private String strategy;
    }

}
