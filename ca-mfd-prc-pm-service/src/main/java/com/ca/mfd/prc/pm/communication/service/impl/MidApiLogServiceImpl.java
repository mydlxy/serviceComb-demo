package com.ca.mfd.prc.pm.communication.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.dto.as.AsRequestDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.dto.AsResultVo;
import com.ca.mfd.prc.pm.communication.dto.MidAsShcShiftDto;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidApiLogMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.remote.app.core.ISysConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidApiLogServiceImpl extends AbstractCrudServiceImpl<IMidApiLogMapper, MidApiLogEntity> implements IMidApiLogService {
    private static final Logger logger = LoggerFactory.getLogger(MidApiLogServiceImpl.class);
    @Autowired
    private ISysConfigurationService sysConfigurationProvider;
    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @Override
    public ResultVO<String> getAsAllQuery(String searchKey, Map searchData) {

        String errMsg = "";
        try {
            ResultVO<String> confgData = sysConfigurationProvider.getConfiguration("as_all_query", "midapi");
            String apiPath = confgData != null && confgData.getSuccess() ? confgData.getData() : "";
            if (StringUtils.isBlank(apiPath)) {
                throw new InkelinkException("没有配置AS查询的地址[as_all_query]");
            }
            //String searchKey = "ascalendardaymode";
            AsRequestDto apiData = new AsRequestDto("MOM", searchKey);
            if (searchData != null) {
                apiData.setParams(searchData);
            }
            String ars = apiPtService.postapi(apiPath, apiData, null);

            ////rowCount
            logger.warn("API平台测试url调用：" + ars);
            AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
            if (asResultVo != null) {
                if (StringUtils.equals("1", asResultVo.getCode())) {
                    if (asResultVo.getDatas() != null) {
                        return new ResultVO<String>().ok(JsonUtils.toJsonString(asResultVo.getDatas()));
                    } else {
                        return new ResultVO<String>().ok("");
                    }
                } else {
                    errMsg = "AS查询[" + searchKey + "]处理失败:" + asResultVo.getMsg();
                    logger.info(errMsg);
                }
            }
        } catch (Exception eas) {
            logger.error("", eas);
            errMsg = "AS查询[" + searchKey + "]处理失败:";
            logger.info(errMsg);
        }
        return new ResultVO<String>().error(-1, errMsg);
    }

    @Override
    public List<MidApiLogEntity> getDoList(String apitype,Long logid) {

        QueryWrapper<MidApiLogEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<MidApiLogEntity> qryLmp = qry.lambda();
        qryLmp.in(MidApiLogEntity::getStatus, Arrays.asList(1, 6))
                .eq(MidApiLogEntity::getApiType, apitype);
        if (logid > 0) {
            qryLmp.eq(MidApiLogEntity::getId, logid);
        }
        return selectList(qry);
    }
}