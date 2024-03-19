package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteCacheLogMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteCacheLogService;
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
public class RcBsRouteCacheLogServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteCacheLogMapper, RcBsRouteCacheLogEntity> implements IRcBsRouteCacheLogService {

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