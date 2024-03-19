package com.ca.mfd.prc.rc.rcps.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteHoldLogMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteHoldLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author inkelink
 * @Description: 路由区暂存日志表服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteHoldLogServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteHoldLogMapper, RcPsRouteHoldLogEntity> implements IRcPsRouteHoldLogService {

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new LinkedHashMap<>(10);
        columnNames.put("bufferCode", "缓存区代码");
        columnNames.put("areaCode", "路由区代码");
        columnNames.put("sn", "车辆识别码");
        columnNames.put("holdName", "HOLD车操作人");
        columnNames.put("holdDt", "HOLD车时间");
        columnNames.put("holdReason", "HOLD车原因");
        columnNames.put("cancelName", "解锁人");
        columnNames.put("cancelDt", "解锁时间");
        columnNames.put("cancelReason", "解锁原因");
        return columnNames;
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("holdDt") && data.getOrDefault("holdDt", null) != null) {
                data.put("holdDt", DateUtils.format((Date) data.get("holdDt"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("cancelDt") && data.getOrDefault("cancelDt", null) != null) {
                data.put("cancelDt", DateUtils.format((Date) data.get("cancelDt"), DateUtils.DATE_TIME_PATTERN));
            }
        }
    }

}