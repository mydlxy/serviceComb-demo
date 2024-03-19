package com.ca.mfd.prc.pqs.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author edwards.qu
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

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @return 唯一码
     */
    @PostMapping(value = "/provider/createsn")
    ResultVO<String> createSn(@RequestParam("category") String category);

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @param para     参数
     * @return 唯一码
     */
    @PostMapping(value = "/provider/createsnbypara")
    ResultVO<String> createSnBypara(@RequestParam("category") String category, @RequestBody Map<String, String> para);
}
