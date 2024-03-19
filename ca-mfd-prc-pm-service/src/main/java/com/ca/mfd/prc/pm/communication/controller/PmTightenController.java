package com.ca.mfd.prc.pm.communication.controller;

import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.LjBaseDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamBaseDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamCurveDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamCurveDetailDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamTightenDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamTightenDetailDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamWoDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamWoScrDTO;
import com.ca.mfd.prc.pm.communication.service.impl.TightenServiceImpl;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmToolService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 拧紧设备调用
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("communication/tighten")
@Tag(name = "拧紧设备调用")
public class PmTightenController {
    private static final Logger logger = LoggerFactory.getLogger(PmTightenController.class);

    private final IPmLineService pmLineService;
    private final IPmWorkStationService pmWorkStationService;
    private final IPmToolService pmToolService;
    private final TightenServiceImpl tightenService;


    @Autowired
    public PmTightenController(IPmLineService pmLineService,
                               IPmWorkStationService pmWorkStationService,
                               IPmToolService pmToolService,
                               TightenServiceImpl tightenService) {
        this.pmWorkStationService = pmWorkStationService;
        this.pmLineService = pmLineService;
        this.pmToolService = pmToolService;
        this.tightenService = tightenService;
    }


    @Operation(summary = "产线信息")
    @Parameters({
            @Parameter(name = "msgId", description = "扭矩系统传递msgId")})
    @GetMapping(value = "get_latest_line")
    public LjBaseDTO getLatestLine(String msgId) {
        LjBaseDTO result = new LjBaseDTO();
        result.setMsgId(msgId);
        try{
            List<PmLineEntity> lines =  pmLineService.getData(null);
            List<Map<String,String>> lineCodeAndNames = new ArrayList<>(lines.size());
            lines.stream().forEach(item ->{
                Map<String,String> mapCodeAndName = new HashMap<>(2);
                mapCodeAndName.put("code",item.getLineCode());
                mapCodeAndName.put("name",item.getLineName());
                lineCodeAndNames.add(mapCodeAndName);
            });
            result.setSuccess(true);
            result.setData(lineCodeAndNames);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
            result.setData(Collections.emptyList());
        }
        return result;
    }

    @Operation(summary = "工位信息")
    @Parameters({
            @Parameter(name = "msgId", description = "扭矩系统传递msgId")})
    @GetMapping(value = "get_latest_statations")
    public LjBaseDTO getLatestStatations(String msgId) {
        LjBaseDTO result = new LjBaseDTO();
        result.setMsgId(msgId);
        try{
            Map<Long,PmLineEntity> mapOfLines =  pmLineService.getData(null).stream()
                    .collect(Collectors.toMap(PmLineEntity :: getId,e -> e));
            List<PmWorkStationEntity> stations =  pmWorkStationService.getData(null);
            List<Map<String,String>> stationCodeAndNames = new ArrayList<>(stations.size());
            stations.stream().forEach(item ->{
                PmLineEntity line = mapOfLines.get(item.getPrcPmLineId());
                Map<String,String> mapCodeAndName = new HashMap<>(3);
                mapCodeAndName.put("lineCode",line != null ? line.getLineCode() : "");
                mapCodeAndName.put("code",item.getWorkstationCode());
                mapCodeAndName.put("name",item.getWorkstationName());
                stationCodeAndNames.add(mapCodeAndName);
            });
            result.setSuccess(true);
            result.setData(stationCodeAndNames);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
            result.setData(Collections.emptyList());
        }
        return result;
    }

    @Operation(summary = "拧紧工具")
    @Parameters({
            @Parameter(name = "msgId", description = "扭矩系统传递msgId")})
    @GetMapping(value = "get_tightening_tools")
    public LjBaseDTO getTighteningTools(String msgId) {
        LjBaseDTO result = new LjBaseDTO();
        result.setMsgId(msgId);
        try{
            Map<Long,PmWorkStationEntity> mapOfStations =  pmWorkStationService.getData(null).stream()
                    .collect(Collectors.toMap(PmWorkStationEntity :: getId,e -> e));
            List<PmToolEntity> tools =  pmToolService.getData(null);
            List<Map<String,Object>> toolAttrs = new ArrayList<>(tools.size());
            tools.stream().forEach(item ->{
                PmWorkStationEntity station = mapOfStations.get(item.getPrcPmWorkstationId());
                Map<String,Object> mapToolAttr = new HashMap<>(6);
                mapToolAttr.put("stationCode",station != null ? station.getWorkstationCode() : "");
                mapToolAttr.put("toolId",String.valueOf(item.getId()));
                mapToolAttr.put("toolCode",item.getToolCode());
                mapToolAttr.put("toolName",item.getToolName());
                mapToolAttr.put("ipAddress",item.getIp());
                mapToolAttr.put("port",item.getPort());
                mapToolAttr.put("isEnable",item.getIsEnable());
                toolAttrs.add(mapToolAttr);
            });
            result.setSuccess(true);
            result.setData(toolAttrs);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
            result.setData(Collections.emptyList());
        }
        return result;
    }

    @Operation(summary = "定时任务发送工艺数据到拧紧平台")
    //@Scheduled(cron = "0 0/5 * * * ?")
    @GetMapping(value = "scheduledExecJobWoParam")
    public void scheduledExecJobWoParam() {
        logger.info("发送工艺数据开始");
        try{
            LjParamBaseDTO ljParamBaseDTO = prepareJobWoParam();
            tightenService.fetchDataFromApi(ljParamBaseDTO,"job_wo","JOB工艺参数接口", ApiTypeConst.TIGHTEN_WO);
        }catch (Exception e){
            logger.error("发送工艺数据失败:{}",e.getMessage());
        }
        logger.info("发送工艺数据结束");
    }

    @Operation(summary = "定时任务发送拧紧数据到拧紧平台")
    //@Scheduled(cron = "0 0/5 * * * ?")
    @GetMapping(value = "scheduledExecTightenParam")
    public void scheduledExecTightenParam() {
        logger.info("发送拧紧数据开始");
        try{
            LjParamBaseDTO ljParamBaseDTO = prepareTightenParam();
            tightenService.fetchDataFromApi(ljParamBaseDTO,"tighten","拧紧接口", ApiTypeConst.TIGHTEN_TIGHTEN);
        }catch (Exception e){
            logger.error("发送拧紧数据失败:{}",e.getMessage());
        }
        logger.info("发送拧紧数据结束");
    }

    @Operation(summary = "定时任务发送曲线数据到拧紧平台")
    //@Scheduled(cron = "0 0/5 * * * ?")
    @GetMapping(value = "submitTightenCurveParam")
    public void submitTightenCurveParam() {
        logger.info("发送曲线数据开始");
        try{
            LjParamBaseDTO ljParamBaseDTO = prepareCurveParam();
            tightenService.fetchDataFromApi(ljParamBaseDTO,"curve","曲线接口", ApiTypeConst.TIGHTEN_CURVE);
        }catch (Exception e){
            logger.error("发送曲线数据失败:{}",e.getMessage());
        }
        logger.info("发送曲线数据结束");
    }

    private LjParamBaseDTO prepareJobWoParam(){
        LjParamBaseDTO ljParamBaseDTO = new LjParamBaseDTO();
        ljParamBaseDTO.setMsgId(UUID.randomUUID().toString());
        LjParamWoDTO ljParamWoDTO = new LjParamWoDTO();
        ljParamBaseDTO.setData(Arrays.asList(ljParamWoDTO));
        ljParamWoDTO.setStationCode("EH_01");
        ljParamWoDTO.setToolId("EH-01-JOB-01");
        ljParamWoDTO.setJobId(999);
        ljParamWoDTO.setJobName("999");
        ljParamWoDTO.setJobBatchMode("1");
        ljParamWoDTO.setRepeatJob("0");
        ljParamWoDTO.setLockdone("0");
        LjParamWoScrDTO ljParamWoScrDTO = new LjParamWoScrDTO();
        ljParamWoScrDTO.setChannelID("1");
        ljParamWoScrDTO.setPsetID("2");
        ljParamWoScrDTO.setJobId("999");
        ljParamWoScrDTO.setBatchSize("2");
        ljParamWoScrDTO.setTorqueMax("14");
        ljParamWoScrDTO.setTorqueMin("10");
        ljParamWoScrDTO.setTorqueTarget("12");
        ljParamWoScrDTO.setAngleMax("180");
        ljParamWoScrDTO.setAngleMin("0");
        ljParamWoScrDTO.setAngleTarget("120");
        ljParamWoScrDTO.setDirection("正向");
        ljParamWoDTO.setJobList(Arrays.asList(ljParamWoScrDTO));
        return ljParamBaseDTO;
    }

    private LjParamBaseDTO prepareTightenParam(){
        LjParamBaseDTO ljParamBaseDTO = new LjParamBaseDTO();
        ljParamBaseDTO.setMsgId(UUID.randomUUID().toString());
        LjParamTightenDTO ljParamTightenDTO = new LjParamTightenDTO();
        ljParamTightenDTO.setVin("L1NSPGHB1PC080544");
        ljParamTightenDTO.setStationCode("EH_01");
        ljParamTightenDTO.setVehicleType("SC6501AAABEV");
        ljParamTightenDTO.setJobResult(0);
        ljParamTightenDTO.setJobTime("2023-11-30 10:11:52");
        ljParamBaseDTO.setData(ljParamTightenDTO);
        LjParamTightenDetailDTO ljParamTightenDetailDTO = new LjParamTightenDetailDTO();
        ljParamTightenDetailDTO.setToolId("EH-01-JOB-01");
        ljParamTightenDetailDTO.setControllerName("控制器1");
        ljParamTightenDetailDTO.setVin("L1NSPGHB1PC080544");
        ljParamTightenDetailDTO.setJobId(999);
        ljParamTightenDetailDTO.setPSetId(1);
        ljParamTightenDetailDTO.setBatchSize(1);
        ljParamTightenDetailDTO.setBatchCounter(1);
        ljParamTightenDetailDTO.setTighteningstatus(1);
        ljParamTightenDetailDTO.setBatchstatus(1);
        ljParamTightenDetailDTO.setTorque(12.87);
        ljParamTightenDetailDTO.setTorqueMax(14D);
        ljParamTightenDetailDTO.setTorqueMin(10D);
        ljParamTightenDetailDTO.setTorqueTarget(0.76);
        ljParamTightenDetailDTO.setAngle(0.74);
        ljParamTightenDetailDTO.setAngleMax(0.78);
        ljParamTightenDetailDTO.setAngleMin(0.69);
        ljParamTightenDetailDTO.setTighteningID(1L);
        ljParamTightenDetailDTO.setTighteningTime("2023-11-30 10:12:52");
        ljParamTightenDTO.setTightenData(Arrays.asList(ljParamTightenDetailDTO));
        return ljParamBaseDTO;
    }

    private LjParamBaseDTO prepareCurveParam(){
        LjParamBaseDTO ljParamBaseDTO = new LjParamBaseDTO();
        ljParamBaseDTO.setMsgId(UUID.randomUUID().toString());
        LjParamCurveDTO ljParamCurveDTO = new LjParamCurveDTO();
        ljParamCurveDTO.setStationCode("EH_01");
        ljParamCurveDTO.setVin("L1NSPGHB1PC080544");
        ljParamCurveDTO.setVehicleType("SC6501AAABEV");
        ljParamCurveDTO.setTightenResult(1);
        ljParamBaseDTO.setData(ljParamCurveDTO);
        LjParamCurveDetailDTO ljParamCurveDetailDTO = new LjParamCurveDetailDTO();
        ljParamCurveDTO.setTightenData(Arrays.asList(ljParamCurveDetailDTO));
        ljParamCurveDetailDTO.setControllerName("控制器1");
        ljParamCurveDetailDTO.setVin("L1NSPGHB1PC080544");
        ljParamCurveDetailDTO.setPSetId(1);
        ljParamCurveDetailDTO.setBoltNumber(1);
        ljParamCurveDetailDTO.setTighteningID(1L);
        ljParamCurveDetailDTO.setTimeCodfficient(0.66);
        List<Double> torquePoints = new ArrayList<>();
        torquePoints.add(10.5843);
        torquePoints.add(11.6834);
        torquePoints.add(12.8743);
        ljParamCurveDetailDTO.setTorquePoints(torquePoints);
        List<Double> anglePoints = new ArrayList<>();
        anglePoints.add(0.693);
        anglePoints.add(0.724);
        anglePoints.add(0.7454);
        ljParamCurveDetailDTO.setAnglePoints(anglePoints);
        List<Double> currentPoints = new ArrayList<>();
        currentPoints.add(2.112);
        currentPoints.add(2.224);
        currentPoints.add(2.335);
        ljParamCurveDetailDTO.setCurrentPoints(currentPoints);
        return ljParamBaseDTO;
    }


}