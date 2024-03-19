package com.ca.mfd.prc.pqs.remote.app.eps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.eps.entity.EpsVehicleWoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qu
 */
@FeignClient(
        name = "ca-mfd-prc-eps-service",
        path = "epsvehiclewo",
        contextId = "inkelink-eps-epsvehiclewo")
public interface IEpsVehicleWoService {

    /**
     * 根据唯一码sn获取工艺列表
     *
     * @param sn 唯一码
     * @return
     */
    @GetMapping(value = "/provider/getepsvehiclewodatas")
    ResultVO<List<EpsVehicleWoEntity>> getEpsVehicleWoDatas(@RequestParam("sn") String sn);
}
