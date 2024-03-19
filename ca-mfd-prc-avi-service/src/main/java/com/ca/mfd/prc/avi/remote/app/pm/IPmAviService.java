package com.ca.mfd.prc.avi.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmavi", contextId = "inkelink-pm-pmavi")
public interface IPmAviService {
    /**
     * @return AVI点列表
     */
    @GetMapping(value = "/provider/getaviinfos")
    ResultVO<List<AviInfoDTO>> getAviInfos();
}
