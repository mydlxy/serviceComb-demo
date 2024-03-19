package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MaintainSearchDTO;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inkelink
 * @Description: 物料主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductmaterialmaster")
@Tag(name = "物料主数据")
public class PmProductMaterialMasterController extends BaseController<PmProductMaterialMasterEntity> {

    private final IPmProductMaterialMasterService pmProductMaterialMasterService;

    @Autowired
    public PmProductMaterialMasterController(IPmProductMaterialMasterService pmProductMaterialMasterService) {
        this.crudService = pmProductMaterialMasterService;
        this.pmProductMaterialMasterService = pmProductMaterialMasterService;
    }

    /**
     * 获取所有的数据
     *
     * @return List<PmProductMaterialMasterEntity>
     */
    @GetMapping(value = "/provider/getalldatas")
    @Operation(summary = "获取所有的数据")
    public ResultVO getAllDatas() {
        return new ResultVO().ok(pmProductMaterialMasterService.getAllDatas());
    }

//    @GetMapping(value = "/getalldatastest")
//    @Operation(summary = "获取所有的数据")
//    public ResultVO getAllDatasTest() {
//        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 128);
//        List<PmProductMaterialMasterEntity> list = pmProductMaterialMasterService.getAllDatas();
//        String listString = JsonUtils.toJsonString(list);
//        byte[] listByte = listString.getBytes(StandardCharsets.UTF_8);
//
//        int length = Math.min(buffer.capacity(), listByte.length);
//        for (int i = 0; i < length; i++) {
//            buffer.put(listByte[i]);
//        }
//
//        return new ResultVO().ok(buffer.hashCode());
//    }

    /**
     * 获取物料信息
     *
     * @return PmProductMaterialMasterEntity
     */
    @GetMapping(value = "/provider/getbymaterialno")
    @Operation(summary = "获取物料信息")
    public ResultVO<PmProductMaterialMasterEntity> getByMaterialNo(String materialNo) {
        List<PmProductMaterialMasterEntity> datas = pmProductMaterialMasterService.getAllDatas();
        PmProductMaterialMasterEntity fdata = datas.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getMaterialNo(), materialNo))
                .findFirst().orElse(null);
        return new ResultVO<PmProductMaterialMasterEntity>().ok(fdata);
    }

    /**
     * 获取物料信息
     *
     * @return ComboDataDTO
     */
    @PostMapping(value = "getbymaterialnobykey")
    @Operation(summary = "获取物料信息")
    public ResultVO<List<ComboDataDTO>> getByMaterialNoByKey(@RequestBody MaintainSearchDTO para) {
        List<ComboDataDTO> result = new ArrayList<>();
        if (StringUtils.isBlank(para.getKey()) && StringUtils.isBlank(para.getSltMaterialNo())) {
            return new ResultVO<List<ComboDataDTO>>().ok(result, "获取成功");
        }
        List<PmProductMaterialMasterEntity> datas = pmProductMaterialMasterService.getAllDatas();
        List<PmProductMaterialMasterEntity> tmps = new ArrayList<>();
        if (!StringUtils.isBlank(para.getSltMaterialNo())) {
            List<String> arrs = Arrays.asList(para.getSltMaterialNo().split(","));
            tmps = datas.stream().filter(c -> arrs.contains(c.getMaterialNo())).collect(Collectors.toList());
            if (tmps == null) {
                tmps = new ArrayList<>();
            }
        }

        if (!StringUtils.isBlank(para.getKey())) {
            List<PmProductMaterialMasterEntity> tdata = datas.stream().filter(c ->
                            StringUtils.contains(c.getMaterialNo(), para.getKey())
                                    || StringUtils.contains(c.getMaterialCn(), para.getKey())).limit(50)
                    .collect(Collectors.toList());
            if (tdata != null && !tdata.isEmpty()) {
                tmps.addAll(tdata);
            }
        }

        result = tmps.stream().distinct().limit(20).map(c -> {
            ComboDataDTO vo = new ComboDataDTO();
            vo.setText(c.getMaterialCn());
            vo.setValue(c.getMaterialNo());
            vo.setLabel("[" + c.getMaterialNo() + "]" + c.getMaterialCn());
            return vo;
        }).collect(Collectors.toList());
        return new ResultVO<List<ComboDataDTO>>().ok(result, "获取成功");
    }

    /**
     * 获取前50条下拉列表
     *
     * @return
     */
    @Operation(summary = "获取前50条下拉列表")
    @Parameters({
            @Parameter(name = "key", description = "物料号"),
            @Parameter(name = "partGroup", description = "partGroup")})
    @GetMapping(value = "/getcombodata")
    public ResultVO getComboData(String key, String partGroup) {
        String msg = "确认成功";
        List<PmProductMaterialMasterEntity> allProductMaterialMasterList = pmProductMaterialMasterService.getAllDatas();
        if (allProductMaterialMasterList.isEmpty()) {
            return new ResultVO().ok(Collections.emptyList(), msg);
        }
        if (StringUtils.isBlank(key)) {
            key = "";
        }
        final String targetKey = key;
        List<PmProductMaterialMasterEntity> subProductMaterialMasterList;
        if (StringUtils.isBlank(partGroup)) {
            subProductMaterialMasterList = allProductMaterialMasterList.stream().filter(t -> t.getMaterialCn().contains(targetKey)
                            || t.getMaterialNo().contains(targetKey))
                    .sorted(Comparator.comparing(PmProductMaterialMasterEntity::getMaterialNo))
                    .limit(50)
                    .collect(Collectors.toList());

        } else {
            subProductMaterialMasterList = allProductMaterialMasterList.stream().filter(
                            t -> partGroup.equals(t.getPartGroup())
                                    && (t.getMaterialCn().contains(targetKey)
                                    || t.getMaterialNo().contains(targetKey)))
                    .sorted(Comparator.comparing(PmProductMaterialMasterEntity::getMaterialNo))
                    .limit(50)
                    .collect(Collectors.toList());
        }
        return getResult(msg, subProductMaterialMasterList);
    }

    @Operation(summary = "获取下拉列表")
    @Parameters({
            @Parameter(name = "partGroup", description = "partGroup")})
    @GetMapping(value = "/getcombodataall")
    public ResultVO getComboDataAll(String partGroup) {
        String msg = "确认成功";
        List<PmProductMaterialMasterEntity> allProductMaterialMasterList = pmProductMaterialMasterService.getAllDatas();
        if (allProductMaterialMasterList.isEmpty()) {
            return new ResultVO().ok(Collections.emptyList(), msg);
        }
        List<PmProductMaterialMasterEntity> subProductMaterialMasterList;
        if (StringUtils.isBlank(partGroup)) {
            subProductMaterialMasterList = allProductMaterialMasterList.stream()
                    .sorted(Comparator.comparing(PmProductMaterialMasterEntity::getMaterialNo))
                    .collect(Collectors.toList());

        } else {
            subProductMaterialMasterList = allProductMaterialMasterList.stream().filter(
                            t -> partGroup.equals(t.getPartGroup()))
                    .sorted(Comparator.comparing(PmProductMaterialMasterEntity::getMaterialNo))
                    .collect(Collectors.toList());
        }
        return getResult(msg, subProductMaterialMasterList);
    }

    private ResultVO getResult(String msg, List<PmProductMaterialMasterEntity> subProductMaterialMasterList) {
        if (subProductMaterialMasterList.isEmpty()) {
            return new ResultVO().ok(Collections.emptyList(), msg);
        }
        List<ComboInfoDTO> targetList = new ArrayList<>(subProductMaterialMasterList.size());
        for (PmProductMaterialMasterEntity pmProductMaterialMasterEntity : subProductMaterialMasterList) {
            targetList.add(new ComboInfoDTO(pmProductMaterialMasterEntity.getMaterialCn(), pmProductMaterialMasterEntity.getMaterialNo()));
        }
        return new ResultVO().ok(targetList, msg);
    }


}