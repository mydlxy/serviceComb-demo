package com.ca.mfd.prc.pps.communication.service.impl;


import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;

import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogBaseService;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.Map;

/**
 *
 * @Description: LMS接口基类实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
public abstract class MidLmsBaseServiceImpl<DTO>{
    private static final Logger logger = LoggerFactory.getLogger(MidLmsBaseServiceImpl.class);

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Qualifier("midApiLogBaseServiceImpl")
    @Autowired
    private IMidApiLogBaseService midApiLogService;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;


    protected final boolean fetchDataFromApi(String bomUrlKey,String title,
                                             String apiTypeConst,DTO dto) {
        String apiUrl = sysConfigurationProvider.getConfiguration(bomUrlKey, "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址["+ bomUrlKey +"]");
        }
        String reqNo = UUIDUtils.getGuid();
        logger.info(title + "[" + reqNo + "]开始调用");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(apiTypeConst);
        int status = 0;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        String verifyData = StringUtils.EMPTY;
        try {
            loginfo.setStatus(status);

            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            // 构建API请求参数
            Object param = getParams(dto);
            if(param!=null) {
                loginfo.setReqData(JsonUtils.toJsonString(param));
            }
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, param, null);
            logger.warn("API平台测试url调用：" + responseData);
            loginfo.setResponseData(responseData);
            AsResultVo resultVO = JsonUtils.parseObject(responseData, AsResultVo.class);
            if(resultVO!= null && "1".equals(resultVO.getCode())){
                status = 1;
                errMsg = title +  "[" + reqNo + "]发送成功";
                logger.info(errMsg);

            }else {
                status = 5;
                errMsg = title + "[" + reqNo + "]LMS系统返回异常：" + resultVO.getMsg();
                logger.error(errMsg);
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = title + "[" + reqNo + "]处理失败:"+ ex.getMessage();
            logger.error(errMsg, ex);
        }
        loginfo.setAttribute1(verifyData);
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info(title + "[" + reqNo + "]执行完成");
        if(status == 5){
            throw new InkelinkException(errMsg);
        }
        return true;
    }


    protected abstract Object getParams(DTO dto);


}