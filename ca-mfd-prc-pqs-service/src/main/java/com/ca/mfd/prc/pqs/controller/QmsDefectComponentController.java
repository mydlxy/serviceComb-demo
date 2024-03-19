package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.BatchAddComponentInfo;
import com.ca.mfd.prc.pqs.dto.QmsApiDataInfo;
import com.ca.mfd.prc.pqs.dto.QmsComponent;
import com.ca.mfd.prc.pqs.dto.QmsComponentData;
import com.ca.mfd.prc.pqs.service.IPqsDefectComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量组件管理
 *
 * @Author: joel
 * @Date: 2023-08-25-16:11
 * @Description:
 */
@RestController
@RequestMapping("qmsdefectcomponent")
@Tag(name = "质量组件管理")
public class QmsDefectComponentController {
    private IPqsDefectComponentService pqsDefectComponentService;

    @PostMapping("batchaddcomponent")
    @Operation(summary = "批量维护组件数据")
    public ResultVO<String> batchAddComponent(@RequestBody QmsApiDataInfo<List<QmsComponent>> info) {
        List<BatchAddComponentInfo> list = new ArrayList<>();
        for (QmsComponent data : info.getData()) {
            for (QmsComponentData item : data.getItems()) {
                BatchAddComponentInfo batchAddComponentInfo = new BatchAddComponentInfo();
                batchAddComponentInfo.setGroupName(data.getFirstcomponentname());
                batchAddComponentInfo.setSubGroupName(item.getSecondarycomponentname());
                batchAddComponentInfo.setCode(item.getSecondaryComponentCode());
                batchAddComponentInfo.setDescription(item.getComponentdescription());
                batchAddComponentInfo.setPosition(item.getComponentLocationDescription());
                list.add(batchAddComponentInfo);
            }
        }
        pqsDefectComponentService.batchAddComponent(list);
        pqsDefectComponentService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("batchdel")
    @Operation(summary = "批量删除组件")
    public ResultVO<String> batchDel(@RequestBody List<String> codeList) {
        pqsDefectComponentService.batchDelByCodes(codeList);
        pqsDefectComponentService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}
