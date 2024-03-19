package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRouteHoldLogMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteHoldLogService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author inkelink
 * @Description: 路由区暂存日志表服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcAviRouteHoldLogServiceImpl extends AbstractCrudServiceImpl<IRcAviRouteHoldLogMapper, RcAviRouteHoldLogEntity> implements IRcAviRouteHoldLogService {

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> maps = new HashMap<>();
        maps.put("workshopCode", "路由区代码");
        maps.put("pointCode", "路由点代码");
        maps.put("sn", "车辆识别码");
        maps.put("holdName", "HOLD车操作人");
        maps.put("holdDt", "HOLD车时间");
        maps.put("holdReason", "HOLD车原因");
        maps.put("cancelName", "解锁人");
        maps.put("cancelDt", "解锁时间");
        maps.put("cancelReason", "解锁原因");
        return maps;
    }
}