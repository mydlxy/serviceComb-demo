package com.ca.mfd.prc.pm.controller;

import cn.hutool.core.util.StrUtil;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.pm.dto.ComponentDataDTO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.ShopExcelDto;
import com.ca.mfd.prc.pm.dto.TextValueStationsMappingDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.service.IPmService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 工厂建模
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pm")
@Tag(name = "工厂建模")
public class PmController extends BaseApiController {
    private final IPmService pmService;
    private final IPmWorkStationService pmWorkplaceService;
    private final IPmVersionService pmVersionService;


    @Autowired
    public PmController(IPmService pmService, IPmWorkStationService pmWorkplaceService,
                        IPmVersionService pmVersionService) {
        this.pmService = pmService;
        this.pmWorkplaceService = pmWorkplaceService;
        this.pmVersionService = pmVersionService;
    }

    @Operation(summary = "获取节点信息")
    @Parameters({
            @Parameter(name = "id", description = "主键"),
            @Parameter(name = "type", description = "节点类型")})
    @GetMapping(value = "gettreenodeinfos")
    public ResultVO getTreeNodeInfos(Long id, String type) {
        List<TreeNode> items = pmService.getTreeNodes(id, type);
        return new ResultVO().ok(items);
    }

    @Operation(summary = "获取某个线体下所有岗位信息")
    @Parameter(name = "id", description = "线体主键")
    @GetMapping("getlineworkplace")
    public ResultVO<List<TextAndValueMappingDTO>> getLineWorkplace(String id) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("PM_AREA_ID", id, ConditionOper.Equal));
        List<PmWorkStationEntity> items = pmWorkplaceService.getData(conditionInfos);
        List<TextAndValueMappingDTO> textAndValueList = new ArrayList<>(items.size());
        items.stream().forEach(item -> {
            TextAndValueMappingDTO textAndValueMappingDTO = new TextAndValueMappingDTO();
            textAndValueMappingDTO.setText(item.getWorkstationName());
            textAndValueMappingDTO.setValue(String.valueOf(item.getId()));
            textAndValueList.add(textAndValueMappingDTO);
        });
        ResultVO<List<TextAndValueMappingDTO>> result = new ResultVO<>();
        return result.ok(textAndValueList);
    }

    @Operation(summary = "获取OT屏层级列表")
    @GetMapping("getotdatas")
    public ResultVO<Map<String, Object>> getOtDatas() {
        PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();
        List<PmOtEntity> pmOtInfos = pmAllDTO.getOts();
        for (PmOtEntity otInfo : pmOtInfos) {
            PmWorkStationEntity workplaceInfo = pmAllDTO.getStations().stream()
                    .filter(c -> c.getId().equals(otInfo.getPrcPmWorkstationId()))
                    .findFirst().orElse(null);
            if (workplaceInfo != null) {
                otInfo.setOtName(workplaceInfo.getWorkstationName());
                otInfo.setRemark(String.valueOf(workplaceInfo.getPrcPmWorkshopId()));
            }
        }
        List<PmWorkStationEntity> pmStationInfos = pmAllDTO.getStations().stream()
                .sorted(Comparator.comparing(PmWorkStationEntity::getWorkstationNo))
                .sorted(Comparator.comparing(PmWorkStationEntity::getDirection))
                .collect(Collectors.toList());
        List<PmLineEntity> pmAreaInfos = pmAllDTO.getLines().stream().sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
        List<PmWorkShopEntity> pmShopInfos = pmAllDTO.getShops().stream().sorted(Comparator.comparing(PmWorkShopEntity::getDisplayNo)).collect(Collectors.toList());
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(5);
        map.put("pmShopInfos", pmShopInfos);
        map.put("pmAreaInfos", pmAreaInfos);
        map.put("pmStationInfos", pmStationInfos);
        map.put("pmOtInfos", pmOtInfos);
        ResultVO<Map<String, Object>> result = new ResultVO();
        return result.ok(map);
    }

    @Operation(summary = "追溯规则")
    @Parameter(name = "query", description = "关键字")
    @Deprecated
    @GetMapping("getcoderuledatas")
    public ResultVO<Void> getCodeRuleDatas(String query) {
        if (StrUtil.isBlank(query)) {
            query = "";
        }
        ResultVO<Void> result = new ResultVO<>();
        return result.ok(null);
    }

    @Operation(summary = "获取车间树")
    @Parameters({
            @Parameter(name = "shopCode", description = "车间编码"),
            @Parameter(name = "level", description = " 1: 只返回车间级    2: 返回到线体级    3:返回到工位")})
    @GetMapping(value = "gettree")
    public ResultVO getTree(String shopCode,Integer level) {
        if( level == null){
            level = 0;
        }
        if(level < 0 || level > 3){
            throw new InkelinkException("level只能是1到3");
        }
        ResultVO<List<PmWorkShopEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        return result.ok(this.pmService.getTree(shopCode,level));
    }



    @Operation(summary = "导出")
    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        pmService.export(null, response);
    }

    @Operation(summary = "导出车间")
    @Parameter(name = "id", description = "车间外键")
    @PostMapping(value = "export", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void export(@RequestBody ShopExcelDto dto, HttpServletResponse response) throws IOException {
        pmService.export(dto.getId(), response);
    }

    @Operation(summary = "获取导入模板")
    @GetMapping("downloadtemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        pmService.getImportTemplate(response);
    }

    @PostMapping(value = "import", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入")
    public ResultVO importExcel(MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile(req.getFileNames().next());
        pmService.importExcel(file.getInputStream());
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    @Operation(summary = "获取线体路径和工位信息")
    @Parameter(name = "shopCode", description = "车间外键")
    @GetMapping("getareastations")
    public ResultVO<List<TextValueStationsMappingDTO>> getAreaStations(String shopCode) {
        ResultVO<List<TextValueStationsMappingDTO>> result = new ResultVO();
        try {
            List<TextValueStationsMappingDTO> areaStations = pmService.getAreaStations(shopCode);
            return result.ok(areaStations, "获取数据成功");
        } catch (Exception e) {
            return result.error(-1, e.getMessage());
        }
    }

    @Operation(summary = "组件列表")
    @Parameter(name = "query", description = "关键字")
    @GetMapping("getdefectcomponetdatas")
    public ResultVO<List<ComponentDataDTO>> getDefectComponetDatas(String query) {
        if (query == null) {
            query = "";
        }
        List<ComponentDataDTO> data = pmService.getLocalComponent(query);
        ResultVO<List<ComponentDataDTO>> result = new ResultVO<>();
        return result.ok(data);
    }
}