package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.CheckFetureExpressionPara;
import com.ca.mfd.prc.pps.dto.FetureTestPara;
import com.ca.mfd.prc.pps.dto.FilterFetureExpressionPara;
import com.ca.mfd.prc.pps.service.IAnalysisFetureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 检查特征表达式Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("analysisfeture")
@Tag(name = "检查特征表达式", description = "检查特征表达式")
public class AnalysisFetureController extends BaseApiController {

    @Autowired
    private IAnalysisFetureService analysisFetureService;

    @Operation(summary = "检查特征表达式是否命中")
    @PostMapping("checkfetureexpression")
    public ResultVO<Boolean> checkFetureExpression(@RequestBody CheckFetureExpressionPara para) {
        ResultVO<Boolean> result = new ResultVO();
        return result.ok(analysisFetureService.checkFetureExpression(para));
    }

    @Operation(summary = "验证特征表达式")
    @PostMapping("verifyfetureexpression")
    public ResultVO<Boolean> verifyFetureExpression(@RequestBody FetureTestPara para) {
        ResultVO<Boolean> result = new ResultVO();
        return result.ok(analysisFetureService.verifyFetureExpression(para));
    }

    @Operation(summary = "过滤命中的特征表达式")
    @PostMapping("filterfetureexpression")
    public ResultVO<List<String>> filterFetureExpression(@RequestBody FilterFetureExpressionPara para) {
        ResultVO<List<String>> result = new ResultVO<>();
        return result.ok(analysisFetureService.filterFetureExpression(para));
    }

}