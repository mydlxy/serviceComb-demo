package com.ca.mfd.prc.core.communication.remote.app.avi;

import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-avi-service", path = "avi/avitrackingrecord", contextId = "inkelink-avi-avitrackingrecord")
public interface IAviTrackingRecordService {

    /**
     * 第三方过点数据
     *
     * @return
     */
    @Operation(summary = "第三方过点数据")
    @PostMapping("saveThirdPointData")
    ResultVO<String> saveThirdPointData(@RequestParam("sn") String sn, @RequestParam("aviCode") String aviCode, @RequestParam("aviType") Integer aviType, @RequestParam("avitrackingenum") Integer avitrackingenum, @RequestParam("isProcess") Boolean isProcess, @RequestParam("passTime") Date passTime);
}
