package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.service.IPqsDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 责任部门配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdept")
@Tag(name = "责任部门配置服务", description = "责任部门配置")
public class PqsDeptController extends BaseController<PqsDeptEntity> {

    private final IPqsDeptService pqsDeptService;

    @Autowired
    public PqsDeptController(IPqsDeptService pqsDeptService) {
        this.crudService = pqsDeptService;
        this.pqsDeptService = pqsDeptService;
    }

    /**
     * 获取部门
     *
     * @return
     */
    @PostMapping("getcombolist")
    @Operation(summary = "获取部门")
    public ResultVO<List<ComboInfoDTO>> getComboList() {
        return new ResultVO<List<ComboInfoDTO>>().ok(pqsDeptService.getComboList(), "获取数据成功");
    }

}