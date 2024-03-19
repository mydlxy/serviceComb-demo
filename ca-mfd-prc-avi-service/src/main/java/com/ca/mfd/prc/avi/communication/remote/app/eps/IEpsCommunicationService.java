package com.ca.mfd.prc.avi.communication.remote.app.eps;

import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mason
 * @Description: 电检EP信息
 * @date 2023年12月27日
 */
@FeignClient(name = "ca-mfd-prc-eps-service", path = "communication/middjepdata", contextId = "inkelink-eps-communication")
public interface IEpsCommunicationService {


    /**
     * 根据vin号组装电检EP信息
     * @param vin
     * @return
     */
    @GetMapping(value = "/provider/getepinfobyvin")
    ResultVO<List<EpInfoDto>> getEpInfoByVin(@RequestParam("vin") String vin);


}
