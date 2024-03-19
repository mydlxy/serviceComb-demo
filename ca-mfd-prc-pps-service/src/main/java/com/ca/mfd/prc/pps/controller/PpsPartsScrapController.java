package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.PartsScrapPara;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.entity.PpsPartsScrapEntity;
import com.ca.mfd.prc.pps.service.IPpsPartsScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 零件报废Controller
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@RestController
@RequestMapping("ppspartsscrap")
@Tag(name = "零件报废服务", description = "零件报废")
public class PpsPartsScrapController extends BaseController<PpsPartsScrapEntity> {

    private IPpsPartsScrapService ppsPartsScrapService;

    @Autowired
    public PpsPartsScrapController(IPpsPartsScrapService ppsPartsScrapService) {
        this.crudService = ppsPartsScrapService;
        this.ppsPartsScrapService = ppsPartsScrapService;
    }

    @PostMapping(value = "scrap")
    @Operation(summary = "报废")
    public ResultVO<String> scrap(@RequestBody PartsScrapPara request) {
        if (StringUtils.isBlank(request.getBarcode())) {
            throw new InkelinkException("请检查输入参数！");
        }
        ppsPartsScrapService.scrap(request);
        ppsPartsScrapService.saveChange();
        return new ResultVO<String>().ok("", "产品报废添加成功");
    }

    @PostMapping(value = "scrapaffirm")
    @Operation(summary = "报废单确认")
    public ResultVO<String> scrapAffirm(@RequestBody ScrapAffirmPara para) {
        ppsPartsScrapService.scrapAffirm(para);
        ppsPartsScrapService.saveChange();
        return new ResultVO<String>().ok("", "报废单确认成功");
    }

}