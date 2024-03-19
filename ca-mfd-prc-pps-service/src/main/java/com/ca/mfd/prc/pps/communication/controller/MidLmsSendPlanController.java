package com.ca.mfd.prc.pps.communication.controller;


import com.alibaba.fastjson.JSON;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: Lms车辆锁定计划下发 Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/lmsapi")
@Tag(name = "Lms车辆锁定计划下发", description = "Lms车辆锁定计划下发")
public class MidLmsSendPlanController extends BaseApiController {
    @Autowired
    IMidApiLogService midApiLogService;

    @Autowired
    IPpsOrderService ppsOrderService;

    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    PmOrgProvider pmOrgProvider;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MidLmsSendPlanController.class);

    @GetMapping(value = "sendplan")
    @Operation(summary = "Lms车辆锁定计划下发(定时调度)")
    public ResultVO<List<LmsLockPlanDto>> sendplan(String vin) {
        List<LmsLockPlanDto> list = new ArrayList<>();
        LmsLockPlanDto dto = createLmsLockPlan(vin);
        list.add(dto);
        //sendLmsLockPlanBak11(list);
        sendLmsLockPlanBak(list);
        return new ResultVO<List<LmsLockPlanDto>>().ok(list);
    }

    private LmsLockPlanDto createLmsLockPlan(String vin) {
        PpsOrderEntity ppsOrderInfo = ppsOrderService.getPpsOrderBySn(vin);
        if (ppsOrderInfo == null) {
            logger.error("发送Lms整车锁定计划异常,vin码:" + vin + ",查询订单为空");
            return null;
        }
        String orgCode = pmOrgProvider.getCurrentOrgCode();
        String aviCode = ppsOrderInfo.getStartAvi();
        if (StringUtils.isBlank(aviCode)) {
            aviCode = "EH001";
        }
        LmsLockPlanDto info = new LmsLockPlanDto();
        String finalAviCode = aviCode;
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDTO.getAvis().stream().filter(s ->
                StringUtils.equals(s.getAviCode(), finalAviCode)).findFirst().orElse(null);
        PmLineEntity lineEntity = pmAllDTO.getLines().stream().
                filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
        String llineCode = lineEntity.getLineCode();
        PmWorkShopEntity workShopEntity = pmAllDTO.getShops().stream()
                .filter(s -> Objects.equals(s.getId(), lineEntity.getPrcPmWorkshopId())).findFirst().orElse(null);
        String shopCode = workShopEntity.getWorkshopCode();
        info.setOrgCode(orgCode);
        info.setWorkshopCode(shopCode);
        info.setLineCode(llineCode);
        info.setAviCode(aviCode);
        info.setVin(vin);
        info.setProductType("1");
        info.setProductCode(ppsOrderInfo.getProductCode());
        info.setPlanNo(ppsOrderInfo.getPlanNo());
        info.setOneId("");
        info.setManager("");
        info.setPassTime(new Date());
        info.setUniqueCode(ppsOrderInfo.getId());
        return info;
    }

    public ResultVO<String> sendLmsLockPlanBak(List<LmsLockPlanDto> list) {
        midApiLogService.sendLmsLockPlan(list);
        return new ResultVO<String>().ok("", "处理成功");
    }


    public ResultVO<String> sendLmsLockPlanBak11(List<LmsLockPlanDto> fbacks) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("LMS整车计划锁定[" + reqNo + "]开始:");
        logger.info("LMS整车计划锁定[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.isEmpty()) {
            throw new InkelinkException("没有需要上报的数据");
        }
        int status = 1;
        String errMsg = "";
        String apiPath = "";
        //String ars = apiPtService.postapi(apiPath, fbacks, null);
        AsResultVo asResultVo = new AsResultVo();
        ResponseEntity<ResultVO> responseEntity = null;
        responseEntity = restTemplate.postForEntity(apiPath, JSON.toJSONString(fbacks), ResultVO.class);
        if (StringUtils.equals("1", asResultVo.getCode())) {
            status = 1;
        } else {
            status = 5;
            errMsg = "LMS整车计划锁定[" + reqNo + "]处理失败:" + asResultVo.getMsg();
            logger.info(errMsg);
        }
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }
}
