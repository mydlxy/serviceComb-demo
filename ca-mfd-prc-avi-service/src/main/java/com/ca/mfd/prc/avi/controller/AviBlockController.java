package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.constant.Constant;
import com.ca.mfd.prc.avi.entity.AviBlockEntity;
import com.ca.mfd.prc.avi.remote.app.pm.IPmVersionService;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmAviServiceProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.service.IAviBlockService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 整车AVI锁定
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-07
 */
@RestController
@RequestMapping("aviblock")
@Tag(name = "整车AVI锁定")
public class AviBlockController extends BaseController<AviBlockEntity> {

    @Autowired
    PmAviServiceProvider pmAviServiceProvider;
    private final IAviBlockService aviBlockService;
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    public AviBlockController(IAviBlockService aviBlockService) {
        this.crudService = aviBlockService;
        this.aviBlockService = aviBlockService;
    }

    @Operation(summary = "获取AVI站点")
    @PostMapping("getaviinfos")
    public ResultVO<List<AviInfoDTO>> getAviInfos() {
        ResultVO<List<AviInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取AVI站点成功");
        List<AviInfoDTO> avisData = pmAviServiceProvider.getAviInfos();
        return result.ok(avisData);
    }

    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviBlockEntity>> getPageData(@RequestBody PageDataDto model) {
        //        List<String> list = new ArrayList<>();
        //        //焊装车间数据权限判断
        //        if (identityHelper.hasPermission(Constant.BODY_SHOP_DATA)) {
        //            //list.add("BodyShop");
        //            list.add("WE");
        //        }
        //        //涂装车间数据权限判断
        //        if (identityHelper.hasPermission(Constant.PAINT_SHOP_DATA)) {
        //            //list.add("PaintShop");
        //            list.add("PA");
        //        }
        //        //总装车间数据权限判断
        //        if (identityHelper.hasPermission(Constant.ASSEMBLY_SHOP_DATA)) {
        //            //list.add("AssemblyShop");
        //            list.add("GA");
        //        }
        //        //Pack车间数据权限判断
        //        if (identityHelper.hasPermission(Constant.PACK_SHOP_DATA)) {
        //            list.add("BA");
        //        }
        //        //PDI车间数据权限判断
        //        if (identityHelper.hasPermission(Constant.PDI_SHOP_DATA)) {
        //            list.add("PDIShop");
        //        }
        //        if (model.getConditions() == null) {
        //            model.setConditions(new ArrayList<>());
        //        }
        List<String> list = new ArrayList<>();
        List<PmWorkShopEntity> shops = pmVersionProvider.getObjectedPm().getShops();
        list = shops.stream().map(s -> s.getWorkshopCode()).collect(Collectors.toList());
        model.getConditions().add(new ConditionDto("WORKSHOP_CODE", String.join("|", list), ConditionOper.In));
        PageData<AviBlockEntity> pageData = crudService.page(model);
        pageData.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        PageData<AviBlockEntity> page = pageData;
        return new ResultVO<PageData<AviBlockEntity>>().ok(page, "获取数据成功");
    }

}