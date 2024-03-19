package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.dto.AssignJsonInfo;
import com.ca.mfd.prc.eps.dto.ModuleGluingJobDto;
import com.ca.mfd.prc.eps.dto.ModuleInBoxJobDto;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxLocationConfigEntity;
import com.ca.mfd.prc.eps.mapper.IEpsModuleInboxStrategyMapper;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxStrategyEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsModuleInboxLocationConfigService;
import com.ca.mfd.prc.eps.service.IEpsModuleInboxStrategyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @Description: 预成组入箱策略服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsModuleInboxStrategyServiceImpl extends AbstractCrudServiceImpl<IEpsModuleInboxStrategyMapper, EpsModuleInboxStrategyEntity> implements IEpsModuleInboxStrategyService {

    private final String cacheName = "PRC_PPS_MODULE_INBOX_STRATEGY";

    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private IEpsModuleInboxLocationConfigService epsModuleInboxLocationConfigService;
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<EpsModuleInboxStrategyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        //判断入箱策略是否已被使用
        QueryWrapper<EpsModuleInboxStrategyEntity> qry = new QueryWrapper<>();
        qry.lambda().in(EpsModuleInboxStrategyEntity::getId, idList).select(EpsModuleInboxStrategyEntity::getStrategySign);
        List<String> datas = selectList(qry).stream().map(EpsModuleInboxStrategyEntity::getStrategySign).collect(Collectors.toList());

        if (ppsOrderProvider.checkCharacteristic10(datas)) {
            throw new InkelinkException("删除的入箱策略已被使用不能被删除");
        }
        removeCache();
    }

    @Override
    public void afterInsert(EpsModuleInboxStrategyEntity model) {
        QueryWrapper<EpsModuleInboxStrategyEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleInboxStrategyEntity::getStrategySign, model.getStrategySign());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("策略标记%s已进行了配置", model.getStrategySign()));
        }
        assignJson(model);
        removeCache();
    }

    @Override
    public void afterUpdate(EpsModuleInboxStrategyEntity model) {
        QueryWrapper<EpsModuleInboxStrategyEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleInboxStrategyEntity::getStrategySign, model.getStrategySign())
                .ne(EpsModuleInboxStrategyEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("策略标记%s已进行了配置", model.getStrategySign()));
        }
        assignJson(model);
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsModuleInboxStrategyEntity> updateWrapper) {
        removeCache();
    }


    /**
     * 获取所有的数据
     *
     * @return List<EpsModuleInboxStrategyEntity>
     */
    @Override
    public List<EpsModuleInboxStrategyEntity> getAllDatas() {
        Function<Object, ? extends List<EpsModuleInboxStrategyEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

    /**
     * 启用策略
     *
     * @param id
     * @return
     */
    @Override
    public void enableStrategy(Long id) {
        EpsModuleInboxStrategyEntity data = get(id);

        UpdateWrapper<EpsModuleInboxStrategyEntity> upsetEnable = new UpdateWrapper<>();
        upsetEnable.lambda().set(EpsModuleInboxStrategyEntity::getIsEnable, false)
                .eq(EpsModuleInboxStrategyEntity::getPackModel, data.getPackModel());
        update(upsetEnable);

        UpdateWrapper<EpsModuleInboxStrategyEntity> upsetEnableId = new UpdateWrapper<>();
        upsetEnableId.lambda().set(EpsModuleInboxStrategyEntity::getIsEnable, true)
                .eq(EpsModuleInboxStrategyEntity::getId, id);
        update(upsetEnableId);
        removeCache();
    }

    private void assignJson(EpsModuleInboxStrategyEntity strategyInfo) {
        List<EpsModuleInboxLocationConfigEntity> locationConfigInfo = epsModuleInboxLocationConfigService.getData(null);
        List<AssignJsonInfo> assignJsonInfos = new ArrayList<>();
        // 封装位置对象(start)
        if (!StringUtils.isBlank(strategyInfo.getL1LineCode()) && !StringUtils.isBlank(strategyInfo.getL1ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL1LineCode());
            et.setModuleCode(strategyInfo.getL1ModuleCode());
            et.setLocation("L1");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL2LineCode()) && !StringUtils.isBlank(strategyInfo.getL2ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL2LineCode());
            et.setModuleCode(strategyInfo.getL2ModuleCode());
            et.setLocation("L2");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL3LineCode()) && !StringUtils.isBlank(strategyInfo.getL3ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL3LineCode());
            et.setModuleCode(strategyInfo.getL3ModuleCode());
            et.setLocation("L3");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL4LineCode()) && !StringUtils.isBlank(strategyInfo.getL4ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL4LineCode());
            et.setModuleCode(strategyInfo.getL4ModuleCode());
            et.setLocation("L4");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL5LineCode()) && !StringUtils.isBlank(strategyInfo.getL5ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL5LineCode());
            et.setModuleCode(strategyInfo.getL5ModuleCode());
            et.setLocation("L5");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL6LineCode()) && !StringUtils.isBlank(strategyInfo.getL6ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL6LineCode());
            et.setModuleCode(strategyInfo.getL6ModuleCode());
            et.setLocation("L6");
            assignJsonInfos.add(et);
        }


        if (!StringUtils.isBlank(strategyInfo.getL7LineCode()) && !StringUtils.isBlank(strategyInfo.getL7ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL7LineCode());
            et.setModuleCode(strategyInfo.getL7ModuleCode());
            et.setLocation("L7");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getL8LineCode()) && !StringUtils.isBlank(strategyInfo.getL8ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getL8LineCode());
            et.setModuleCode(strategyInfo.getL8ModuleCode());
            et.setLocation("L8");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR1LineCode()) && !StringUtils.isBlank(strategyInfo.getR1ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR1LineCode());
            et.setModuleCode(strategyInfo.getR1ModuleCode());
            et.setLocation("R1");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR2LineCode()) && !StringUtils.isBlank(strategyInfo.getR2ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR2LineCode());
            et.setModuleCode(strategyInfo.getR2ModuleCode());
            et.setLocation("R2");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR3LineCode()) && !StringUtils.isBlank(strategyInfo.getR3ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR3LineCode());
            et.setModuleCode(strategyInfo.getR3ModuleCode());
            et.setLocation("R3");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR4LineCode()) && !StringUtils.isBlank(strategyInfo.getR4ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR4LineCode());
            et.setModuleCode(strategyInfo.getR4ModuleCode());
            et.setLocation("R4");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR5LineCode()) && !StringUtils.isBlank(strategyInfo.getR5ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR5LineCode());
            et.setModuleCode(strategyInfo.getR5ModuleCode());
            et.setLocation("R5");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR6LineCode()) && !StringUtils.isBlank(strategyInfo.getR6ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR6LineCode());
            et.setModuleCode(strategyInfo.getR6ModuleCode());
            et.setLocation("R6");
            assignJsonInfos.add(et);
        }


        if (!StringUtils.isBlank(strategyInfo.getR7LineCode()) && !StringUtils.isBlank(strategyInfo.getR7ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR7LineCode());
            et.setModuleCode(strategyInfo.getR7ModuleCode());
            et.setLocation("R7");
            assignJsonInfos.add(et);
        }

        if (!StringUtils.isBlank(strategyInfo.getR8LineCode()) && !StringUtils.isBlank(strategyInfo.getR8ModuleCode())) {
            AssignJsonInfo et = new AssignJsonInfo();
            et.setLineCode(strategyInfo.getR8LineCode());
            et.setModuleCode(strategyInfo.getR8ModuleCode());
            et.setLocation("R8");
            assignJsonInfos.add(et);
        }

        for (AssignJsonInfo item : assignJsonInfos) {
            EpsModuleInboxLocationConfigEntity locationInfo = locationConfigInfo.stream().filter(c ->
                            StringUtils.equals(c.getLineCode(), item.getLineCode()))
                    .findFirst().orElse(null);
            if (locationInfo == null) {
                throw new InkelinkException("模组生产线体" + item.getLineCode() + "未找到对应的入箱配置");
            }
            item.setGummingWorkstation(locationInfo.getGelatinizeWorkstationCode());
            item.setInBoxWorkstation(locationInfo.getInboxWorkstationCode());
            item.setRouteSign(locationInfo.getRouteSign());
        }

        // 封装位置对象(end)
        if (assignJsonInfos.isEmpty()) {
            throw new InkelinkException("该数据位置集合信息未空");
        }
        List<String> signs = assignJsonInfos.stream().map(AssignJsonInfo::getRouteSign).distinct().collect(Collectors.toList());
        for (String rsign : signs) {
            strategyInfo.setRouteSign(strategyInfo.getRouteSign() + rsign);
        }
        List<String> lineCodes = assignJsonInfos.stream().map(AssignJsonInfo::getLineCode).distinct().collect(Collectors.toList());

        List<ModuleGluingJobDto> gluingJobs = new ArrayList<>();
        List<ModuleInBoxJobDto> inBoxJobs = new ArrayList<>();

        for (String lineCode : lineCodes) {
            StringBuilder gjob = new StringBuilder(StringUtils.EMPTY);
            List<AssignJsonInfo> infos = assignJsonInfos.stream().filter(c ->
                    StringUtils.equals(c.getLineCode(), lineCode)).collect(Collectors.toList());

            gjob.append(infos.stream().filter(c -> "L1".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L2".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L3".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L4".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L5".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L6".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L7".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "L8".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R1".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R2".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R3".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R4".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R5".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R6".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R7".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");
            gjob.append(infos.stream().filter(c -> "R8".equals(c.getLocation())).findFirst().orElse(null) != null ? "1" : "0");

            EpsModuleInboxLocationConfigEntity locationInfo = locationConfigInfo.stream().filter(c ->
                    StringUtils.equals(c.getLineCode(), lineCode)).findFirst().orElse(null);
            ModuleGluingJobDto jobDto = new ModuleGluingJobDto();
            jobDto.setWorkstationCode(locationInfo.getGelatinizeWorkstationCode());
            jobDto.setJob(String.valueOf(ConvertUtils.toInt16(ConvertUtils.binaryStringToByteArray(gjob.toString()), 0)));
            gluingJobs.add(jobDto);

            inBoxJobs = infos.stream().map(c -> {
                ModuleInBoxJobDto et = new ModuleInBoxJobDto();
                et.setLineCode(c.getLineCode());
                et.setWorkstationCode(locationInfo.getInboxWorkstationCode());
                et.setLocation(c.getLocation());
                et.setModuleCode(c.getModuleCode());
                return et;
            }).collect(Collectors.toList());
        }
        strategyInfo.setGluingJobJson(JsonUtils.toJsonString(gluingJobs));
        strategyInfo.setInBoxJson(JsonUtils.toJsonString(inBoxJobs));
    }

}