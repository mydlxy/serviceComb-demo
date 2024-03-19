package com.ca.mfd.prc.pm.communication.controller;


import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.MidLmsDatasVo;
import com.ca.mfd.prc.pm.communication.dto.MidLmsParaDto;
import com.ca.mfd.prc.pm.communication.dto.MidLmsSigtrueVo;
import com.ca.mfd.prc.pm.communication.dto.WorkstationMaterialDto;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.entity.PmMaterialToLesEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmMaterialToLesService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialSubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author inkelink
 * @Description: Lms 工位物料接口
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@RestController
@RequestMapping("communication/lmsstationmaterial")
@Tag(name = "Lms工位物料服务", description = "Lms工位物料服务")
public class MidLmsStationMaterialController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsStationMaterialController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IPmMaterialToLesService pmMaterialToLesService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IPmWorkstationMaterialSubService pmWorkstationMaterialSubService;

    @Autowired
    private IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;

    @PostMapping(value = "receivesigtrue")
    @Operation(summary = "Lms工位物料令牌请求")
    public ResultVO<List<MidLmsSigtrueVo>> receivesigtrue(@Valid @RequestBody List<MidLmsParaDto> models) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("Lms工位物料令牌请求服务,开始接收数据,时间:" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) +
                "数据:" + JsonUtils.toJsonString(models));
        List<MidLmsSigtrueVo> sigtrueVoList = new ArrayList<>();
        if (models == null || models.isEmpty()) {
            throw new InkelinkException("数据不能为空");
        }
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_WORKSTATOS_MATERIALSIGTRUE);
        Integer status = 1;
        String errMsg;
        try {
            loginfo.setRequestStartTime(new Date());
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            for (MidLmsParaDto items : models) {
                String[] shopCodes = items.getShopCodes();
                String shops = "all";
                if (shopCodes.length > 0) {
                    shops = String.join(", ", shopCodes);
                }
                MidLmsSigtrueVo midLmsSigtrueVo = new MidLmsSigtrueVo();
                midLmsSigtrueVo.setMaterialCode(items.getProductCode());
                Long versionId = 0L;
                String version = "0";
                int type = items.getType();
                if(type == 0){
                    type = 1;
                    items.setType(type);
                }
                if(type == 1){
                    PmProductCharacteristicsVersionsEntity versionInfo = pmProductCharacteristicsVersionsService.getCharacteristicsVersionsByCode(items.getProductCode());
                    if (versionInfo == null) {
                        midLmsSigtrueVo.setMessage("无效的物料号");
                        sigtrueVoList.add(midLmsSigtrueVo);
                        continue;
                    }
                    versionId = versionInfo.getId();
                    version = versionInfo.getVersions();
                }
                String specifyDate = items.getSpecifyDate();
                if(StringUtils.isBlank(specifyDate)){
                    Date now = new Date();
                    specifyDate = DateUtils.format(now,DateUtils.DATE_PATTERN);
                }
                midLmsSigtrueVo.setUniqueCode(versionId);

                StringJoiner sigtrueSj = new StringJoiner("|");
                sigtrueSj.add(items.getProductCode());
                sigtrueSj.add(version);
                sigtrueSj.add(shops);
                sigtrueSj.add(String.valueOf(items.getType()));
                sigtrueSj.add("60");
                sigtrueSj.add(specifyDate);
                String sigtrueStr = sigtrueSj.toString();
                byte[] bytes = sigtrueStr.getBytes(StandardCharsets.UTF_8);
                // 使用Base64编码
                String encoded = Base64.getEncoder().encodeToString(bytes);
                midLmsSigtrueVo.setSigtrue(encoded);
                sigtrueVoList.add(midLmsSigtrueVo);
            }
            loginfo.setDataLineNo(sigtrueVoList.size());
            loginfo.setStatus(1);
            errMsg = "Lms工位物料令牌请求服务[" + reqNo + "]处理完成";
            logger.info(errMsg + ",返回数据:" + JsonUtils.toJsonString(sigtrueVoList));
        } catch (Exception ex) {
            status = 5;
            errMsg = "Lms工位物料令牌请求服务[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("Lms工位物料令牌请求服务[" + reqNo + "]执行完成:");
        if (status == 1) {
            //发送消息队列生成物料数据
            //pmWorkstationMaterialSubService.sendCreateLmsSigtrueMes(sigtrueVoList);
            return new ResultVO<List<MidLmsSigtrueVo>>().ok(sigtrueVoList, "查询成功");
        } else {
            return new ResultVO<List<MidLmsSigtrueVo>>().ok(sigtrueVoList, "查询失败");
        }
    }


    @PostMapping(value = "receivedatasbak")
    @Operation(summary = "Lms工位物料数据请求")
    public ResultVO<MidLmsDatasVo> receivedatasBak(@Valid @RequestBody MidLmsSigtrueVo sigtrue) {
        String reqNo = UUIDUtils.getGuid();
        String sigtrueStr = sigtrue.getSigtrue();
        logger.info("Lms工位物料令牌请求服务[" + reqNo + "]开始接收数据:" + (sigtrueStr));
        PmMaterialToLesEntity pmMaterialToLesEntity = pmMaterialToLesService.getPmMaterialToLesBySigtrue(sigtrueStr);
        MidLmsDatasVo datasVo = new MidLmsDatasVo();
        if (pmMaterialToLesEntity != null) {
            datasVo.setProductCode(pmMaterialToLesEntity.getProductCode());
            datasVo.setSigtrue(sigtrueStr);
            datasVo.setUniqueCode(pmMaterialToLesEntity.getId());
            if (StringUtils.isNotBlank(pmMaterialToLesEntity.getMaterialContetn())) {
                List<WorkstationMaterialDto> list = JsonUtils.parseArray(pmMaterialToLesEntity.getMaterialContetn(), WorkstationMaterialDto.class);
                datasVo.setData(list);
                pmMaterialToLesService.updatePmMaterialToLesStatus(sigtrueStr);
                pmMaterialToLesService.saveChange();
            }
            return new ResultVO<MidLmsDatasVo>().ok(datasVo, "查询成功");
        } else {
            return new ResultVO<MidLmsDatasVo>().ok(datasVo, "无效的令牌");
        }

    }


    @PostMapping(value = "receivedatas")
    @Operation(summary = "Lms工位物料数据请求")
    public ResultVO<MidLmsDatasVo> receivedatas(@Valid @RequestBody MidLmsSigtrueVo sigtrue) {
        String reqNo = UUIDUtils.getGuid();
        String sigtrueStr = sigtrue.getSigtrue();
        logger.info("Lms工位物料令牌请求服务[" + reqNo + "]开始接收数据:" + (sigtrueStr));
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_WORKSTATOS_MATERIAL);
        MidLmsDatasVo datasVo = new MidLmsDatasVo();
        Integer status = 1;
        String errMsg;
        try {
            loginfo.setRequestStartTime(new Date());
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            datasVo = pmWorkstationMaterialSubService.getPmWorkstationMaterial(sigtrueStr);
            loginfo.setDataLineNo(datasVo != null && datasVo.getData() != null ? datasVo.getData().size() : 0);
            loginfo.setStatus(1);
            errMsg = "Lms工位物料数据请求[" + reqNo + "]处理完成";
            logger.info(errMsg + ",返回数据" + JsonUtils.toJsonString(datasVo));
        } catch (Exception ex) {
            status = 5;
            errMsg = "Lms工位物料令牌请求服务[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("Lms工位物料令牌请求服务[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<MidLmsDatasVo>().ok(datasVo, "查询成功");
        } else {
            return new ResultVO<MidLmsDatasVo>().ok(datasVo, "查询失败:"+ errMsg);
        }
    }
}
