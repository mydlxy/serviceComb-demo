package com.ca.mfd.prc.pm.remote.app.pqs;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pm.remote.app.pqs.dto.DefectAnomalyParaInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 阳波
 * @ClassName IPqsLogicService
 * @description:
 * @date 2023年10月09日
 * @version: 1.0
 */
@FeignClient(name = "ca-mfd-prc-pqs-service", path = "pqslogic", contextId = "inkelink-pqs-pqslogic")
public interface IPqsLogicService {
    /**
     * 获取缺陷数据
     * @param info
     * @return
     */
    @PostMapping("getanomalyshowlist")
    ResultVO<PageData<DefectAnomalyDto>> getWorkPlaceList(@RequestBody DefectAnomalyParaInfo info);
}
