package com.ca.mfd.prc.core.fs.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "多目录文件列表VO")
@Data
public class MultiDirFilesVO {
    @Schema(title = "文件名称")
    private String fileName;
    @Schema(title = "文件路径")
    private String filePath;
    @Schema(title = "安全策略")
    private String strategy;
}
