package com.ca.mfd.prc.pps.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.yidiantong.BizContent;
import com.ca.mfd.prc.common.utils.yidiantong.Client;
import com.ca.mfd.prc.common.utils.yidiantong.HttpTool;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IYiDianTongService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 伊点通接口调用类
 */
@Service
public class YiDianTongServiceImpl implements IYiDianTongService {

    private static final Logger logger = LoggerFactory.getLogger(YiDianTongServiceImpl.class);
    private static final String ORDER_CODE_ISSUED_METHOD = "changan.proc.orderCodeIssued";

    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IPpsEntryPartsService ppsEntryPartsService;

    //客户编码
    @Value("${inkelink.yidiantong.customerCode:changan}")
    private String customerCode;
    //客户秘钥
    @Value("${inkelink.yidiantong.secret:298c97f0fc2611ed}")
    private String secret;

    @Autowired
    @Qualifier("yiDianTongClient")
    private Client client;


    private Boolean orderCodeIssued(BizContent bizContent) {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method(ORDER_CODE_ISSUED_METHOD)
                .version("1.0").customerCode(customerCode).customerSecret(secret)
                .bizContent(bizContent)
                .httpMethod(HttpTool.HTTPMethod.POST);
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.YIDIANTONG);
        int status = 0;
        String errMsg = "";
        loginfo.setDataLineNo(1);
        loginfo.setRequestStartTime(new Date());
        loginfo.setAttribute1(JSON.toJSONString(bizContent));
        String responseData = "";
        try{
            responseData = client.execute(requestBuilder);
            errMsg = "调用伊点通'工单号下发'接口返回:" + responseData;
            logger.info(errMsg);
        }catch (Exception e){
            status = 5;
            errMsg= "调用伊点通'工单号下发'接口失败:" + e.getMessage();
            logger.error(errMsg,e);
        }
        loginfo.setRequestStopTime(new Date());
        if(StringUtils.isNotBlank(responseData)){
            JSONObject jsonObject = JSON.parseObject(responseData);
            if(jsonObject == null){
                status = 5;
                errMsg = "伊点通'工单号下发'接口返回:null";
            }else{
                if(Objects.equals(String.valueOf(jsonObject.get("status")),"200")){
                    status = 1;
                    errMsg = "下发成功";
                }else{
                    status = 5;
                    errMsg = "伊点通'工单号下发'接口返回:" + jsonObject.get("msg");
                }
            }

        }
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.insert(loginfo);
        return status == 1;
    }

    @Override
    public ResultVO<Boolean> sendOrderNos(String processCode){
        QueryWrapper<PpsEntryPartsEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryPartsEntity> lqw = qw.lambda();
        lqw.eq(PpsEntryPartsEntity :: getAttribute1,processCode);
        lqw.eq(PpsEntryPartsEntity :: getOrderCategory,3);
        lqw.eq(PpsEntryPartsEntity :: getStatus,3);
        lqw.eq(PpsEntryPartsEntity :: getIsFreeze,0);
        lqw.eq(PpsEntryPartsEntity :: getIsSend,false);
        lqw.orderByAsc(PpsEntryPartsEntity :: getEstimatedStartDt);
        List<PpsEntryPartsEntity> ppsEntryPartsEntities =  ppsEntryPartsService.getData(qw,false);
        if(ppsEntryPartsEntities.isEmpty()){
            throw new InkelinkException("没有任何符合条件的工单号");
        }
//        AtomicInteger countSuccess = new AtomicInteger();
//        ppsEntryPartsEntities.forEach(item -> {
//                    if (sendOrderNo(item)) {
//                        countSuccess.getAndIncrement();
//                    }
//                }
//                );
        boolean res = sendOrderNo(ppsEntryPartsEntities.get(0));
        ppsEntryPartsService.saveChange();
        return new ResultVO().ok(true,String.format("共1条工单,成功发送[%s]条",res ? 1 : 0));
    }

    private boolean sendOrderNo(PpsEntryPartsEntity ppsEntryPartsEntity){
        BizContent bizContent = new BizContent();
        bizContent.put("sn",ppsEntryPartsEntity.getAttribute1());
        bizContent.put("orderCode",ppsEntryPartsEntity.getEntryNo());
        bizContent.put("planNum",ppsEntryPartsEntity.getPlanQuantity());
        bizContent.put("partsNo",ppsEntryPartsEntity.getMaterialNo());
        boolean execResult = orderCodeIssued(bizContent);
        if(execResult){
            UpdateWrapper<PpsEntryPartsEntity> qw = new UpdateWrapper<>();
            LambdaUpdateWrapper<PpsEntryPartsEntity> lqw = qw.lambda();
            lqw.set(PpsEntryPartsEntity :: getIsSend,true);
            lqw.eq(PpsEntryPartsEntity :: getId,ppsEntryPartsEntity.getId());
            ppsEntryPartsService.update(qw);
        }
        return execResult;
    }


}