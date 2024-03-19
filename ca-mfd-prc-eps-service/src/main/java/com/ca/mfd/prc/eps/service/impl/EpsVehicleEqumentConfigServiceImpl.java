package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.eps.dto.ImPortExcelModel;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentConfigEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentParConfigEntity;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigEntity;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleEqumentConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentConfigService;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentParConfigService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author inkelink
 * @Description: 追溯设备工艺配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsVehicleEqumentConfigServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleEqumentConfigMapper, EpsVehicleEqumentConfigEntity> implements IEpsVehicleEqumentConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsVehicleEqumentConfigServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_VEHICLE_EQUMENT_CONFIG";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IEpsVehicleEqumentParConfigService epsVehicleEqumentParConfigService;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsVehicleEqumentConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsVehicleEqumentConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(EpsVehicleEqumentConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsVehicleEqumentConfigEntity> updateWrapper) {
        removeCache();
    }


    @Override
    public void beforeInsert(EpsVehicleEqumentConfigEntity entity) {
        validData(entity,false);
    }

    @Override
    public void beforeUpdate(EpsVehicleEqumentConfigEntity entity) {
        validData(entity,true);
    }

    private void validData(EpsVehicleEqumentConfigEntity model,boolean isUpdate) {
        if (StringUtils.isBlank(model.getEqumentNo())) {
            throw new InkelinkException("设备编号不能为空");
        }

        QueryWrapper<EpsVehicleEqumentConfigEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<EpsVehicleEqumentConfigEntity> lmp = qry.lambda();
        lmp.eq(EpsVehicleEqumentConfigEntity::getEqumentNo, model.getEqumentNo());
        if (isUpdate) {
            lmp.ne(EpsVehicleEqumentConfigEntity::getId, model.getId());
        }
        if (selectCount(qry) > 0) {
            throw new InkelinkException("设备编号“" + model.getEqumentNo() + "”已经配置！");
        }

        if (StringUtils.isNotBlank(model.getWoCode())) {
            QueryWrapper<EpsVehicleEqumentConfigEntity> qryWo = new QueryWrapper<>();
            LambdaQueryWrapper<EpsVehicleEqumentConfigEntity> lmpWo = qryWo.lambda();
            lmpWo.eq(EpsVehicleEqumentConfigEntity::getEqumentNo, model.getEqumentNo());
            if (isUpdate) {
                lmpWo.ne(EpsVehicleEqumentConfigEntity::getId, model.getId());
            }
            if (selectCount(qryWo) > 0) {
                throw new InkelinkException("工艺编号“" + model.getEqumentNo() + "”已经配置！");
            }
        }
    }


    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsVehicleEqumentConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsVehicleEqumentConfigEntity>> getDataFunc = obj -> {
                List<EpsVehicleEqumentConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsVehicleEqumentConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, 60 * 10);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 导出eswich地址
     */
    @Override
    public void exprotEswitch(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {

        List<EpsVehicleEqumentConfigEntity> equmentConfigs = getData(conditions, sorts, false);
        List<EpsVehicleEqumentParConfigEntity> config = epsVehicleEqumentParConfigService.getData(null);
        List<ImPortExcelModel> list = new ArrayList<>();
        for (EpsVehicleEqumentConfigEntity item : equmentConfigs) {
            list.add(new ImPortExcelModel(item.getEqumentNo(), "CollectorFlag", "CollectorFlag", "触发", "DbInt6", "DB3601.0"));
            list.add(new ImPortExcelModel(item.getEqumentNo(), "Identify;Sn", "Identify;Sn", "vin", "Dstring", "DB3601.2.17"));
            int init = 0;
            int initLenght = 0;
            for (EpsVehicleEqumentParConfigEntity par : config.stream().filter(s -> Objects.equals(s.getPrcEpsVehicleEqumentConfigId(), item.getId())).collect(Collectors.toList())) {

                initLenght = (46 * init) + 22;
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";Result", par.getEqumentParCode() + ";Result", "结果", "DbInt6", "DB3601." + initLenght + ".16"));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";UpLimit", par.getEqumentParCode() + ";UpLimit", "上线值", "Dfloat", "DB3601." + (18 + initLenght)));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";Standard", par.getEqumentParCode() + ";Standard", "", "Dfloat", "DB3601." + (22 + initLenght) + ".8"));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";Unit", par.getEqumentParCode() + ";Unit", "单位", "Dstring", "DB3601." + (32 + initLenght)));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";Name", par.getEqumentParCode() + ";Name", "工艺", "Dstring", "DB3601." + (34 + initLenght)));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";Value", par.getEqumentParCode() + ";Value", "值", "Dfloat", "DB3601." + (38 + initLenght)));
                list.add(new ImPortExcelModel(item.getEqumentNo(), par.getEqumentParCode() + ";DownLimit", par.getEqumentParCode() + ";DownLimit", "下线值", "Dfloat", "DB3601." + (42 + initLenght)));
                init += 1;
            }
        }

        List<ImPortExcelModel> datas = list;
        List<Map<String, Object>> excelDatas = new ArrayList<>();
        if (datas.size() != 0) {
            excelDatas = InkelinkExcelUtils.getListMap(datas);
            dealExcelDatas(excelDatas);
        }
        Map<String, String> fieldParam = new LinkedHashMap<>();
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getEqumentNo), "设备编码");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getPointName), "点位名称");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getPointCode), "点位编码");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getDes), "描述");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getP), "扫描速率(毫秒)");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getStatus), "状态(0 禁用 1 启用 2 模拟)");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getDbType), "数据类型");
        fieldParam.put(MpSqlUtils.getColumnName(ImPortExcelModel::getPointLamb), "点位表达式");
        InkelinkExcelUtils.exportByDc(fieldParam, excelDatas, fileName, response);
    }

}