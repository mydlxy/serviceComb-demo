package com.ca.mfd.prc.pps.communication.controller;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.as.AsResultDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.MidAsAviPointDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS车辆过点记录Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/avipoint")
@Tag(name = "AS车辆过点记录服务", description = "AS车辆过点记录")
public class MidAsAviPointController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsAviPointController.class);
    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private IPpsAsAviPointService midAsAviPointService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IPpsPlanService ppsPlanService;
    @Autowired
    private IPpsPlanAviService ppsPlanAviService;
    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "AS车辆过点记录发送(定时调度)")
    public ResultVO<String> send() {
        List<MidAsAviPointDto> fbacks = new ArrayList<>();
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS车辆过点记录[" + reqNo + "]开始:");
        List<PpsAsAviPointEntity> baths = midAsAviPointService.getNoSendList(500);
        logger.info("AS车辆过点记录[" + reqNo + "]开始查询数据:" + (baths == null ? 0 : baths.size()));
        //校验
        if (baths == null || baths.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("asavipoint_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[asavipoint_send]");
        }
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_AVI_POINT);
        loginfo.setDataLineNo(baths.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            fbacks = getAviDtoData(pmall, baths, true);
           /* if(fbacks.isEmpty()){
                return new ResultVO<String>().ok("", "没有需要上报的数据");
            }*/
            loginfo.setDataLineNo(fbacks.size());
            if(fbacks.isEmpty()){
                midAsAviPointService.updateAsSendStatus(baths.stream()
                        .map(PpsAsAviPointEntity::getId).collect(Collectors.toList()));

            }else {

                AsResultDto<MidAsAviPointDto> apiData = new AsResultDto(fbacks, "MOM", "vehicleprogressfeedback");
                String sendData = JsonUtils.toJsonString(fbacks);
                loginfo.setReqData(sendData);
                String ars = apiPtService.postapi(apiPath, apiData, null);
                loginfo.setResponseData(ars);
                try {
                    ////rowCount
                    logger.warn("API平台测试url调用：" + ars);
                    AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                    if (asResultVo != null) {
                        if (StringUtils.equals("1", asResultVo.getCode())) {
                        //|| StringUtils.contains(asResultVo.getMsg(),"数据处理成功")
                            List<Long> ids = new ArrayList<>();
                            for (PpsAsAviPointEntity item : baths) {
                                ids.add(item.getId());
                            }
                            if (ids.size() > 0) {
                                midAsAviPointService.updateAsSendStatus(ids);
                            }
                            status = 1;
                        } else {
                            status = 5;
                            errMsg = "AS车辆过点记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                            logger.info(errMsg);
                        }
                    }
                } catch (Exception eas) {
                    logger.error("", eas);
                    status = 1;
                    errMsg = "AS车辆过点记录[" + reqNo + "]处理失败:";
                    logger.info(errMsg);
                }
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "AS车辆过点记录[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midAsAviPointService.clearChange();
        }

        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midAsAviPointService.saveChange();

        logger.info("AS车辆过点记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    private List<MidAsAviPointDto> getAviDtoData(PmAllDTO pmall, List<PpsAsAviPointEntity> baths, Boolean isSave) {
        List<MidAsAviPointDto> fbacks = new ArrayList<>();
        List<PpsAsAviPointEntity> bathUpdate = new ArrayList<>();
        Map<String, PpsPlanEntity> plansDic = new Hashtable<>();
        Map<String, List<PpsPlanAviEntity>> planAviDic = new Hashtable<>();
        if (baths != null) {
            for (PpsAsAviPointEntity btplan : baths) {
                PpsPlanEntity plan = null;
                if (plansDic.containsKey(btplan.getPlanNo())) {
                    plan = plansDic.get(btplan.getPlanNo());
                } else {
                    PpsPlanEntity planTmp = ppsPlanService.getFirstByPlanNo(btplan.getPlanNo());
                    plansDic.put(btplan.getPlanNo(), planTmp);
                    plan = planTmp;
                }
                //过滤非AS系统的计划
                if (plan == null || plan.getPlanSource() != 1) {
                    btplan.setAsSendFlag(3);
                    bathUpdate.add(btplan);
                }
                //查找计划履历
                List<PpsPlanAviEntity> planAvis = null;
                if (planAviDic.containsKey(btplan.getPlanNo())) {
                    planAvis = planAviDic.get(btplan.getPlanNo());
                } else {
                    List<PpsPlanAviEntity> planAviTmp = ppsPlanAviService.getListByPlanNo(btplan.getPlanNo());
                    planAviDic.put(btplan.getPlanNo(), planAviTmp);
                    planAvis = planAviTmp;
                }
                if (planAvis == null || planAvis.isEmpty()) {
                    btplan.setAsSendFlag(3);
                    bathUpdate.add(btplan);
                    continue;
                }

                //查找上报点
                PmAviEntity avi = pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), btplan.getAviCode()))
                        .findFirst().orElse(null);
                if(avi == null){
                    btplan.setAsSendFlag(3);
                    bathUpdate.add(btplan);
                    continue;
                }
                String tpCode = StringUtils.isBlank(avi.getDb3()) ? avi.getAviCode() : avi.getDb3();
                PpsPlanAviEntity planAviInfo = planAvis.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAttribute1(), tpCode)).findFirst().orElse(null);
                if (planAviInfo == null) {
                    btplan.setAsSendFlag(3);
                    bathUpdate.add(btplan);
                    continue;
                }
                MidAsAviPointDto et = new MidAsAviPointDto();
                et.setOrgCode(btplan.getOrgCode());
                et.setVrn(btplan.getPlanNo());
                et.setVin(btplan.getVin());
                //1：一般通过、2：车辆报废、4：车辆HOLD、5：车辆UNHOLD、6：车辆SETIN、7：车辆SETOUT
                if ("2".equalsIgnoreCase(btplan.getScanType())) {
                    //2：SETOUT、3：SETIN
                    //6：车辆SETIN、7：车辆SETOUT
                    et.setFeedBackType("7");
                } else if ("3".equalsIgnoreCase(btplan.getScanType())) {
                    et.setFeedBackType("6");
                } else {
                    et.setFeedBackType("1");
                }
                et.setScanType(btplan.getScanType());
                et.setScanUser(""); //自动过点方式，没有用户
                et.setPassTimes(1); //每次过点上报，次数为1
                et.setActualPlantCode(btplan.getOrgCode());
                et.setActualWorkShop(btplan.getWorkshopCode());
                et.setActualLineCode(btplan.getLineCode());
                //修改AS的返回TP点编码
                et.setActualWorkStation(tpCode);
                et.setActualDate(DateUtils.format(btplan.getScanTime(), DateUtils.DATE_PATTERN));
                et.setActualShift(btplan.getActualShift());
                et.setActualTime(btplan.getScanTime());

                fbacks.add(et);
            }
            fbacks = fbacks.stream().distinct().collect(Collectors.toList());
        }
        if (isSave) {
           // midAsAviPointService.updateBatchById(bathUpdate);
        }
        return fbacks;
    }

    @GetMapping(value = "getsenddata")
    @Operation(summary = "AS_WBSPBS记录发送test")
    public ResultVO<List<MidAsAviPointDto>> getSendData() {
        List<PpsAsAviPointEntity> baths = midAsAviPointService.getNoSendList(100);
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        List<MidAsAviPointDto> fbacks = getAviDtoData(pmall, baths, false);
        return new ResultVO<List<MidAsAviPointDto>>().ok(fbacks);
    }

    @GetMapping(value = "test")
    @Operation(summary = "AS_WBSPBS记录发送test")
    public ResultVO<String> test() {
        String apiPath = sysConfigurationProvider.getConfiguration("asavipoint_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[asavipoint_send]");
        }
        List<MidAsAviPointDto> dtos = new ArrayList<>();
        MidAsAviPointDto et = new MidAsAviPointDto();
        et.setOrgCode("CA");
        et.setVin("L1NSPGHB1PB080534");
        et.setVrn("1000009");
        et.setFeedBackType("1");
        et.setScanType("1");
        et.setScanUser("");
        et.setPassTimes(1);
        et.setActualPlantCode("CA");
        et.setActualWorkShop("WE");
        et.setActualLineCode("WE1");
        et.setActualWorkStation("WE1-1");
        et.setActualDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        et.setActualShift("白班");
        et.setActualTime(new Date());
        dtos.add(et);

        AsResultDto<MidAsAviPointDto> apiData = new AsResultDto(dtos, "MOM", "vehicleprogressfeedback");
        String ars = apiPtService.postapi(apiPath, apiData, null);
        return new ResultVO<String>().error(-1, ars);
    }

}