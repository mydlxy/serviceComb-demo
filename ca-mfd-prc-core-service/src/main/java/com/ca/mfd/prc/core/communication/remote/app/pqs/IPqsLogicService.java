package com.ca.mfd.prc.core.communication.remote.app.pqs;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.dto.IccDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pqs-service",
        path = "pqsLogic",
        contextId = "inkelink-pqs-pqsLogic")
public interface IPqsLogicService {

    /**
     * 同步到缺陷库
     * @param dtos
     * @return
     */
    @PostMapping(value = "/provider/receiveiccdata")
    ResultVO receiveIccData(@RequestBody List<IccDto> dtos);

    /**
     * 校验缺陷库
     * @param dtos
     * @return
     */
    @PostMapping("/provider/checkiccdata")
    ResultVO<List<IccDto>> checkIccData(@RequestBody List<IccDto> dtos);
}
