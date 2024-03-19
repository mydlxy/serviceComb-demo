package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.BatchAddPositionInfo;
import com.ca.mfd.prc.pqs.dto.QmsApiItemInfo;
import com.ca.mfd.prc.pqs.dto.QmsPositionInfo;
import com.ca.mfd.prc.pqs.service.IPqsDefectPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量缺陷位置
 *
 * @Author: joel
 * @Date: 2023-08-25-16:29
 * @Description:
 */
@RestController
@RequestMapping("qmsdefectposition")
@Tag(name = "质量缺陷位置")
public class QmsDefectPositionController {
    private IPqsDefectPositionService pqsDefectPositionService;

    @PostMapping("batchaddpostion")
    @Operation(summary = "批量维护缺陷位置数据")
    public ResultVO<String> batchAddPostion(@RequestBody QmsApiItemInfo<List<QmsPositionInfo>> info) {
        List<BatchAddPositionInfo> list = new ArrayList<>();
        for (QmsPositionInfo item : info.getItems()) {
            BatchAddPositionInfo batchAddPositionInfo = new BatchAddPositionInfo();
            batchAddPositionInfo.setGroupName(item.getFirstgroupingname());
            batchAddPositionInfo.setSubGroupName(item.getSecondarygroupingname());
            batchAddPositionInfo.setCode(item.getAzimuthcode());
            batchAddPositionInfo.setDescription(item.getAzimuthname());
            list.add(batchAddPositionInfo);
        }
        pqsDefectPositionService.batchAddPosition(list);
        pqsDefectPositionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("batchdel")
    @Operation(summary = "批量删除缺陷位置")
    public ResultVO<String> batchDel(@RequestBody List<String> codeList) {
        pqsDefectPositionService.batchDelByCodes(codeList);
        pqsDefectPositionService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}
