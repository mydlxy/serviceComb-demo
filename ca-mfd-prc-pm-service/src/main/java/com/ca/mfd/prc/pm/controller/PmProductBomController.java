package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.TopDataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.service.IPmProductBomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: BOM
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductbom")
@Tag(name = "BOM")
public class PmProductBomController extends BaseController<PmProductBomEntity> {

    private final IPmProductBomService pmProductBomService;

    @Autowired
    public PmProductBomController(IPmProductBomService pmProductBomService) {
        this.crudService = pmProductBomService;
        this.pmProductBomService = pmProductBomService;
    }

    /**
     * 获取列表数据
     *
     * @param model 条件表达式
     * @return List<PmProductBomEntity>
     */
    @Operation(summary = "获取列表数据")
    @PostMapping("/provider/gettopdatas")
    ResultVO<List<PmProductBomEntity>> getTopDatas(@RequestBody TopDataDto model) {
        ResultVO<List<PmProductBomEntity>> result = new ResultVO<>();
        List<PmProductBomEntity> data = pmProductBomService.getTopDatas(model.getTop(), model.getConditions(), model.getSorts());
        return result.ok(data);
    }

}