package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.AviBindTagDto;
import com.ca.mfd.prc.pps.dto.BodyShopStartWorkPara;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.dto.UnbindTagPara;
import com.ca.mfd.prc.pps.entity.PpsBindingTagEntity;
import com.ca.mfd.prc.pps.service.IPpsBindingTagService;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eric.zhou
 * @Description: 吊牌绑定管理Controller
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@RestController
@RequestMapping("ppsbindingtag")
@Tag(name = "吊牌绑定管理服务", description = "吊牌绑定管理")
public class PpsBindingTagController extends BaseController<PpsBindingTagEntity> {

    private final IPpsBindingTagService prcPpsBindingTagService;
    @Autowired
    private IPpsLogicService ppsLogicService;

    @Autowired
    public PpsBindingTagController(IPpsBindingTagService prcPpsBindingTagService) {
        this.crudService = prcPpsBindingTagService;
        this.prcPpsBindingTagService = prcPpsBindingTagService;
    }

    @Operation(summary = "焊装车间开工并下发")
    @PostMapping("bodyshopstartwork")
    public ResultVO<OrderEntryInfo> bodyShopStartWork(@RequestBody BodyShopStartWorkPara para) {
        OrderEntryInfo orderEntryInfo = ppsLogicService.bodyShopStartWork(para.getTagNo(), para.getWorkstationCode(), para.getModel());

        return new ResultVO<OrderEntryInfo>().ok(orderEntryInfo);
    }

    @Operation(summary = "焊装车间开工并下发(指定计划号)")
    @PostMapping("bodyshopstartworkbyplan")
    public ResultVO<OrderEntryInfo> bodyShopStartWorkByPlan(@RequestBody BodyShopStartWorkPara para) {
        OrderEntryInfo orderEntryInfo = ppsLogicService.bodyShopStartWorkByPlan(
                para.getTagNo(),
                para.getWorkstationCode(),
                para.getModel(),
                para.getPlanNo());
        return new ResultVO<OrderEntryInfo>().ok(orderEntryInfo);
    }

    @Operation(summary = "解绑吊牌")
    @PostMapping("unbindtag")
    public ResultVO<String> unbindTag(@RequestBody UnbindTagPara para) {
        prcPpsBindingTagService.unbindTag(para.getTagNo());
        prcPpsBindingTagService.saveChange();
        return new ResultVO<String>().ok("", "解绑吊牌成功");
    }

    @Operation(summary = "绑定吊牌")
    @PostMapping("avibindtag")
    public ResultVO<String> aviBindTag(@RequestBody AviBindTagDto para) {
        prcPpsBindingTagService.bindingTag(para.getTagNo(), para.getVin(), para.getAviCode(),para.getBindingMedium());
        prcPpsBindingTagService.saveChange();
        return new ResultVO<String>().ok("", "绑定成功");
    }

}