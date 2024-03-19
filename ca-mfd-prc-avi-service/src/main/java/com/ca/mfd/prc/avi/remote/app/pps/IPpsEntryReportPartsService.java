package com.ca.mfd.prc.avi.remote.app.pps;

import com.ca.mfd.prc.avi.host.scheduling.dto.PpsEntryReportPartsDto;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsentryreportparts", contextId = "inkelink-pps-ppsentryreportparts")
public interface IPpsEntryReportPartsService {
    /**
     * 根据分类获取前20条
     *
     * @param orderCategory
     * @return 列表
     */
    @GetMapping("/provider/gettopdatabyordercategory")
    ResultVO<List<PpsEntryReportPartsEntity>> gettopdatabyordercategory(@RequestParam("orderCategory") Integer orderCategory);


    @PostMapping("/provider/getrecordbyordercategory")
    ResultVO<List<PpsEntryReportPartsEntity>> getrecordbyordercategory(@RequestBody PpsEntryReportPartsDto datas);

    /**
     * 更新状态
     *
     * @param id 主键
     */
    @GetMapping("/provider/updateispassavibyid")
    ResultVO<String> updateIsPassAviById(@RequestParam("id") Long id);
}
