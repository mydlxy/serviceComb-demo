package com.ca.mfd.prc.pps.communication.controller;

import cn.hutool.core.date.StopWatch;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsShopPlanEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsVehicleEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsShopPlanService;
import com.ca.mfd.prc.pps.communication.service.IMidAsVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS车间计划Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/asshopplan")
@Tag(name = "AS车间计划服务", description = "AS车间计划")
public class MidAsShopPlanController  extends BaseController<MidAsShopPlanEntity> {
    final Logger logger = LoggerFactory.getLogger(MidAsShopPlanController.class);

    private final IMidAsShopPlanService midAsShopPlanService;

    @Autowired
    public MidAsShopPlanController(IMidAsShopPlanService midAsShopPlanService) {
        this.crudService = midAsShopPlanService;
        this.midAsShopPlanService = midAsShopPlanService;
    }

    @Autowired
    private IMidAsVehicleService midAsVehicleService;

    @Autowired
    private IMidApiLogService midApiLogService;

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<MidAsShopPlanEntity>> getPageData(@RequestBody PageDataDto model) {
        if (model.getConditions() != null && model.getConditions().stream()
                .anyMatch(c -> StringUtils.endsWithIgnoreCase(c.getColumnName(), "prcMidApiLogIdVe"))) {
            ConditionDto con = model.getConditions().stream()
                    .filter(c -> StringUtils.endsWithIgnoreCase(c.getColumnName(), "prcMidApiLogIdVe")).findFirst().orElse(null);
            if (!StringUtils.isBlank(con.getValue())) {
                Long veLogId = ConvertUtils.stringToLong(con.getValue());
                List<MidAsVehicleEntity> veList = midAsVehicleService.getListByLog(veLogId);
                if (veList != null && !veList.isEmpty()) {
                    con.setColumnName("prcMidApiLogId");
                    String vrn = veList.get(0).getVrn();
                    String releaseVer = veList.get(0).getReleaseVer();
                    Long shopLogId = midAsShopPlanService.getLogId(vrn, releaseVer);
                    con.setValue(shopLogId.toString());
                }
            }

            //model.getConditions().remove(con);
        }

        PageData<MidAsShopPlanEntity> page = midAsShopPlanService.page(model);
        return new ResultVO<PageData<MidAsShopPlanEntity>>().ok(page, "获取数据成功");
    }


    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody List<MidAsShopPlanEntity> models
    ) {
        StopWatch sw = new StopWatch();
        sw.start();
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS车间计划[" + reqNo + "]开始接收数据:" + (models == null ? 0 : models.size()));
        //校验
        if (models == null || models.size() == 0) {
            throw new InkelinkException("AS车间计划接收数据不能为空");
        }
        logger.info("AS车间计划[" + reqNo + "]接收校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_SHOP_PLAN);
        loginfo.setDataLineNo(models.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            for (MidAsShopPlanEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(loginfo.getId());
            }
            midAsShopPlanService.insertBatch(models,200,false,1);
            midAsShopPlanService.saveChange();
            status = 1;
            errMsg = "AS车间计划[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "AS车间计划[" + reqNo + "]接收失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midAsShopPlanService.saveChange();

        logger.info("AS车间计划[" + reqNo + "]接收执行完成:");
        sw.stop();
        logger.info("=====================================批次结束" + sw.getTotalTimeSeconds());
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收失败:" + errMsg);
        }
    }

    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        String msg = midAsShopPlanService.excute(logid);
        if (StringUtils.isBlank(msg)) {
            return new ResultVO<String>().ok("", "成功");
        } else {
            return new ResultVO<String>().error(-1, msg);
        }
    }


    @GetMapping(value = "test")
    @Operation(summary = "测试")
    public ResultVO<String> test(String tp) throws Exception {

        List<MidAsShopPlanEntity> models = new ArrayList<>();
        StopWatch sw = new StopWatch();
        sw.start();
        for (int i = 0; i < 15000; i++) {
            MidAsShopPlanEntity et = new MidAsShopPlanEntity();
            et.setOrgCode("test111");
            et.setVrn("22");
            et.setSequenceNo(String.valueOf(i));
            et.setSchedShopCode("aa");
            et.setSchedWsCode("2222");
            et.setPlantCode("22");
            et.setPlanShift("1");
            et.setPlanDate(new Date());
            et.setPlanTime(new Date());
            et.setReleaseVer("11");
            models.add(et);
        }
        if (StringUtils.isBlank(tp)) {

            return receive(models);
        } else if (StringUtils.equals(tp, "1")) {
            for (MidAsShopPlanEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(10L);
            }
            midAsShopPlanService.insertBatch(models,200,false,1);
            midAsShopPlanService.saveChange();
            sw.stop();
            logger.info("=====================================批次结束" + sw.getTotalTimeSeconds());
        }
        return new ResultVO<String>().ok("rev", "成功");
    }
}