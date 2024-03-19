package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.MidAsVehicleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MidAsVehicleProvider
 * @author mason
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "communication/asvehicle", contextId = "inkelink-pps-midasvehicle")
public interface IMidAsVehicleService {
    @GetMapping("/provider/getvehiclebyplanno")
    ResultVO<MidAsVehicleDto> getVehicleByPlanNo(@RequestParam("planNo") String planNo);
}
