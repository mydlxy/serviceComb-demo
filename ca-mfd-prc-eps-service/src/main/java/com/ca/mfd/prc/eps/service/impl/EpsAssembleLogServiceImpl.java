package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.MapHelper;
import com.ca.mfd.prc.eps.dto.GetAssembleDataInfo;
import com.ca.mfd.prc.eps.entity.EpsAssembleLogEntity;
import com.ca.mfd.prc.eps.mapper.IEpsAssembleLogMapper;
import com.ca.mfd.prc.eps.service.IEpsAssembleLogService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 装配单日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsAssembleLogServiceImpl extends AbstractCrudServiceImpl<IEpsAssembleLogMapper, EpsAssembleLogEntity> implements IEpsAssembleLogService {
    private static final int MAX_ARR_LENGTH = 10;
    private static final String CACHE_NAME = "PRC_EPS_ASSEMBLE_LOG";
    private static final Object LOCK_OBJ = new Object();
    @Autowired
    IEpsAssembleLogMapper epsAssembleLogDao;
    @Autowired
    private LocalCache localCache;

    /**
     * 获取装配指示详细数据
     */
    @Override
    public List<EpsAssembleLogEntity> getAssembleLogData(String sn) {
        List<EpsAssembleLogEntity> datas = localCache.getObject(CACHE_NAME + sn);
        if (datas == null || datas.isEmpty()) {
            synchronized (LOCK_OBJ) {
                datas = localCache.getObject(CACHE_NAME + sn);
                if (datas == null || datas.isEmpty()) {
                    datas = getListBySn(sn);
                    localCache.addObject(CACHE_NAME + sn, datas, -1);
                }
            }
        }
        return datas;
    }

    private List<EpsAssembleLogEntity> getListBySn(String sn) {
        QueryWrapper<EpsAssembleLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsAssembleLogEntity::getSn, sn);
        return selectList(qry);
    }

    /**
     * 添加装配单日志
     */
    @Override
    public void addInsertLog(String sn, String tplCode) {
        QueryWrapper<EpsAssembleLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsAssembleLogEntity::getSn, sn).eq(EpsAssembleLogEntity::getTplCode, tplCode);
        EpsAssembleLogEntity info = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (info != null) {
            UpdateWrapper<EpsAssembleLogEntity> delWrp = new UpdateWrapper<>();
            delWrp.lambda().eq(EpsAssembleLogEntity::getSn, sn).eq(EpsAssembleLogEntity::getTplCode, tplCode);
            delete(delWrp, false);
            localCache.removeObject(CACHE_NAME + sn);
        }

        Map<String, Object> asmp = Maps.newHashMapWithExpectedSize(2);
        asmp.put("sn", sn);
        asmp.put("tplCode", tplCode);
        List<GetAssembleDataInfo> datas = epsAssembleLogDao.getAssemble(asmp);
        datas.sort(Comparator.comparing(GetAssembleDataInfo::getDisplayNo));

        List<EpsAssembleLogEntity> list = new ArrayList<>();

        for (int i = 1; i <= datas.size(); i++) {
            HashMap data = JsonUtils.parseObject(JsonUtils.toJsonString(datas.get(i - 1)), HashMap.class);

            for (int j = 1; j <= MAX_ARR_LENGTH; j++) {
                if (!StringUtils.isBlank(MapHelper.getValueString(data, "V_C" + j))
                        && !StringUtils.isBlank(MapHelper.getValueString(data, "W_C" + j))
                ) {
                    EpsAssembleLogEntity et = new EpsAssembleLogEntity();
                    et.setDisplayNo(i + j);
                    et.setKeyContent(MapHelper.getValueString(data, "V_C" + j));
                    et.setKeyTitle(MapHelper.getValueString(data, "H_C" + j));
                    et.setSn(sn);
                    et.setTplCode(tplCode);
                    et.setWorkstationName(MapHelper.getValueString(data, "W_C" + j));
                    list.add(et);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            insertBatch(list);
        }
    }
}