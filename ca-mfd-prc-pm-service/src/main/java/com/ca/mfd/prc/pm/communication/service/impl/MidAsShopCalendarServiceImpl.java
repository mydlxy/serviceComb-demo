package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShopCalendarEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidAsShopCalendarMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShopCalendarService;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS车间日历中间表服务实现
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@Service
public class MidAsShopCalendarServiceImpl extends AbstractCrudServiceImpl<IMidAsShopCalendarMapper, MidAsShopCalendarEntity> implements IMidAsShopCalendarService {
    private static final Logger logger = LoggerFactory.getLogger(MidAsShopCalendarServiceImpl.class);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsShopCalendarEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsShopCalendarEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsShopCalendarEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }


    /**
     * 执行数据处理逻辑(包含线体、车间日历)(考虑异步)
     */
    @Override
    public void excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmShcCalendarService pmShcCalendarService = SpringContextUtils.getBean(IPmShcCalendarService.class);
        IMidAsShopCalendarService midAsShopCalendarService = SpringContextUtils.getBean(IMidAsShopCalendarService.class);

        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_SHOP_CALENDAR, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                List<MidAsShopCalendarEntity> datas = midAsShopCalendarService.getListByLog(apilog.getId());
                List<CalendarFromASDTO> calendars = new ArrayList<>();
                for (MidAsShopCalendarEntity dt : datas) {
                    CalendarFromASDTO cl = new CalendarFromASDTO();
                    cl.setDate(dt.getWorkDay());
                    cl.setShopCode(dt.getShopCode());
                    cl.setShiftCode(dt.getShiftCode());
                    calendars.add(cl);
                }
                pmShcCalendarService.syncCalendarFromAS(calendars, 1);
                pmShcCalendarService.saveChange();
                success = true;

            } catch (Exception exception) {
                logger.debug("数据保存异常：{}", exception.getMessage());
            }
            try {
                midApiLogService.clearChange();
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogEnd);
                midApiLogService.saveChange();
            } catch (Exception exception) {
                logger.debug("日志保存异常：{}", exception.getMessage());
            }
        }
    }

}