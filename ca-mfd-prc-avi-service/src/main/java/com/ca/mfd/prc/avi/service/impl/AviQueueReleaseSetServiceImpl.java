package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.avi.dto.AviQueueItems;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseSetEntity;
import com.ca.mfd.prc.avi.mapper.IAviQueueReleaseSetMapper;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.avi.remote.app.core.sys.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseSetService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 队列发布配置表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviQueueReleaseSetServiceImpl extends AbstractCrudServiceImpl<IAviQueueReleaseSetMapper, AviQueueReleaseSetEntity> implements IAviQueueReleaseSetService {

    private static final Logger logger = LoggerFactory.getLogger(AviQueueReleaseSetServiceImpl.class);
    private static final String cacheName = "PRC_AVI_QUEUE_RELEASE_SET";
    private static final Object lockObj = new Object();
    @Autowired
    SysSequenceNumberProvider sysSequenceNumberProvider;
    @Autowired
    PmVersionProvider pmVersionProvider;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AviQueueReleaseSetEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AviQueueReleaseSetEntity model) {
        removeCache();
        addSeqConfig(model);
    }

    @Override
    public void afterUpdate(AviQueueReleaseSetEntity model) {
        removeCache();
        addSeqConfig(model);
    }

    @Override
    public void beforeInsert(AviQueueReleaseSetEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviQueueReleaseSetEntity model) {
        valid(model);
    }

    private void valid(AviQueueReleaseSetEntity model) {
        if (StringUtils.isBlank(model.getAviCode())) {
            throw new InkelinkException("AVI信息未传入");
        }
        if (StringUtils.isBlank(model.getQueueCode())) {
            throw new InkelinkException("请输入队列代码");
        }
        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo != null) {
            model.setAviName(aviInfo.getAviName());
        }
        Long number = getQueueReleaseNumber(model.getQueueCode(), model.getId());
        if (number > 0) {
            throw new InkelinkException("该站点已存在队列代码:【" + model.getQueueCode() + "】");
        }
    }

    private Long getQueueReleaseNumber(String queueCode, Long id) {
        QueryWrapper<AviQueueReleaseSetEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseSetEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseSetEntity::getQueueCode, queueCode);
        //lambdaQueryWrapper.eq(AviQueueReleaseSetEntity::getAviCode, aviCode);
        lambdaQueryWrapper.ne(AviQueueReleaseSetEntity::getId, id);
        return selectCount(queryWrapper);
    }


    @Override
    public void afterUpdate(Wrapper<AviQueueReleaseSetEntity> updateWrapper) {
        removeCache();
    }


    private void addSeqConfig(AviQueueReleaseSetEntity model) {
        Long number = getQueueReleaseNumber(model.getQueueCode(), model.getId());
        if (number > 0) {
            throw new InkelinkException("该站点已存在队列代码:【" + model.getQueueCode() + "】");
        }
        String aviQueueSeqCodes = "AviQueue_" + model.getAviCode() + "_" + model.getQueueCode() + "_Seq";
        //判断是否已经配置了工单生成规则
        SysSequenceNumberEntity sysSequenceNumbers = sysSequenceNumberProvider.getSysSequenceInfoByType(aviQueueSeqCodes);
        if (sysSequenceNumbers == null) {
            SysSequenceNumberEntity info = new SysSequenceNumberEntity();
            info.setSequenceType(aviQueueSeqCodes);
            info.setSequencenumberLen(9);
            info.setMinValue(1);
            info.setMaxValue(999999999);
            info.setResetType("scope");
            info.setCurMaxDate(new Date());
            sysSequenceNumberProvider.insert(info);
        }
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<AviQueueReleaseSetEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<AviQueueReleaseSetEntity>> getDataFunc = (obj) -> {
                List<AviQueueReleaseSetEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<AviQueueReleaseSetEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }


    @Override
    public List<ComboInfoDTO> getReSetQueueList() {
        List<AviQueueReleaseSetEntity> lst = getData(null);
        if (lst == null || lst.size() == 0) {
            return new ArrayList<>();
        }
        return lst.stream()
                .sorted(Comparator.comparing(AviQueueReleaseSetEntity::getId))
                .map(c -> new ComboInfoDTO(c.getQueueName(), c.getQueueCode()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> map = Maps.newHashMapWithExpectedSize(2);
        map.put("queueCode", "队列代码");
        map.put("queueName", "代码描述");
        map.put("aviCode", "AVI站点代码");
        map.put("plcIp", "接收PLC");
        map.put("plcMode", "接收PLC类型");
        map.put("dbName", "接收DB");
        return map;
    }

    /**
     * 验证导入的数据
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        //验证必填
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(getExcelColumnNames().get("queueCode"), i + 1, data.get("queueCode"), "");
            validExcelDataRequire(getExcelColumnNames().get("queueName"), i + 1, data.get("queueName"), "");
            validExcelDataRequire(getExcelColumnNames().get("aviCode"), i + 1, data.get("aviCode"), "");
            validExcelDataUnique(datas, "Code", "queueCode");
        }
    }

    @Override
    public void saveExcelData(List<AviQueueReleaseSetEntity> entities) {
        List<AviQueueReleaseSetEntity> infos = new ArrayList<>();
        if (entities.stream().anyMatch(s -> StringUtils.isBlank(s.getAviCode()))) {
            throw new InkelinkException("队列代码不能为空");
        }
        String codes = StringUtils.join(entities.stream().map(AviQueueReleaseSetEntity::getQueueCode).collect(Collectors.toList()), "|");
        List<PmAviEntity> aviInfo = pmVersionProvider.getObjectedPm().getAvis();
        List<AviQueueItems> names = this.getAllDatas().stream().map(s -> {
            AviQueueItems et = new AviQueueItems();
            et.setId(s.getId());
            et.setQueueCode(s.getQueueCode());
            et.setQueueName(s.getQueueName());
            return et;
        }).collect(Collectors.toList());
        for (AviQueueReleaseSetEntity item : entities) {
            if (names.stream().anyMatch(s -> StringUtils.equals(s.getQueueCode(), item.getQueueCode()) && StringUtils.equals(s.getQueueName(), item.getQueueName()))) {
                Long tempid = Objects.requireNonNull(names.stream().filter(s -> StringUtils.equals(s.getQueueCode(), item.getQueueCode()) && StringUtils.equals(s.getQueueName(), item.getQueueName())).findFirst().orElse(null)).getId();
                item.setId(tempid);
                UpdateWrapper<AviQueueReleaseSetEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<AviQueueReleaseSetEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getQueueCode, item.getQueueCode());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getQueueName, item.getQueueName());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getAviCode, item.getAviCode());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getAviName, item.getAviName());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getPlcIp, item.getPlcIp());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getPlcMode, item.getPlcMode());
                lambdaUpdateWrapper.set(AviQueueReleaseSetEntity::getDbName, item.getDbName());
                lambdaUpdateWrapper.eq(AviQueueReleaseSetEntity::getId, item.getId());
            } else {
                infos.add(item);
            }
        }
        this.insertBatch(infos);
        saveChange();
    }

}