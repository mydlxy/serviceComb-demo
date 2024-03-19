package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.PrmJoinUserDto;
import com.ca.mfd.prc.core.prm.dto.PrmUserjoinDepartDto;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentJoinUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;


/**
 * 部门关联员工表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmdepartmentjoinuser")
@Tag(name = "部门关联员工表")
public class PrmDepartmentJoinUserController extends BaseController<PrmDepartmentJoinUserEntity> {

    private final IPrmDepartmentJoinUserService prmDepartmentJoinUserService;

    @Autowired
    public PrmDepartmentJoinUserController(IPrmDepartmentJoinUserService prmDepartmentJoinUserService) {
        this.crudService = prmDepartmentJoinUserService;
        this.prmDepartmentJoinUserService = prmDepartmentJoinUserService;
    }


    @PostMapping(value = "/save")
    @Operation(summary = "保存当前所有用户")
    public ResultVO save(@RequestBody PrmJoinUserDto saveDepartJoinUser) {
        //保证当前自有一个访问项
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("保存成功");
        prmDepartmentJoinUserService.save(saveDepartJoinUser);
        prmDepartmentJoinUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "/bathusersave")
    @Operation(summary = "批量保存")
    public ResultVO bathUserSave(@RequestBody PrmJoinUserDto saveDepartJoinUser) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("保存成功");

        List<Serializable> oldUserId = prmDepartmentJoinUserService.getOldUserIds(saveDepartJoinUser);
        for (Serializable item : oldUserId) {
            saveDepartJoinUser.getUserId().remove(item);
        }
        for (Serializable item : saveDepartJoinUser.getUserId()) {
            PrmDepartmentJoinUserEntity et = new PrmDepartmentJoinUserEntity();
            et.setPrcPrmDepartmentId(Long.valueOf(saveDepartJoinUser.getDeparId().toString()));
            et.setPrcPrmUserId(Long.valueOf(item.toString()));
            prmDepartmentJoinUserService.insert(et);
        }
        prmDepartmentJoinUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "/bathdeluser")
    @Operation(summary = "批量删除用户")
    public ResultVO bathDelUser(@RequestBody PrmJoinUserDto saveDepartJoinUser) {
        ResultVO<Serializable> result = new ResultVO<>();
        result.setMessage("保存成功");

        for (Serializable item : saveDepartJoinUser.getUserId()) {
            prmDepartmentJoinUserService.deleteByDeptUserId(saveDepartJoinUser.getDeparId(), item);
        }
        prmDepartmentJoinUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "/getuserpagedatas")
    @Operation(summary = "获取分页数据")
    public ResultVO getUserPageDatas(@RequestBody PageDataDto model) {
        ResultVO<PageData<PrmUserjoinDepartDto>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        PageData<PrmUserjoinDepartDto> datas = prmDepartmentJoinUserService.getUserPageDatas(model.getConditions()
                , model.getPageIndex() == null ? 1 : model.getPageIndex(),
                model.getPageSize() == null ? 20 : model.getPageSize());
        return result.ok(datas);
    }

}