package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.mapper.IPpsProductProcessMapper;
import com.ca.mfd.prc.pps.service.IPpsProductProcessAviService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inkelink ${email}
 * @Description: 工艺路径设置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PpsProductProcessServiceImpl extends AbstractCrudServiceImpl<IPpsProductProcessMapper, PpsProductProcessEntity> implements IPpsProductProcessService {

    private final String cacheName = "PRC_PPS_PRODUCT_PROCESS";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPpsProductProcessAviService ppsProductProcessAviService;

    /**
     * 清空缓存
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    /**
     * 更新默认路径
     */
    private void changeDefault(PpsProductProcessEntity model) {
        if (model.getIsDefault()) {
            LambdaUpdateWrapper<PpsProductProcessEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PpsProductProcessEntity::getIsDefault, false)
                    .ne(PpsProductProcessEntity::getId, model.getId())
                    .eq(PpsProductProcessEntity::getOrderCategory, model.getOrderCategory())
                    .eq(PpsProductProcessEntity::getOrderSign, model.getOrderSign());
            update(upset);
            //_ppsProductProcessDal.UpdateEntity(() => new PpsProductProcessInfo() { IsDefault = false }, o => o.Id != model.Id
            // && o.OrderCategory == model.OrderCategory && o.OrderType == model.OrderType);
        }
    }

    /**
     * 数据验证
     */
    private void valid(PpsProductProcessEntity model) {
        QueryWrapper<PpsProductProcessEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsProductProcessEntity::getProcessNo, model.getProcessNo())
                .eq(PpsProductProcessEntity::getVersion, model.getVersion())
                .ne(PpsProductProcessEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("已经存在“" + model.getProcessNo() + "[" + model.getVersion() + "]”的数据");
        }
    }

    @Override
    public void afterInsert(PpsProductProcessEntity model) {
        changeDefault(model);
        removeCache();
    }

    @Override
    public void afterUpdate(PpsProductProcessEntity model) {
        changeDefault(model);
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsProductProcessEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterDelete(Wrapper<PpsProductProcessEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsProductProcessEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(PpsProductProcessEntity model) {
        valid(model);
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsProductProcessEntity>
     */
    @Override
    public List<PpsProductProcessEntity> getAllDatas() {
        Function<Object, ? extends List<PpsProductProcessEntity>> getDataFunc = (obj) -> {
            List<PpsProductProcessEntity> datas = getData(new ArrayList<>());
            for (PpsProductProcessEntity data : datas) {
                data.setPpsProductProcessAviInfos(ppsProductProcessAviService.getByProductProcessId(data.getId()));
            }
            return datas;
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

    @Override
    public PpsProductProcessEntity getProcess(String orderCategory) {
        return getAllDatas().stream().filter(c -> c.getIsDefault() && StringUtils.equals(orderCategory, c.getOrderCategory())).findFirst().orElse(null);
    }

    /**
     * 获取工艺路径列表
     *
     * @param orderCategory
     * @param orderType
     * @return
     */
    @Override
    public List<PpsProductProcessEntity> getProcessList(String orderCategory, String orderType) {
        List<PpsProductProcessEntity> allPpsProductProcessList = getAllDatas();
        Stream<PpsProductProcessEntity> allPpsProductProcessStream = allPpsProductProcessList.stream();
        if (StringUtils.isNotBlank(orderCategory)) {
            allPpsProductProcessStream = allPpsProductProcessStream.filter(e -> orderCategory.equals(e.getOrderCategory()));
        }
        if (StringUtils.isNotBlank(orderType)) {
            allPpsProductProcessStream = allPpsProductProcessStream.filter(e -> orderType.equals(e.getOrderSign()));
        }
        return allPpsProductProcessStream.sorted(Comparator.comparing(PpsProductProcessEntity::getCreationDate)).collect(Collectors.toList());
    }
}