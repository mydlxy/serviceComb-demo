package com.ca.mfd.prc.core.fs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "base65图片模型")
public class ImgModel {
    @Schema(title = "图片base64字符串")
    private String base64;
    @Schema(title = "文件名称")
    private String fileName;

}
