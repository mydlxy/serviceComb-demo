package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.OtDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmVersionDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmVersionEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.LinkException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 建模版本控制
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmversion")
@Tag(name = "建模版本控制")
public class PmVersionController extends BaseController<PmVersionEntity> {

    private final IPmVersionService pmVersionService;

    @Autowired
    public PmVersionController(IPmVersionService pmVersionService) {
        this.crudService = pmVersionService;
        this.pmVersionService = pmVersionService;
    }
//    @Autowired
//    private ISinglembomService singlembomService;

    @GetMapping("test")
    public String test() {
        //singlembomService.adddata();
        //pmVersionService.test();
        return "ok";
    }

    /**
     * 查询一个实体
     *
     * @return 返回一个实体
     */
    @GetMapping(value = "/provider/getobjectedpm")
    @Operation(summary = "查询当前版本的建模数据")
    public ResultVO getObjectedPm() {
        return new ResultVO().ok(pmVersionService.getObjectedPm());
    }

    /**
     * 根据工位编号获取关联的QG岗位信息
     */
    @GetMapping(value = "/provider/getrelevanceqgworkplacebystationid")
    @Operation(summary = "根据工位编号获取关联的QG岗位信息")
    public ResultVO getRelevanceQgWorkplaceByStationId(@RequestParam String stationId) {
        return new ResultVO().ok(pmVersionService.getRelevanceQgWorkplaceByStationId(stationId));
    }

    /**
     * 根据工位编号获取关联的QG岗位信息
     */
    @GetMapping(value = "/provider/getrelevanceworkplacebystation")
    @Operation(summary = "根据工位编号获取关联的QG岗位信息")
    public ResultVO getRelevanceQgWorkplaceByStation(@RequestParam String workstationCode) {
        return new ResultVO().ok(pmVersionService.getRelevanceQgWorkplaceByStation(workstationCode));
    }

    /**
     * 查询整个工厂对应节点的所有数据
     *
     * @param className 转换类型
     * @return 返回一个查询列表
     */
    @PostMapping(value = "/provider/getallelements")
    @Operation(summary = "查询整个工厂对应节点的所有数据")
    public ResultVO getAllElements(@RequestBody Class className) {
        return new ResultVO().ok(pmVersionService.getAllElements(className));
    }

    @PostMapping(value = "/provider/getcurretpm")
    @Operation(summary = "获取当前版本的格式化数据")
    public ResultVO<Document> getCurretPm() {
        return new ResultVO().ok(pmVersionService.getCurretPm());
    }

    /**
     * 发布工厂建模
     */
    @PostMapping("publishpm")
    @Operation(summary = "发布工厂建模")
    public ResultVO publishPm(@RequestBody PmVersionDTO model) {
        pmVersionService.publishShopPm(model);
        pmVersionService.saveChange();
        return new ResultVO().ok(null, "发布成功");
    }

    /**
     * 启用特定版本的工程建模
     */
    @PostMapping("enableversion")
    @Operation(summary = "启用特定版本的工程建模")
    public ResultVO enableVersion(@RequestBody PmVersionDTO dto) throws LinkException {
        return pmVersionService.enableShopVersion(dto.getId());
    }

    /**
     * 该方法主要用于mom和通用工厂两边工位数据不一致或通用工厂工位重复等一些列补偿（数据修复）
     * @param type
     * @return
     * @throws LinkException
     */
    @GetMapping("sync_mon_to_cmc")
    @Operation(summary = "通用工厂补偿方法")
    public ResultVO syncMonToCmc(int type) throws LinkException {
        pmVersionService.syncMonToCmc(type);
        return new ResultVO().ok(null, "同步成功");
    }



    /**
     * 同步通用工厂
     */
    @GetMapping("synccommfactory")
    @Operation(summary = "同步通用工厂")
    public ResultVO syncCommFactory(@NotNull(message = "请传入版本ID") Long versionId,int syncEquipment) {
        return pmVersionService.syncCommFactory(versionId,syncEquipment);
    }


    /**
     * 重新下发配置
     */
    @GetMapping("restdownconfig")
    @Operation(summary = "重新下发配置")
    public ResultVO restdownconfig(@NotNull(message = "shopCode不能为空") String shopCode) {
        return pmVersionService.restDownConfig(shopCode);
    }

    /**
     * 获取树节点
     */
    @GetMapping("gettreedata")
    @Operation(summary = "获取树节点")
    public ResultVO getTreeData(Long id, String shopCode, String nodeTypes) {
        return pmVersionService.getTreeData(id, shopCode, nodeTypes);
    }

    /**
     * 预览
     */
    @GetMapping("preview")
    @Operation(summary = "预览")
    public ResultVO preView(Long id) {
        return pmVersionService.getPreview(id);
    }

    /**
     * 比较PM Version
     */
    @GetMapping("getpmdifference")
    @Operation(summary = "比较PM Version")
    public ResultVO getPmDifference(String ids) {
        return pmVersionService.getPmDifference(ids);
    }

    /**
     * 获取车间列表
     */
    @GetMapping("getshopinfos")
    @Operation(summary = "获取车间列表")
    public ResultVO getShopInfos() {
        PmAllDTO pm = pmVersionService.getObjectedPm();
        List<PmWorkShopEntity> list = pm.getShops().stream()
                .sorted(Comparator.comparing(PmWorkShopEntity::getDisplayNo)).collect(Collectors.toList());
        return new ResultVO<>().ok(list, "获取数据成功");
    }

    /**
     * 根据车间获取线体
     */
    @GetMapping("getareainfos")
    @Operation(summary = "根据车间获取线体")
    public ResultVO getAreaInfos(String pmShopId) {
        PmAllDTO pm = pmVersionService.getObjectedPm();
        List<PmLineEntity> list = new ArrayList<>();
        if (!StringUtil.isNullOrEmpty(pmShopId)) {
            list = pm.getLines().stream().filter(t -> t.getPrcPmWorkshopId().equals(ConvertUtils.stringToLong(pmShopId)))
                    .sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
        } else {
            list = pm.getLines().stream().sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
        }
        return new ResultVO<>().ok(list, "获取数据成功");
    }

    /**
     * 根据线体获得下边AVI
     */
    @GetMapping("getaviinfos")
    @Operation(summary = "根据线体获得下边AVI")
    public ResultVO getAviInfos(String pmAreaId) {
        PmAllDTO pm = pmVersionService.getObjectedPm();
        List<PmAviEntity> list = pm.getAvis();
        if (StringUtils.isNotBlank(pmAreaId)) {
            List<PmAviEntity> subList = pm.getAvis().stream().filter(t -> t.getPrcPmLineId().equals(ConvertUtils.stringToLong(pmAreaId)))
                    .sorted(Comparator.comparing(PmAviEntity::getAviDisplayNo)).collect(Collectors.toList());
            return new ResultVO<>().ok(subList, "获取数据成功");
        }
        return new ResultVO<>().ok(list, "获取数据成功");
    }

    /**
     * 获取整个工厂combo数据
     */
    @GetMapping("getpmcombo")
    @Operation(summary = "获取整个工厂combo数据")
    public ResultVO getPmCombo(String nodeName, String parentId) {
        return pmVersionService.getPmCombo(nodeName, parentId);
    }

    /**
     * 根据车间获取combo数据
     */
    @GetMapping("getpmshopcombo")
    @Operation(summary = "根据车间获取combo数据")
    public ResultVO getPmShopCombo(String shopCode, String nodeName, String parentId) {
        return pmVersionService.getPmShopCombo(shopCode, nodeName, parentId);
    }

    /**
     * 根据车间获取combo数据
     */
    @PostMapping("getshopcombo")
    @Operation(summary = "根据车间获取combo数据")
    public ResultVO getShopCombo() {
        return pmVersionService.getShopCombo();
    }

    /**
     * 根据车间获取combo数据
     */
    @GetMapping("getallpmshopcombo")
    @Operation(summary = "根据车间获取combo数据")
    public ResultVO getAllpmShopCombo() {
        return pmVersionService.getAllShopCombo();
    }

    /**
     * 获取线体路径
     */
    @GetMapping("getareaposition")
    @Operation(summary = "获取线体路径")
    public ResultVO getAreaPosition(String shopCode) {
        return pmVersionService.getAreaPosition(shopCode);
    }

    /**
     * 获取岗位数据
     */
    @GetMapping("getworkstationcombo")
    @Operation(summary = "获取岗位数据")
    public ResultVO getWorkStationCombo(String shopCode, Integer workplaceType) {
        return StringUtils.isBlank(shopCode) ? pmVersionService.geWorkStationAllCombo(workplaceType)
                : pmVersionService.getWorkStationCombo(shopCode, workplaceType);
    }

    /**
     * 获取岗位数据
     */
    @GetMapping("/provider/getworkplacecombo")
    @Operation(summary = "获取岗位数据")
    public ResultVO getWorkplaceCombo(String shopCode, Integer workplaceType) {
        return pmVersionService.getWorkStationCombo(shopCode, workplaceType);
    }

    /**
     * 获取所有岗位数据
     */
    @GetMapping("getworkstationallcombo")
    @Operation(summary = "获取所有岗位数据")
    public ResultVO getWorkStationAllCombo(Integer workstationType) {
        return pmVersionService.geWorkStationAllCombo(workstationType);
    }

    /**
     * 获取线体下面的岗位数据
     */
    @GetMapping("getworkstationcombobyareaid")
    @Operation(summary = "获取线体下面的岗位数据")
    public ResultVO getWorkstationComboByAreaId(Long areaId, Integer workStation) {
        return pmVersionService.getWorkStationComboByLineId(areaId, workStation);
    }


    /**
     * 获取线体下面的岗位数据
     */
    @GetMapping("getworkstationbyareacode")
    @Operation(summary = "获取线体下面的岗位数据")
    public ResultVO getWorkstationByAreaCode(String areaCode, Integer workStation) {
        return pmVersionService.getWorkStationByLineCode(areaCode, workStation);
    }

    /**
     * 31
     */
    @GetMapping("getallobjectpm")
    @Operation(summary = "获取当前建模下的所有数据")
    public ResultVO getAllObjectPm() {
        return pmVersionService.getAllObjectPm();
    }

    /**
     * 获取ot层级列表
     *
     * @param workshopCode
     * @param workstationType
     * @return
     */
    @GetMapping("getotdatas")
    @Operation(summary = "获取ot层级列表")
    public ResultVO<OtDto> getOtDatas(String workshopCode, String workstationType) {
        return pmVersionService.getOtDatas(workshopCode, workstationType);
    }

    @Operation(summary = "根据车间获取AVI信息")
    @GetMapping(value = "getaviinfosbyshopcode")
    public ResultVO<List<PmAviEntity>> getAviInfosByShopCode(String workshopCode) {
        PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();
        List<Long> workshopIds = new ArrayList<>(16);
        if (StringUtils.isNotBlank(workshopCode)) {
            PmWorkShopEntity shopInfo = pmAllDTO.getShops().stream().filter(item -> Objects.equals(item.getWorkshopCode(), workshopCode)).findFirst().orElse(null);
            if (shopInfo == null) {
                throw new InkelinkException("未找到车间");
            }
            workshopIds.add(shopInfo.getId());
        } else {
            workshopIds.addAll(pmAllDTO.getShops().stream().map(PmWorkShopEntity::getId).collect(Collectors.toList()));
        }
        List<PmAviEntity> aviList = pmAllDTO.getAvis().stream().filter(item -> workshopIds.contains(item.getPrcPmWorkshopId()))
                .sorted(Comparator.comparing(PmAviEntity::getAviDisplayNo)).collect(Collectors.toList());
        return new ResultVO<List<PmAviEntity>>().ok(aviList, "获取数据成功");

    }
}