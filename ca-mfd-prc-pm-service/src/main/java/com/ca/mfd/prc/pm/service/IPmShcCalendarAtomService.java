package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAtomEntity;

import java.text.ParseException;


/**
 * 工厂日历清洗
 *
 * @author jay.he
 */
public interface IPmShcCalendarAtomService extends ICrudService<PmShcCalendarAtomEntity> {

    void calendarAtomHandle() throws ParseException;

}