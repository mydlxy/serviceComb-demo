package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;
import com.ca.mfd.prc.core.main.entity.SysLogExceptionEntity;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import com.ca.mfd.prc.core.main.service.ISysLogExceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统异常日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syslogexception")
@Tag(name = "系统异常日志")
public class SysLogExceptionController extends BaseController<SysLogExceptionEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SysLogExceptionController.class);
    private final ISysLogExceptionService sysLogExceptionService;

    @Autowired
    private ISysConfigurationService sysConfigurationService;

    @Autowired
    public SysLogExceptionController(ISysLogExceptionService sysLogExceptionService) {
        this.crudService = sysLogExceptionService;
        this.sysLogExceptionService = sysLogExceptionService;
    }

    @Operation(summary = "获取ExceptionEmail配置所有数据")
    @GetMapping("getexceptionemail")
    public ResultVO getExceptionEmail() {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("category", "ExceptionEmail", ConditionOper.Equal));
        List<SysConfigurationEntity> data = sysConfigurationService.getData(dtos);
        return new ResultVO().ok(data, "获取数据成功");
    }

    @Operation(summary = "编辑ExceptionEmail 对象")
    @PostMapping("editexceptionemail")
    public ResultVO editExceptionEmail(List<SysConfigurationEntity> model) {
        for (SysConfigurationEntity item : model) {
            sysConfigurationService.update(item);
        }
        sysConfigurationService.saveChange();
        return new ResultVO().ok("", "保存成功");
    }
}