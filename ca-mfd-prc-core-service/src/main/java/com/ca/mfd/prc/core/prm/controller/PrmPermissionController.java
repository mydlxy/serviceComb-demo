package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmpermission")
@Tag(name = "权限")
public class PrmPermissionController extends BaseController<PrmPermissionEntity> {

    private final IPrmPermissionService prmPermissionService;

    @Autowired
    public PrmPermissionController(IPrmPermissionService prmPermissionService) {
        this.crudService = prmPermissionService;
        this.prmPermissionService = prmPermissionService;
    }

    @GetMapping(value = "/getcombodata")
    @Operation(summary = "获取权限列表")
    public ResultVO getComboData(String category) {
        List<ComboInfoDTO> comboInfos = prmPermissionService.getAllOwnDatas()
                .stream().sorted(Comparator.comparing(PrmPermissionEntity::getModel)
                        .thenComparing(PrmPermissionEntity::getPermissionName))
                .map(c -> {
                    ComboInfoDTO et = new ComboInfoDTO();
                    et.setText("[" + c.getModel() + "]" + c.getPermissionName());
                    et.setValue(c.getId().toString());
                    return et;
                }).collect(Collectors.toList());

        return new ResultVO<List<ComboInfoDTO>>().ok(comboInfos);
    }
}