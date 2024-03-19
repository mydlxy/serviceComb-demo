package com.ca.mfd.prc.pqs.remote.app.otweb;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-mom-gateway",
        path = "/mfd/prc/iot/otweb/workplace",
        contextId = "ca-mfd-mom-gateway")
public interface IOtWebService {

    /**
     * 后台修改工艺状态
     *
     * @param woId
     * @param status
     * @return
     */
    @GetMapping(value = "/systemsavewostatus")
    ResultVO systemSaveWoStatus(@RequestParam("woId") Long woId, @RequestParam("status") Integer status);
}
