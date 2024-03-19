package com.ca.mfd.prc.avi.remote.app.eps;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-eps-service", path = "epssparebindingdetail", contextId = "inkelink-eps-epssparebindingdetail")
public interface IEpsSpareBindingDetailService {
    /**
     * 备件过点 查询虚拟VIN号
     *
     * @param id 主键
     * @return 虚拟VIN号集合
     */
    @GetMapping(value = "/provider/getpartvirtualvinbyparttrackid")
    ResultVO<List<String>> getPartVirtualVinByPartTrackId(@RequestParam("id") String id);
}
