package com.ca.mfd.prc.core.fs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "base64图片上传DTO")
public class FileUploadViewModel2 {
    @Schema(title = "分类")
    private String category;
    @Schema(title = "上传图片列表")
    private List<ImgModel> data;
}
