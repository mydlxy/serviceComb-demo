package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import com.ca.mfd.prc.pm.service.IPmShcCalendarAtomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * 工厂日历清洗
 *
 * @author jay.he
 * @since 1.0.0 2023-09-08
 */
@RestController
@RequestMapping("pmshccalendaratom")
@Tag(name = "工厂 日历")
public class PmShcCalendarAtomController extends BaseController<PmShcCalendarEntity> {

    @Autowired
    IPmShcCalendarAtomService pmShcCalendarAtomService;

    @Autowired
    public PmShcCalendarAtomController() {

    }

    @Operation(summary = "清理工厂日历")
    @GetMapping("calendaratomhandle")
    public ResultVO calendarAtomHandle() throws ParseException {
        ResultVO result = new ResultVO<>();
        pmShcCalendarAtomService.calendarAtomHandle();
        return result.ok("触发成功！");
    }

}