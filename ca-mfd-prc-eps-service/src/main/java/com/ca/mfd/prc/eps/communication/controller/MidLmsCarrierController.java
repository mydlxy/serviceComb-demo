package com.ca.mfd.prc.eps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.dto.LmsCarrierDto;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.entity.EpsCarrierEntity;
import com.ca.mfd.prc.eps.service.IEpsCarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @Description: 载具信息Controller
 * @date 2024年02月24日
 * @变更说明 BY inkelink At 2024年02月24日
 */
@RestController
@RequestMapping("communication/lmscarrier")
@Tag(name = "lms载具信息服务", description = "lms载具信息服务")
public class MidLmsCarrierController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsCarrierController.class);

    @Autowired
    private IEpsCarrierService epsCarrierService;
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @PostMapping(value = "receive")
    @Operation(summary = "保存Lms推送料框基础数据")
    public ResultVO<String> receive(@Valid @RequestBody List<LmsCarrierDto> models) {
        if (models.isEmpty() || models.size() == 0) {
            logger.error("Lms推送料框基础数据异常，数据为空");
            throw new InkelinkException("Lms推送料框基础数据异常，数据为空");
        }
        String reqNo = UUIDUtils.getGuid();
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_CARRIER_DATA);
        loginfo.setDataLineNo(models.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            //getCheckRepeat
            List<EpsCarrierEntity> list = new ArrayList<>();
            for (LmsCarrierDto item : models) {
                if (item == null || StringUtils.isBlank(item.getPackageCode())) {
                    continue;
                }
                Boolean result = epsCarrierService.getCheckRepeat(item.getPackageCode());
                if (result) {
                    EpsCarrierEntity entity = new EpsCarrierEntity();
                    entity.setCarrierBarcode(item.getPackageCode()); //载具条码
                    entity.setBatchNumber(""); //批次号
                    entity.setIsUse(false); //是否空闲
                    entity.setIsVerifyQuantity(false);//是否验证载具数量
                    entity.setMaterialNo(""); //物料号
                    entity.setMaterialCn("");//可以根据物料号查询中文描述
                    entity.setMaterialCount(5); //物料容纳数量 -- 根据需求 -- 龚老板确定
                    entity.setPracticalCount(0); //实际数量 -- 根据需求 -- 龚老板确定
                    entity.setProcessCode(""); //工序编码
                    entity.setMaterialDes("");// 物料描述
                    entity.setIsPlan(false); // 是否验证同一计划
                    entity.setIsMaterial(false);// 是否验证统一物料
                    list.add(entity);
                }
            }
            if (list != null && list.size() > 0) {
                epsCarrierService.insertBatch(list,200,false,1);
                epsCarrierService.saveChange();
            }
            status = 1;
            errMsg = "Lms推送料框基础数据[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "Lms推送料框基础数据[" + reqNo + "]接收失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        epsCarrierService.saveChange();

        logger.info("Lms推送料框基础数据[" + reqNo + "]接收执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收失败:" + errMsg);
        }
    }
}
