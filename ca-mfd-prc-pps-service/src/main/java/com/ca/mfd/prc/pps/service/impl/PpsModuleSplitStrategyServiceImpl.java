package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pps.dto.ModuleSpacerDto;
import com.ca.mfd.prc.pps.dto.ModuleStructDto;
import com.ca.mfd.prc.pps.dto.ModuleUnitDto;
import com.ca.mfd.prc.pps.entity.PpsModuleSplitStrategyEntity;
import com.ca.mfd.prc.pps.mapper.IPpsModuleSplitStrategyMapper;
import com.ca.mfd.prc.pps.service.IPpsModuleSplitStrategyService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
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
 * @Description: 电池结构配置服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsModuleSplitStrategyServiceImpl extends AbstractCrudServiceImpl<IPpsModuleSplitStrategyMapper, PpsModuleSplitStrategyEntity> implements IPpsModuleSplitStrategyService {

    private final String cacheName = "PRC_PPS_MODULE_SPLIT_STRATEGY";

    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsModuleSplitStrategyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsModuleSplitStrategyEntity model) {
        QueryWrapper<PpsModuleSplitStrategyEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleSplitStrategyEntity::getStrategySign, model.getStrategySign());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("策略%s已进行了配置", model.getStrategySign()));
        }

        List<ModuleStructDto> tempDatas = JsonUtils.parseArray(model.getStructContent(), ModuleStructDto.class);

        List<ModuleStructDto> datas = verify(tempDatas);
        model.setModuleTypeNum(datas.size());
        model.setModuleNum(datas.stream().mapToInt(ModuleStructDto::getModuleNum).sum());
        model.setStructContent(JsonUtils.toJsonString(datas));

        removeCache();
    }

    @Override
    public void afterUpdate(PpsModuleSplitStrategyEntity model) {
        QueryWrapper<PpsModuleSplitStrategyEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleSplitStrategyEntity::getStrategySign, model.getStrategySign())
                .ne(PpsModuleSplitStrategyEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("策略%s已进行了配置", model.getStrategySign()));
        }

        List<ModuleStructDto> tempDatas = JsonUtils.parseArray(model.getStructContent(), ModuleStructDto.class);

        List<ModuleStructDto> datas = verify(tempDatas);
        model.setModuleTypeNum(datas.size());
        model.setModuleNum(datas.stream().mapToInt(ModuleStructDto::getModuleNum).sum());
        model.setStructContent(JsonUtils.toJsonString(datas));
        PpsModuleSplitStrategyEntity oldModel = get(model.getId());
        model.setIsEnable(oldModel.getIsEnable());
        removeCache();
    }


    private List<ModuleStructDto> verify(List<ModuleStructDto> datas) {
        for (ModuleStructDto item : datas) {
            if (StringUtils.isBlank(item.getLineCode())) {
                throw new InkelinkException("模组列表中有线体编码为空的数据");
            }
            if (StringUtils.isBlank(item.getModuleCode())) {
                throw new InkelinkException("模组列表中有模组编码为空的数据");
            }

            if (item.getModuleNum() <= 0) {
                throw new InkelinkException(String.format("%s-%s列表中有模组数量不能小于0"
                        , item.getLineCode(), item.getModuleCode()));
            }

            if (datas.stream().filter(c -> StringUtils.equals(c.getLineCode(), item.getLineCode())
                    && StringUtils.equals(c.getModuleCode(), item.getModuleCode())).count() > 1) {
                throw new InkelinkException(String.format("%s-%s有重复了配置，请检测"
                        , item.getLineCode(), item.getModuleCode()));
            }

            //判断隔热垫数据是否正确
            if (item.getSpacers() == null || item.getSpacers().isEmpty()) {
                throw new InkelinkException(String.format("模组%s未配置隔热垫", item.getModuleCode()));
            }
            if (item.getSpacers().stream().filter(c -> StringUtils.isBlank(c.getSpacerCode()) || c.getSpacerNum() <= 0)
                    .findFirst().orElse(null) != null) {
                throw new InkelinkException(String.format("模组%s对应的隔热垫为空的数据，或隔热垫数量为0的数据", item.getModuleCode()));
            }

            Map<String, List<ModuleSpacerDto>> spacerTmps = item.getSpacers().stream().collect(Collectors.groupingBy(ModuleSpacerDto::getSpacerCode));
            List<ModuleSpacerDto> spacerGroups = new ArrayList<>();
            spacerTmps.forEach((key, val) -> {
                ModuleSpacerDto etgp = new ModuleSpacerDto();
                etgp.setSpacerCode(key);
                etgp.setSpacerNum(val == null ? 0 : val.size());
                spacerGroups.add(etgp);
            });
            ModuleSpacerDto spacerGroup = spacerGroups.stream().filter(c -> c.getSpacerNum() > 1).findFirst().orElse(null);
            if (spacerGroup != null) {
                throw new InkelinkException(String.format("模组%s配置的隔热垫编码%s有重复数据", item.getModuleCode(), spacerGroup.getSpacerCode()));
            }
            //判断小单元数据是否正确
            if (item.getUnits() == null || item.getUnits().isEmpty()) {
                throw new InkelinkException(String.format("模组%s未配置小单元", item.getModuleCode()));
            }

            if (item.getUnits().stream().filter(c -> StringUtils.isBlank(c.getUnitCode()) || c.getUnitNum() <= 0).findFirst().orElse(null) != null) {
                throw new InkelinkException(String.format("模组%s对应的小单元为空的数据，或小单元数量为0的数据", item.getModuleCode()));
            }

            Map<String, List<ModuleUnitDto>> unitCodeTmps = item.getUnits().stream().collect(Collectors.groupingBy(ModuleUnitDto::getUnitCode));
            List<ModuleSpacerDto> unitCodeGroups = new ArrayList<>();
            unitCodeTmps.forEach((key, val) -> {
                ModuleSpacerDto etgp = new ModuleSpacerDto();
                etgp.setSpacerCode(key);
                etgp.setSpacerNum(val == null ? 0 : val.size());
                unitCodeGroups.add(etgp);
            });

            ModuleSpacerDto unitCodeGroup = unitCodeGroups.stream().filter(c -> c.getSpacerNum() > 1).findFirst().orElse(null);
            if (unitCodeGroup != null) {
                throw new InkelinkException(String.format("模组%s配置的小单元编码%s有重复数据", item.getModuleCode()
                        , unitCodeGroup.getSpacerCode()));
            }
            if (item.getUnits().stream().filter(c -> StringUtils.isBlank(c.getUnitCode()) || c.getUnitNum() <= 0).findFirst().orElse(null) != null) {
                throw new InkelinkException(String.format("模组%s对应的小单元为空的数据，或小单元数量为0的数据", item.getModuleCode()));
            }

            for (ModuleUnitDto uitem : item.getUnits()) {
                if (StringUtils.isBlank(uitem.getCellCode())) {
                    throw new InkelinkException(String.format("小单元%s对应的电芯未配置", uitem.getCellCode()));
                }
                if (uitem.getCellNum() <= 0) {
                    throw new InkelinkException(String.format("小单元%s对应的电芯数量配置有误", uitem.getCellCode()));
                }
                if (StringUtils.isBlank(uitem.getUpBoardCode())) {
                    throw new InkelinkException(String.format("小单元%s对应的上端板编码未配置", uitem.getCellCode()));
                }
                uitem.setUpBoardNum(1);
                if (StringUtils.isBlank(uitem.getDownBoardCode())) {
                    throw new InkelinkException(String.format("小单元%s对应的下端板编码未配置", uitem.getCellCode()));
                }
                uitem.setDownBoardNum(1);
            }
        }

        return datas;
    }

    @Override
    public void afterUpdate(Wrapper<PpsModuleSplitStrategyEntity> updateWrapper) {
        removeCache();
    }


    /**
     * 获取所有的数据
     *
     * @return List<PpsModuleSplitStrategyEntity>
     */
    @Override
    public List<PpsModuleSplitStrategyEntity> getAllDatas() {
        Function<Object, ? extends List<PpsModuleSplitStrategyEntity>> getDataFunc = (obj) -> {
            List<PpsModuleSplitStrategyEntity> datas = getData(new ArrayList<>());
            if (datas != null) {
                for (PpsModuleSplitStrategyEntity item : datas) {
                    item.setModules(JsonUtils.parseArray(item.getStructContent(), ModuleStructDto.class));
                }
            }
            return datas;
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
        PpsModuleSplitStrategyEntity data = get(id);

        UpdateWrapper<PpsModuleSplitStrategyEntity> upsetEnable = new UpdateWrapper<>();
        upsetEnable.lambda().set(PpsModuleSplitStrategyEntity::getIsEnable, false)
                .eq(PpsModuleSplitStrategyEntity::getPackModel, data.getPackModel());
        update(upsetEnable);

        UpdateWrapper<PpsModuleSplitStrategyEntity> upsetEnableId = new UpdateWrapper<>();
        upsetEnableId.lambda().set(PpsModuleSplitStrategyEntity::getIsEnable, true)
                .eq(PpsModuleSplitStrategyEntity::getId, data.getId());
        update(upsetEnableId);
        removeCache();
    }

}