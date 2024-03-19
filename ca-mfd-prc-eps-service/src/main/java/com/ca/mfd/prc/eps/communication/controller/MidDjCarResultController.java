package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.entity.MidDjCarResultEntity;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.communication.service.IMidDjCarResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author inkelink
 * @Description: 电检整车结果数据Controller
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
@RestController
@RequestMapping("communication/middjcarresult")
@Tag(name = "电检整车结果数据服务", description = "电检整车结果数据")
public class MidDjCarResultController extends BaseController<MidDjCarResultEntity> {
    final Logger logger = LoggerFactory.getLogger(MidDjCarResultController.class);
    private IMidDjCarResultService midDjCarResultService;

    @Autowired
    public MidDjCarResultController(IMidDjCarResultService midDjCarResultService) {
        this.crudService = midDjCarResultService;
        this.midDjCarResultService = midDjCarResultService;
    }

    @Autowired
    private IMidApiLogService midApiLogService;


    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody MidDjCarResultEntity models
    ) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("电检整车结果信息[" + reqNo + "]开始接收数据:" + (models == null ? 0 : 1));
        //校验
        if (models == null) {
            throw new InkelinkException("电检整车结果信息接收数据不能为空");
        }
        logger.info("电检整车结果信息[" + reqNo + "]接收校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_CARINFO_RESULT);
        loginfo.setDataLineNo(1);
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            models.setExeStatus(0);
            models.setExeTime(new Date());
            models.setExeMsg(StringUtils.EMPTY);
            models.setOpCode(1);
            models.setPrcMidApiLogId(loginfo.getId());

            midDjCarResultService.insert(models);
            midDjCarResultService.saveChange();
            status = 1;
            errMsg = "电检整车结果信息[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检整车结果信息[" + reqNo + "]接收处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midDjCarResultService.saveChange();

        logger.info("电检整车结果信息[" + reqNo + "]接收执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收处理失败:" + errMsg);
        }
    }

}