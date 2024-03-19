package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysMenuItemVersionEntity;
import com.ca.mfd.prc.core.main.service.ISysMenuItemVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 菜单版本管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmmenuitemversion")
@Tag(name = "菜单版本管理")
public class SysMenuItemVersionController extends BaseController<SysMenuItemVersionEntity> {

    private final ISysMenuItemVersionService sysMenuItemVersionService;

    @Autowired
    public SysMenuItemVersionController(ISysMenuItemVersionService sysMenuItemVersionService) {
        this.crudService = sysMenuItemVersionService;
        this.sysMenuItemVersionService = sysMenuItemVersionService;
    }

    @GetMapping(value = "/getmenuversion")
    @Operation(summary = "获取历史版本")
    public ResultVO getMenuVersion(String menuId) {
        List<SysMenuItemVersionEntity> data = sysMenuItemVersionService.getMenuVersion(menuId);
        ResultVO<List<SysMenuItemVersionEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

}