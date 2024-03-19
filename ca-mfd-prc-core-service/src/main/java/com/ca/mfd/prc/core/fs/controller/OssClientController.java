package com.ca.mfd.prc.core.fs.controller;

import com.ca.mfd.prc.common.obs.model.CaTemporarySignatureResponse;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.fs.constant.OssClientConst;
import com.ca.mfd.prc.core.obs.service.IObsService;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OssClientController oss客户端
 *
 * @author 李国伟
 * @date 2023/09/25
 */
@Tag(name = "oss客户端服务", description = "oss客户端服务API")
@RestController
@RequestMapping("fs/ossclient")
public class OssClientController {

    @Autowired
    private IObsService obsService;

    @Autowired
    private ISysConfigurationService sysConfigurationService;

    @Operation(summary = "获取上传临时签名")
    @GetMapping("/getclientsing")
    public ResultVO<Object> getClientSing(String contentType, String fileName) {
        String ossConfig = sysConfigurationService.getConfiguration("Client", "Oss");
        CaTemporarySignatureResponse response;
        ResultVO<Object> result = new ResultVO<>();
        if (ossConfig.equals(OssClientConst.HUA_WEI)) {
            response = obsService.getClientSing(contentType, fileName);
            return result.ok(response, "返回成功");
        }

        return result.ok("/", "返回成功");

    }
}
