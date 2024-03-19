package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 工单订阅配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsentryconfig")
@Tag(name = "工单订阅配置")
public class PpsEntryConfigController extends BaseController<PpsEntryConfigEntity> {

    private final IPpsEntryConfigService ppsEntryConfigService;
    @Autowired
    private PmVersionProvider ppsVersionProvider;

    @Autowired
    public PpsEntryConfigController(IPpsEntryConfigService ppsEntryConfigService) {
        this.crudService = ppsEntryConfigService;
        this.ppsEntryConfigService = ppsEntryConfigService;
    }

    private PmAllDTO getObjectedPm() {
        return ppsVersionProvider.getObjectedPm();
    }

    @Operation(summary = "根据线体id 查询车间")
    @GetMapping("getppsentryconfigshop")
    public ResultVO<List<ComboInfoDTO>> getShop(String pmplanId) {
        List<ComboInfoDTO> list = new ArrayList<>();
        PmAllDTO pmAllDTO = getObjectedPm();
        if (pmAllDTO == null) {
            return new ResultVO<List<ComboInfoDTO>>().ok(list);
        }
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        if (shops == null) {
            return new ResultVO<List<ComboInfoDTO>>().ok(list);
        }
        if (StringUtils.isNotBlank(pmplanId)) {
            shops = shops.stream().filter(s -> StringUtils.equals(s.getPrcPmOrganizationId().toString(), pmplanId)).collect(Collectors.toList());
        }
        list = shops.stream().map(s -> new ComboInfoDTO(s.getWorkshopName(), s.getWorkshopCode())).collect(Collectors.toList());
        return new ResultVO<List<ComboInfoDTO>>().ok(list);
    }

    @Operation(summary = "根据车间代码查询线体")
    @GetMapping("getppsentryconfigarea")
    public ResultVO<List<ComboInfoDTO>> getArea(String shopCode) {
        List<ComboInfoDTO> list = new ArrayList<>();
        PmAllDTO allVersion = getObjectedPm();
        if (allVersion == null) {
            return new ResultVO<List<ComboInfoDTO>>().ok(list, "获取数据成功");
        }
        Long shopId = Constant.DEFAULT_ID;
        PmWorkShopEntity pmShopEntity = allVersion.getShops().stream().filter(s -> StringUtils.equals(s.getWorkshopCode(), shopCode))
                .findFirst().orElse(null);
        if (pmShopEntity != null) {
            shopId = pmShopEntity.getId();
        }
        List<PmLineEntity> areas = allVersion.getLines();
        if (areas == null) {
            return new ResultVO<List<ComboInfoDTO>>().ok(list);
        }
        Long finalShopId = shopId;
        List<PmLineEntity> areaEntityList = areas.stream().filter(s -> Objects.equals(s.getPrcPmWorkshopId(), finalShopId)).
                collect(Collectors.toList());
        list = areaEntityList.stream().map(s -> new ComboInfoDTO(s.getLineName() + "(" + s.getLineCode() + ")", s.getLineCode()))
                .collect(Collectors.toList());
        return new ResultVO<List<ComboInfoDTO>>().ok(list, "获取数据成功");
    }

    @Operation(summary = "获取配置")
    @GetMapping("getconfigbysub")
    public ResultVO<PpsEntryConfigEntity> getConfigBySub(String subCode) {
        return new ResultVO<PpsEntryConfigEntity>().ok(ppsEntryConfigService.getFirstBySubCode(subCode));
    }
}