package com.ca.mfd.prc.avi.communication.remote.app.pps;

import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pps-service", path = "communication/midelectricalinspection", contextId = "inkelink-pps-communication")
public interface IPpsCommunicationService {

    /**
     * 根据vin号组装软件信信息
     *
     * @return
     */
    @GetMapping(value = "/provider/getecucarinfobyvin")
    ResultVO<EcuCarInfoDto> getEcuCarInfoByVin(@RequestParam("vin") String vin);

    /**
     * 根据vin号组装整车信息
     *
     * @return
     */
    @GetMapping(value = "/provider/getcarinfobyvin")
    ResultVO<CarInfoDto> getCarInfoByVin(@RequestParam("vin") String vin);

    /**
     * 根据vin号组装过点信息
     *
     * @return
     */
    @GetMapping(value = "/provider/getsiteinfobyvin")
    ResultVO<SiteInfoDto> getSiteInfoByVin(@RequestParam("vin") String vin);




    /**
     * 组装软件信息-手动
     *
     * @return
     */
    @GetMapping(value = "/provider/getecucarinfobyvintest")
    ResultVO<EcuCarInfoDto> getEcuCarInfoByVinTest(@RequestBody MidDjTestSendEntity dto);

    /**
     * 组装整车信息-手动
     *
     * @return
     */
    @GetMapping(value = "/provider/getcarinfobyvintest")
    ResultVO<CarInfoDto> getCarInfoByVinTest(@RequestBody MidDjTestSendEntity dto);

}
