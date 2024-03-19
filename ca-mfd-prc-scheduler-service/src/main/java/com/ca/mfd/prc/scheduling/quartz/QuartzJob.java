package com.ca.mfd.prc.scheduling.quartz;

import com.alibaba.fastjson.JSON;
import com.ca.mfd.prc.common.config.RestTemplateConfig;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.scheduling.entity.LogModel;
import com.ca.mfd.prc.scheduling.entity.QuartzConstant;
import com.ca.mfd.prc.scheduling.entity.RequestTypeEnum;
import lombok.SneakyThrows;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


// 持久化
@PersistJobDataAfterExecution
// 禁止并发执行
@DisallowConcurrentExecution
//@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.scheduling.quartz.enable'))}")
public class QuartzJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(QuartzJob.class);
    private static final int maxLogCount = 4; //最多保存日志数量

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //  URLEncoder
        String requestUrl = "";
        String appId = "";
        String requestStatus = "True";
        String requestTimeout = "True";
        Integer requestType = 0;
        RequestTypeEnum requestTypeEnum = RequestTypeEnum.NONE;
        Integer scheduleDelay = 2;
        Integer scheduleDelayTimeout = 5;
        //  String enableSendMessage = "";
        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.REQUESTURL)) {
            requestUrl = URLDecoder.decode(String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.REQUESTURL)), "UTF-8");
        }
        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.AppId)) {
            appId = String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.AppId));
        }
        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.JobRequestStatus)) {
            requestStatus = String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.JobRequestStatus));
        }
        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.JobRequestTimeout)) {
            requestTimeout = String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.JobRequestTimeout));
        }
        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.REQUESTTYPE)) {
            try {
                int value = Integer.parseInt(String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.REQUESTTYPE)));
                requestTypeEnum = RequestTypeEnum.find(value);
                if (requestTypeEnum != null) {
                    requestType = value;
                }
            } catch (Exception ex) {

            }
        }
        //获取相关的参数
        /*
        var scheduleDelay = configurationBll.GetSysConfigurations("ScheduleDelay").FirstOrDefault()?.Value;
        var scheduleDelayTimeout = configurationBll.GetSysConfigurations("ScheduleDelayTimeout").FirstOrDefault()?.Value;
        var enableSendMessage = configurationBll.GetSysConfigurations("SendMessage").FirstOrDefault()?.Value;
       */

        LogModel logModel = new LogModel();
        logModel.setRequestUrl(requestUrl);
        logModel.setRequestType(String.valueOf(requestType));
        List<LogModel> logs;
        if (!context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.LOGS))
            logs = new ArrayList<>();
        else {
            String strLogs = String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.LOGS));
            logs = JSON.parseArray(strLogs, LogModel.class);
        }
        if (!CollectionUtils.isEmpty(logs)) {
            while (true) {
                if (logs.size() < maxLogCount) {
                    break;
                }
                logs.remove(0);
            }
        }
        Date currentTime = new Date();
        Date memoryNextFireTime = currentTime;

        Boolean haveMemoryNextFireTime = false;//内存里面是否有记录下一次执行时间

        if (context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.NextRequestTime)) {
            String strMemoryNextFireTime = String.valueOf(context.getJobDetail().getJobDataMap().get(QuartzConstant.NextRequestTime));
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = fmt.parse(strMemoryNextFireTime);
                memoryNextFireTime = date;
                haveMemoryNextFireTime = true;
            } catch (Exception ex) {

            }
        }

        Calendar calScheduleDelayTimeout = Calendar.getInstance();
        calScheduleDelayTimeout.setTime(memoryNextFireTime);
        calScheduleDelayTimeout.add(Calendar.MINUTE, scheduleDelayTimeout);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strScheduleDelayTimeout = sdf.format(calScheduleDelayTimeout.getTime());

        Calendar calScheduleDelay = Calendar.getInstance();
        calScheduleDelay.setTime(memoryNextFireTime);
        calScheduleDelay.add(Calendar.MINUTE, scheduleDelay);
        String strScheduleDelay = sdf.format(calScheduleDelay.getTime());


        String strCurrentTime = sdf.format(currentTime);

        try {
            ResponseEntity responseEntity;
            //time<cal.getTime()
            if (haveMemoryNextFireTime && "False".equalsIgnoreCase(requestTimeout) && currentTime.compareTo(calScheduleDelayTimeout.getTime()) < 0) {
                String errorMsg = "请求超时1，下次请求时间在 " + strScheduleDelayTimeout + " 之后，当前时间：" + strCurrentTime;
                logModel.setErrorMsg(errorMsg);
                logModel.setError(true);
            } else if (haveMemoryNextFireTime && "False".equalsIgnoreCase(requestStatus) && currentTime.compareTo(calScheduleDelay.getTime()) < 0) {
                String errorMsg = "接口服务异常，下次请求在" + strScheduleDelay + " 之后，开始请求接口服务！";
                logModel.setErrorMsg(errorMsg);
                logModel.setError(true);
            } else {
                responseEntity = requestAsync(requestType,
                        context,
                        appId,
                        requestUrl,
                        logModel
                );
                if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.REQUEST_TIMEOUT)) {
                    memoryNextFireTime = new Date();
                    currentTime = new Date();
                    strCurrentTime = sdf.format(currentTime);

                    Calendar calTimeout = Calendar.getInstance();
                    calTimeout.setTime(memoryNextFireTime);
                    calTimeout.add(Calendar.MINUTE, scheduleDelayTimeout);
                    String strCalTimeout = sdf.format(calTimeout.getTime());

                    //  _logger.LogError(timeoutException, timeoutException.Message);

                    String errorMsg = "请求超时，下次请求时间在" + strCalTimeout + "之后执行，当前时间：" + strCurrentTime;
                    logModel.setErrorMsg(errorMsg);
                    logModel.setError(true);

                    context.getJobDetail().getJobDataMap().put(QuartzConstant.JobRequestTimeout, "False");
                    // if (!context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.NextRequestTime)) {
                    context.getJobDetail().getJobDataMap().put(QuartzConstant.NextRequestTime, strCurrentTime);
                    //  }

                } else if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    if (requestTypeEnum == null) {
                        requestTypeEnum = RequestTypeEnum.NONE;
                    }
                    logModel.setEndTime(new Date());
                    logModel.setBody(String.valueOf(responseEntity.getBody()));
                    logModel.setResult("请求地址：" + requestUrl + ";请求类型：" + requestTypeEnum.name().toUpperCase());

                    context.getJobDetail().getJobDataMap().put(QuartzConstant.JobRequestStatus, "True");
                    context.getJobDetail().getJobDataMap().put(QuartzConstant.JobRequestTimeout, "True");
                   /*
                    logger.LogInformation(JsonConvert.SerializeObject(response));*/
                } else {
                    String responseStatus = "";
                    String responseMessage = "";
                    if (responseEntity != null) {
                        responseStatus = String.valueOf(responseEntity.getStatusCode().value());
                        responseMessage = String.valueOf(responseEntity.getBody());
                    }

                    context.getJobDetail().getJobDataMap().put(QuartzConstant.JobRequestStatus, "False");
                    String errorMsg = "业务接口异常,请求状态：" + responseStatus + "，异常信息：" + responseMessage;
                    logModel.setErrorMsg(errorMsg);
                    logModel.setError(true);
                }
            }
        } catch (Exception ex) {
            memoryNextFireTime = new Date();
            currentTime = new Date();
            strCurrentTime = sdf.format(currentTime);

            Calendar calTimeout = Calendar.getInstance();
            calTimeout.setTime(memoryNextFireTime);
            calTimeout.add(Calendar.MINUTE, scheduleDelay);
            strScheduleDelay = sdf.format(calTimeout.getTime());

            //  _logger.LogError(exception, exception.Message);
            String errorMsg = "请求状态：" + requestStatus + "，下次请求时间在 " + strScheduleDelay + " 之后，当前时间：" + strCurrentTime + "，异常信息：" + ex.getMessage();
            logModel.setErrorMsg(errorMsg);
            logModel.setError(true);
            context.getJobDetail().getJobDataMap().put(QuartzConstant.JobRequestStatus, "False");
            //  if (!context.getJobDetail().getJobDataMap().containsKey(QuartzConstant.NextRequestTime)) {
            context.getJobDetail().getJobDataMap().put(QuartzConstant.NextRequestTime, strCurrentTime);
            //  }

        } finally {
            stopWatch.stop(); //  停止监视
            double seconds = stopWatch.getTotalTimeSeconds(); //总秒数
            logModel.setUseTime(seconds);
            logModel.setEndTime(new Date());
            //  String strLog = JSON.toJSONString(logModel);
            logs.add(logModel);
            String strLogs = JSON.toJSONString(logs);
            context.getJobDetail().getJobDataMap().put(QuartzConstant.LOGS, strLogs);
        }
    }

    /**
     * 请求url
     *
     * @param requestType
     * @param context
     * @param appId
     * @param requestUrl
     * @param logModel
     */
    private ResponseEntity requestAsync(Integer requestType,
                                        JobExecutionContext context,
                                        String appId,
                                        String requestUrl,
                                        LogModel logModel) {
        RequestTypeEnum requestTypeEnum = RequestTypeEnum.find(requestType);
        context.getJobDetail().getJobDataMap().put(QuartzConstant.NextRequestTime, "");
        if (requestTypeEnum == null) {
            log.info("执行没有请求类型！");
            return null;
        }
        //不需要参数，所以只需要有url然后请求即可
        String interfaceUrl = requestUrl;//默认认为是访问外部链接，直接用requestUrl
        ResponseEntity responseEntity = null;
        RestTemplateConfig restTemplateConfig = AppContextUtil.getBean(RestTemplateConfig.class);
        RestTemplateUtil restTemplateUtil = new RestTemplateUtil(restTemplateConfig.newRestTemplate());//默认是访问外部地址
        if (StringUtils.hasText(appId)) {
            //选择了全局id，说明是内部服务
            restTemplateUtil = AppContextUtil.getBean(RestTemplateUtil.class);
            if (requestUrl.indexOf("http") != 0) {
                if (appId.indexOf("http") == 0) {
                    interfaceUrl = appId + requestUrl;
                } else {
                    interfaceUrl = "http://" + appId + requestUrl;
                }
            }
        }

        HashMap<String, String> mapHeader = new HashMap<>();
        mapHeader.put("user-agent", "Iot");
        mapHeader.put("x-from", "Iot");
        if (requestTypeEnum.equals(RequestTypeEnum.GET)) {
            responseEntity = restTemplateUtil.getStringForResponse(interfaceUrl, mapHeader);
        } else if (requestTypeEnum.equals(RequestTypeEnum.POST)) {
            responseEntity = restTemplateUtil.postJsonFroResponse(interfaceUrl, null, mapHeader);
        }

        return responseEntity;
    }

}
