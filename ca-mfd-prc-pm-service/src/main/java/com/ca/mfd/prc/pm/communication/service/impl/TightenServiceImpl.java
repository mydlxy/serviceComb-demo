package com.ca.mfd.prc.pm.communication.service.impl;


import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.dto.LjBaseDTO;
import com.ca.mfd.prc.pm.communication.dto.LjParamBaseDTO;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: bom接口基类实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@Service
public  class TightenServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TightenServiceImpl.class);

    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;


    public void fetchDataFromApi(LjParamBaseDTO params, String bompart_receive, String title, String apiTypeConst) {
//        String apiUrl = sysConfigurationProvider.getConfiguration(bompart_receive, "tighten_api");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置拧紧设备上报的地址["+ bompart_receive +"]");
//        }
        //String apiUrl = "http://192.168.71.190:8081/api/TighteningCurveData/UploadTighteningCurveData";
        //String apiUrl = "http://192.168.71.190:8081/api/TighteningDataManage/UpLoadTighteningData";
        String apiUrl = "http://192.168.71.190:8081/api/TechnologicalParameters/UpLoadJobParameters";
        List<MidApiLogEntity> midApiLogEntities = callApi(params, apiUrl, title, params.getMsgId(),5);
        MidApiLogEntity midApiLogEntity = midApiLogEntities.get(midApiLogEntities.size()-1);
        midApiLogService.insertBatch(midApiLogEntities,100,false,0);
        midApiLogService.saveChange();
        if(midApiLogEntity.getStatus() == 6){
            throw new InkelinkException(String.format("调用拧紧设备接口出错：%s",midApiLogEntity.getRemark()));
        }
    }


    private List<MidApiLogEntity> callApi(LjParamBaseDTO params,String apiUrl,String title,String reqNo,int callTimes){
       int callIndex = 0;
       List<MidApiLogEntity> midApiLogEntities = new ArrayList<>(5);
       while (callIndex < callTimes){
           MidApiLogEntity midApiLogEntity = callApi(params, apiUrl, title, reqNo);
           callIndex++;
           midApiLogEntities.add(midApiLogEntity);
           if(midApiLogEntity.getStatus() == 1){
               return midApiLogEntities;
           }
       }
       return midApiLogEntities;
    }

    private MidApiLogEntity callApi(LjParamBaseDTO params,String apiUrl,String title,String reqNo){
        MidApiLogEntity midApiLogEntity = new MidApiLogEntity();
        midApiLogEntity.setStatus(0);
        String responseData = "";
        logger.info(title + "[" + reqNo + "]开始接收数据");
        // 发起HTTP请求
        midApiLogEntity.setRequestStartTime(new Date());
        try{
            responseData = apiPtService.postapi(apiUrl, params, null);
            printLog(1,String.format("%s[%s]执行成功",title,reqNo),midApiLogEntity);
        }catch (Exception e){
            printLog(6,String.format("%s[%s]调用拧紧系统接口异常:%s",title,reqNo,e.getMessage()),midApiLogEntity);
        }
        midApiLogEntity.setRequestStopTime(new Date());
        LjBaseDTO resultVO = null;
        try{
            resultVO = JsonUtils.parseObject(responseData, LjBaseDTO.class);
        }catch (Exception e){
            printLog(6,String.format("%s[%s]拧紧系统返回的格式不是指定报文格式:%s",title,reqNo,e.getMessage()),midApiLogEntity);
        }
        if(resultVO == null){
            printLog(6,String.format("%s[%s]拧紧系统返回空字符串",title,reqNo),midApiLogEntity);
        }
        if(!resultVO.isSuccess()){
            printLog(6,String.format("%s[%s]拧紧系统返回",title,reqNo,resultVO.getErrMsg()),midApiLogEntity);
        }
        logger.info(title + "[" + reqNo + "]接收完毕");
        return midApiLogEntity;
    }

    private void printLog(int status,String msg,MidApiLogEntity midApiLogEntity){
        midApiLogEntity.setStatus(status);
        midApiLogEntity.setRemark(msg);
        logger.error(msg);
    }

}