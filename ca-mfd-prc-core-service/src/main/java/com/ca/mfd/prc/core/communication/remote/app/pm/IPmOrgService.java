package com.ca.mfd.prc.core.communication.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmplant", contextId = "inkelink-pm-pmplant")
public interface IPmOrgService {

    /**
     * 获取工厂编码
     *
     * @return
     */
    @GetMapping("/provider/getcurrentorgcode")
    ResultVO<String> getCurrentOrgCode();
}
