package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Schema(description = "pair参对象")
public class FileSaveDto {

    private String filepath;

    private String fileName;

    private String remark;



}

