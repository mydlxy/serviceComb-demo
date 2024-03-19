package com.ca.mfd.prc.pm.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmSyncEntity;
import com.ca.mfd.prc.pm.mapper.IPmSyncMapper;
import com.ca.mfd.prc.pm.service.IPmSyncService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 数据同步缓存表服务实现
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Service
public class PmSyncServiceImpl extends AbstractCrudServiceImpl<IPmSyncMapper, PmSyncEntity> implements IPmSyncService {


    @Override
    public Map<Long, Long> getPrcIdAndCmcIdMapping(List<String> prcIds) {
        List<PmSyncEntity> pmSyncList = getData(Arrays.asList(
                new ConditionDto("prcId", String.join("|", prcIds), ConditionOper.In)));
        Map<Long,Long> target = new HashMap<>(pmSyncList.size());
        pmSyncList.forEach(e->{
            target.put(e.getPrcId(),e.getCmcId());
        });
        return target;
    }

    @Override
    public Map<Long, Long> getPrcIdAndCmcIdMapping() {
        List<PmSyncEntity> pmSyncList = getData(null);
        Map<Long,Long> target = new HashMap<>(pmSyncList.size());
        pmSyncList.forEach(e->{
            target.put(e.getPrcId(),e.getCmcId());
        });
        return target;
    }

    @Override
    public List<PmSyncEntity> getPmSyncsByCmcIds(List<String> cmcIds) {
        return getData(Arrays.asList(
                new ConditionDto("cmcId", String.join("|", cmcIds), ConditionOper.In)));
    }
}