package com.ca.mfd.prc.pqs.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author qu
 */
@FeignClient(
        name = "ca-mfd-prc-core-service",
        path = "main/syssequencenumber",
        contextId = "inkelink-core-syssequencenumber")
public interface ISysSequenceNumberService {

    @GetMapping({"/provider/getseqnumwithtransaction"})
    ResultVO<String> getSeqNumWithTransaction(@RequestParam("seqType") String seqType);

}
