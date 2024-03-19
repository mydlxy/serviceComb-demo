package com.ca.mfd.prc.pqs.communication.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pqs.communication.dto.LmsSupplierDto;
import com.ca.mfd.prc.pqs.communication.dto.LmsSupplierResultVo;
import com.ca.mfd.prc.pqs.communication.service.ILmsService;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: 调用Lms服务实现
 * @date 2024年02月19日
 */
@Service
public class LmsServiceImpl implements ILmsService {
    private static final Logger logger = LoggerFactory.getLogger(ILmsService.class);
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @Override
    public List<LmsSupplierDto> querysupplrelbymaterialcode(String materialCode) {
        List<LmsSupplierDto> dtoList = new ArrayList<>();
        String str = sysConfigurationProvider.getConfiguration("status", "LmsSupplier");
        if (StringUtils.isBlank(str)) {
            throw new InkelinkException("没有配置Lms供应商接口调用状态参数");
        }

        if (str.equals("0")) {
            LmsSupplierDto dto = new LmsSupplierDto();
            dto.setSupplierCode("S31638");
            dto.setSupplierName("重庆渝北汽车公司01");
            dtoList.add(dto);

            dto = new LmsSupplierDto();
            dto.setSupplierCode("SS2024012920");
            dto.setSupplierName("广州测试科技20");
            dtoList.add(dto);

            return dtoList;
        }
        String url = sysConfigurationProvider.getConfiguration("querysupplrelbymaterialcode", "LmsSupplierApiUrl");
        if (StringUtils.isBlank(url)) {
            throw new InkelinkException("没有配置Lms供应商接口地址");
        }
        String response = apiPtService.lmsSupplierGetApi(url, materialCode, null);
        LmsSupplierResultVo resultVo = JsonUtils.parseObject(response, LmsSupplierResultVo.class);
        logger.info("调用lms供应商接口返回结果:" + JsonUtils.toJsonString(resultVo));
        if (!resultVo.getCode().equals("1")) {
            throw new InkelinkException("调用lms供应商接口异常,错误信息:" + resultVo.getMsg());
        }
        if (resultVo.getData() == null || resultVo.getData().isEmpty()) {
            return null;
        }
        dtoList = resultVo.getData();
        return dtoList;
    }
}