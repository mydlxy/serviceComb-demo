package com.ca.mfd.prc.eps.remote.app.pps;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * IPpsEntryService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsentry", contextId = "inkelink-pps-ppsentry")
public interface IPpsEntryService {

    @Operation(summary = "获取条件数据")
    @PostMapping("/provider/getdata")
    ResultVO<List<PpsEntryEntity>> getData(@RequestBody List<ConditionDto> conditions);

    @Operation(summary = "获取条件数据")
    @GetMapping("/provider/getfirstentrytypeshopcodesn")
    ResultVO<PpsEntryEntity> getFirstEntryTypeShopCodeSn(@RequestParam("sn") String sn, @RequestParam("entryType") Integer entryType, @RequestParam("shopCode") String shopCode);
}
