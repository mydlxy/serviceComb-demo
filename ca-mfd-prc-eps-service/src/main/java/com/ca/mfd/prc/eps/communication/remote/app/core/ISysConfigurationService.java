package com.ca.mfd.prc.eps.communication.remote.app.core;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ISysConfigurationService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "main/sysconfiguration", contextId = "inkelink-core-midsysconfiguration")
public interface ISysConfigurationService {
    @GetMapping(value = "/provider/getsysconfigurations")
    ResultVO<List<SysConfigurationEntity>> getSysConfigurations(@RequestParam("category") String category);

    @GetMapping(value = "/provider/getalldatas")
    ResultVO<List<SysConfigurationEntity>> getAllDatas();

    @GetMapping(value = "/provider/getcombodatas")
    ResultVO<List<ComboInfoDTO>> getComboDatas(@RequestParam("category") String category);

    @GetMapping(value = "/provider/getconfiguration")
    ResultVO<String> getConfiguration(@RequestParam("key") String key, @RequestParam("category") String category);
}
