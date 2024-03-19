package com.ca.mfd.prc.avi.remote.app.pqs;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pqs.entity.PqsDefectAnomalyEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ca-mfd-prc-pqs-service", path = "pqsdefectanomaly", contextId = "inkelink-pqs-pqsdefectanomaly")
public interface IPqsdefectanomalyService {
    /**
     * 根据code查询缺陷
     *
     * @param code 缺陷Code
     * @return 返回实体
     */
    @GetMapping("/provider/getentitybycode")
    ResultVO<PqsDefectAnomalyEntity> getEntityByCode(@RequestParam("code") String code);
}
