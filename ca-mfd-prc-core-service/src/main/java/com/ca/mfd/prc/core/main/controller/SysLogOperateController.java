package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysLogOperateEntity;
import com.ca.mfd.prc.core.main.service.ISysLogOperateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syslogoperate")
@Tag(name = "系统操作日志")
public class SysLogOperateController extends BaseController<SysLogOperateEntity> {

    private final ISysLogOperateService sysLogOperateService;

    @Autowired
    public SysLogOperateController(ISysLogOperateService sysLogOperateService) {
        this.crudService = sysLogOperateService;
        this.sysLogOperateService = sysLogOperateService;
    }

    @Override
    public ResultVO<PageData<SysLogOperateEntity>> getPageData(@RequestBody PageDataDto model) {
        return super.getPageData(model);
    }
}