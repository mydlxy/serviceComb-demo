package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 接口权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prminterfacepermission")
@Tag(name = "接口权限表")
public class PrmInterfacePermissionController extends BaseController<PrmInterfacePermissionEntity> {

    private final IPrmInterfacePermissionService prmInterfacePermissionService;

    @Autowired
    public PrmInterfacePermissionController(IPrmInterfacePermissionService prmInterfacePermissionService) {
        this.crudService = prmInterfacePermissionService;
        this.prmInterfacePermissionService = prmInterfacePermissionService;
    }

    @PostMapping(value = "/getinterfacedatas")
    @Operation(summary = "获取所有数据")
    public ResultVO getInterfaceDatas(@RequestBody DataDto model) {
        ResultVO<List<PrmInterfacePermissionListInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<PrmInterfacePermissionListInfo> datas = prmInterfacePermissionService.getInterfaceDatas(model.getConditions(), model.getSorts(), Boolean.FALSE);
        return result.ok(datas);
    }

    @PostMapping(value = "/getinterfacepagedatas")
    @Operation(summary = "分页获取")
    public ResultVO getInterfacePageDatas(@RequestBody PageDataDto model) {
        ResultVO<PageData<PrmInterfacePermissionListInfo>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        PageData<PrmInterfacePermissionListInfo> datas = prmInterfacePermissionService
                .getInterfacePageDatas(model.getConditions(), model.getSorts()
                        , model.getPageIndex() == null ? 1 : model.getPageIndex(),
                        model.getPageSize() == null ? 20 : model.getPageSize(), Boolean.FALSE);

        return result.ok(datas);
    }

}