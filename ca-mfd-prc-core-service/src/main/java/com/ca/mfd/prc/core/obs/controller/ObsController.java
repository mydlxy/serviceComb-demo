package com.ca.mfd.prc.core.obs.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.obs.service.IObsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "obs对象存储服务", description = "obs对象存储API")
@RestController
@RequestMapping("/obsmanager")
public class ObsController {
    @Autowired
    private IObsService obsService;


    @Operation(summary = "文件上传")
    @Parameters({
            @Parameter(name = "userId", required = false, in = ParameterIn.HEADER, description = "操作者", example = "xxxxx")})
    @PostMapping(value = "/uploadobject")
    public ResultVO<String> uploadobject(@RequestHeader(name = "userId", required = false) String userId,
                                         @RequestParam(value = "uploadfile") MultipartFile multipartFile) {
        ResultVO<String> resultVO = obsService.uploadFile(multipartFile);
        return resultVO;
    }

    @Operation(summary = "文件下载")
    @Parameters({
            @Parameter(name = "userId", required = false, in = ParameterIn.HEADER, description = "操作者", example = "xxxxx")})
    @PostMapping(value = "/downfile")
    public void downFile(HttpServletResponse response, @RequestParam(value = "fileName") String fileName) {
        obsService.downFile(response, fileName);
    }

}
