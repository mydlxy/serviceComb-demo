package com.ca.mfd.prc.avi.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmversion", contextId = "inkelink-pm-pmversion")
public interface IPmVersionService {
    /**
     * 查询一个实体
     *
     * @return 返回一个实体
     */
    @GetMapping("/provider/getobjectedpm")
    ResultVO<PmAllDTO> getObjectedPm();
}
