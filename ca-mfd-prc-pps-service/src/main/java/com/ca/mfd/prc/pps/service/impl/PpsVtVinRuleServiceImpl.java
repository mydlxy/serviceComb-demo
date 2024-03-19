package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsVtVinRuleEntity;
import com.ca.mfd.prc.pps.entity.PpsVtVinYearEntity;
import com.ca.mfd.prc.pps.mapper.IPpsVtVinRuleMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.service.IPpsVtVinRuleService;
import com.ca.mfd.prc.pps.service.IPpsVtVinTimeService;
import com.ca.mfd.prc.pps.service.IPpsVtVinYearService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * VIN配置,前7位
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsVtVinRuleServiceImpl extends AbstractCrudServiceImpl<IPpsVtVinRuleMapper, PpsVtVinRuleEntity> implements IPpsVtVinRuleService {
    private static final int VALIDATION_VIN_LEN = 18;
    private static final int VIN_CODE_LEN = 8;
    private static final Integer WIN_ALL_LEN = 17;
    private static final int WIN_MID_INDEX = 8;
    private static final int WIN_MID_CHAR_START = 48;
    private static final int WIN_MID_CHAR_END = 57;
    private static final int WIN_MID_CHAR_MAX = 88;
    private final String cacheName = "PRC_PPS_VT_VIN_RULE";
    private final Map<String, String> orderDic = new LinkedHashMap<>();
    @Autowired
    private IPpsVtVinYearService ppsVtVinYearService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberProvider;
    @Autowired
    private IPpsVtVinTimeService ppsVtVinTimeService;
    @Autowired
    private PmOrgProvider pmOrgProvider;
    @Autowired
    private LocalCache localCache;

    {
        //orderDic.put(MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getMaterialNo), "整车物料号");
        orderDic.put(MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getVinCode), "VIN码前八位");
        orderDic.put(MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getModel), "大车型");
    }

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsVtVinRuleEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsVtVinRuleEntity model) {
        QueryWrapper<PpsVtVinRuleEntity> qry = new QueryWrapper<>();
        qry.lambda()
                //.eq(PpsVtVinRuleEntity::getMaterialNo, model.getMaterialNo());
                .eq(PpsVtVinRuleEntity::getModel, model.getModel());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("车型已存在");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(PpsVtVinRuleEntity model) {
        QueryWrapper<PpsVtVinRuleEntity> qry = new QueryWrapper<>();
        qry.lambda()
                //.eq(PpsVtVinRuleEntity::getMaterialNo, model.getMaterialNo())
                .eq(PpsVtVinRuleEntity::getModel, model.getModel())
                .ne(PpsVtVinRuleEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("车型已存在");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsVtVinRuleEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsVtVinRuleEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PpsVtVinRuleEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 创建流水号
     */
    private String createSn(String category) {
         return sysSnConfigProvider.createSn(category);
    }

    private String getSeqNum(String vinCode,String orderSign,String nowYear,String orgCode) {
        if (StringUtils.isBlank(vinCode)) {
            throw new InkelinkException("VIN前缀配置不存在，生成VIN失败！");
        }
        if (orderSign == null) {
            orderSign = "";
        }
        String sequenceType = getSequenceType(vinCode, orderSign, nowYear, orgCode);
        SysSequenceNumberEntity seqConfig = sysSequenceNumberProvider.getSysSequenceInfoByType(sequenceType);
        //没有找到，使用默认
        if (seqConfig == null) {
            orderSign = "";
            sequenceType = getSequenceType(vinCode, orderSign, nowYear, orgCode);
            seqConfig = sysSequenceNumberProvider.getSysSequenceInfoByType(sequenceType);
            if (seqConfig == null) {
                throw new InkelinkException("VIN流水号配置不存在[" + sequenceType + "]！");
            }
        }
        return sysSequenceNumberProvider.getSeqNum(sequenceType);
    }

    private String getSequenceType(String vinCode,String orderSign,String nowYear,String orgCode){
        return "SN_VIN" + vinCode + "," + orderSign+ "," + nowYear+ "," + orgCode;
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
    private List<ComboInfoDTO> getComboDatas(String category) {
        return sysConfigurationProvider.getComboDatas(category);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PpsVtVinRuleEntity model, boolean isUpdate) {
        if (model.getVinCode().length() != VIN_CODE_LEN) {
            throw new InkelinkException("VIN码前8位长度不匹配");
        }
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("MODEL", model.getModel(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PpsVtVinRuleEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("录入的车型" + model.getModel() + "已经存在，不允许重复录入！");
        }
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsVtVinRuleEntity>
     */
    @Override
    public List<PpsVtVinRuleEntity> getAllDatas() {
        Function<Object, ? extends List<PpsVtVinRuleEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

    /**
     * 获取所有的数据
     *
     * @return List<ComboInfoDTO>
     */
    @Override
    public List<ComboInfoDTO> getVinList() {
        List<PpsVtVinRuleEntity> datas = getAllDatas();
        if (datas == null || datas.isEmpty()) {
            return new ArrayList<>();
        }
        List<ComboInfoDTO> list = new ArrayList<>();
        for (PpsVtVinRuleEntity et : datas) {
            ComboInfoDTO cm = new ComboInfoDTO();
            cm.setValue(et.getVinCode());
            String models = datas.stream().filter(c->StringUtils.equalsIgnoreCase(c.getVinCode(),et.getVinCode()))
                    .map(c->c.getModel()).distinct().collect(Collectors.joining(","));
            cm.setText(models);
            list.add(cm);
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 创建VIN号 标准方法 其他使用此方法生成项目  Todo Vin确认
     *
     * @param ppsOrderInfo 订单实体
     * @return 创建的vin号
     */
    @Override
    public String createVin(PpsOrderEntity ppsOrderInfo) {
        String orgCode = pmOrgProvider.getCurrentOrgCode();
        String nowYear = DateUtils.format(new Date(), "yyyy");
        //前8位代码
        String vinCode = getPpsVtVinRuleCode(ppsOrderInfo);
        //第9位验证码
        String validationCode = "0";
        //获取年份代码10
        String yearCode = getYearCode(nowYear, ppsOrderInfo.getOrderSign());
        //获取工厂代码11
        String plantCode = getPlantCode(orgCode);
        //获取流水号6
        String currentSeq = getSeqNum(vinCode, ppsOrderInfo.getOrderSign(), nowYear, orgCode);
        if (StringUtils.isBlank(currentSeq)) {
            throw new InkelinkException("VIN流水号生成失败");
        }
        //临时vin号
        String tempVin = vinCode + validationCode + yearCode + plantCode + currentSeq;
        validationCode = calValidationVinCode(tempVin);
        //正式vin号
        String vin = vinCode + validationCode + yearCode + plantCode + currentSeq;
        if (!checkVin(vin)) {
            throw new InkelinkException("非法VIN号");
        }
        return vin;
    }

    private Boolean checkVin(String vin) {
        if (vin == null || vin.length() != WIN_ALL_LEN) {
            return false;
        }
        int symbol, weight, verify, sum = 0;
        Boolean isRepeat = true;
        if (vin.charAt(WIN_MID_INDEX) >= WIN_MID_CHAR_START && vin.charAt(WIN_MID_INDEX) <= WIN_MID_CHAR_END) {
            //0到9
            verify = vin.charAt(WIN_MID_INDEX) - WIN_MID_CHAR_START;
        } else if (vin.charAt(WIN_MID_INDEX) == WIN_MID_CHAR_MAX) {
            //X
            verify = 10;
        } else {
            return false;
        }
        for (int i = 0; i < vin.length(); i++) {
            if (i < 7) {
                weight = 8 - i;
            } else if (i > 8) {
                weight = 18 - i;
            } else if (i == 7) {
                weight = 10;
            } else {
                continue;
            }
            if (vin.charAt(i) >= 48 && vin.charAt(i) <= 57) {
                //0-9
                symbol = vin.charAt(i) - 48;
            } else if (vin.charAt(i) >= 65 && vin.charAt(i) <= 72) {
                //A-H
                symbol = vin.charAt(i) - 64;
            } else if (vin.charAt(i) >= 74 && vin.charAt(i) <= 82 && vin.charAt(i) != 79 && vin.charAt(i) != 81) {
                //J-R不含O,Q
                symbol = vin.charAt(i) - 73;
            } else if (vin.charAt(i) >= 83 && vin.charAt(i) <= 90) {
                //S-Z
                symbol = vin.charAt(i) - 81;
            } else {
                return false;
            }
            sum += symbol * weight;
            if (isRepeat && i > 0) {
                isRepeat = vin.charAt(i) == vin.charAt(i - (i != 9 ? 1 : 2));
            }
        }

        if (isRepeat) {
            return false;
        }
        return verify == (sum % 11);
    }

    /**
     * 计算Vin号验证码
     */
    String calValidationVinCode(String tempVin) {
        String code = "";
        List<ComboInfoDTO> vinVinCorrespondingValuelist = getComboDatas("VinCorrespondingValue");
        List<ComboInfoDTO> vinVinWeightingConffcient = getComboDatas("VinWeightingConffcient");
        int tempValidationValue = 0;
        Integer tempModule;
        for (int i = 1; i < VALIDATION_VIN_LEN; i++) {
            char temptchar = tempVin.charAt(i - 1);
            String value = "0";
            ComboInfoDTO comboInfoDTO = vinVinCorrespondingValuelist.stream()
                    .filter(s -> StringUtils.equals(s.getValue(), String.valueOf(temptchar)))
                    .findFirst().orElse(null);
            if (comboInfoDTO != null) {
                value = comboInfoDTO.getText();
            }
            String weight = "0";
            int finali = i;
            ComboInfoDTO weightComboInfo = vinVinWeightingConffcient.stream()
                    .filter(s -> StringUtils.equals(s.getValue(), String.valueOf(finali)))
                    .findFirst().orElse(null);
            if (weightComboInfo != null) {
                weight = weightComboInfo.getText();
            }
            tempValidationValue = tempValidationValue + (Integer.parseInt(value) * Integer.parseInt(weight));
        }
        tempModule = tempValidationValue % 11;
        code = tempModule == 10 ? "X" : String.valueOf(tempModule);
        return code;
    }

    /**
     * 获取工厂代码
     *
     * @return 工厂编码
     */
    private String getPlantCode(String orgCode) {
        //ppsOrderInfo.getProductCode()
        String plandCode = getConfiguration(orgCode, "VinPlantCode");
        if (StringUtils.isBlank(plandCode)) {
            throw new InkelinkException("VIN号没有配置对应的工厂");
        }
        return plandCode;
    }

    /**
     * 获取VIN号年份代码
     *
     * @return 获取年份代码10
     */
    String getYearCode(String nowYear,String orderSign) {
        String yearCode = "";
        //TODO 未确认，先不启用 String nowYear = ppsVtVinTimeService.getYearByOrderSign(orderSign);

        PpsVtVinYearEntity data = ppsVtVinYearService.getEntityByYear(nowYear);
        if (data != null) {
            yearCode = data.getVinYearCode();
        } else {
            throw new InkelinkException("VIN号没有配置年份代码");
        }
        return yearCode;
    }

    /**
     * 获取对应车型VIN号前8位
     *
     * @param ppsOrderInfo 订单对象
     * @return 返回对应车型VIN号前8位
     */
    String getPpsVtVinRuleCode(PpsOrderEntity ppsOrderInfo) {
        String code = "";
        //获取车型
        String materialNoFisrtSeven =com.ca.mfd.prc.common.utils.StringUtils.getModel(ppsOrderInfo.getProductCode());
        QueryWrapper<PpsVtVinRuleEntity> qry = new QueryWrapper<>();
        qry.lambda()
                //.eq(PpsVtVinRuleEntity::getMaterialNo, materialNoFisrtSeven)
                .eq(PpsVtVinRuleEntity::getModel, materialNoFisrtSeven);
        PpsVtVinRuleEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("VIN号前8位代码没有配置！" + materialNoFisrtSeven);
        }
        code = data.getVinCode();
        return code;
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        return orderDic;
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        //验证必填
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getVinCode), i + 1);
            //validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getMaterialNo), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsVtVinRuleEntity::getModel), i + 1);
        }
    }

    void validExcelDataRequire(Map<String, String> fieldParam, Map<String, String> data, String col, int rowIndex) {
        String columnName = fieldParam.get(col);
        String val = data.get(col);
        if (StringUtils.isBlank(val)) {
            throw new InkelinkException("第“" + rowIndex + "”行，“" + columnName + "”列：不能为空");
        }
    }


}