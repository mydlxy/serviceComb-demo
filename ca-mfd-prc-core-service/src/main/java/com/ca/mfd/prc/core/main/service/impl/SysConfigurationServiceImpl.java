package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;
import com.ca.mfd.prc.core.main.mapper.ISysConfigurationMapper;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统配置
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Service
public class SysConfigurationServiceImpl extends AbstractCrudServiceImpl<ISysConfigurationMapper, SysConfigurationEntity>
        implements ISysConfigurationService {

    private static final Object lockObj = new Object();
    private final String cacheName = "PRC_SYS_CONFIGURATION";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<SysConfigurationEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(SysConfigurationEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(SysConfigurationEntity model) {
        removeCache();
    }
    @Override
    public void beforeInsert(SysConfigurationEntity model) {
        check(model);
    }
    @Override
    public void beforeUpdate(SysConfigurationEntity model) {
        check(model);
    }

    @Override
    public void afterUpdate(Wrapper<SysConfigurationEntity> updateWrapper) {
        removeCache();
    }


    /**
     * 获取Combo列表
     *
     * @param category 分类key
     */
    @Override
    public List<ComboInfoDTO> getComboDatasNoCache(String category) {
        List<ConditionDto> list = new ArrayList<>();
        list.add(new ConditionDto("CATEGORY", category, ConditionOper.Equal));
        List<SysConfigurationEntity> lst = getData(list);
        if (lst == null || lst.size() == 0) {
            return new ArrayList<>();
        }
        lst.sort(Comparator.comparing(SysConfigurationEntity::getDisplayNo));

        return lst.stream()
                .sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo))
                .map(c -> new ComboInfoDTO(c.getText(), c.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 获取Combo列表
     *
     * @param category 分类名称
     * @return 下拉集合模型
     */
    @Override
    public List<ComboInfoDTO> getComboDatas(String category) {
        return getComboDatas(category, "", false);
    }

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @return 下拉集合模型
     */
    @Override
    public List<ComboInfoDTO> getComboDatas(String category, String emptyText) {
        return getComboDatas(category, emptyText, false);
    }

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @param isHide    是否显示
     * @return 下拉集合模型
     */
    @Override
    public List<ComboInfoDTO> getComboDatas(String category, String emptyText, Boolean isHide) {
        Stream<SysConfigurationEntity> source = getAllDatas().stream().filter(c -> StringUtils.equals(c.getCategory(), category));
        if (isHide != null) {
            source = source.filter(o -> o.getIsHide().equals(isHide));
        }
        List<ComboInfoDTO> list = source.sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo))
                .map(c -> new ComboInfoDTO(c.getText(), c.getValue())).collect(Collectors.toList());
        if (!StringUtils.isBlank(emptyText)) {
            list.add(0, new ComboInfoDTO(emptyText, ""));
        }
        return list;
    }

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     */
    @Override
    public Map<String, String> getSysConfigurationMaps(String category) {
        List<SysConfigurationEntity> lst = getSysConfigurations(category);
        Map<String, String> mp = new HashMap<>(lst.size());
        lst.forEach(c -> {
            mp.put(c.getText(), c.getValue());
        });
        return mp;
    }

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     */
    @Override
    public List<SysConfigurationEntity> getSysConfigurations(String category) {
        List<ConditionDto> list = new ArrayList<>();
        list.add(new ConditionDto("CATEGORY", category, ConditionOper.Equal));
        List<SysConfigurationEntity> lst = getAllDatas();
        if (lst == null || lst.size() == 0) {
            return new ArrayList<>();
        }
        lst =
                lst.stream()
                        .filter(c -> c.getCategory().equals(category) && c.getIsHide() == false)
                        .collect(Collectors.toList());
        if (lst == null || lst.size() == 0) {
            return new ArrayList<>();
        }
        return lst;
    }

    @Override
    public List<SysConfigurationEntity> getAllDatas() {
        List<SysConfigurationEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public String getConfiguration(String key, String category) {
        String empty = "WebConfig";
        if (empty.equals(category)) {
        }
        List<SysConfigurationEntity> listall = getAllDatas();
        SysConfigurationEntity entity = listall.stream().filter(c -> c.getCategory().equals(category)
                && c.getValue().equals(key)).findFirst().orElse(null);
        if (entity == null) {
            return "";
        } else {
            return entity.getText();
        }
    }

    /**
     * 根据参数类型获取参数值（val模糊查询）
     *
     * @param category 分类key
     * @param valLike  val模糊查询
     */
    @Override
    public List<SysConfigurationEntity> getSysConfigurationsLike(String category, String valLike) {
        //.Table.Where(s=>s.Category== "EnvironmentaCrad" && s.Value.Contains("Condition04"))
        QueryWrapper<SysConfigurationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysConfigurationEntity::getCategory, category).like(SysConfigurationEntity::getValue, valLike);
        return selectList(qry);
    }

    @Override
    public List<ComboInfoDTO> getAllAppId() {
        String category = "appid";
        List<SysConfigurationEntity> sysConfigurationEntities = getAllDatas();
        Stream<SysConfigurationEntity> source = sysConfigurationEntities.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getCategory(), category) && c.getIsHide().equals(false));
        List<ComboInfoDTO> list = source.sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo))
                .map(c -> new ComboInfoDTO(c.getText(), c.getValue())).collect(Collectors.toList());
        return list;
    }

    @Override
    public String getServerUrl(String serverName) {
        return null;
    }

    @Override
    public List<ComboInfoDTO> getConfigurationByCategoryToList(String category) {
        QueryWrapper<SysConfigurationEntity> qry = new QueryWrapper<>();
        if (StringUtils.isBlank(category)) {
            category = "WebConfig";
        }
        qry.lambda().eq(SysConfigurationEntity::getCategory, category);
        List<SysConfigurationEntity> list = selectList(qry);
        List<ComboInfoDTO> comboInfoDtoList = list.stream().sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo))
                .map(c -> new ComboInfoDTO(c.getText(), c.getValue())).collect(Collectors.toList());
        return comboInfoDtoList;
    }

    @Override
    public List<ComboInfoDTO> getCategoryList(String category) {
        Stream<SysConfigurationEntity> source = getAllDatas().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getCategory(), category) && c.getIsHide().equals(false));
        List<ComboInfoDTO> list = source.sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo))
                .map(c -> new ComboInfoDTO(c.getText(), c.getValue())).collect(Collectors.toList());
        return list;
    }

    @Override
    public String getRmoteConfiguration(String key, String category) {
        QueryWrapper<SysConfigurationEntity> qry = new QueryWrapper<>();
        if (StringUtils.isBlank(category)) {
            category = "WebConfig";
        }
        qry.lambda().eq(SysConfigurationEntity::getValue, key);
        qry.lambda().eq(SysConfigurationEntity::getCategory, category);
        SysConfigurationEntity entity = selectList(qry).stream().findFirst().orElse(null);
        if (entity == null) {
            return "";
        } else {
            return entity.getText();
        }
    }

    @Override
    public List<ComboInfoDTO> getSysConfigurationsGroupBycategory() {
        List<ComboInfoDTO> list = new ArrayList<>();
        Map<String, List<SysConfigurationEntity>> map = getAllDatas().stream()
                .collect(Collectors.groupingBy(SysConfigurationEntity::getCategory));
        map.entrySet().forEach(entry -> {
            ComboInfoDTO comboInfoDTO = new ComboInfoDTO();
            comboInfoDTO.setText(entry.getKey());
            comboInfoDTO.setValue(entry.getKey());
            list.add(comboInfoDTO);
        });
        return list;
    }

    @Override
    public ResultVO updateBycategory(String value, String category, String text) {
        LambdaUpdateWrapper<SysConfigurationEntity> upset1 = new LambdaUpdateWrapper();
        upset1.eq(SysConfigurationEntity::getValue, value);
        upset1.eq(SysConfigurationEntity::getCategory, category);
        upset1.set(SysConfigurationEntity::getText, text);
        boolean upset = update(upset1);
        saveChange();
        return new ResultVO<Boolean>().ok(upset);
    }

    @Override
    public List<SysConfigurationEntity> getCategoryDataList(String category) {
        List<SysConfigurationEntity> comboInfos = getAllDatas().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getCategory(), category) && c.getIsHide().equals(false)).sorted(Comparator.comparing(SysConfigurationEntity::getDisplayNo)).collect(Collectors.toList());
        return comboInfos;
    }

    private void check(SysConfigurationEntity model) {
        Long count = getRecordCount(model);
        if (count > 0) {
            throw new InkelinkException("该分类已存在");
        }
    }

    private Long getRecordCount(SysConfigurationEntity model) {
        QueryWrapper<SysConfigurationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysConfigurationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(SysConfigurationEntity::getCategory, model.getCategory());
        lambdaQueryWrapper.eq(SysConfigurationEntity::getValue, model.getValue());
        lambdaQueryWrapper.eq(SysConfigurationEntity::getText, model.getText());
        lambdaQueryWrapper.eq(SysConfigurationEntity::getIsDelete, 0);
        lambdaQueryWrapper.ne(SysConfigurationEntity::getId,model.getId());
        return selectCount(queryWrapper);
    }
}