package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsLineCalendarEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsLineCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: AS线体日历中间表Controller
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@RestController
@RequestMapping("communication/aslinecalendar")
@Tag(name = "AS线体日历中间表服务", description = "AS线体日历中间表")
public class MidAsLineCalendarController extends BaseController<MidAsLineCalendarEntity> {

    private final IMidAsLineCalendarService midAsLineCalendarService;
    @Autowired
    public MidAsLineCalendarController(IMidAsLineCalendarService midAsLineCalendarService) {
        this.crudService = midAsLineCalendarService;
        this.midAsLineCalendarService = midAsLineCalendarService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MidAsLineCalendarController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody List<MidAsLineCalendarEntity> models
    ) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS线体日历信息[" + reqNo + "]开始接收数据:" + (models == null ? 0 : models.size()));
        //校验
        if (models == null || models.size() == 0) {
            throw new InkelinkException("数据不能为空");
        }
        logger.info("AS线体日历信息[" + reqNo + "]校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_LINE_CALENDAR);
        loginfo.setDataLineNo(models.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            for (MidAsLineCalendarEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(loginfo.getId());
            }
            midAsLineCalendarService.insertBatch(models,100,false,1);
            midAsLineCalendarService.saveChange();
            status = 1;
            errMsg = "AS线体日历信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "AS线体日历信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midAsLineCalendarService.saveChange();

        logger.info("AS线体日历信息[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, "处理失败");
        }
    }


    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑(包含线体、车间日历)")
    public ResultVO<String> excute(String logid) {
        midAsLineCalendarService.excute(logid);
        return new ResultVO<String>().ok("", "成功");
    }

    @GetMapping(value = "saveall")
    @Operation(summary = "保存全部")
    public ResultVO<String> saveall() {
        midAsLineCalendarService.saveAllAsLineCalendar();
        return new ResultVO<String>().ok("", "成功");
    }
}