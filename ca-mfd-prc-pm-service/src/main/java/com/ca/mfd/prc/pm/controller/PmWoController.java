package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.service.IPmWoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 岗位操作
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmwo")
@Tag(name = "岗位操作")
public class PmWoController extends PmBaseController<PmWoEntity> {

    private final IPmWoService pmWoService;

    @Autowired
    public PmWoController(IPmWoService pmWoService) {
        this.crudService = pmWoService;
        this.pmWoService = pmWoService;
    }

    /**
     * 根据岗位id 查询工位下没有绑定的操作
     * <param name="workplaceId">岗位编号</param>
     */
    @GetMapping("/getworkplacewo")
    @Operation(summary = "根据岗位id 查询工位下没有绑定的操作")
    public ResultVO<List<ComboInfoDTO>> getWorkplaceWo(Long workplaceId) {
        return pmWoService.getWorkStationWo(workplaceId);
    }

    @GetMapping("/getWoComboInfo")
    @Operation(summary = "根据岗位id 查询工位下没有绑定的操作")
    public ResultVO<List<ComboInfoDTO>> getWoComboInfo(Long workplaceId) {
        return pmWoService.getWoComboInfo(workplaceId);
    }


}