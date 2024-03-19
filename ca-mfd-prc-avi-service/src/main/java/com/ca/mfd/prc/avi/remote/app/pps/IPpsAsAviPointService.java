package com.ca.mfd.prc.avi.remote.app.pps;

import com.ca.mfd.prc.avi.host.scheduling.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pps-service", path = "aviasavipoint", contextId = "inkelink-pps-aviasavipoint")
public interface IPpsAsAviPointService {
    @PostMapping("/provider/insertasavipoint")
    ResultVO<String> insertAsAviPoint(@RequestBody InsertAsAviPointInfo datas);

    @GetMapping("/provider/insertdataasavipoint")
    ResultVO<String> insertDataAsAviPoint(@RequestParam("vehicleSn") String vehicleSn, @RequestParam("aviCode") String aviCode, @RequestParam("AviType") int AviType);
}
