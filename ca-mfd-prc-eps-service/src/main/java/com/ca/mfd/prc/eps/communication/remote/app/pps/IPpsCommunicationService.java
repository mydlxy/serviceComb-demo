package com.ca.mfd.prc.eps.communication.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.BomConfigDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.SoftwareBomListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "communication", contextId = "inkelink-pps-midcommunication")
public interface IPpsCommunicationService {

    /**
     * 根据整车物料号获取单车配置字
     *
     * @return
     */
    @GetMapping(value = "/midsoftwarebomconfig/provider/getbomconfig")
    ResultVO<List<BomConfigDto>> getBomConfig(@RequestParam("materialNo") String materialNo ,@RequestParam("effectiveDate") String effectiveDate);
    /**
     * 根据整车物料号获取软件清单
     *
     * @return
     */
    @GetMapping(value = "/midsoftwarebomlist/provider/getsoftbom")
    ResultVO<List<SoftwareBomListDto>> getSoftBom(@RequestParam("materialNo") String materialNo,@RequestParam("effectiveDate") String effectiveDate);


}
