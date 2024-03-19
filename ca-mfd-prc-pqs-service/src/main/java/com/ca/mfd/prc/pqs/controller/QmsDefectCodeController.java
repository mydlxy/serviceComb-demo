package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.BatchAddCodeInfo;
import com.ca.mfd.prc.pqs.dto.QmsApiDataInfo;
import com.ca.mfd.prc.pqs.dto.QmsCode;
import com.ca.mfd.prc.pqs.dto.QmsCodeItem;
import com.ca.mfd.prc.pqs.service.IPqsDefectCodeService;
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
 * 质量分类管理
 *
 * @Author: joel
 * @Date: 2023-08-25-15:56
 * @Description:
 */
@RestController
@RequestMapping("qmsdefectcode")
@Tag(name = "质量分类管理")
public class QmsDefectCodeController {
    @Autowired
    private IPqsDefectCodeService pqsDefectCodeService;

    @PostMapping("batchaddcode")
    @Operation(summary = "批量添加缺陷分类数据")
    public ResultVO<String> batchAddCode(@RequestBody QmsApiDataInfo<List<QmsCode>> info) {
        List<BatchAddCodeInfo> list = new ArrayList<>();
        for (QmsCode data : info.getData()) {
            for (QmsCodeItem item : data.getChilden()) {
                BatchAddCodeInfo batchAddCodeInfo = new BatchAddCodeInfo();
                batchAddCodeInfo.setGroupName(data.getClassIDefects());
                batchAddCodeInfo.setSubGroupName(data.getClassIIDefects());
                batchAddCodeInfo.setCode(item.getDefectcode());
                batchAddCodeInfo.setDescription(item.getDefectname());
                list.add(batchAddCodeInfo);
            }
        }
        pqsDefectCodeService.batchAddCode(list);
        pqsDefectCodeService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping("batchdel")
    @Operation(summary = "批量删除缺陷分类数据")
    public ResultVO<String> batchDel(@RequestBody List<String> codeList) {
        pqsDefectCodeService.batchDelByCodes(codeList);
        pqsDefectCodeService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }
}
