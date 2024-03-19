package com.ca.mfd.prc.eps.communication.remote.app.qps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @author mason.mei
 * @Description: 法规
 * @date 2024年1月05日
 */
@FeignClient(name = "ca-mfd-quc-qps-service", path = "communication/midcarcloudcarinfo", contextId = "inkelink-qps-midcarcloudcarinfo")
public interface IQpsCommunicationService {

    /**
     * 整车数据接口(法规相关字段赋值)
     * @param dto
     * @return
     */
    @PostMapping(value = "/provider/carinfosupplement")
    ResultVO<CarCloudCarInfoDto> carInfoSupplement(@RequestBody CarCloudCarInfoDto dto);
}
