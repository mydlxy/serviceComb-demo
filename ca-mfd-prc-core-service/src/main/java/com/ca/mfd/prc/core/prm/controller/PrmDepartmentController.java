package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.PrmDepartMentNode;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentService;
import com.ca.mfd.prc.core.main.dto.SaveDepart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 部门管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmdepartment")
@Tag(name = "部门管理")
public class PrmDepartmentController extends BaseController<PrmDepartmentEntity> {

    private final IPrmDepartmentService prmDepartmentService;

    @Autowired
    public PrmDepartmentController(IPrmDepartmentService prmDepartmentService) {
        this.crudService = prmDepartmentService;
        this.prmDepartmentService = prmDepartmentService;
    }

    @GetMapping(value = "/getdropdownprmdepartment")
    @Operation(summary = "获取下拉部门")
    public ResultVO getDropDownPrmDepartment(String organizationName) {
        List<ComboInfoDTO> data = prmDepartmentService.getDropDownPrmDepartment(organizationName);
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @GetMapping(value = "/getdepartmenttree")
    @Operation(summary = "获取部门树")
    public ResultVO getDepartmentTree(String organizationName) {
        List<PrmDepartMentNode> data = prmDepartmentService.getPrmDepartmentTree(organizationName);
        ResultVO<List<PrmDepartMentNode>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(data);
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存菜单数据")
    public ResultVO save(@RequestBody SaveDepart saveDepart) {
        //保证当前自有一个访问项
        if (StringUtils.isBlank(saveDepart.getOrganizationName())) {
            throw new InkelinkException("组织机构缺失");
        }
        ResultVO<List<PrmDepartmentEntity>> result = new ResultVO<>();
        result.setMessage("保存成功");

        List<PrmDepartmentEntity> sysMenuItemInfos = JsonUtils.parseArray(saveDepart.getDatas(), PrmDepartmentEntity.class);
        //Json.JsonConvert.DeserializeObject<IList<PrmDepartmentInfo>>(saveDepart.datas);
        prmDepartmentService.save(sysMenuItemInfos, saveDepart.getOrganizationName());
        prmDepartmentService.saveChange();
        return result.ok(null);

    }

}