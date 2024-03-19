package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviPointOperationBlockEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.service.IAviPointOperationBlockService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * AVI车辆过点工艺完成阻塞检查
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avipointoperationblock")
@Tag(name = "AVI车辆过点工艺完成阻塞检查")
public class AviPointOperationBlockController extends BaseController<AviPointOperationBlockEntity> {

    private final IAviPointOperationBlockService aviPointOperationBlockService;

    @Autowired
    //private IPmVersionService pmVersionService;
    private PmVersionProvider pmVersionProvider;

    @Autowired
    public AviPointOperationBlockController(IAviPointOperationBlockService aviPointOperationBlockService) {
        this.crudService = aviPointOperationBlockService;
        this.aviPointOperationBlockService = aviPointOperationBlockService;
    }

    private PmAllDTO getObjectedPm() {
        return pmVersionProvider.getObjectedPm();
    }

    @PostMapping("getpm")
    @Operation(summary = "获取当前建模版本数据")
    public ResultVO<PmAllDTO> getPm() {
        ResultVO<PmAllDTO> result = new ResultVO<>();
        result.setMessage("获取Shop站点成功");
        return result.ok(getObjectedPm());
    }

    @Operation(summary = "获取车间站点")
    @PostMapping("getshopinfos")
    public ResultVO<List<PmWorkShopEntity>> getShopInfos() {
        ResultVO<List<PmWorkShopEntity>> result = new ResultVO<>();
        result.setMessage("获取Shop站点成功");
        PmAllDTO pmAllDTO = getObjectedPm();
        List<PmWorkShopEntity> data = pmAllDTO.getShops();
        return result.ok(data);
    }

    @Operation(summary = "获取线体站点")
    @PostMapping("getareainfos")
    public ResultVO<List<PmLineEntity>> getAreaInfos(String shopId) {
        Long shopIdNumber = ConvertUtils.stringToLong(shopId);
        ResultVO<List<PmLineEntity>> result = new ResultVO<>();
        result.setMessage("获取Area站点成功");
        PmAllDTO pmAllDTO = getObjectedPm();
        List<PmLineEntity> data = pmAllDTO.getLines().stream().filter(c -> Objects.equals(c.getPrcPmWorkshopId(), shopIdNumber)).collect(Collectors.toList());
        return result.ok(data);
    }

    @Operation(summary = "获取AVI站点")
    @PostMapping("getaviinfos")
    public ResultVO<List<PmAviEntity>> getAviInfos(String areaId) {
        Long areaIdNumber = ConvertUtils.stringToLong(areaId);
        ResultVO<List<PmAviEntity>> result = new ResultVO<>();
        result.setMessage("获取AVI站点成功");
        PmAllDTO pmAllDTO = getObjectedPm();
        List<PmAviEntity> data = pmAllDTO.getAvis().stream().filter(c -> Objects.equals(c.getPrcPmLineId(), areaIdNumber)).collect(Collectors.toList());
        return result.ok(data);
    }

    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviPointOperationBlockEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<AviPointOperationBlockEntity> pageData = crudService.page(model);
        pageData.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        PageData<AviPointOperationBlockEntity> page = pageData;
        return new ResultVO<PageData<AviPointOperationBlockEntity>>().ok(page, "获取数据成功");
    }
}