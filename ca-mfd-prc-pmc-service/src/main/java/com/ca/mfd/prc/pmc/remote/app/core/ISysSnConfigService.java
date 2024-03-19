package com.ca.mfd.prc.pmc.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mason
 */
@FeignClient(
        name = "ca-mfd-prc-core-service",
        path = "main/syssnconfig",
        contextId = "inkelink-core-syssnconfig")
public interface ISysSnConfigService {

    /**
     * 创建唯一码
     *
     * @param materialNo 产品编码
     * @param category   分类
     * @return 唯一码
     */
    @PostMapping(value = "/provider/createsn")
    ResultVO<String> createSn(@RequestParam("materialNo") String materialNo, @RequestParam("category") String category);
}
