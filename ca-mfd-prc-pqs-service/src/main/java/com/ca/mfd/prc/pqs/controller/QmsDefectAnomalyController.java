package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.BatchAddAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.QmsActiveAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.QmsAnomaly;
import com.ca.mfd.prc.pqs.dto.QmsApiItemInfo;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量缺陷数据
 *
 * @Author: joel
 * @Date: 2023-08-25-14:14
 * @Description:
 */
@RestController
@RequestMapping("qmsdefectanomaly")
@Tag(name = "质量缺陷数据")
public class QmsDefectAnomalyController {
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;

    @Autowired
    private IPqsLogicService pqsLogicService;

    @PostMapping("batchaddanomaly")
    @Operation(summary = "批量维护缺陷数据")
    public ResultVO<String> batchAddAnomaly(@RequestBody QmsApiItemInfo<List<QmsAnomaly>> info) {
        List<BatchAddAnomalyInfo> list = new ArrayList<>();
        for (QmsAnomaly data : info.getItems()) {
            BatchAddAnomalyInfo item = new BatchAddAnomalyInfo();
            item.setCode(data.getCombinationcode());
            item.setDescription(data.getCombinationname());
            item.setDescription(data.getSecondaryComponentCode());
            item.setDefectCode(data.getAzimuthcode());
            item.setDefectPositionCode(data.getDefectcode());
            item.setDutyDepartment(data.getDutyDepartment());

            switch (data.getDefectlevel()) {
                case "A":
                    item.setLevel(1);
                    break;
                case "B":
                    item.setLevel(2);
                    break;
                case "C":
                    item.setLevel(3);
                    break;
                default:
                    item.setLevel(0);
                    break;
            }
            list.add(item);
        }
        pqsDefectAnomalyService.batchAddAnomaly(list);
        pqsDefectAnomalyService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("qmsactiveanomaly")
    @Operation(summary = "QMS系统激活缺陷")
    public ResultVO<String> qMsActiveAnomaly(@RequestBody QmsActiveAnomalyInfo info) {
        pqsLogicService.qMsActiveAnomaly(info.getTpsCode(), info.getStatus());
        pqsDefectAnomalyService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("batchdel")
    @Operation(summary = "批量删除缺陷数据")
    public ResultVO<String> batchDel(@RequestBody List<String> codeList) {
        pqsDefectAnomalyService.delete(codeList.toArray(new String[codeList.size()]));
        pqsDefectAnomalyService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}
