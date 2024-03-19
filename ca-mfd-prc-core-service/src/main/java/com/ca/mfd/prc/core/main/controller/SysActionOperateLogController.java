package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.dto.SysActionOperateLogDTO;
import com.ca.mfd.prc.core.main.entity.SysActionOperateLogEntity;
import com.ca.mfd.prc.core.main.service.ISysActionOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 系统请求操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysactionoperatelog")
@Tag(name = "系统请求操作日志")
public class SysActionOperateLogController extends BaseController<SysActionOperateLogEntity> {

    private final ISysActionOperateLogService sysActionOperateLogService;

    @Autowired
    public SysActionOperateLogController(ISysActionOperateLogService sysActionOperateLogService) {
        this.crudService = sysActionOperateLogService;
        this.sysActionOperateLogService = sysActionOperateLogService;
    }

    /**
     * 获取所有数据
     *
     * @return 返回数据
     */
    @GetMapping(value = "/getalldatas")
    @Operation(summary = "获取所有系统请求操作日志数据")
    public ResultVO<List<SysActionOperateLogDTO>> getAllDatas() {
        ResultVO<List<SysActionOperateLogDTO>> result = new ResultVO<>();
        List<SysActionOperateLogDTO> data = sysActionOperateLogService.getAllDatas();
        return result.ok(data);
    }

    /**
     * 重新初始化菜单日志记录
     *
     * @return 返回数据
     */
    @GetMapping(value = "/initsysmenuitemscommand")
    @Operation(summary = "重新初始化菜单日志记录")
    public ResultVO initSysMenuItemsCommand() {
        sysActionOperateLogService.initSysMenuItemsCommand();
        return new ResultVO<>().ok(null, "刷新成功");
    }
}