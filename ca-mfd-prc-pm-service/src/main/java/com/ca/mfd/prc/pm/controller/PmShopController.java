package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author inkelink
 * @Description: 车间
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmshop")
@Tag(name = "车间")
public class PmShopController extends PmBaseController<PmWorkShopEntity> {

    private final IPmWorkShopService pmShopService;
    private final IPmVersionService pmVersionService;

    @Autowired
    public PmShopController(IPmWorkShopService pmShopService,
                            IPmVersionService pmVersionService) {
        this.crudService = pmShopService;
        this.pmShopService = pmShopService;
        this.pmVersionService = pmVersionService;
    }

    @Operation(summary = "获取当前车间启用的版本")
    @GetMapping(value = "/getpmshop")
    public ResultVO getPmShop() {
        //通过车间得到所有的班组信息通过班组信息得到线体信息
        PmAllDTO allVersion = pmVersionService.getObjectedPm();
        List<PmWorkShopEntity> shopsList = allVersion.getShops();
        if (shopsList.isEmpty()) {
            return new ResultVO().ok(Collections.emptyList(), "获取数据成功");
        }
        List<ComboInfoDTO> targetList = new ArrayList<>(shopsList.size());
        for (PmWorkShopEntity pmShopEntity : shopsList) {
            targetList.add(new ComboInfoDTO(pmShopEntity.getWorkshopName(), String.valueOf(pmShopEntity.getId())));
        }
        return new ResultVO().ok(targetList, "获取数据成功");
    }


}