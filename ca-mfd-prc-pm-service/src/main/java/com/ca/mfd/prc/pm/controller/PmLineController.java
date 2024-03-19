package com.ca.mfd.prc.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmAviMapper;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 生产线体
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmline")
@Tag(name = "生产线体1")
public class PmLineController extends PmBaseController<PmLineEntity> {

    private final IPmLineService pmLineService;

    private final IPmVersionService pmVersionService;



    @Autowired
    public PmLineController(IPmLineService pmLineService,
                            IPmVersionService pmVersionService,
                            IPmAviMapper pmAviMapper) {
        this.crudService = pmLineService;
        this.pmVersionService = pmVersionService;
        this.pmLineService = pmLineService;
    }

    @Operation(summary = "根据线体id 查询该线体下的所有班组")
    @Parameters({
            @Parameter(name = "pmshopId", description = "车间编号")})
    @GetMapping(value = "getpmarea")
    public ResultVO getPmArea(Long pmshopId) {
        //通过车间得到所有的班组信息通过班组信息得到线体信息
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        PmAllDTO allVersion = this.pmVersionService.getObjectedPm();
        final Set<Long> k;
        final List<PmLineEntity> areasByShopIdAndAreaIdList;
        final List<ComboInfoDTO> all;
        if (pmshopId == null) {
            k = allVersion.getStations().stream()
                    .filter(item -> item.getPrcPmWorkshopId().equals(pmshopId))
                    .map(PmWorkStationEntity::getPrcPmLineId)
                    .collect(Collectors.toSet());
            areasByShopIdAndAreaIdList = allVersion.getLines().stream()
                    .filter(item -> item.getPrcPmWorkshopId().equals(pmshopId)
                            && k.contains(item.getId()))
                    .collect(Collectors.toList());
            all = new ArrayList<>(areasByShopIdAndAreaIdList.size());
            if (!areasByShopIdAndAreaIdList.isEmpty()) {
                for (PmLineEntity pmAreaEntity : areasByShopIdAndAreaIdList) {
                    all.add(new ComboInfoDTO(pmAreaEntity.getLineName(), String.valueOf(pmAreaEntity.getId())));
                }
            }
            result.ok(all);
        } else {
            k = allVersion.getStations().stream()
                    .map(PmWorkStationEntity::getPrcPmLineId)
                    .collect(Collectors.toSet());
            areasByShopIdAndAreaIdList = allVersion.getLines().stream()
                    .filter(item -> k.contains(item.getId()))
                    .collect(Collectors.toList());
            all = new ArrayList<>(areasByShopIdAndAreaIdList.size());
            if (areasByShopIdAndAreaIdList.isEmpty()) {
                return result.ok(Collections.emptyList());
            }
            for (PmLineEntity pmAreaEntity : areasByShopIdAndAreaIdList) {
                all.add(new ComboInfoDTO(pmAreaEntity.getLineName(), pmAreaEntity.getLineCode() + "[" + pmAreaEntity.getLineName() + "]"));
            }
            result.ok(all);
        }
        return result;
    }

    @Operation(summary = "获取当前线体所在车间的avi点")
    @Parameters({
            @Parameter(name = "pmshopId", description = "车间ID")})
    @GetMapping(value = "getavis")
    public ResultVO getAvis(Long pmshopId) {
        if(pmshopId == null || pmshopId == 0){
            throw new InkelinkException("pmshopId不能为空");
        }
        ResultVO<List<PmAviEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
       return result.ok(this.pmLineService.getAvis(pmshopId));
    }

}