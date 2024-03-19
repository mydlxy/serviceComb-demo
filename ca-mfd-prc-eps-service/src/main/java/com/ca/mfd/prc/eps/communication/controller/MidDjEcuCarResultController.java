package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.dto.CarCloudDeviceDto;
import com.ca.mfd.prc.eps.communication.dto.MidDjEcuCarResultDto;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.entity.MidDjEcuCarResultEntity;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.communication.service.IMidDjEcuCarResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 电检软件结果信息数据Controller
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
@RestController
@RequestMapping("communication/middjecucarresult")
@Tag(name = "电检软件结果信息数据服务", description = "电检软件结果信息数据")
public class MidDjEcuCarResultController extends BaseController<MidDjEcuCarResultEntity> {
    final Logger logger = LoggerFactory.getLogger(MidDjEcuCarResultController.class);
    private IMidDjEcuCarResultService midDjEcuCarResultService;

    @Autowired
    public MidDjEcuCarResultController(IMidDjEcuCarResultService midDjEcuCarResultService) {
        this.crudService = midDjEcuCarResultService;
        this.midDjEcuCarResultService = midDjEcuCarResultService;
    }

    @Autowired
    private IMidApiLogService midApiLogService;


    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody MidDjEcuCarResultDto models
    ) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("电检软件结果信息[" + reqNo + "]开始接收数据:" + (models == null ? 0 : 1));
        //校验
        if (models == null) {
            throw new InkelinkException("电检软件结果信息接收数据不能为空");
        }
        logger.info("电检软件结果信息[" + reqNo + "]接收校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_ECU_CARINFO_RESULT);
        loginfo.setDataLineNo(1);
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            //中间表赋值
            MidDjEcuCarResultEntity entity=new MidDjEcuCarResultEntity();
            entity.setVinCode(models.getVinCode());
            entity.setEcuList(JsonUtils.toJsonString(models.getEcuList()));
            entity.setTestTime(models.getTestTime());
            entity.setUploadDate(models.getUploadDate());
            entity.setExeStatus(0);
            entity.setExeTime(new Date());
            entity.setExeMsg(StringUtils.EMPTY);
            entity.setOpCode(1);
            entity.setPrcMidApiLogId(loginfo.getId());

            midDjEcuCarResultService.insert(entity);
            midDjEcuCarResultService.saveChange();
            status = 1;
            errMsg = "电检软件结果信息[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检软件结果信息[" + reqNo + "]接收处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midDjEcuCarResultService.saveChange();

        logger.info("电检软件结果信息[" + reqNo + "]接收执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收处理失败:" + errMsg);
        }
    }

    @GetMapping(value = "/provider/queryecucarbyvincode")
    @Operation(summary = "根据vin号查询软件结果数据")
    public ResultVO<MidDjEcuCarResultDto> queryEcuCarByVinCode(String vinCode) {
        ResultVO<MidDjEcuCarResultDto> result = new ResultVO<>();
        MidDjEcuCarResultDto dto = midDjEcuCarResultService.queryEcuCarByVinCode(vinCode);
        return result.ok(dto);
    }

}