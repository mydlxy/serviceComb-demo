package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.core.main.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.core.main.entity.SysSnConfigEntity;
import com.ca.mfd.prc.core.main.mapper.ISysSnConfigMapper;
import com.ca.mfd.prc.core.main.service.ISysSequenceNumberService;
import com.ca.mfd.prc.core.main.service.ISysSnConfigService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 唯一码配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysSnConfigServiceImpl extends AbstractCrudServiceImpl<ISysSnConfigMapper, SysSnConfigEntity> implements ISysSnConfigService {

    private static final Object lockObj = new Object();
    private final String cacheName = "PRC_SYS_SN_CONFIG";
    @Autowired
    ISysSequenceNumberService sysSequenceNumberService;
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterInsert(SysSnConfigEntity model) {
        insertSeq(model);
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterUpdate(SysSnConfigEntity model) {
        updateSeq(model);
        removeCache();
    }

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @return 唯一码
     */
    @Override
    public String createSn(String category) {
        return this.createSn(category, null);
    }


    /**
     * 创建唯一码
     *
     * @param category 分类
     * @param para     参数
     * @return 唯一码
     */
    @Override
    public String createSn(String category, Map<String, String> para) {
        String sn = "";
        List<SysSnConfigEntity> snConfigs = getAllDatas().stream()
                .filter(o -> StringUtils.equals(o.getCategory(), category)).collect(Collectors.toList());
        for (SysSnConfigEntity snConfig : snConfigs) {
            String value = "";
            //顺序号
            if (StringUtils.equals(snConfig.getModel(), "1")) {
                value = sysSequenceNumberService.getSeqNum("SN_" + category);
            }
            //固定值
            if (StringUtils.equals(snConfig.getModel(), "4")) {
                {
                    value = snConfig.getParam1();
                }
            }
            //时间
            if (StringUtils.equals(snConfig.getModel(), "5")) {
                if (StringUtils.isNotBlank(snConfig.getParam1())) {
                    value = DateUtils.format(new Date(), snConfig.getParam1());
                } else {
                    value = DateUtils.format(new Date(), DateUtils.DATE_PATTERN_C);
                }
            }
            //动态参数
            if (StringUtils.equals(snConfig.getModel(), "8")) {
                if (para == null || !para.containsKey(snConfig.getParam1())) {
                    throw new InkelinkException("未找到动态参数" + snConfig.getParam1());
                }
                value = para.get(snConfig.getParam1());
            }
            //当前天数
            if ("9".equals(snConfig.getModel())) {
                String days = String.valueOf(getDayOfyear());
                value = StringUtils.leftPad(days, 3, "0");
            }
            value = StringUtils.isNotBlank(value) ? value : "";
            if (snConfig.getLength() != 0) {
                //如果长度超出设定值，截位
                if (value.length() > snConfig.getLength()) {
                    value = value.substring(0, snConfig.getLength());
                }
                //如果长度小于设定值，左补0
                if (value.length() < snConfig.getLength()) {
                    value = StringUtils.leftPad(value, snConfig.getLength(), "0");
                }
            }
            sn += value;
            if (StringUtils.isBlank(sn)) {
                throw new InkelinkException("唯一码" + category + "规则未配置");
            }
        }
        return sn;
    }

    private Integer getDayOfyear() {
        Calendar ca = Calendar.getInstance();
        return ca.get(Calendar.DAY_OF_YEAR);
    }

    private List<SysSnConfigEntity> getAllDatas() {
        List<SysSnConfigEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = this.getData(null).stream().sorted(Comparator.comparing(SysSnConfigEntity::getDisplayNo))
                            .collect(Collectors.toList());
                    localCache.addObject(cacheName, datas, -1);
                }
            }
        }
        return datas;
    }


    /**
     * 添加编号规则
     *
     * @param seqDatas
     */
    @Override
    public void addSeqConfig(List<SysSnConfigEntity> seqDatas) {
        if (seqDatas == null || seqDatas.size() == 0) {
            return;
        }
        for (SysSnConfigEntity seqkey : seqDatas) {
            if (StringUtils.isNotBlank(seqkey.getCategory())) {
                QueryWrapper<SysSnConfigEntity> snQry = new QueryWrapper<>();
                snQry.lambda().eq(SysSnConfigEntity::getCategory, seqkey.getCategory());
                SysSnConfigEntity seqinfo = getTopDatas(1, snQry).stream().findFirst().orElse(null);
                if (seqinfo == null) {
                    insert(seqkey);
                }
            }
        }
    }

    /**
     * 删除
     *
     * @param categorys
     */
    @Override
    public void deleteByCategory(List<String> categorys) {
        if (categorys == null || categorys.size() == 0) {
            return;
        }
        UpdateWrapper<SysSnConfigEntity> delUp = new UpdateWrapper<>();
        delUp.lambda().in(SysSnConfigEntity::getCategory, categorys);
        delete(delUp);
    }

    /**
     * 插入唯一编码配置
     *
     * @param model
     */
    private void insertSeq(SysSnConfigEntity model) {
        String sequenceType = "SN_" + model.getCategory();
        if ("1".equals(model.getModel())) {
            if (StringUtil.isNullOrEmpty(model.getParam1())) {
                model.setParam1("scope");
            }
            SysSequenceNumberEntity data = sysSequenceNumberService.getData(null).stream().filter(o -> o.getSequenceType().equals(sequenceType)).findFirst().orElse(null);
            if (data == null) {
                SysSequenceNumberEntity sequenceEntity = new SysSequenceNumberEntity();
                sequenceEntity.setSequenceType(sequenceType);
                sequenceEntity.setResetType(model.getParam1());
                sequenceEntity.setCurMaxDate(new Date());
                sequenceEntity.setCurMaxSequenceNumber(0);
                sequenceEntity.setMaxValue(Integer.parseInt(String.format("%0" + model.getLength() + "d", 0).replaceAll("0", "9")));
                sequenceEntity.setMinValue(1);
                sequenceEntity.setSequencenumberLen(model.getLength());
                sysSequenceNumberService.insert(sequenceEntity);
            } else {
                data.setSequencenumberLen(model.getLength());
                data.setCurMaxDate(new Date());
                data.setCurMaxSequenceNumber(0);
                data.setResetType(model.getParam1());
                data.setMaxValue(Integer.parseInt(String.format("%0" + model.getLength() + "d", 0).replaceAll("0", "9")));
                sysSequenceNumberService.update(data);
            }
        }
    }

    private void updateSeq(SysSnConfigEntity model) {
        String sequenceType = "SN_" + model.getCategory();
        if ("1".equals(model.getModel())) {
            if (StringUtil.isNullOrEmpty(model.getParam1())) {
                model.setParam1("scope");
            }
            SysSequenceNumberEntity data = sysSequenceNumberService.getData(null).stream().filter(o -> o.getSequenceType().equals(sequenceType)).findFirst().orElse(null);
            if (data == null) {
                SysSequenceNumberEntity sequenceEntity = new SysSequenceNumberEntity();
                sequenceEntity.setSequenceType(sequenceType);
                sequenceEntity.setResetType(model.getParam1());
                sequenceEntity.setCurMaxDate(new Date());
                sequenceEntity.setCurMaxSequenceNumber(0);
                sequenceEntity.setMaxValue(Integer.parseInt(String.format("%0" + model.getLength() + "d", 0).replaceAll("0", "9")));
                sequenceEntity.setMinValue(1);
                sequenceEntity.setSequencenumberLen(model.getLength());
                sysSequenceNumberService.insert(sequenceEntity);
            } else {
                if (!Objects.equals(data.getSequencenumberLen(), model.getLength())) {
                    data.setSequencenumberLen(model.getLength());
                    data.setResetType(model.getParam1());
                    data.setMaxValue(Integer.parseInt(String.format("%0" + model.getLength() + "d", 0).replaceAll("0", "9")));
                    sysSequenceNumberService.update(data);
                }
            }
        }
    }
}
