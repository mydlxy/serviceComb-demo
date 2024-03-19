package com.ca.mfd.prc.pps.remote.app.eps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.eps.entity.EpsBodySparePartTrackEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ca-mfd-prc-eps-service", path = "epsbodyspareparttrack", contextId = "inkelink-eps-epsbodyspareparttrack")
public interface IEpsBodySparePartTrackService {
    /**
     * 根据 虚拟VIN号 查询备件运输跟踪
     *
     * @param vehicleSn 虚拟VIN号
     * @return 返回 焊装车间备件运输跟踪
     */
    @GetMapping(value = "/provider/getentitybyvirtualvin")
    ResultVO<EpsBodySparePartTrackEntity> getEntityByVirtualVin(@RequestParam("vehicleSn") String vehicleSn);
}
