package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.MidAsVehicleDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsVehicleEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS整车信息Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/asvehicle")
@Tag(name = "AS整车信息服务", description = "AS整车信息")
public class MidAsVehicleController  extends BaseController<MidAsVehicleEntity> {
    final Logger logger = LoggerFactory.getLogger(MidAsShopPlanController.class);

    private final IMidAsVehicleService midAsVehicleService;

    @Autowired
    public MidAsVehicleController(IMidAsVehicleService midAsVehicleService) {
        this.crudService = midAsVehicleService;
        this.midAsVehicleService = midAsVehicleService;
    }

    @Autowired
    private IMidApiLogService midApiLogService;


    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody List<MidAsVehicleEntity> models
    ) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS车辆信息[" + reqNo + "]开始接收数据:" + (models == null ? 0 : models.size()));
        //校验
        if (models == null || models.size() == 0) {
            throw new InkelinkException("AS车辆信息接收数据不能为空");
        }
        logger.info("AS车辆信息[" + reqNo + "]接收校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_VEHICLE_PLAN);
        loginfo.setDataLineNo(models.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            for (MidAsVehicleEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(loginfo.getId());
            }
            midAsVehicleService.insertBatch(models, 200, false, 1);
            midAsVehicleService.saveChange();
            status = 1;
            errMsg = "AS车辆信息[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "AS车辆信息[" + reqNo + "]接收处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midAsVehicleService.saveChange();

        logger.info("AS车辆信息[" + reqNo + "]接收执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收处理失败:" + errMsg);
        }
    }

    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑")
    @Async
    public ResultVO<String> excute(String logid) {
        String msg = midAsVehicleService.excute(logid);
        if (StringUtils.isBlank(msg)) {
            return new ResultVO<String>().ok("", "成功");
        } else {
            return new ResultVO<String>().error(-1, msg);
        }
    }

//    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Operation(summary = "获取分页数据")
//    public ResultVO<PageData<MidAsVehicleEntity>> getPageData(@RequestBody PageDataDto model) {
//        PageData<MidAsVehicleEntity> page = midAsVehicleService.page(model);
//        return new ResultVO<PageData<MidAsVehicleEntity>>().ok(page, "获取数据成功");
//    }


    @GetMapping(value = "getvehiclematerial")
    @Operation(summary = "获取物料号")
    public ResultVO getvehicleMaterial(Long logid) {
        return new ResultVO().ok(midAsVehicleService.getvehicleMaterial(logid), "成功");
    }

    @GetMapping(value = "/provider/getvehiclebyplanno")
    @Operation(summary = "根据计划单号获取整车信息")
    public ResultVO<MidAsVehicleDto> getVehicleByPlanNo(String planNo) {
        return new ResultVO<MidAsVehicleDto>().ok(midAsVehicleService.getVehicleByPlanNo(planNo));
    }
}