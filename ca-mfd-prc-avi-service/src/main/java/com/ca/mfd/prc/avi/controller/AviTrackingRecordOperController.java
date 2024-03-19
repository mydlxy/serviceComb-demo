package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.constant.Constant;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordOperService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 产品过点信息行为记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avitrackingrecordoper")
@Tag(name = "产品过点信息行为记录")
public class AviTrackingRecordOperController extends BaseController<AviTrackingRecordOperEntity> {

    @Autowired
    private IdentityHelper identityHelper;

    private final IAviTrackingRecordOperService aviTrackingRecordOperService;

    @Autowired
    public AviTrackingRecordOperController(IAviTrackingRecordOperService aviTrackingRecordOperService) {
        this.crudService = aviTrackingRecordOperService;
        this.aviTrackingRecordOperService = aviTrackingRecordOperService;
    }

    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviTrackingRecordOperEntity>> getPageData(@RequestBody PageDataDto model) {
        /*List<PmShopEntity> shops = pmVersionService.getObjectedPm().getShops();*/
        /*判断权限*/
        List<String> list = new ArrayList<>();
        //焊装车间数据权限判断
        if (identityHelper.hasPermission(Constant.BODY_SHOP_DATA)) {
            list.add("WE");
        }
        //涂装车间数据权限判断
        if (identityHelper.hasPermission(Constant.PAINT_SHOP_DATA)) {
            list.add("PA");
        }
        //总装车间数据权限判断
        if (identityHelper.hasPermission(Constant.ASSEMBLY_SHOP_DATA)) {
            list.add("GA");
        }
        //Pack车间数据权限判断
        if (identityHelper.hasPermission(Constant.PACK_SHOP_DATA)) {
            list.add("BA");
        }
        //PDI车间数据权限判断
        if (identityHelper.hasPermission(Constant.PDI_SHOP_DATA)) {
            list.add("PDIShop");
        }
        if (model.getConditions() == null) {
            model.setConditions(new ArrayList<>());
        }
        model.getConditions().add(new ConditionDto("WORKSHOP_CODE", String.join("|", list), ConditionOper.In));
        PageData<AviTrackingRecordOperEntity> page = crudService.page(model);
        return new ResultVO<PageData<AviTrackingRecordOperEntity>>().ok(page, "获取数据成功");
    }

}