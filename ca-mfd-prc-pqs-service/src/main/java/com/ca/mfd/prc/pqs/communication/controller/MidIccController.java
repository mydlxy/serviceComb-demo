package com.ca.mfd.prc.pqs.communication.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pqs.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pqs.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.pqs.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pqs.communication.service.IMidIccApiService;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("communication/icc")
@Tag(name = "icc服务", description = "icc数据")
public class MidIccController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(MidIccController.class);
    @Autowired
    private IMidApiLogService apiLogService;
    @Autowired
    private IMidIccApiService iccApiService;

    @Autowired
    private IPqsLogicService pqsLogicService;


    @GetMapping(value = "excute")
    @Operation(summary = "执行缺陷库数据处理逻辑")
    public ResultVO<String> excute() {
        List<MidApiLogEntity> apilogs = apiLogService.getSyncList(ApiTypeConst.PMS_ICC_DATA);
        String errorMsg = "成功";
        if (CollectionUtils.isNotEmpty(apilogs)) {
            for (MidApiLogEntity apilog : apilogs) {
                boolean success = false;
                try {
                    UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                    uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                            .eq(MidApiLogEntity::getId, apilog.getId());
                    apiLogService.update(uplogStart);
                    apiLogService.saveChange();

                    List<MidIccApiEntity> datas = iccApiService.getListByLog(apilog.getId());
                    pqsLogicService.receiveIccData(datas);
                    success = true;

                } catch (Exception exception) {
                    logger.debug("数据保存异常：{}", exception.getMessage());
                    errorMsg = "数据保存异常："+ exception.getMessage();
                    apiLogService.clearChange();
                }
                try {
                    apiLogService.clearChange();
                    UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                    LambdaUpdateWrapper<MidApiLogEntity> upwaper = uplogEnd.lambda();
                    upwaper.set(MidApiLogEntity::getStatus, success ? 5 : 6);
                    if (!StringUtils.isBlank(errorMsg)) {
                        upwaper.set(MidApiLogEntity::getRemark, errorMsg.substring(0, errorMsg.length()<=300? errorMsg.length():300));
                    }
                    upwaper.eq(MidApiLogEntity::getId, apilog.getId());
                    apiLogService.update(uplogEnd);
                    apiLogService.saveChange();
                } catch (Exception exception) {
                    errorMsg = "数据保存异常："+ exception.getMessage();
                    logger.debug("日志保存异常：{}", exception.getMessage());
                }
            }
        }
        return new ResultVO<String>().ok("", errorMsg);
    }
}
