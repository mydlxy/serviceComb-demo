package com.ca.mfd.prc.rc.rcps.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteCacheLogMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteCacheLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 路由出车记录服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteCacheLogServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteCacheLogMapper, RcPsRouteCacheLogEntity> implements IRcPsRouteCacheLogService {

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("beginDt") && data.getOrDefault("beginDt", null) != null) {
                data.put("beginDt", DateUtils.format((Date) data.get("beginDt"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("endDt") && data.getOrDefault("endDt", null) != null) {
                data.put("endDt", DateUtils.format((Date) data.get("endDt"), DateUtils.DATE_TIME_PATTERN));
            }
        }
    }
}