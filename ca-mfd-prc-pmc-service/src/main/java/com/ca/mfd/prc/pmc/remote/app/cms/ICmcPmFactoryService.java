package com.ca.mfd.prc.pmc.remote.app.cms;

import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.PmEquipmentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ca-mfd-cmc-pm-service", path = "factory", contextId = "inkelink-cmc-pm-factory")
public interface ICmcPmFactoryService {
    /**
     * 获取Bop信息
     *
     * @param model
     * @return
     */
    //@PostMapping(value = "/bop/getdata")
    //ResultVO<List<CmcPmBopVo>> getBopPage(@RequestBody DataDto model);

    /**
     * 获取设备信息
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/equipment/getdata")
    ResultVO<List<PmEquipmentVO>> getEquipmentPage(@RequestBody DataDto model);
}
