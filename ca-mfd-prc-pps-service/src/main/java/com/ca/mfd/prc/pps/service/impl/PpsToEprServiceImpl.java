package com.ca.mfd.prc.pps.service.impl;

import cn.hutool.json.JSONObject;
import com.ca.mfd.prc.common.config.RestTemplateConfig;
import com.ca.mfd.prc.common.dto.pm.ErpResponse;
import com.ca.mfd.prc.common.dto.pps.ErpBomOrCharacter;
import com.ca.mfd.prc.common.dto.pps.ErpBomResponse;
import com.ca.mfd.prc.common.exception.ExceptionUtils;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.BomInfo;
import com.ca.mfd.prc.pps.remote.app.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomVersionsProvider;
import com.ca.mfd.prc.pps.service.IPpsToEprService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PpsToEprService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Service
public class PpsToEprServiceImpl implements IPpsToEprService {

    private static final Logger logger = LoggerFactory.getLogger(PpsToEprServiceImpl.class);

    private static final String DATE_NAME_UPER = "Data";
    private static final String DATE_NAME_LOWER = "data";

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private PmProductBomVersionsProvider pmProductBomVersionsProvider;

    RestTemplateUtil getRestTemplateUtil() {
        RestTemplateConfig restTemplateConfig = SpringContextUtils.getBean(RestTemplateConfig.class);
        return new RestTemplateUtil(restTemplateConfig.newRestTemplate());
    }

    /**
     * 获取系统配置
     */
    private String getConfiguration(String key, String category) {
        return sysConfigurationProvider.getConfiguration(key, category);
    }

    /**
     * 获取系统配置
     */
    private List<SysConfigurationEntity> getSysConfigurations(String category) {
        return sysConfigurationProvider.getSysConfigurations(category);
    }

    @Override
    public String getErpBomVersion(String materialNo) {
        String recieveBomDataUrl = getConfiguration("RecieveBomData", "ErpInterface");

        if (StringUtils.isBlank(recieveBomDataUrl)) {
            throw new InkelinkException("ErpInterface RecieveBomData 请求url");
        }
        String bomVersion = "";
        String whiteBodyProductType = "";
        ErpBomOrCharacter request = new ErpBomOrCharacter();
        request.setProductMaterialNo(materialNo);

        String responseStr = "";
        ErpResponse<List<ErpBomResponse>> requestModel = new ErpResponse<>();
        try {
            ResponseEntity<String> requestTxt = getRestTemplateUtil().postJson(recieveBomDataUrl, request, null, String.class);
            responseStr = requestTxt.getBody();
            if (StringUtils.isNotBlank(responseStr)) {
                JSONObject response = JsonUtils.parseObject(responseStr, JSONObject.class);
                requestModel.setCode(response.getInt("code"));
                requestModel.setSuccess(response.getStr("success"));
                String datestr = "";
                if (response.get(DATE_NAME_UPER) != null) {
                    datestr = response.get(DATE_NAME_UPER).toString();
                } else if (response.get(DATE_NAME_LOWER) != null) {
                    datestr = response.get(DATE_NAME_LOWER).toString();
                }
                if (StringUtils.isNotBlank(datestr)) {
                    requestModel.setData(JsonUtils.parseArray(datestr, ErpBomResponse.class));
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            responseStr = ExceptionUtils.getErrorMessage(ex);
        }
        if (requestModel.getCode() != 0 || requestModel.getData() == null) {
            throw new InkelinkException("Bom失败," + responseStr);
        }

        MaintainBomDTO info = new MaintainBomDTO();
        info.setProductMaterialNo(materialNo);
        info.setBomVersions("");
        info.setBomData(requestModel.getData().stream().map(c -> {
            BomInfo et = new BomInfo();
            et.setMaterialNo(c.getMaterialNo());
            et.setMaterialCn(c.getMaterialCn() == null ? "" : c.getMaterialCn());
            et.setMaterialEn(c.getMaterialEn() == null ? "" : c.getMaterialEn());
            et.setQuantity(c.getQuantity());
            et.setUnit(c.getUnit() == null ? "" : c.getUnit());
            et.setOrgCode("");
            et.setShopCode("");
            et.setLineCode("");
            et.setStationCode("");
            et.setWeight(BigDecimal.valueOf(0));
            et.setWeightUnit("");
            et.setPackageCount(BigDecimal.valueOf(1));
            return et.filterEmpty();
        }).collect(Collectors.toList()));
        bomVersion = pmProductBomVersionsProvider.maintainBom(info);
        return bomVersion;
    }

}