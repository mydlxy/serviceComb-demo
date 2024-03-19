package com.ca.mfd.prc.pps.communication.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.remote.app.pm.entity.MidColorBaseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "communication", contextId = "inkelink-pm-communication")
public interface IPmCommunicationService {

    /**
     * 根据整车物料号获取特征版本号
     *
     * @return
     */
    @GetMapping(value = "/midcharacteristics/getcharacteristicsversion")
    ResultVO<String> getCharacteristicsVersion(@RequestParam("materialNo") String materialNo);

    /**
     * 根据整车物料号获取bom版本号
     *
     * @return
     */
    @GetMapping(value = "/midmaterial/getbomversion")
    ResultVO<String> getBomVersion(@RequestParam("plantCode") String plantCode,@RequestParam("materialNo") String materialNo,@RequestParam("specifyDate") String specifyDate);

    /**
     * 根据零件物料号获取零件bom版本号
     *
     * @return
     */
    @GetMapping(value = "/midbompart/getbompartversion")
    ResultVO<String> getBomPartVersion(@RequestParam("materialNo") String materialNo,@RequestParam("plantCode") String plantCode,@RequestParam("specifyDate") String specifyDate);

    /**
     * 获取颜色
     *
     * @return
     */
    @PostMapping(value = "/midcolorbase/getbyclorcode")
    ResultVO<List<MidColorBaseEntity>> getByClorCode(@RequestParam("colorCode") String colorCode);
}
