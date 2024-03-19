package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.MidVehicleMasterEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IPpsEntryReportService
 *
 * @author inkelink
 * @date 2023/09/05
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "communication/midvehiclemode", contextId = "inkelink-pps-vehiclemasterprocess")
public interface IPpsVehicleMasterProcessService {
    @GetMapping(value = "/provider/getvehiclemasterbyparam")
    ResultVO<MidVehicleMasterEntity> getVehicleMasterByParam(@RequestParam("vehicleMaterialNumber") String vehicleMaterialNumber,@RequestParam("bomRoom") String bomRoom);
}
