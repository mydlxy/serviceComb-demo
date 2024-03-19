package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IApiPtformBaseService;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;

import com.ca.mfd.prc.pps.communication.dto.BomResultVo;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogBaseService;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @Description: bom接口基类实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
public abstract class MidBomBaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity,INFO> extends AbstractCrudServiceImpl<M, T>{
    private static final Logger logger = LoggerFactory.getLogger(MidBomBaseServiceImpl.class);

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Qualifier("midApiLogBaseServiceImpl")
    @Autowired
    private IMidApiLogBaseService midApiLogService;

    @Autowired
    @Qualifier("apiBomPtService")
    private IApiPtformBaseService apiPtService;

    protected final List<INFO> fetchDataFromApi(String bomUrlKey,String title,String apiTypeConst, boolean verifyExists, String... params) {
        return fetchDataFromApi(bomUrlKey,title,apiTypeConst,true,verifyExists,params);
    }

    protected final List<INFO> fetchDataFromApi(String bomUrlKey,String title,String apiTypeConst,boolean mustBatchAdd, boolean verifyExists, String... params) {
        String apiUrl = sysConfigurationProvider.getConfiguration(bomUrlKey, "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址["+ bomUrlKey +"]");
        }
        String reqNo = UUIDUtils.getGuid();
        logger.info(title + "[" + reqNo + "]开始接收数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(apiTypeConst);
        int status = 0;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        List<INFO> infos = new ArrayList<>();
        String verifyData = StringUtils.EMPTY;
        try {
            loginfo.setStatus(status);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            // 构建API请求参数
            Map<String,String> param = getParams(params);
            param.put("serviceId",reqNo);
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, param, null);
            logger.warn("API平台测试url调用：" + responseData);
            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
            if(resultVO!= null && resultVO.getCode() == 1){
                infos = fetchEntity(resultVO);
                if(infos.isEmpty()){
                    status = 1;
                    errMsg = title + "[" + reqNo + "]获取成功,但BOM系统返回空数据";
                    logger.error(errMsg);
                }else if(mustBatchAdd){
                    if(verifyExists){
                        verifyData = md5(JsonUtils.toJsonString(infos));
                        QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
                        queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
                        List<MidApiLogEntity> data = midApiLogService.getData(queryWrapper, false);
                        if(CollectionUtils.isEmpty(data)){
                            this.insertBatch(infos.stream().map(c -> getEntity(c,loginfo.getId())).collect(Collectors.toList()),200,false,1);
                            this.saveChange();
                            status = 1;
                            errMsg = title +  "[" + reqNo + "]接收保存成功";
                            logger.info(errMsg);
                        }else {
                            status = 1;
                            errMsg = title + "[" + reqNo + "]处理失败，数据已存在";
                            logger.error(errMsg);
                        }
                    }else{
                        this.insertBatch(infos.stream().map(c -> getEntity(c,loginfo.getId())).collect(Collectors.toList()),200,false,1);
                        this.saveChange();
                        status = 1;
                        errMsg = title +  "[" + reqNo + "]接收保存成功";
                        logger.info(errMsg);
                    }
                } else{
                    status = 1;
                    errMsg = title +  "[" + reqNo + "]接收成功";
                    logger.info(errMsg);
                }

            }else {
                status = 5;
                errMsg = title + "[" + reqNo + "]获取成功，但BOM系统返回异常：" + resultVO.getMsg();
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
        return infos;
    }


    protected final Map<Long,List<INFO>> fetchDataFromSoftApi(String bomUrlKey,String title,String apiTypeConst,boolean mustBatchAdd, boolean verifyExists, String... params) {
        String apiUrl = sysConfigurationProvider.getConfiguration(bomUrlKey, "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址["+ bomUrlKey +"]");
        }
        String reqNo = UUIDUtils.getGuid();
        logger.info(title + "[" + reqNo + "]开始接收数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(apiTypeConst);
        int status = 0;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        List<INFO> infos = new ArrayList<>();
        String verifyData = StringUtils.EMPTY;
        try {
            loginfo.setStatus(status);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            // 构建API请求参数
            Map<String,String> param = getParams(params);
            param.put("serviceId",reqNo);
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, param, null);
            logger.warn("API平台测试url调用：" + responseData);
            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
            if(resultVO!= null && resultVO.getCode() == 1){
                infos = fetchEntity(resultVO);
                if(infos.isEmpty()){
                    status = 5;
                    errMsg = title + "[" + reqNo + "]获取成功,但BOM系统返回空数据";
                    logger.error(errMsg);
                }else if(mustBatchAdd){
                    if(verifyExists){
                        verifyData = md5(JsonUtils.toJsonString(infos));
                        QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
                        queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
                        List<MidApiLogEntity> data = midApiLogService.getData(queryWrapper, false);
                        if(CollectionUtils.isEmpty(data)){
                            this.insertBatch(infos.stream().map(c -> getEntity(c,loginfo.getId())).collect(Collectors.toList()),200,false,1);
                            this.saveChange();
                            status = 1;
                            errMsg = title +  "[" + reqNo + "]接收保存成功";
                            logger.info(errMsg);
                        }else {
                            status = 5;
                            errMsg = title + "[" + reqNo + "]处理失败，数据已存在";
                            logger.error(errMsg);
                        }
                    }else{
                        this.insertBatch(infos.stream().map(c -> getEntity(c,loginfo.getId())).collect(Collectors.toList()),200,false,1);
                        this.saveChange();
                        status = 1;
                        errMsg = title +  "[" + reqNo + "]接收保存成功";
                        logger.info(errMsg);
                    }
                } else{
                    status = 1;
                    errMsg = title +  "[" + reqNo + "]接收成功";
                    logger.info(errMsg);
                }

            }else {
                status = 5;
                errMsg = title + "[" + reqNo + "]获取成功，但BOM系统返回异常：" + resultVO.getMsg();
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
        Map<Long,List<INFO>> map=new HashMap<>();
        map.put(loginfo.getId(),infos);
        return map;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    protected abstract T getEntity(INFO info,Long loginfoId);

    protected abstract List<INFO> fetchEntity(BomResultVo resultVO);

    protected abstract Map<String,String> getParams(String... params);


}