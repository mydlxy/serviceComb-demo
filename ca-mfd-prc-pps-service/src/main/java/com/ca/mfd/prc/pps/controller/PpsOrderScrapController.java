package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.OrderScrapPara;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.dto.ScrapOrderPara;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderScrapEntity;
import com.ca.mfd.prc.pps.service.IPpsOrderScrapService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 生产报废订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsorderscrap")
@Tag(name = "生产报废订单")
public class PpsOrderScrapController extends BaseController<PpsOrderScrapEntity> {

    private final IPpsOrderScrapService ppsOrderScrapService;

    @Autowired
    private IPpsOrderService ppsOrderService;

    @Autowired
    public PpsOrderScrapController(IPpsOrderScrapService ppsOrderScrapService) {
        this.crudService = ppsOrderScrapService;
        this.ppsOrderScrapService = ppsOrderScrapService;
    }

    @PostMapping(value = "orderscrap")
    @Operation(summary = "整车报废")
    public ResultVO<String> orderscrap(@RequestBody OrderScrapPara para) {
        if (StringUtils.isBlank(para.getBarcode())) {
            throw new InkelinkException("无效的产品条码");
        }
        if (StringUtils.isBlank(para.getOrderCategory())) {
            throw new InkelinkException("无效的产品类型");
        }

        PpsOrderEntity orderInfo = ppsOrderService.getFirstByBarCodeOrderCategory(para.getBarcode(), para.getOrderCategory());
        if (orderInfo == null) {
            throw new InkelinkException("未找到对应的产品");
        }
        PpsOrderScrapEntity et = new PpsOrderScrapEntity();
        et.setOrderNo(orderInfo.getOrderNo());
        et.setRemark(para.getRemark());
        et.setBarcode(para.getBarcode());
        et.setOrderCategory(para.getOrderCategory());
        edit(et);
        ppsOrderScrapService.saveChange();
        return new ResultVO<String>().ok("", "产品报废添加成功");
    }


    @Operation(summary = "报废单确认")
    @PostMapping("scrapaffirm")
    public ResultVO<String> scrapAffirm(@RequestBody ScrapAffirmPara ppsOrderScrapInfo) {
        ppsOrderScrapService.scrapAffirm(ppsOrderScrapInfo);
        ppsOrderScrapService.saveChange();
        //发送AS报废信息
        ppsOrderScrapService.sendAsScrapMessage(ppsOrderScrapInfo);
        return new ResultVO<String>().ok("", "报废单确认成功");
    }

    @Operation(summary = "订单号报废")
    @PostMapping("ordervescrap")
    public ResultVO<String> orderVeScrap(@RequestBody ScrapOrderPara ppsOrderScrapInfo) {
        ScrapAffirmPara res = ppsOrderScrapService.orderVeScrap(ppsOrderScrapInfo.getOrderNo());
        ppsOrderScrapService.saveChange();
        //发送AS报废信息
        if (res != null) {
            ppsOrderScrapService.sendAsScrapMessage(res);
        }
        return new ResultVO<String>().ok("", "报废单确认成功");
    }

}