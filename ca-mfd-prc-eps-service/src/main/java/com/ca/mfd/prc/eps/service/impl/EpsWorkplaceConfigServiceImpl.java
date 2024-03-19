package com.ca.mfd.prc.eps.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.eps.mapper.IEpsWorkplaceConfigMapper;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceConfigLogService;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceConfigService;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceMiddleService;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigEntity;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigLogEntity;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceMiddleEntity;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 开工检查项配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsWorkplaceConfigServiceImpl extends AbstractCrudServiceImpl<IEpsWorkplaceConfigMapper, EpsWorkplaceConfigEntity> implements IEpsWorkplaceConfigService {
    @Autowired
    private IEpsWorkplaceMiddleService epsWorkplaceMiddleService;
    @Autowired
    private IEpsWorkplaceConfigLogService epsWorkplaceConfigLogService;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IdentityHelper identityHelper;

    @Override
    public void beforeInsert(EpsWorkplaceConfigEntity entity) {
        validData(entity);
    }

    @Override
    public void beforeUpdate(EpsWorkplaceConfigEntity entity) {
        validData(entity);
    }

    private void validData(EpsWorkplaceConfigEntity model) {
        if (Strings.isNullOrEmpty(model.getWorkstationName())) {
            throw new InkelinkException("工位未传入");
        }
    }

    @Override
    public void setConfirmList(List<EpsWorkplaceConfigEntity> list) {
        List<EpsWorkplaceMiddleEntity> pmcWorkplaceMiddleList = new ArrayList<>();
        List<EpsWorkplaceConfigLogEntity> pmcWorkplaceLogList = new ArrayList<>();
        Date dt = new Date();
        Map<String, List<EpsWorkplaceConfigEntity>> workPlaceList = list.stream().map(s -> {
            EpsWorkplaceConfigEntity configEntity = new EpsWorkplaceConfigEntity();
            configEntity.setWorkstationCode(s.getWorkstationCode());
            configEntity.setWorkstationName(s.getWorkstationName());
            return configEntity;
        }).collect(Collectors.groupingBy(s -> s.getWorkstationCode() + "#" + s.getWorkstationName()));

        for (Map.Entry<String, List<EpsWorkplaceConfigEntity>> workPlace : workPlaceList.entrySet()) {
            if (workPlace.getValue() != null && workPlace.getValue().size() > 0) {
                EpsWorkplaceMiddleEntity middleEntity = new EpsWorkplaceMiddleEntity();
                middleEntity.setOperaDatetime(dt);
                middleEntity.setOperaUserId(identityHelper.getUserId());
                middleEntity.setOperaUserName(identityHelper.getUserName());
                middleEntity.setWorkstationCode(workPlace.getValue().get(0).getWorkstationCode());
                middleEntity.setWorkstationName(workPlace.getValue().get(0).getWorkstationName());
                middleEntity.setPrcEpsWorkplaceConfigId(IdGenerator.getId());
                middleEntity.setId(IdGenerator.getId());
                pmcWorkplaceMiddleList.add(middleEntity);

                for (EpsWorkplaceConfigEntity workplaceConfigEntity : list) {
                    //中间表插入数据
                    EpsWorkplaceConfigLogEntity logEntity = new EpsWorkplaceConfigLogEntity();
                    logEntity.setWorkstationCode(workplaceConfigEntity.getWorkstationCode());
                    logEntity.setWorkstationName(workplaceConfigEntity.getWorkstationName());
                    logEntity.setDetectionName(workplaceConfigEntity.getDetectionName());
                    logEntity.setPrcEpsWorkplaceMiddleId(middleEntity.getId());
                    logEntity.setOperaUserId(identityHelper.getUserId());
                    logEntity.setOperaUserName(identityHelper.getUserName());
                    pmcWorkplaceLogList.add(logEntity);
                }
            }
        }

        epsWorkplaceMiddleService.insertBatch(pmcWorkplaceMiddleList);
        epsWorkplaceConfigLogService.insertBatch(pmcWorkplaceLogList);
        epsWorkplaceConfigLogService.saveChange();
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> map = Maps.newHashMapWithExpectedSize(2);
        map.put("workstationCode", "工位编码");
        map.put("detectionName", "检测项目描述");
        return map;
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        List<PmWorkStationEntity> workplace = pmVersionProvider.getObjectedPm().getStations();
        List<Map<String, String>> list = datas.stream().map(o -> {
            Map<String, String> m = Maps.newHashMapWithExpectedSize(2);
            m.put("workstationCode", o.get("workstationCode"));
            m.put("detectionName", o.get("detectionName"));
            return m;
        }).distinct().collect(Collectors.toList());

        for (Map<String, String> category : list) {
            List<ConditionDto> dtos = new ArrayList<>();
            dtos.add(new ConditionDto("workstationCode", category.get("workstationCode"), ConditionOper.Equal));
            dtos.add(new ConditionDto("detectionName", category.get("detectionName"), ConditionOper.Equal));
            List<EpsWorkplaceConfigEntity> existDatas = this.getData(dtos);
            if (existDatas.size() > 0) {
                throw new InkelinkException("工位 “" + category.get("workstationCode") + "” 已经存在检测项为“" + category.get("detectionName") + "”的项目");
            }
        }

        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(getExcelColumnNames().get("workstationCode"), i + 1, data.get("workstationCode"), "");
            validExcelDataRequire(getExcelColumnNames().get("detectionName"), i + 1, data.get("detectionName"), "");
            validExcelDataMaxLength(getExcelColumnNames().get("workstationCode"), i + 1, data.get("workstationCode"), 300, "");
            validExcelDataMaxLength(getExcelColumnNames().get("detectionName"), i + 1, data.get("detectionName"), 300, "");

            List<PmWorkStationEntity> workElements = workplace.stream().filter(s -> StringUtils.equals(s.getWorkstationCode(), data.get("workstationCode"))).collect(Collectors.toList());
            if (workElements == null || workElements.isEmpty()) {
                throw new InkelinkException("工位不存在");
            } else {
                PmWorkStationEntity element = workElements.stream().findFirst().orElse(null);
                if (element != null) {
                    data.put("workstationCode", element.getWorkstationCode());
                }
            }
        }
    }
}