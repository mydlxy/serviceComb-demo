package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.pm.dto.ComponentDataDTO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmInfo;
import com.ca.mfd.prc.pm.dto.TextValueStationsMappingDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import com.ca.mfd.prc.pm.excel.PmAllModel;
import com.ca.mfd.prc.pm.excel.PmSheetTableName;
import com.ca.mfd.prc.pm.mapper.IPmTraceComponentMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pm.remote.app.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pm.remote.app.pqs.provider.PqsLogicProvider;
import com.ca.mfd.prc.pm.service.IPmAviParentService;
import com.ca.mfd.prc.pm.service.IPmBopService;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import com.ca.mfd.prc.pm.service.IPmEquipmentPowerService;
import com.ca.mfd.prc.pm.service.IPmEquipmentService;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
import com.ca.mfd.prc.pm.service.IPmOtService;
import com.ca.mfd.prc.pm.service.IPmPullCordService;
import com.ca.mfd.prc.pm.service.IPmService;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import com.ca.mfd.prc.pm.service.IPmToolService;
import com.ca.mfd.prc.pm.service.IPmTraceComponentService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import com.ca.mfd.prc.pm.service.IPmWorkStationOperBookService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.ca.mfd.prc.common.constant.Constant.AVI_TYPE;
import static com.ca.mfd.prc.common.constant.Constant.VEHICLE_MODEL;

/**
 * @author luowenbing
 * @Description: PmServiceImpl class
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */

@Service
public class PmServiceImpl implements IPmService {


    private static final String[] NUMBER_FIELD_NAMES_LINE = {"stationCount", "stationLength", "lineLength", "lineDesignJph", "productTime", "lineProductTime"};
    private static final String[] NUMBER_FIELD_NAMES_STATION = {"beginDistance", "endDistance", "alarmDistance", "productTime"};
    private static final String[] NUMBER_FIELD_NAMES_EQUIPMENT = {"standardValue", "maxValue", "minValue"};
    private static final String[] NUMBER_FIELD_NAMES_BOP = {"processNo","woOperType","woDisplayNo","toolType","toolNetType",
            "beginTime","endTime","processCount","processType","materialQuantity"};
    private static final String[] EQUIPMENT_POWER_FIELD_NAMES = {"powerType","standardValue","maxValue","minValue","unit"};
    private static final String LINE_CODE = "lineCode";
    private static final String WORKSTATION_CODE = "workstationCode";
    private static final String EQUIPMENT_NAME = "equipmentName";
    private static final String EQUIPMENT_CODE = "equipmentCode";
    private static final String EQUIPMENT_POWER_TYP = "powerType";
    private static final String AVI_FEATURES = "Avifeatures";
    private static final String AVI_FUNCTION = "aviFunction";
    private static final String PULL_CORD_STOP_TYPE = "pullcordStopType";
    private static final String PULL_CORD = "pullcord";
    private static final String OT_TEMPLATE = "OTTemplate";
    private static final String WO_TYPE = "woType";
    private static final String WO_OPER_TYPE = "WoOperType";
    private static final String TOOL_TYPE = "toolType";
    private static final String TOOL_BRAND = "toolBrand";
    private static final String TOOL_COMM_TYPE = "toolCommType";
    private static final String EQUIPMENT_POWER_TYPE = "equipmentPower";
    private static final String EQUIPMENT_POWER_UNIT = "equipmentPowerUnit";
    private static final String BOOK_TYPE = "PmWoBookType";


    private final static Logger logger = LoggerFactory.getLogger(PmServiceImpl.class);
    private final IPmWorkShopService pmShopService;
    private final IPmLineService pmLineService;
    private final IPmAviParentService pmAviService;
    private final IPmWorkStationService pmStationService;
    private final IPmOtService pmOtService;
    private final IPmWoService pmWoService;
    private final IPmToolService pmToolService;
    private final IPmPullCordService pmPullCordService;
    private final IPmOrganizationService pmOrgService;
    private final IPmToolJobService pmToolJobService;
    private final IPmBopService pmBopService;
    private final IPmEquipmentService pmEquipmentService;
    private final IPmEquipmentPowerService pmEquipmentPowerService;
    private final IPmWorkstationMaterialService pmWorkstationMaterialService;
    private final SysConfigurationProvider sysConfigurationService;
    private final IPmOrganizationService pmOrganizationService;
    private final IPmTraceComponentService pmTraceComponentService;
    private final IPmCharacteristicsDataService pmCharacteristicsDataService;
    private final IPmWorkStationOperBookService pmWorkStationOperBookService;
    private final PqsLogicProvider pqsLogicService;


    @Autowired
    public PmServiceImpl(IPmWorkShopService pmShopService,
                         IPmLineService pmLineService,
                         IPmAviParentService pmAviParentService,
                         IPmWorkStationService pmStationService,
                         SysConfigurationProvider sysConfigurationService,
                         IPmToolJobService pmToolJobService,
                         IPmOtService pmOtService,
                         IPmWoService pmWoService,
                         IPmToolService pmToolService,
                         IPmPullCordService pmPullCordService,
                         IPmOrganizationService pmOrgService,
                         IPmBopService pmBopService,
                         IPmOrganizationService pmOrganizationService,
                         IPmTraceComponentService pmTraceComponentService,
                         IPmCharacteristicsDataService pmCharacteristicsDataService,
                         IPmWorkstationMaterialService pmWorkstationMaterialService,
                         PqsLogicProvider pqsLogicService,
                         IPmEquipmentService pmEquipmentService,
                         IPmEquipmentPowerService pmEquipmentPowerService,
                         IPmWorkStationOperBookService pmWorkStationOperBookService
    ) {
        this.pmShopService = pmShopService;
        this.pmLineService = pmLineService;
        this.pmAviService = pmAviParentService;
        this.pmStationService = pmStationService;
        this.sysConfigurationService = sysConfigurationService;
        this.pmToolJobService = pmToolJobService;
        this.pmOtService = pmOtService;
        this.pmWoService = pmWoService;
        this.pmToolService = pmToolService;
        this.pmPullCordService = pmPullCordService;
        this.pmOrgService = pmOrgService;
        this.pmBopService = pmBopService;
        this.pmOrganizationService = pmOrganizationService;
        this.pmCharacteristicsDataService = pmCharacteristicsDataService;
        this.pmTraceComponentService = pmTraceComponentService;
        this.pmWorkstationMaterialService = pmWorkstationMaterialService;
        this.pqsLogicService = pqsLogicService;
        this.pmEquipmentService = pmEquipmentService;
        this.pmEquipmentPowerService = pmEquipmentPowerService;
        this.pmWorkStationOperBookService = pmWorkStationOperBookService;
    }

    @Override
    public PmInfo getCurrentVersionShop(String shopCode) {
        PmInfo info = new PmInfo();
        PmWorkShopEntity shop = this.pmShopService.getPmShopEntityByCodeAndVersion(shopCode, 0, Boolean.FALSE);
        info.getWorkShops().add(shop != null ? shop : new PmWorkShopEntity());
        if (shop == null) {
            return info;
        }
        //获取线体
        List<PmLineEntity> lines = this.pmLineService.getByShopId(shop.getId());
        if (lines.isEmpty()) {
            return info;
        }
        info.setLines(lines);
        List<Long> lineIds = lines.stream().map(PmLineEntity::getId).collect(Collectors.toList());
        //获取avi列表
        List<PmAviEntity> avis = this.pmAviService.getByShopId(shop.getId());
        if (!avis.isEmpty()) {
            //筛选线体下所有avi
            avis = avis.stream().filter(s -> lineIds.contains(s.getPrcPmLineId())).collect(Collectors.toList());
            info.setAvis(avis);
        }
        //获取工位
        List<PmWorkStationEntity> stations = this.pmStationService.getByShopId(shop.getId());
        if (stations.isEmpty()) {
            return info;
        }
        //筛选线体下所有工位
        stations = stations.stream().filter(s -> lineIds.contains(s.getPrcPmLineId())).collect(Collectors.toList());
        if (stations.isEmpty()) {
            return info;
        }
        info.setWorkStations(stations);
        List<Long> stationIds = stations.stream().map(s -> s.getId()).collect(Collectors.toList());
        //获取andon列表
        List<PmPullCordEntity> pullcords = this.pmPullCordService.getByShopId(shop.getId());
        if (!pullcords.isEmpty()) {
            //筛选所有工位下所有andon
            pullcords = pullcords.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setPullCords(pullcords);
        }
        //获取OT
        List<PmOtEntity> ots = this.pmOtService.getByShopId(shop.getId());
        if (!ots.isEmpty()) {
            //筛选所有工位下所有ot
            ots = ots.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setOts(ots);
        }
        //获取wo
        List<PmWoEntity> wos = this.pmWoService.getByShopId(shop.getId());
        if (!wos.isEmpty()) {
            //获取wo
            wos = wos.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setWos(wos);
        }
        //获取material
        List<PmWorkstationMaterialEntity> materials = this.pmWorkstationMaterialService.getByShopId(shop.getId());
        if (!materials.isEmpty()) {
            //获取wo
            materials = materials.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setWorkstationMaterials(materials);
        }
        //获取工具
        List<PmToolEntity> tools = this.pmToolService.getByShopId(shop.getId());
        if (!tools.isEmpty()) {
            //获取tool
            tools = tools.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setTools(tools);
            List<Long> toolsIds = tools.stream().map(s -> s.getId()).collect(Collectors.toList());
            //获取job
            List<PmToolJobEntity> toolJobs = this.pmToolJobService.getByShopId(shop.getId());
            toolJobs = toolJobs.stream().filter(s -> toolsIds.contains(s.getPrcPmToolId())).collect(Collectors.toList());
            info.setToolJobs(toolJobs);
        }
        //获取bop
        List<PmBopEntity> bops = this.pmBopService.getByShopId(shop.getId());
        if (!bops.isEmpty()) {
            //获取wo
            bops = bops.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setBops(bops);
        }
        //获取设备
        List<PmEquipmentEntity> equipments = this.pmEquipmentService.getByShopId(shop.getId());
        if (!equipments.isEmpty()) {
            equipments = equipments.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setEquipments(equipments);
        }
        //获取设备能力
        List<PmEquipmentPowerEntity> equipmentPowers = this.pmEquipmentPowerService.getByShopId(shop.getId());
        if (!equipmentPowers.isEmpty()) {
            equipmentPowers = equipmentPowers.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setEquipmentPowers(equipmentPowers);
        }
        return info;
    }

    @Override
    public List<PmWorkShopEntity> getTree(String shopCode, int level) {
        QueryWrapper<PmWorkShopEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lqw = qw.lambda();
        lqw.eq(PmWorkShopEntity :: getVersion,0);
        if(StringUtils.isNotBlank(shopCode)){
            lqw.eq(PmWorkShopEntity :: getWorkshopCode,shopCode);
        }
        lqw.orderByAsc(PmWorkShopEntity :: getDisplayNo);
        List<PmWorkShopEntity> shops = this.pmShopService.getData(qw,false);
        if(level == 1){
            return shops;
        }
        List<Long> shopIds = shops.stream().map(PmWorkShopEntity :: getId).collect(Collectors.toList());
        if(level == 0 || level == 2 || level == 3){
            QueryWrapper<PmLineEntity> qwLine = new QueryWrapper<>();
            LambdaQueryWrapper<PmLineEntity> lqwLine = qwLine.lambda();
            lqwLine.in(PmLineEntity :: getPrcPmWorkshopId,shopIds);
            lqwLine.eq(PmLineEntity :: getVersion,0);
            lqwLine.orderByAsc(PmLineEntity :: getLineDisplayNo);
            List<PmLineEntity> lines = this.pmLineService.getData(qwLine,false);
            if(lines.isEmpty()){
                return shops;
            }
            Map<Long,List<PmLineEntity>> linesByShopId = new HashMap<>(shops.size());
            for(PmLineEntity pmLineEntity : lines){
                linesByShopId.computeIfAbsent(pmLineEntity.getPrcPmWorkshopId(),v -> new ArrayList<>()).add(pmLineEntity);
            }
            for(PmWorkShopEntity pmWorkShopEntity : shops){
                List<PmLineEntity> linesInWorkShop = linesByShopId.get(pmWorkShopEntity.getId());
                if(linesInWorkShop == null){
                    linesInWorkShop = new ArrayList<>();
                }
                pmWorkShopEntity.setPmLineEntity(linesInWorkShop);
            }
            if(level == 0 || level == 3){
                QueryWrapper<PmWorkStationEntity> qwStation = new QueryWrapper<>();
                LambdaQueryWrapper<PmWorkStationEntity> lqwStation = qwStation.lambda();
                lqwStation.in(PmWorkStationEntity :: getPrcPmWorkshopId,shopIds);
                lqwStation.eq(PmWorkStationEntity :: getVersion,0);
                lqwStation.orderByAsc(PmWorkStationEntity :: getWorkstationNo);
                lqwStation.orderByAsc(PmWorkStationEntity :: getDirection);
                List<PmWorkStationEntity> stations = this.pmStationService.getData(qwStation,false);
                if(stations.isEmpty()){
                    return shops;
                }
                Map<Long,List<PmWorkStationEntity>> stationsByLineId = new HashMap<>(lines.size());
                for(PmWorkStationEntity pmWorkStationEntity : stations){
                    stationsByLineId.computeIfAbsent(pmWorkStationEntity.getPrcPmLineId(),v -> new ArrayList<>()).add(pmWorkStationEntity);
                }
                for(PmLineEntity pmLineEntity : lines){
                    List<PmWorkStationEntity> stationsInLine = stationsByLineId.get(pmLineEntity.getId());
                    if(stationsInLine == null){
                        stationsInLine = new ArrayList<>();
                    }
                    pmLineEntity.setPmWorkStationEntity(stationsInLine);
                }
            }
        }
        return shops;

    }

    public PmAllDTO getShopsByVersion(PmAllDTO pmAllDTO, ConditionDto conditionDto) {
        pmAllDTO.getShops().addAll(this.pmShopService.getData(Arrays.asList(conditionDto)));
        //获取线体
        pmAllDTO.getLines().addAll(this.pmLineService.getData(Arrays.asList(conditionDto)));
        //获取avi列表
        pmAllDTO.getAvis().addAll(this.pmAviService.getData(Arrays.asList(conditionDto)));
        //获取工位
        pmAllDTO.getStations().addAll(this.pmStationService.getData(Arrays.asList(conditionDto)));
        //获取andon列表
        pmAllDTO.getPullCords().addAll(this.pmPullCordService.getData(Arrays.asList(conditionDto)));
        //获取OT
        pmAllDTO.getOts().addAll(this.pmOtService.getData(Arrays.asList(conditionDto)));
        //获取wo
        pmAllDTO.getWos().addAll(this.pmWoService.getData(Arrays.asList(conditionDto)));
        //获取material
        pmAllDTO.getMaterials().addAll(this.pmWorkstationMaterialService.getData(Arrays.asList(conditionDto)));
        //获取工具
        pmAllDTO.getTools().addAll(this.pmToolService.getData(Arrays.asList(conditionDto)));
        //获取bop
        pmAllDTO.getBops().addAll(this.pmBopService.getData(null));
        //获取设备
        pmAllDTO.getEquipments().addAll(this.pmEquipmentService.getData(Arrays.asList(conditionDto)));
        //获取设备能力
        pmAllDTO.getEquipmentPowers().addAll(this.pmEquipmentPowerService.getData(Arrays.asList(conditionDto)));
        //获取操作指导书
        pmAllDTO.getOperBooks().addAll(this.pmWorkStationOperBookService.getData(null));
        return pmAllDTO;
    }

    @Override
    public PmInfo getVersionShop(String shopCode, int version) {
        PmInfo info = new PmInfo();
        PmWorkShopEntity shop = getPmShopEntityByCodeAndVersion(shopCode, version);
        info.getWorkShops().add(shop != null ? shop : new PmWorkShopEntity());
        if (shop == null) {
            return info;
        }
        //获取线体
        List<PmLineEntity> lines = getPmLineList(shop.getId(), version);
        if (lines.isEmpty()) {
            return info;
        }
        info.setLines(lines);
        List<Long> lineIds = lines.stream().map(PmLineEntity::getId).collect(Collectors.toList());
        //获取avi列表
        List<PmAviEntity> avis = getPmAviList(shop.getId(), version);
        if (!avis.isEmpty()) {
            //筛选线体下所有avi
            avis = avis.stream().filter(s -> lineIds.contains(s.getPrcPmLineId())).collect(Collectors.toList());
            info.setAvis(avis);
        }
        //获取工位
        List<PmWorkStationEntity> stations = getPmWorkStationList(shop.getId(), version);
        if (stations.isEmpty()) {
            return info;
        }
        //筛选线体下所有工位
        stations = stations.stream().filter(s -> lineIds.contains(s.getPrcPmLineId())).collect(Collectors.toList());
        if (stations.isEmpty()) {
            return info;
        }
        info.setAllWorkStations(stations);
        List<Long> stationIds = stations.stream().map(s -> s.getId()).collect(Collectors.toList());
        //获取andon列表
        List<PmPullCordEntity> pullcords = getPmPullcordList(shop.getId(), version);
        if (!pullcords.isEmpty()) {
            //筛选所有工位下所有andon
            pullcords = pullcords.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setPullCords(pullcords);
        }
        //获取OT
        List<PmOtEntity> ots = getPmOtList(shop.getId(), version);
        if (!ots.isEmpty()) {
            //筛选所有工位下所有ot
            ots = ots.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setOts(ots);
        }
        //获取OW
        List<PmWoEntity> wos = getPmWoList(shop.getId(), version);
        if (!wos.isEmpty()) {
            //获取wo
            wos = wos.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setWos(wos);
        }
        //获取工具
        List<PmToolEntity> tools = getPmToolList(shop.getId(), version);
        if (!tools.isEmpty()) {
            //获取tool
            tools = tools.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setTools(tools);
            List<Long> toolsIds = tools.stream().map(s -> s.getId()).collect(Collectors.toList());
            //获取job
            List<PmToolJobEntity> toolJobs = getPmToolJobList(shop.getId(), version);
            toolJobs = toolJobs.stream().filter(s -> toolsIds.contains(s.getPrcPmToolId())).collect(Collectors.toList());
            info.setToolJobs(toolJobs);
        }
        //获取物料
        List<PmWorkstationMaterialEntity> materials = getPmWorkstationMaterialList(shop.getId(), version);
        if (!materials.isEmpty()) {
            //获取wo
            materials = materials.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setWorkstationMaterials(materials);
        }
        //获取操作指导书
        List<PmWorkstationOperBookEntity> books = getPmWorkstationOperBookList(shop.getId());
        if (!books.isEmpty()) {
            //获取wo
            books = books.stream().filter(s -> stationIds.contains(s.getPrcPmWorkstationId())).collect(Collectors.toList());
            info.setOperBooks(books);
        }
        return info;
    }

    private List<PmLineEntity> getPmLineList(Long shopId, int version) {
        List<PmLineEntity> areas = getPmAreaEntityByVersion(shopId, version);
        if (areas == null || areas.isEmpty()) {
            return Collections.emptyList();
        }
        areas.stream().sorted(Comparator.comparing(PmLineEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmLineEntity::getId));
        return areas;
    }

    private List<PmWorkStationEntity> getPmWorkStationList(Long shopId, int version, Boolean isDeleted) {
        List<PmWorkStationEntity> stations = this.pmStationService.getPmStationEntityByVersion(shopId, version, isDeleted);
        if (stations == null || stations.isEmpty()) {
            return Collections.emptyList();
        }
        stations.stream().sorted(Comparator.comparing(PmWorkStationEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmWorkStationEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmWorkStationEntity::getId));
        return stations;
    }

    private List<PmWorkStationEntity> getPmWorkStationList(Long shopId, int version) {
        return getPmWorkStationList(shopId, version, true);
    }

    private List<PmAviEntity> getPmAviList(Long shopId, int version) {
        List<PmAviEntity> avis = getPmAviEntityByVersion(shopId, version);
        if (avis == null || avis.isEmpty()) {
            return Collections.emptyList();
        }
        avis.stream().sorted(Comparator.comparing(PmAviEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmAviEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmAviEntity::getId));
        return avis;
    }

    private List<PmOtEntity> getPmOtList(Long shopId, int version) {
        List<PmOtEntity> ots = getPmOtEntityByVersion(shopId, version);
        if (ots == null || ots.isEmpty()) {
            return Collections.emptyList();
        }
        ots.stream().sorted(Comparator.comparing(PmOtEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmOtEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmOtEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmOtEntity::getId));
        return ots;
    }

    private List<PmWoEntity> getPmWoList(Long shopId, int version) {
        List<PmWoEntity> wos = getPmWoEntityByVersion(shopId, version);
        if (wos == null || wos.isEmpty()) {
            return Collections.emptyList();
        }
        wos.stream().sorted(Comparator.comparing(PmWoEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmWoEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmWoEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmWoEntity::getId));
        return wos;
    }

    private List<PmWorkstationMaterialEntity> getPmWorkstationMaterialList(Long shopId, int version) {
        List<PmWorkstationMaterialEntity> materials = getPmWorkstationMaterialEntityByVersion(shopId, version);
        if (materials == null || materials.isEmpty()) {
            return Collections.emptyList();
        }
        materials.stream().sorted(Comparator.comparing(PmWorkstationMaterialEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmWorkstationMaterialEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmWorkstationMaterialEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmWorkstationMaterialEntity::getId));
        return materials;
    }

    private List<PmWorkstationOperBookEntity> getPmWorkstationOperBookList(Long shopId) {
        List<PmWorkstationOperBookEntity> books = getPmWorkstationOperBookEntity(shopId);
        if (books == null || books.isEmpty()) {
            return Collections.emptyList();
        }
        books.stream().sorted(Comparator.comparing(PmWorkstationOperBookEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmWorkstationOperBookEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmWorkstationOperBookEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmWorkstationOperBookEntity::getId));
        return books;
    }

    private List<PmPullCordEntity> getPmPullcordList(Long shopId, int version) {
        List<PmPullCordEntity> pullcords = getPmPullCordEntityByVersion(shopId, version);
        if (pullcords == null || pullcords.isEmpty()) {
            return Collections.emptyList();
        }
        pullcords.stream().sorted(Comparator.comparing(PmPullCordEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmPullCordEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmPullCordEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmPullCordEntity::getId));
        return pullcords;
    }

    private List<PmToolEntity> getPmToolList(Long shopId, int version) {
        List<PmToolEntity> tools = getPmToolEntityByVersion(shopId, version);
        if (tools == null || tools.isEmpty()) {
            return Collections.emptyList();
        }
        tools.stream().sorted(Comparator.comparing(PmToolEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmToolEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmToolEntity::getPrcPmWorkstationId))
                .sorted(Comparator.comparing(PmToolEntity::getId));
        return tools;
    }

    private List<PmToolJobEntity> getPmToolJobList(Long shopId, int version) {
        List<PmToolJobEntity> toolJobs = getPmToolJobEntityByVersion(shopId, version);
        if (toolJobs == null || toolJobs.isEmpty()) {
            return Collections.emptyList();
        }
        toolJobs.stream().sorted(Comparator.comparing(PmToolJobEntity::getPrcPmWorkshopId))
                .sorted(Comparator.comparing(PmToolJobEntity::getPrcPmLineId))
                .sorted(Comparator.comparing(PmToolJobEntity::getId));
        return toolJobs;
    }

    private Map<String, Map<String, String>> pmDic() {

        Map<String, Map<String, String>> map = new LinkedHashMap<>();

        Map<String, String> pmShopMap = new LinkedHashMap<>(6);

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getWorkshopCode), "代码");

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getWorkshopName), "名称");

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getWorkshopDesignJph), "JPH");

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getProductTime), "生产L/T(分钟)");

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getVehicleCount), "标准在制");

        pmShopMap.put(MpSqlUtils.getColumnName(PmWorkShopEntity::getRemark), "备注");

        map.put(PmSheetTableName.PM_SHOP, pmShopMap);


        Map<String, String> pmAviMap = new LinkedHashMap<>(15);

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getPrcPmWorkshopId), "区域代码");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getPrcPmLineId), "工作中心代码");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getAviCode), "编码");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getAviName), "名称");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getAviType), "站点类型");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getIsEnable), "是否启用");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getAviAttribute), "特性");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getIsMain), "关键点");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getIpAddress), "AVI站点地址");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getOpcConnector), "PLC链接");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getPointDb), "站点DB");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getAviFunctions), "AVI功能");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getDefaultPage), "AVI默认页面");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getRemark), "备注");

        pmAviMap.put(MpSqlUtils.getColumnName(PmAviEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_AVI, pmAviMap);


        Map<String, String> pmAreaMap = new LinkedHashMap<>(35);

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkshopCode), "区域代码");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkshopName), "区域名称");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkshopDesignJph), "区域JPH");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkshopProductTime), "区域生产L/T（分钟）");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineCode), "线体代码");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineName), "线体名称");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getStationCount), "线体工位数");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getStationLength), "线体工位长度(cm)");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineType), "线体类型");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getRunType), "线体运行模式");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineLength), "线体长度(cm)");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineDesignJph), "线体JPH");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineProductTime), "线体生产L/T(分钟)");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getOpcConnect), "线体链接");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getQueueDb), "队列DB");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getStationDb), "岗位DB");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getAndonOpcConnect), "安灯链接");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getAndonDb), "安灯DB");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineIsEnable), "线体是否启用");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getLineIsDelete), "线体是否删除");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkstationCode), "工位代码");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkstationName), "工位名称");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkstationType), "工位类型");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkstationNo), "工位号");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getDirection), "方位");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getTeamNo), "班组");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getBeginDistance), "工位开始");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getEndDistance), "工位结束");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getAlarmDistance), "工位预警距离");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getWorkstationProductTime), "工位生产L/T(S)");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getStationIsEnable), "工位是否启用");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getStationIsDelete), "工位是否删除");

        pmAreaMap.put(MpSqlUtils.getColumnName(PmAllModel::getRemark), "备注");

        map.put(PmSheetTableName.PM_ALL, pmAreaMap);


        Map<String, String> pmPullCordMap = new LinkedHashMap<>(10);

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getPrcPmWorkshopId), "车间代码");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getPrcPmLineId), "线体代码");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getPrcPmWorkstationId), "工位代码");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getPullcordName), "名称");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getType), "拉绳类型");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getStopType), "停线类型");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getTimeDelay), "延时");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getIsEnable), "是否启用");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getRemark), "备注");

        pmPullCordMap.put(MpSqlUtils.getColumnName(PmPullCordEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_PULL_CORD, pmPullCordMap);


        Map<String, String> pmOtMap = new LinkedHashMap<>(10);

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getPrcPmWorkshopId), "车间代码");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getPrcPmLineId), "线体代码");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getPrcPmWorkstationId), "工位代码");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getOtName), "名称");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getTemplate), "模板");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getIpAddress), "IP地址");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getOtDescription), "描述");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getIsEnable), "是否启用");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getRemark), "备注");

        pmOtMap.put(MpSqlUtils.getColumnName(PmOtEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_OT, pmOtMap);


        Map<String, String> pmWOMap = new LinkedHashMap<>(13);

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getPrcPmWorkshopId), "车间代码");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getPrcPmLineId), "线体代码");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getPrcPmWorkstationId), "工位代码");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getDisplayNo), "顺序号");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoCode), "编码");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoType), "类型");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getOperType), "操作类型");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoGroupName), "操作组");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getQmDefectComponentId), "组件");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getQmDefectAnomalyCode), "缺陷代码");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getQmDefectAnomalyDescription), "缺陷名称");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getTrcByGroup), "批量追溯");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getFeatureCode), "特征");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoDescription), "描述");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getIsEnable), "是否启用");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getRemark), "备注");

        pmWOMap.put(MpSqlUtils.getColumnName(PmWoEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_WO, pmWOMap);


        Map<String, String> pmMaterialMap = new LinkedHashMap<>(7);

        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getPrcPmWorkshopId), "车间代码");

        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getPrcPmLineId), "线体代码");

        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getPrcPmWorkstationId), "工位代码");

        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getAttribute1), "BOM行标识");
        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getMaterialNo), "物料编码");
        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getMasterChinese), "物料名称");
        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getFeatureCode), "使用规则");
        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getMaterialNum), "工位用量");
        pmMaterialMap.put(MpSqlUtils.getColumnName(PmWorkstationMaterialEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_MATERIAL, pmMaterialMap);


        Map<String, String> pmToolMap = new LinkedHashMap<>(13);

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getPrcPmWorkshopId), "车间代码");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getPrcPmLineId), "线体代码");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getPrcPmWorkstationId), "工位代码");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolCode), "工具号");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolName), "名称");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolType), "工具类型");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getBrand), "品牌");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getNetType), "交互方式");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIp), "IP");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getPort), "DB端口");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getRemark), "备注");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIsEnable), "是否启用");

        pmToolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_TOOL, pmToolMap);


        Map<String, String> pmToolJobMap = new LinkedHashMap<>(15);

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getPrcPmWorkshopId), "车间代码");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getPrcPmLineId), "线体代码");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getPrcPmToolId), "工具号");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getJobNo), "参数号");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getPmWoId), "操作");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getFeatureCode), "特征");

        pmToolJobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_TOOL_JOB, pmToolJobMap);


        Map<String, String> pmEquipmentPowerMap = new LinkedHashMap<>(17);
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getId), "设备ID");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getWorkstationCode), "工位编码");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getEquipmentCode), "设备编码");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getEquipmentName), "设备名称");

        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getPowerType), "设备能力");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getStandardValue), "标准值");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getMaxValue), "最大值");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getMinValue), "最小值");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getAttribute1), "平均值");
        pmEquipmentPowerMap.put(MpSqlUtils.getColumnName(PmEquipmentPowerEntity::getUnit), "单位");

        map.put(PmSheetTableName.PM_EQUIPMENT_AND_POWER, pmEquipmentPowerMap);


        Map<String, String> pmBopMap = new LinkedHashMap<>(40);

        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWorkshopCode), "车间代码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getLineCode), "线体代码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWorkstationCode), "工位代码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessNo), "工序");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessRemark), "工序描述");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessStep), "作业步骤");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getFeatureCode), "特征表达式");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoCode), "操作编码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoQmDefectAnomalyCode), "缺陷代码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoQmDefectComponentCode), "组件代码");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoTrcByGroup), "批量追溯");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoGroupName), "分组");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessType), "工艺类型");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoOperType), "操作类型");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getActionRemark), "动作描述");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessObjects), "作业对象");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialNo), "零件编号");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialName), "零件名称");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialQuantity), "零件用量");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEquipmentCode), "设备编号");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEquipmentName), "设备名称");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolCode), "工具编号");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolName), "工具名称");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolType), "工具类型");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolBrand), "工具品牌");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolNetType), "工具交互类型");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolIp), "工具IP");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolPort), "端口号");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getJob), "JOB号");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getActionImage), "操作图册文件名");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialImage), "零件图册文件名");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getBeginTime), "时序开始时间");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEndTime), "时序结束时间");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessCount), "作业次数");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getSpecialRequest), "特殊要求");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getPfmeaFile), "PFMEA文件");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getControlFile), "控制计划");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getIsEnable), "是否启用");
        pmBopMap.put(MpSqlUtils.getColumnName(PmBopEntity::getIsDelete), "是否删除");
        map.put(PmSheetTableName.PM_BOP, pmBopMap);

        Map<String, String> pmBookOperMap = new LinkedHashMap<>(10);

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getPrcPmWorkshopId), "车间代码");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getPrcPmLineId), "线体代码");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getPrcPmWorkstationId), "工位代码");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getWoBookName), "操作名称");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getWoBookPath), "文件地址");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getWoBookPath), "IP地址");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getMaterialNo), "物料号");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getMaterialName), "物料名称");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getRemark), "备注");

        pmBookOperMap.put(MpSqlUtils.getColumnName(PmWorkstationOperBookEntity::getIsDelete), "是否删除");

        map.put(PmSheetTableName.PM_BOOK_OPR, pmBookOperMap);

        return map;

    }

    @Override
    public void export(Long shopId, HttpServletResponse response) throws IOException {
        PmWorkShopEntity shop = pmShopService.getByShopId(shopId).stream().findFirst().orElse(null);
        List<PmLineEntity> lineList = pmLineService.getByShopId(shopId);
        List<PmAviEntity> aviList = pmAviService.getByShopId(shopId);
        List<PmWorkStationEntity> stationList = pmStationService.getByShopId(shopId);
        List<PmPullCordEntity> pullCordList = pmPullCordService.getByShopId(shopId);
        List<PmOtEntity> otList = pmOtService.getByShopId(shopId);
        List<PmWoEntity> woList = pmWoService.getByShopId(shopId);
        List<PmToolEntity> toolList = pmToolService.getByShopId(shopId);
        List<PmToolJobEntity> toolJobList = pmToolJobService.getByShopId(shopId);
        List<PmWorkstationMaterialEntity> stationMaterialList = pmWorkstationMaterialService.getByShopId(shopId);
        List<PmWorkstationOperBookEntity> stationOperBookList = pmWorkStationOperBookService.getPmWorkstationEntity(shopId);
        List<PmBopEntity> bopList = pmBopService.getByShopId(shopId);
        List<PmEquipmentPowerEntity> equipmentPowerList = pmEquipmentPowerService.getEquipmentPowersByShopId(shopId);
        List<PmTraceComponentEntity> componentList = pmTraceComponentService.getAllDatas();
        List<SysConfigurationEntity> sysConfigurationAllDatas = sysConfigurationService.getAllDatas();
        List<PmAllModel> allExcelDatas = pmLineService.getAllExcelDatas(String.valueOf(shopId));
        Map<String, Map<String, String>> configTextAndValueByCategoryMap = getTextAndValueGroupByCategory(sysConfigurationAllDatas);
        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
        Map<String, Map<String, String>> pmDic = pmDic();
        for (Map.Entry<String, Map<String, String>> item : pmDic.entrySet()) {
            switch (item.getKey()) {
                case PmSheetTableName.PM_SHOP: {
                    List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(Arrays.asList(shop));
                    for (Map<String, Object> eachRow : excelDatas) {
                        setValue(eachRow,configTextAndValueByCategoryMap,"shopCode","workshopCode");
                    }
                    mapLists.add(excelDatas);
                    break;
                }
                case PmSheetTableName.PM_AVI: {
                    mapLists.add(generateAviDataForExcel(aviList, shop, lineList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_ALL: {
                    mapLists.add(InkelinkExcelUtils.getListMap(allExcelDatas));
                    break;
                }
                case PmSheetTableName.PM_PULL_CORD: {
                    mapLists.add(generatePullCordDataForExcel(pullCordList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_OT: {
                    mapLists.add(generateOtDataForExcel(otList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_WO: {
                    mapLists.add(generateWoDataForExcel(woList, shop, lineList, stationList, componentList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_EQUIPMENT_AND_POWER: {
                    mapLists.add(generateEquipmentPowerDataForExcel(equipmentPowerList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_MATERIAL: {
                    mapLists.add(generateDataForExcel(stationMaterialList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_BOOK_OPR: {
                    mapLists.add(generateDataForExcel(stationOperBookList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_TOOL: {
                    mapLists.add(generateToolDataForExcel(toolList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_TOOL_JOB: {
                    mapLists.add(generateToolJobDataForExcel(toolJobList, shop, lineList,toolList,woList,configTextAndValueByCategoryMap));
                    break;
                }
                case PmSheetTableName.PM_BOP: {
                    List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(bopList);
                    for (Map<String, Object> eachRow : excelDatas) {
                        setValue(eachRow,configTextAndValueByCategoryMap,"shopCode","workshopCode");
                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_TYPE,"toolType");
                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_BRAND,"toolBrand");
                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_COMM_TYPE,"toolNetType");
                        setValue(eachRow,configTextAndValueByCategoryMap,WO_TYPE,"processType");
                        setValue(eachRow,configTextAndValueByCategoryMap,WO_OPER_TYPE,"woOperType");
                    }
                    mapLists.add(excelDatas);
                    break;
                }
                default:break;
            }
        }
        List<String> sheetNames = new ArrayList<>(pmDic.keySet());
        List<Map<String, String>> fieldParam = new ArrayList<>(pmDic.values());
        InkelinkExcelUtils.exportSheets(sheetNames, fieldParam, mapLists, shop.getWorkshopName() + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C), response);
    }
//    @Override
//    public void export(Long shopId, HttpServletResponse response) throws IOException {
//        PmWorkShopEntity shop = pmShopService.getByShopId(shopId).stream().findFirst().orElse(null);
//        if (shop == null) {
//            throw new InkelinkException("未查询到车间");
//        }
//        List<PmWorkShopEntity> shopList = new ArrayList<>();
//        shopList.add(shop);
//        List<PmLineEntity> lineList = pmLineService.getByShopId(shopId);
//        List<PmAviEntity> aviList = new ArrayList<>();
//        List<PmWorkStationEntity> stationList = new ArrayList<>();
//        List<PmPullCordEntity> pullCordList = new ArrayList<>();
//        List<PmOtEntity> otList = new ArrayList<>();
//        List<PmWoEntity> woList = new ArrayList<>();
//        List<PmWorkstationMaterialEntity> materialList = new ArrayList<>();
//        List<PmToolEntity> toolList = new ArrayList<>();
//        List<ToolJobDto> toolJobList = new ArrayList<>();
//        List<PmBopEntity> bopList = new ArrayList<>();
//        List<PmEquipmentEntity> equipmentList = new ArrayList<>();
//        List<PmEquipmentPowerEntity> equipmentPowerList = new ArrayList<>();
//
//        List<PmAviEntity> aviListDb = pmAviService.getAllDatas();
//        List<PmWorkStationEntity> stationListDb = pmStationService.getAllDatas();
//        List<PmPullCordEntity> pullCordListDb = pmPullCordService.getAllDatas();
//        List<PmOtEntity> otListDb = pmOtService.getAllDatas();
//        List<PmWoEntity> woListDb = pmWoService.getAllDatas();
//        List<PmToolEntity> toolListDb = pmToolService.getAllDatas();
//        List<PmToolJobEntity> toolJobListDb = pmToolJobService.getAllDatas();
//        List<PmWorkstationMaterialEntity> stationMaterialListDb = pmWorkstationMaterialService.getAllDatas();
//        List<PmBopEntity> bopListDb = pmBopService.getAllDatas();
//        List<PmEquipmentEntity> equipmentListDb = pmEquipmentService.getAllDatas();
//        List<PmEquipmentPowerEntity> equipmentPowerListDb = pmEquipmentPowerService.getAllDatas();
//        List<PmTraceComponentEntity> componentList = pmTraceComponentService.getDataCache();
//        lineList.stream().forEach(c -> {
//            aviList.addAll(aviListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId())).collect(Collectors.toList()));
//            stationList.addAll(stationListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId())).collect(Collectors.toList()));
//            stationList.stream().forEach(x -> {
//                pullCordList.addAll(pullCordListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                otList.addAll(otListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                woList.addAll(woListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                equipmentPowerList.addAll(equipmentPowerListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                materialList.addAll(stationMaterialListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                List<PmToolEntity> temp = toolListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList());
//                toolList.addAll(temp);
//                bopList.addAll(bopListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmWorkstationId().equals(x.getId())).collect(Collectors.toList()));
//                temp.stream().forEach(t -> {
//                    toolJobList.addAll(toolJobListDb.stream().filter(a -> a.getPrcPmWorkshopId().equals(shopId) && a.getPrcPmLineId().equals(c.getId()) && a.getPrcPmToolId().equals(t.getId())).map(m -> {
//                        ToolJobDto toolJobDto = new ToolJobDto();
//                        BeanUtils.copyProperties(m, toolJobDto);
//                        toolJobDto.setPrcPmWorkshopCode(shop.getWorkshopCode());
//                        toolJobDto.setPrcPmLineCode(c.getLineCode());
//                        toolJobDto.setStationName(x.getWorkstationCode());
//                        toolJobDto.setToolCode(t.getToolCode());
//                        toolJobDto.setIsDelete(m.getIsDelete());
//                        toolJobDto.setWoCode(woList.stream().filter(v -> v.getId().equals(m.getPmWoId())).map(PmWoEntity::getWoCode).findFirst().orElse(""));
//                        return toolJobDto;
//                    }).collect(Collectors.toList()));
//                });
//
//            });
//        });
//
//        List<SysConfigurationEntity> sysConfigurationAllDatas = sysConfigurationService.getAllDatas();
//        List<PmAllModel> allExcelDatas = pmLineService.getAllExcelDatas(String.valueOf(shopId));
//        Map<String, Map<String, String>> configTextAndValueByCategoryMap = getTextAndValueGroupByCategory(sysConfigurationAllDatas);
//        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
//        Map<String, Map<String, String>> pmDic = pmDic();
//        for (Map.Entry<String, Map<String, String>> item : pmDic.entrySet()) {
//            switch (item.getKey()) {
//                case PmSheetTableName.PM_SHOP: {
//                    List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(shopList);
//                    for (Map<String, Object> eachRow : excelDatas) {
//                        setValue(eachRow,configTextAndValueByCategoryMap,"shopCode","workshopCode");
//                    }
//                    mapLists.add(excelDatas);
//                    break;
//                }
//                case PmSheetTableName.PM_AVI: {
//                    mapLists.add(generateAviDataForExcel(aviList, shop, lineList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_ALL: {
//                    mapLists.add(InkelinkExcelUtils.getListMap(allExcelDatas));
//                    break;
//                }
//                case PmSheetTableName.PM_PULL_CORD: {
//                    mapLists.add(generatePullCordDataForExcel(pullCordList, shop, lineList, stationList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_OT: {
//                    mapLists.add(generateOtDataForExcel(otList, shop, lineList, stationList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_WO: {
//                    mapLists.add(generateWoDataForExcel(woList, shop, lineList, stationList, componentList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_EQUIPMENT_AND_POWER: {
//                    mapLists.add(generateEquipmentPowerDataForExcel(equipmentPowerList, shop, lineList, stationList, equipmentList, configTextAndValueByCategoryMap));
//                    break;
//                }
//
//                case PmSheetTableName.PM_MATERIAL: {
//                    mapLists.add(generateMaterialDataForExcel(materialList, shop, lineList, stationList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_TOOL: {
//                    mapLists.add(generateToolDataForExcel(toolList, shop, lineList, stationList, configTextAndValueByCategoryMap));
//                    break;
//                }
//                case PmSheetTableName.PM_TOOL_JOB: {
//                    List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(toolJobList);
//                    for (Map<String, Object> eachRow : excelDatas) {
//                        setValue(eachRow,configTextAndValueByCategoryMap,"shopCode","workshopCode");
//                    }
//                    mapLists.add(excelDatas);
//                    break;
//                }
//                case PmSheetTableName.PM_BOP: {
//                    List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(bopList);
//                    for (Map<String, Object> eachRow : excelDatas) {
//                        setValue(eachRow,configTextAndValueByCategoryMap,"shopCode","workshopCode");
//                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_TYPE,"toolType");
//                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_BRAND,"toolBrand");
//                        setValue(eachRow,configTextAndValueByCategoryMap,TOOL_COMM_TYPE,"toolNetType");
//                        setValue(eachRow,configTextAndValueByCategoryMap,WO_TYPE,"processType");
//                        setValue(eachRow,configTextAndValueByCategoryMap,WO_OPER_TYPE,"woOperType");
//
//
//
//                    }
//                    mapLists.add(excelDatas);
//                    break;
//                }
//                default:break;
//            }
//        }
//        List<String> sheetNames = new ArrayList<>(pmDic.keySet());
//        List<Map<String, String>> fieldParam = new ArrayList<>(pmDic.values());
//        InkelinkExcelUtils.exportSheets(sheetNames, fieldParam, mapLists, shopList.get(0).getWorkshopName() + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C), response);
//    }


    private void setValue(Map<String, Object> eachRow,Map<String,
            Map<String, String>> configTextAndValueByCategoryMap,
            String type,
            String fieldName){
        String value = "";
        Map<String, String> mapping = configTextAndValueByCategoryMap.get(type);
        if (mapping != null && eachRow.get(fieldName) != null) {
            value = mapping.get(String.valueOf(eachRow.get(fieldName)));
        }
        eachRow.put(fieldName, StringUtils.isNotBlank(value) ? value : "");
    }


    private List<Map<String, Object>> generateAviDataForExcel(List<PmAviEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList,
                                                              Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        Map<String, String> aviTypeMap = configTextAndValueByCategoryMap.get(AVI_TYPE);
        Map<String, String> aviFeaturesMap = configTextAndValueByCategoryMap.get(AVI_FEATURES);
        Map<String, String> aviFunctionMap = configTextAndValueByCategoryMap.get(AVI_FUNCTION);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String aviType = "aviType";
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String aviAttributeField = "aviAttribute";
        String aviFunctionsField = "aviFunctions";
        String defaultPageField = "defaultPage";
        for (Map<String, Object> eachRow : excelDatas) {
            String typeName = "";
            if (aviTypeMap != null && eachRow.get(aviType) != null) {
                String[] str = eachRow.get(aviType).toString().split(",");
                String[] k = new String[str.length];
                int i = 0;
                for (String strItem : str) {
                    k[i] = aviTypeMap.get(strItem);
                    i++;
                }
                typeName = String.join(",", k);
            }
            eachRow.put(aviType, typeName != null ? typeName : "");

            String aviFunctionName = "";
            if (aviFunctionMap != null && eachRow.get(aviFunctionsField) != null) {
                String[] str = eachRow.get(aviFunctionsField).toString().split(",");
                List<String> k = new ArrayList<>();
                for (String strItem : str) {
                    if (aviFunctionMap.get(strItem) == null) {
                        continue;
                    }
                    k.add(aviFunctionMap.get(strItem));
                }
                aviFunctionName = String.join(",", k);
            }
            eachRow.put(aviFunctionsField, aviFunctionName != null ? aviFunctionName : "");
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");

            String aviFeaturesName = "";
            if (aviFeaturesMap != null && eachRow.get(aviAttributeField) != null) {
                aviFeaturesName = aviFeaturesMap.get(String.valueOf(eachRow.get(aviAttributeField)));
            }
            eachRow.put(aviAttributeField, aviFeaturesName != null ? aviFeaturesName : "");
            String defaultPageName = "";
            if (aviFunctionMap != null && eachRow.get(defaultPageField) != null) {
                defaultPageName = aviFunctionMap.get(String.valueOf(eachRow.get(defaultPageField)));
            }
            eachRow.put(defaultPageField, defaultPageName != null ? defaultPageName : "");
        }
        return excelDatas;
    }


    private List<Map<String, Object>> generatePullCordDataForExcel(List<PmPullCordEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                                   Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String stopTypeField = "stopType";
        String typeField = "type";
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,shop.getWorkshopCode() + PULL_CORD,typeField);
            setValue(eachRow,configTextAndValueByCategoryMap,PULL_CORD_STOP_TYPE,stopTypeField);
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put(stationField, stationName != null ? stationName : "");


        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateOtDataForExcel(List<PmOtEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                             Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        Map<String, String> otMap = configTextAndValueByCategoryMap.get(OT_TEMPLATE);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        String templateField = "template";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,OT_TEMPLATE,templateField);
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put(stationField, stationName != null ? stationName : "");

        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateWoDataForExcel(List<PmWoEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                             List<PmTraceComponentEntity> componentList,
                                                             Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String operTypeField = "operType";
        String woField = "woType";
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        String componentIdField = "qmDefectComponentId";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,WO_TYPE,woField);
            setValue(eachRow,configTextAndValueByCategoryMap,WO_OPER_TYPE,operTypeField);
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put(stationField, stationName != null ? stationName : "");
            String componentName = "";
            if (eachRow.get(componentIdField) != null) {
                componentName = componentList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(componentIdField)))).map(PmTraceComponentEntity::getTraceComponentCode).findFirst().orElse("");
            }
            eachRow.put(componentIdField, componentName != null ? componentName : "");


        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateEquipmentPowerDataForExcel(List<PmEquipmentPowerEntity> powerList,
                                                             Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(powerList);
        String powerTypeField = "powerType";
        String unitField = "unit";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,EQUIPMENT_POWER_TYPE,powerTypeField);
            setValue(eachRow,configTextAndValueByCategoryMap,EQUIPMENT_POWER_UNIT,unitField);
        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateDataForExcel(List<?> materialList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                             Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(materialList);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        for (Map<String, Object> eachRow : excelDatas) {
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put(stationField, stationName != null ? stationName : "");

        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateToolJobDataForExcel(List<PmToolJobEntity> materialList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmToolEntity> toolList,List<PmWoEntity> woList,
                                                                   Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(materialList);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String toolIdField = "prcPmToolId";
        String woIdField = "pmWoId";
        for (Map<String, Object> eachRow : excelDatas) {
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String toolCode = "";
            if (eachRow.get(toolIdField) != null) {
                toolCode = toolList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(toolIdField)))).map(PmToolEntity::getToolCode).findFirst().orElse("");
            }
            eachRow.put(toolIdField, toolCode != null ? toolCode : "");
            String woCode = "";
            if (eachRow.get(woIdField) != null) {
                woCode = woList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(woIdField)))).map(PmWoEntity::getWoCode).findFirst().orElse("");
            }
            eachRow.put(woIdField, woCode != null ? woCode : "");

        }
        return excelDatas;
    }


    private List<Map<String, Object>> generateToolDataForExcel(List<PmToolEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                               Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        Map<String, String> shopCodeMap = configTextAndValueByCategoryMap.get("shopCode");
        String netTypeField = "netType";
        String brandField = "brand";
        String toolTypeField = "toolType";
        String shopIdField = "prcPmWorkshopId";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,TOOL_TYPE,toolTypeField);
            setValue(eachRow,configTextAndValueByCategoryMap,TOOL_BRAND,brandField);
            setValue(eachRow,configTextAndValueByCategoryMap,TOOL_COMM_TYPE,netTypeField);
            String shopName = "";
            if (eachRow.get(shopIdField) != null) {
                shopName = String.valueOf(shop.getId()).equals(String.valueOf(eachRow.get(shopIdField))) ? shop.getWorkshopCode() : "";
                if (shopCodeMap != null && shopCodeMap.get(shopName) != null) {
                    shopName = shopCodeMap.get(shopName);
                }
            }
            eachRow.put(shopIdField, shopName != null ? shopName : "");
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put(lineIdField, lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put(stationField, stationName != null ? stationName : "");

        }
        return excelDatas;
    }


    private Map<String, Map<String, String>> getTextAndValueGroupByCategory(List<SysConfigurationEntity> configurationList) {
        if (configurationList.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> targetMap = new HashMap<>();
        for (SysConfigurationEntity sysConfigurationEntity : configurationList) {
            targetMap.computeIfAbsent(sysConfigurationEntity.getCategory(), v -> new HashMap<>()).put(sysConfigurationEntity.getValue(), sysConfigurationEntity.getText());
        }
        return targetMap;
    }


    @Override
    public void getImportTemplate(HttpServletResponse response) throws IOException {
        Map<String, Map<String, String>> dic = getPmDic();
        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
        InkelinkExcelUtils.exportSheets(new ArrayList<>(dic.keySet()), new ArrayList<>(dic.values()), mapLists, "pm_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C), response);
    }

    @Override
    public void importExcel(InputStream file) throws Exception {
        Map<String, Map<String, String>> pmDic = getPmDic();
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(file, pmDic.keySet()
                .toArray(new String[pmDic.keySet().size()]));
        if (datas.isEmpty()) {
            throw new InkelinkException("未读取到指定(Sheet)名称的表格,表格Sheet名称应该在[" + String.join(",", pmDic.keySet()) + "]内!");
        }
        Map<String, Map<String, String>> sysConfigTextAndValue = getSysConfigTextAndValueByCategory();
        Set<String> setOfDicKey = pmDic.keySet();
        for (String dicKey : setOfDicKey) {
            List<Map<String, String>> dataOfEachSheet = datas.get(dicKey);
            if (dataOfEachSheet == null || dataOfEachSheet.isEmpty()) {
                continue;
            }
            PmAllDTO pmAllDTO = getUnDeployData();
            List<Map<String, String>> listOfEachSheet = validImportDatas(dataOfEachSheet, pmDic.get(dicKey), dicKey);
            switch (dicKey) {
                case PmSheetTableName.PM_ALL:
                    //获取线体字段
                    Map<String, String> pmLineDic = pmLineService.getExcelHead();
                    List<Map<String, String>> listOfLineData = getFromFinalData(listOfEachSheet, pmLineDic.keySet());
                    //拍重
                    Collection<Map<String, String>> listOfUniqueLineData = getUniqueData(listOfLineData, LINE_CODE);
                    //验证数字类型字段对无值的设置初始值
                    validateAndSetDefaultVal(listOfUniqueLineData, Arrays.asList(NUMBER_FIELD_NAMES_LINE));
                    //设置开始结束距离
                    setLineProperty(listOfUniqueLineData);
                    this.pmLineService.importExcel(new ArrayList<>(listOfUniqueLineData), sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmLineService.saveChange();

                    //获取工位字段
                    Map<String, String> pmStationDic = pmStationService.getExcelHead();
                    List<Map<String, String>> listOfStationData = getFromFinalData(listOfEachSheet, pmStationDic.keySet());
                    //拍重
                    Collection<Map<String, String>> listOfUniqueStationData = getUniqueData(listOfStationData, WORKSTATION_CODE);
                    //验证数字类型字段对无值的设置初始值
                    validateAndSetDefaultVal(listOfUniqueStationData, Arrays.asList(NUMBER_FIELD_NAMES_STATION));
                    //设置工位名称
                    setWorkStationProperty(listOfUniqueStationData);
                    //更新
                    pmAllDTO = getUnDeployData();
                    this.pmStationService.importExcel(new ArrayList<>(listOfUniqueStationData), sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmStationService.saveChange();
                    break;
                case PmSheetTableName.PM_LINE:
                    this.pmLineService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmLineService.saveChange();
                    break;
                case PmSheetTableName.PM_STATION:
                    this.pmStationService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmStationService.saveChange();
                    break;
                case PmSheetTableName.PM_AVI:
                    this.pmAviService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmAviService.saveChange();
                    break;
                case PmSheetTableName.PM_PULL_CORD:
                    this.pmPullCordService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmPullCordService.saveChange();
                    break;
                case PmSheetTableName.PM_OT:
                    this.pmOtService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmOtService.saveChange();
                    break;
                case PmSheetTableName.PM_WO:
                    throw new InkelinkException("工艺配置请从BOP配置里导入");
//                    importWo(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
//                    this.pmWoService.saveChange();
                case PmSheetTableName.PM_MATERIAL:
                    importMaterial(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmWorkstationMaterialService.saveChange();
                    break;
                case PmSheetTableName.PM_BOOK_OPR:
//                    importBookOper(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
//                    this.pmWorkStationOperBookService.saveChange();
                    throw new InkelinkException("操作指导书配置请从BOP配置里导入");
                case PmSheetTableName.PM_TOOL:
//                    importTool(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
//                    this.pmToolService.saveChange();
                    throw new InkelinkException("工具配置请从BOP配置里导入");
                case PmSheetTableName.PM_TOOL_JOB:
//                    importToolJob(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
//                    this.pmToolJobService.saveChange();
                    throw new InkelinkException("工具Job请从BOP配置里导入");
                case PmSheetTableName.PM_EQUIPMENT_AND_POWER:
                    //获取设备字段
                    Map<String, String> pmEquipmentDic = pmEquipmentService.getExcelHead();
                    List<Map<String, String>> listOfEquipmentData = getFromFinalData(listOfEachSheet, pmEquipmentDic.keySet());
                    //去重全厂唯一
                    Collection<Map<String, String>> listOfUniqueEquipmentDicData = getUniqueData(listOfEquipmentData,EQUIPMENT_CODE);
                    this.pmEquipmentService.importExcel(new ArrayList<>(listOfUniqueEquipmentDicData), sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmEquipmentService.saveChange();
                    //获取设备能力字段
                    Map<String, String> pmEquipmentPowerDic = pmEquipmentPowerService.getExcelHead();
                    List<Map<String, String>> listOfEquipmentPowerData = getFromFinalData(listOfEachSheet, pmEquipmentPowerDic.keySet());
                    //获取设备能力相关字段不为空的数据
                    listOfEquipmentPowerData = filterEquipmentPowerEmptyData(listOfEquipmentPowerData);
                    //去重
                    Collection<Map<String, String>> listOfUniqueEquipmentPowerData = getUniqueData(listOfEquipmentPowerData,EQUIPMENT_CODE,EQUIPMENT_POWER_TYP);

                    if(!listOfUniqueEquipmentPowerData.isEmpty()){
                        listOfEquipmentPowerData = new ArrayList<>(listOfUniqueEquipmentPowerData);
                        //验证数字类型字段对无值的设置初始值
                        validateAndSetDefaultVal(listOfEquipmentPowerData, Arrays.asList(NUMBER_FIELD_NAMES_EQUIPMENT));
                        this.pmEquipmentPowerService.importExcel(listOfEquipmentPowerData, sysConfigTextAndValue, dicKey, pmAllDTO);
                        this.pmEquipmentPowerService.saveChange();
                    }
                    break;
                case PmSheetTableName.PM_BOP:
                    throw new InkelinkException("Bop请从BOP配置里导入");
                    //验证数字类型字段对无值的设置初始值

                    /*
                    validateAndSetDefaultVal(listOfEachSheet, Arrays.asList(NUMBER_FIELD_NAMES_BOP));
                    setQmDefectAnomalyDescription(listOfEachSheet);
                    setQmDefectComponentDescription(listOfEachSheet);
                    */

//                    Map<String,List<Map<String, String>>> listOfDataByBusinessType = pmBopService.getSeparateList(listOfEachSheet);
//                    if(!listOfDataByBusinessType.isEmpty()){
//                        //获取wo
//                        List<Map<String, String>> listOfWoData = listOfDataByBusinessType.get("wo");
//                        if(listOfWoData != null && !listOfWoData.isEmpty()){
//                            importWo(listOfWoData, sysConfigTextAndValue, dicKey, pmAllDTO);
//                        }
//                        this.pmWoService.saveChange();
//                        //获取material
//                        List<Map<String, String>> listOfMaterialData = listOfDataByBusinessType.get("material");
//                        if(listOfMaterialData != null && !listOfMaterialData.isEmpty()){
//                            importMaterial(listOfMaterialData, sysConfigTextAndValue, dicKey, pmAllDTO);
//                        }
//                        this.pmWorkstationMaterialService.saveChange();
//                        //获取TOOL
//                        List<Map<String, String>> listOfToolData = listOfDataByBusinessType.get("tool");
//                        if(listOfToolData != null && !listOfToolData.isEmpty()){
//                            importTool(listOfToolData, sysConfigTextAndValue, dicKey, pmAllDTO);
//                        }
//                        this.pmToolService.saveChange();
//                        //获取job
//                        List<Map<String, String>> listOfToolJobData = listOfDataByBusinessType.get("toolJob");
//                        if(listOfToolJobData != null && !listOfToolJobData.isEmpty()){
//                            importToolJob(listOfToolJobData, sysConfigTextAndValue, dicKey, pmAllDTO);
//                        }
//                        this.pmToolJobService.saveChange();
//                        //获取operBook
//                        List<Map<String, String>> listOfOperBookData = listOfDataByBusinessType.get("operBook");
//                        if(listOfOperBookData != null && !listOfOperBookData.isEmpty()){
//                            importOperBook(listOfOperBookData, sysConfigTextAndValue, dicKey, pmAllDTO);
//                        }
//                        this.pmWorkStationOperBookService.saveChange();
//                    }

                    /*
                    importBop(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
                    this.pmBopService.saveChange();
                    */
                    //break;
                default:break;
            }

        }
    }

    private List<Map<String, String>> filterEquipmentPowerEmptyData(Collection<Map<String, String>> listOfEquipmentPowerData){
        if(listOfEquipmentPowerData.isEmpty()){
            return Collections.emptyList();
        }
        List<Map<String, String>> targetMap = new ArrayList<>(listOfEquipmentPowerData.size());
        for(Map<String,String> eachItem : listOfEquipmentPowerData){
            boolean canAdd = true;
            for(Map.Entry<String,String> eachField : eachItem.entrySet()){
                if(verifyEquipmentPowerFieldName(eachField.getKey())
                    && StringUtils.isBlank(eachField.getValue())){
                    canAdd = false;
                    break;
                }
            }
            if(canAdd){
                targetMap.add(eachItem);
            }
        }
        return targetMap;
    }

    private boolean verifyEquipmentPowerFieldName(String fieldName){
       return Arrays.asList(EQUIPMENT_POWER_FIELD_NAMES).contains(fieldName);
    }

    private void setQmDefectAnomalyDescription(List<Map<String, String>> listOfEachSheet){
        List<DefectAnomalyDto> defectAnomalyDtos = this.pqsLogicService.getWorkPlaceList();
        if(defectAnomalyDtos.isEmpty()){
            return;
        }
        Map<String,String> mapOfDefectAnomaly = defectAnomalyDtos.stream().collect(Collectors.toMap(DefectAnomalyDto :: getDefectAnomalyCode,
                DefectAnomalyDto :: getDefectAnomalyDescription));
        for(Map<String,String> eachRow : listOfEachSheet){
            String woQmDefectAnomalyCode = eachRow.get("woQmDefectAnomalyCode");
            if(StringUtils.isNotBlank(woQmDefectAnomalyCode)){
                if(!mapOfDefectAnomaly.containsKey(woQmDefectAnomalyCode)){
                    throw new InkelinkException(String.format("缺陷代码[%s]不存在",woQmDefectAnomalyCode));
                }else{
                    eachRow.put("woQmDefectAnomalyDescription",mapOfDefectAnomaly.get(woQmDefectAnomalyCode));
                }
            }
        }
    }

    private void setQmDefectComponentDescription(List<Map<String, String>> listOfEachSheet){
        List<PmTraceComponentEntity> listOfTraceComponent = pmTraceComponentService.getAllDatas();
        if(listOfTraceComponent.isEmpty()){
            return;
        }
        Map<String,String> mapOfTraceComponent = new HashMap<>(listOfTraceComponent.size());
        for(PmTraceComponentEntity pmTraceComponentEntity : listOfTraceComponent){
            mapOfTraceComponent.put(pmTraceComponentEntity.getTraceComponentCode(),pmTraceComponentEntity.getTraceComponentDescription());
        }
        for(Map<String,String> eachRow : listOfEachSheet){
            String woQmDefectComponentCode = eachRow.get("woQmDefectComponentCode");
            if(StringUtils.isNotBlank(woQmDefectComponentCode)){
                if(!mapOfTraceComponent.containsKey(woQmDefectComponentCode)){
                    throw new InkelinkException(String.format("组件代码[%s]不存在",woQmDefectComponentCode));
                }else{
                    eachRow.put("woQmDefectComponentDescription",mapOfTraceComponent.get(woQmDefectComponentCode));
                }
            }
        }
    }

    private void importWo(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                          String dicKey,PmAllDTO pmAllDTO) throws Exception {
        List<SysConfigurationEntity> listOfVehicleModle = this.sysConfigurationService.getSysConfigurations(VEHICLE_MODEL);
        List<String> listOfVehicleMasterFeatures = pmCharacteristicsDataService.getAllDatas().stream()
                .map(i -> i.getCharacteristicsValue()).collect(Collectors.toList());
        List<PmTraceComponentEntity> listOfTraceComponent = pmTraceComponentService.getAllDatas();
        Map<String, Long> mapOfComponentByCode = new HashMap<>(listOfTraceComponent.size());
        for (PmTraceComponentEntity pmTraceComponentEntity : listOfTraceComponent) {
            mapOfComponentByCode.put(pmTraceComponentEntity.getTraceComponentCode(), pmTraceComponentEntity.getId());
        }
        this.pmWoService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO,
                mapOfComponentByCode, listOfVehicleMasterFeatures, listOfVehicleModle);
    }

    private void importTool(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                          String dicKey,PmAllDTO pmAllDTO) throws Exception {
        this.pmToolService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private void importToolJob(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                            String dicKey,PmAllDTO pmAllDTO) throws Exception {
        this.pmToolJobService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private void importOperBook(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                               String dicKey,PmAllDTO pmAllDTO) {
        this.pmWorkStationOperBookService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private void importMaterial(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                               String dicKey,PmAllDTO pmAllDTO){
        this.pmWorkstationMaterialService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private void importBookOper(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                                String dicKey,PmAllDTO pmAllDTO){
        this.pmWorkStationOperBookService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private void importBop(List<Map<String, String>> listOfEachSheet,Map<String, Map<String, String>> sysConfigTextAndValue,
                                String dicKey,PmAllDTO pmAllDTO) {
        this.pmBopService.importExcel(listOfEachSheet, sysConfigTextAndValue, dicKey, pmAllDTO);
    }

    private List<Map<String, String>> getFromFinalData(List<Map<String, String>> listOfFinalData, Set<String> listOfPmDicKey) {
        List<Map<String, String>> targets = new ArrayList<>(listOfPmDicKey.size());
        for (Map<String, String> eachRowFromExcel : listOfFinalData) {
            Map<String, String> target = new HashMap<>(listOfPmDicKey.size());
            for (String fieldName : listOfPmDicKey) {
                target.put(fieldName, eachRowFromExcel.get(fieldName));
            }
            targets.add(target);
        }
        return targets;
    }

    private Collection<Map<String, String>> getUniqueData(List<Map<String, String>> srcList, String ... uniqueFieldNames) {
        Map<String, Map<String, String>> mapByCondtion = new HashMap<>(srcList.size());
        for (Map<String, String> eachRowData : srcList) {
            String val = getCombinationFieldValues(eachRowData,uniqueFieldNames);
            mapByCondtion.put(val, eachRowData);
        }
        return mapByCondtion.values();
    }

    private String getCombinationFieldValues(Map<String, String> eachRowData,String ... uniqueFieldNames){
        StringJoiner combinationValue = new StringJoiner("_");
        for(String uniqueFieldName : uniqueFieldNames){
            String val = eachRowData.get(uniqueFieldName);
            if(StringUtils.isNotBlank(val)){
                combinationValue.add(val);
            }
        }
        return combinationValue.toString();
    }

    private void validateAndSetDefaultVal(Collection<Map<String, String>> srcList, List<String> numberFieldNames) {
        for (Map<String, String> eachRowData : srcList) {
            for (String numberFieldName : numberFieldNames) {
                String value = eachRowData.get(numberFieldName);
                if (StringUtils.isBlank(value)) {
                    eachRowData.put(numberFieldName, "0");
                } else {
                    try {
                        Integer.valueOf(value);
                    } catch (Exception e) {
                        eachRowData.put(numberFieldName, "0");
                    }
                }
            }
        }
    }

    /**
     * 替换特殊字符
     *
     * @param eachRowData
     */
    private void convertInvalidChars(Map<String, String> eachRowData) {
        if (eachRowData != null && !eachRowData.isEmpty()) {
            for (Map.Entry<String, String> eachColume : eachRowData.entrySet()) {
                eachColume.setValue(eachColume.getValue().replace("/", ""));
            }
        }
    }

    private void setLineProperty(Collection<Map<String, String>> srcList) {
        for (Map<String, String> eachRowData : srcList) {
            //设置开始结束距离
            convertInvalidChars(eachRowData);
            eachRowData.put("beginDistance", "0");
            eachRowData.put("endDistance", eachRowData.get("lineLength"));
            eachRowData.put("productTime", eachRowData.get("lineProductTime"));

        }
    }

    private void setWorkStationProperty(Collection<Map<String, String>> srcList) {
        for (Map<String, String> eachRowData : srcList) {
            convertInvalidChars(eachRowData);
            String workstationType = eachRowData.get("workstationType");
            if (StringUtils.isBlank(workstationType)) {
                eachRowData.put("workstationType", "1");
            }

        }
    }


    private Map<String, Map<String, String>> getSysConfigTextAndValueByCategory() {
        List<SysConfigurationEntity> configs = this.sysConfigurationService.getAllDatas();
        if (configs.isEmpty()) {
            return Collections.emptyMap();
        }
        //剔除不显示的配置
        configs = configs.stream().filter(item -> Boolean.FALSE.equals(item.getIsHide())).collect(Collectors.toList());
        Map<String, Map<String, String>> mapOfConfig = new HashMap<>();
        for (SysConfigurationEntity sysConfigurationEntity : configs) {
            mapOfConfig.computeIfAbsent(sysConfigurationEntity.getCategory(), v -> new HashMap<>()).put(sysConfigurationEntity.getText(), sysConfigurationEntity.getValue());
        }
        return mapOfConfig;
    }


    private PmAllDTO getUnDeployData() {
        List<PmOrganizationEntity> listPmOrganization = this.pmOrganizationService.getData(null);
        if (listPmOrganization.isEmpty()) {
            throw new InkelinkException("没有工厂,请先创建工厂!");
        }
        PmAllDTO pmAllDTO = new PmAllDTO();
        pmAllDTO.setVersion("0");
        pmAllDTO.setOrganization(listPmOrganization.get(0));
        ConditionDto conditionDto = new ConditionDto("VERSION", "0", ConditionOper.Equal);
        return getShopsByVersion(pmAllDTO,conditionDto);
//        List<PmWorkShopEntity> listShop = this.pmShopService.getData(Arrays.asList(conditionDto));
//        if (!listShop.isEmpty()) {
//            for (PmWorkShopEntity shop : listShop)
//                pmAllDTO.addPmInfo(this.getCurrentVersionShop(shop.getWorkshopCode()));
//            }
//        }
//        return pmAllDTO;
    }

    private List<Map<String, String>> validImportDatas(List<Map<String, String>> listDataFromExcel,
                                                       Map<String, String> mapColumnEnAndCn, String sheetName) {
        Map<String, String> firstData = listDataFromExcel.get(0);
        Set<String> excelColunms = firstData.keySet();
        Map<String, String> mapColumnCnAndEn = new HashMap<>(mapColumnEnAndCn.size());
        for (Map.Entry<String, String> eachColumnEnAndCnEntry : mapColumnEnAndCn.entrySet()) {
            mapColumnCnAndEn.put(eachColumnEnAndCnEntry.getValue(), eachColumnEnAndCnEntry.getKey());
        }
        //验证必要的字段是否都存在
        Set<String> notExistColumnNames = new HashSet<>();
        for (Map.Entry<String, String> eachColumn : mapColumnCnAndEn.entrySet()) {
            if (!excelColunms.contains(eachColumn.getKey())) {
                notExistColumnNames.add(eachColumn.getKey());
            }
        }
        if (!notExistColumnNames.isEmpty()) {
            throw new InkelinkException(sheetName + "模板不符合规范,字段(" + String.join(",", notExistColumnNames) + ")不存在");
        }
        //转换dic的key，用实体的属性名称，替换excel的title，方便后面的操作
        List<Map<String, String>> listDataFromExcelTarget = new ArrayList<>(listDataFromExcel.size());
        for (Map<String, String> eachRow : listDataFromExcel) {
            Map<String, String> newRow = new HashMap<>();
            for (Map.Entry<String, String> eachColumInRow : eachRow.entrySet()) {
                if (mapColumnCnAndEn.containsKey(eachColumInRow.getKey())) {
                    newRow.put(mapColumnCnAndEn.get(eachColumInRow.getKey()), eachColumInRow.getValue());
                }
            }
            listDataFromExcelTarget.add(newRow);
        }
        return listDataFromExcelTarget;
    }

    /**
     * 获取渲染的树形结构
     *
     * @param id
     * @param noteType
     * @return
     */
    @Override
    public List<TreeNode> getTreeNodes(Long id, String noteType) {
        List<SysConfigurationEntity> sysconfigs = sysConfigurationService.getAllDatas();
        if (id == null) {
            List<TreeNode> plant = getPlantNodes(sysconfigs);
            return plant;
        }
        List<TreeNode> datas;
        switch (noteType) {
            case "PmPlant":
            case "PmOrg":
            case "PmOrganization":
                datas = getShopNodes(sysconfigs,id);
                break;
            case "PmShop":
            case "PmWorkShop":
                datas = getLineNodes(sysconfigs,id);
                break;
            case "PmArea":
            case "PmLine":
                //获取avi和工位
                datas = getAreaChildrenNodes(sysconfigs,id);
                break;
            case "PmStation":
            case "PmWorkStation":
                datas = getWorkPlaceChildernNodes(sysconfigs,id);
                break;
            case "PmTool":
                datas = getToolJobNodes(sysconfigs,id);
                break;
            default:
                datas = getPlantNodes(sysconfigs);
                break;
        }

        return datas;
    }

    private List<TreeNode> getToolJobNodes(List<SysConfigurationEntity> sysconfigs,Long id) {
        String name = getModelName(PmToolJobEntity.class);
        List<TreeNode> nodes = new ArrayList<>();
        List<PmToolJobEntity> datas = pmToolJobService.getListByParentId(id);
        for (PmToolJobEntity data : datas) {
            String text = getEnableName(data.getJobNo(), true);
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", name);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,name));
            node.setLeaf(true);
            nodes.add(node);
        }
        return nodes;
    }

    private List<TreeNode> getWorkPlaceChildernNodes(List<SysConfigurationEntity> sysconfigs,Long id) {
        List<TreeNode> nodes = new ArrayList<>();
        List<PmOtEntity> otDatas = pmOtService.getListByParentId(id);
        String otName = getModelName(PmOtEntity.class);
        List<PmWoEntity> woDatas = pmWoService.getListByParentId(id);
        String woName = getModelName(PmWoEntity.class);
        List<PmToolEntity> toolDatas = pmToolService.getListByParentId(id);
        String toolName = getModelName(PmToolEntity.class);
        List<PmPullCordEntity> pullcords = pmPullCordService.getListByParentId(id);
        String pullcordName = getModelName(PmPullCordEntity.class);
        List<PmEquipmentEntity> equipments = pmEquipmentService.getListByParentId(id);
        String equipmentName = getModelName(PmEquipmentEntity.class);

        for (PmOtEntity data : otDatas) {
            String text = getEnableName(data.getOtName(), data.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", otName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,otName));
            node.setLeaf(true);
            nodes.add(node);
        }
        List<PmWoEntity> collect = woDatas.stream().sorted(Comparator.comparing(PmWoEntity::getDisplayNo).thenComparing(PmWoEntity::getQmDefectAnomalyDescription)).collect(Collectors.toList());
        for (PmWoEntity data : collect) {
            String text = getEnableName(data.getWoCode(), data.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setCode(data.getWoCode());
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", woName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,woName));
            node.setLeaf(true);
            nodes.add(node);
        }
        for (PmToolEntity data : toolDatas) {
            String text = getEnableName(data.getToolCode(), data.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setCode(data.getToolCode());
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", toolName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,toolName));
            nodes.add(node);
        }
        for (PmPullCordEntity data : pullcords) {
            String text = getEnableName(data.getPullcordName(), data.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", pullcordName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,pullcordName));
            node.setLeaf(true);
            nodes.add(node);
        }
        for (PmEquipmentEntity euipment : equipments) {
            String text = getEnableName(euipment.getEquipmentName(), true);
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(euipment.getId()));
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", equipmentName);
            m.put("data", euipment);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,equipmentName));
            node.setLeaf(true);
            nodes.add(node);
        }
        return nodes;
    }

    private List<TreeNode> getAreaChildrenNodes(List<SysConfigurationEntity> sysconfigs,Long lineId) {
        List<TreeNode> nodes = new ArrayList<>();
        List<PmAviEntity> aviList = this.pmAviService.getListByLineId(lineId);
        List<PmWorkStationEntity> stationList = pmStationService.getListByLineId(lineId);
        String aviName = getModelName(PmAviEntity.class);
        for (PmAviEntity data : aviList) {
            String text = getEnableName(data.getAviName(), data.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setCode(data.getAviCode());
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", aviName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,aviName));
            node.setLeaf(true);
            nodes.add(node);
        }
        String stationName = getModelName(PmWorkStationEntity.class);
        for (PmWorkStationEntity data : stationList) {
            String text = getEnableName(data.getWorkstationName(), data.getIsEnable())
                    + "(" + data.getWorkstationNo() + "-" + data.getDirection() + ")";
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(data.getId()));
            node.setCode(data.getWorkstationCode());
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", stationName);
            m.put("data", data);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,stationName));
            nodes.add(node);
        }
        return nodes;
    }


    private List<TreeNode> getLineNodes(List<SysConfigurationEntity> sysconfigs,Long id) {
        String name = getModelName(PmLineEntity.class);
        List<TreeNode> nodes = new ArrayList<>();
        List<PmLineEntity> lineList = pmLineService.getListByParentId(id);
        for (PmLineEntity line : lineList) {
            String text = getEnableName(line.getLineName() + "(" + line.getLineCode() + ")", line.getIsEnable());
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(line.getId()));
            node.setCode(line.getLineCode());
            node.setText(text);
            Map<String,Object> m = new HashMap(2);
            m.put("type", name);
            m.put("data", line);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,name));
            nodes.add(node);
        }
        return nodes;
    }

    private String getEnableName(String name, Boolean isEnable) {
        if (!isEnable) {
            name = "[" + name + "]";
        }
        return name;
    }

    private List<TreeNode> getShopNodes(List<SysConfigurationEntity> sysconfigs,Long id) {
        String name = getModelName(PmWorkShopEntity.class);
        List<TreeNode> nodes = new ArrayList<>();
        List<PmWorkShopEntity> shopList = pmShopService.getListByParentId(id);
        for (PmWorkShopEntity shop : shopList) {
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(shop.getId()));
            node.setCode(shop.getWorkshopCode());
            node.setText(shop.getWorkshopName());
            Map<String,Object> m = new HashMap(2);
            m.put("type", name);
            m.put("data", shop);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,name));
            nodes.add(node);
        }
        return nodes;
    }

    private List<TreeNode> getPlantNodes(List<SysConfigurationEntity> sysconfigs) {
        String name = getModelName(PmOrganizationEntity.class);
        List<TreeNode> nodes = new ArrayList<>();
        List<SortDto> sortDtos = new ArrayList<>();
        List<PmOrganizationEntity> orgList = pmOrgService.getData(null, sortDtos, false);
        for (PmOrganizationEntity org : orgList) {
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(org.getId()));
            node.setCode(org.getOrganizationCode());
            node.setText(org.getOrganizationName());
            Map<String,Object> m = new HashMap(2);
            m.put("type", name);
            m.put("data", org);
            node.setExtendData(m);
            node.setIconCls(getIcon(sysconfigs,name));
            node.setLeaf(false);
            nodes.add(node);
        }
        return nodes;
    }

    private String getIcon(List<SysConfigurationEntity> sysconfigs,String name) {
        //return sysConfigurationService.getConfiguration(name, "PMIcon");
        // SysConfigurationEntity pmIcon = sysConfigurationService.getAllDatas().stream().findFirst().filter(o -> o.getCategory().equals("PMIcon") && o.getText().equals(name)).orElse(null);

        if (StringUtils.isBlank(name) || sysconfigs == null || sysconfigs.size() == 0) {
            return "";
        }
        SysConfigurationEntity pmIcon = sysconfigs.stream().findFirst().filter(o -> o.getCategory().equals("PMIcon") && o.getText().equals(name)).orElse(null);
        if (pmIcon != null) {
            return pmIcon.getValue() == null ? "" : pmIcon.getValue();
        }
        return "";
    }


    private String getModelName(Class a) {
        String name = a.getSimpleName();
        name = name.substring(0, name.length() - 6);
        return name;
    }


    @Override
    public Document generationShopModel(Long shopId) {
        Document document = null;
        try {
            document = getPmShopDocument(shopId);
        } catch (Exception e) {
            logger.error("exec generationShopModel failed:{}", e.getMessage(), e);
        }
        return document;
    }

    @Override
    public List<TextValueStationsMappingDTO> getAreaStations(String shopCode) {
        List<TextValueStationsMappingDTO> areas = new ArrayList<>();
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("WORKSHOP_CODE", shopCode, ConditionOper.Equal));
        PmWorkShopEntity shop = pmShopService.getData(conditionInfos).stream().findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("车间代码不存在=>" + shopCode);
        }

        List<ConditionDto> areaConditionInfos = new ArrayList<>();
        //areaConditionInfos.add(new ConditionDto("PM_AREA_ID", shop.getId().toString(), ConditionOper.Equal));
        areaConditionInfos.add(new ConditionDto("PRC_PM_WORKSHOP_ID", shop.getId().toString(), ConditionOper.Equal));
        areaConditionInfos.add(new ConditionDto("VERSION", "0", ConditionOper.Equal));
        List<PmLineEntity> areaEles = pmLineService.getData(areaConditionInfos);
        for (PmLineEntity areaEle : areaEles) {
            TextValueStationsMappingDTO textValueStationsMappingDTO = new TextValueStationsMappingDTO();

            textValueStationsMappingDTO.setValue(String.valueOf(areaEle.getId()));
            textValueStationsMappingDTO.setText(shop.getWorkshopCode() + "->" + areaEle.getLineName());
            List<ConditionDto> stationConditionInfos = new ArrayList<>();
            //stationConditionInfos.add(new ConditionDto("PM_AREA_ID", areaEle.getId().toString(), ConditionOper.Equal));
            stationConditionInfos.add(new ConditionDto("PRC_PM_LINE_ID", areaEle.getId().toString(), ConditionOper.Equal));
            stationConditionInfos.add(new ConditionDto("VERSION", "0", ConditionOper.Equal));
            List<PmWorkStationEntity> Stations = pmStationService.getData(stationConditionInfos);
            textValueStationsMappingDTO.setStations(Stations);
            areas.add(textValueStationsMappingDTO);
        }
        return areas;
    }

    @Override
    public List<ComponentDataDTO> getLocalComponent(String query) {
        LambdaQueryWrapper<PmTraceComponentEntity> predicate = Wrappers.lambdaQuery(PmTraceComponentEntity.class);
        return getLocalComponent(query, predicate);
    }

    @Override
    public JSONObject generationJson(Long shopId) {
        //相关建模表和字段
        Map<String, Object> entityMap = getEntitiesByShopId(shopId);
        //公共字段不发布
        List<String> notPublishFieldNameList = getBaseFieldNameList();
        PmWorkShopEntity pmShopEntity = (PmWorkShopEntity) entityMap.get(PmWorkShopEntity.class.getSimpleName());
        PmOrganizationEntity pmOrganizationEntity = (PmOrganizationEntity) entityMap.get(PmOrganizationEntity.class.getSimpleName());
        JSONObject jsonContent = new JSONObject();
        if (pmShopEntity != null && pmOrganizationEntity != null) {
            JSONObject shopJson;
            try {
                shopJson = getJson(pmShopEntity, notPublishFieldNameList);
                setLineAndChildNodeJson(notPublishFieldNameList, jsonContent, entityMap);
                jsonContent.put("workShops", Arrays.asList(shopJson));
                jsonContent.put("organization", getJson(pmOrganizationEntity, notPublishFieldNameList));
                return jsonContent;
            } catch (Exception e) {
                logger.error("exec generationJson failed:{}", e.getMessage(), e);
            }
        }
        return jsonContent;
    }

    private void setLineAndChildNodeJson(List<String> baseFieldNameList,
                                         JSONObject jsonContent,
                                         Map<String, Object> entityMap) throws IllegalAccessException {
        List<PmLineEntity> pmAreaEntities = (List<PmLineEntity>) entityMap.get(PmLineEntity.class.getSimpleName());
        if (pmAreaEntities != null && !pmAreaEntities.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (PmLineEntity pmAreaEntity : pmAreaEntities) {
                JSONObject jsonObject = getJson(pmAreaEntity, baseFieldNameList);
                //设置生产线下面的AVI
                setAviJson(baseFieldNameList, jsonContent, entityMap, String.valueOf(pmAreaEntity.getId()));
                //设置生产线下面的工位
                setStationJsons(baseFieldNameList, jsonContent, entityMap, String.valueOf(pmAreaEntity.getId()));
                setAllStationJsons(baseFieldNameList, jsonContent, entityMap, String.valueOf(pmAreaEntity.getId()));
                jsonArray.add(jsonObject);

            }
            jsonContent.put("lines", jsonArray);
        }
    }


    private void setStationJsons(List<String> baseFieldNameList,
                                 JSONObject parentJson,
                                 Map<String, Object> entityMap,
                                 String parentId) throws IllegalAccessException {
        List<PmWorkStationEntity> pmWorkStationEntityList = (List<PmWorkStationEntity>) entityMap.get(PmWorkStationEntity.class.getSimpleName());
        if (pmWorkStationEntityList == null || pmWorkStationEntityList.isEmpty()) {
            return;
        }
        List<PmWorkStationEntity> pmWorkStationEntityListByAreaId = pmWorkStationEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmLineId()))).collect(Collectors.toList());
        if (!pmWorkStationEntityListByAreaId.isEmpty()) {
            JSONArray jsonArray = parentJson.getJSONArray("workStations");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmWorkStationEntity pmWorkStationEntity : pmWorkStationEntityListByAreaId) {
                JSONObject workplace = getJson(pmWorkStationEntity, baseFieldNameList);
                //添加ot
                setOtJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加Wo
                setWoJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加工具
                setToolJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加拉绳
                setPullCordJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //BOP
                setBopJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //物料
                setWorkstationMaterialJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //操作指导书
                setWorkstationOperBookJson(baseFieldNameList, parentJson, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //设备
                setEquipmentJson(baseFieldNameList,parentJson,entityMap,String.valueOf(pmWorkStationEntity.getId()));
                jsonArray.add(workplace);
            }
            parentJson.put("workStations", jsonArray);
        }

    }

    private void setAttribsToJson(BaseEntity baseEntity, JSONObject jobj) {
        if (baseEntity == null || jobj == null) {
            return;
        }
        jobj.put("attribute1", baseEntity.getAttribute1());
        jobj.put("attribute2", baseEntity.getAttribute2());
        jobj.put("attribute3", baseEntity.getAttribute3());
        jobj.put("attribute4", baseEntity.getAttribute4());
        jobj.put("attribute5", baseEntity.getAttribute5());
        jobj.put("attribute6", baseEntity.getAttribute6());
        jobj.put("attribute7", baseEntity.getAttribute7());
        jobj.put("attribute8", baseEntity.getAttribute8());
        jobj.put("attribute9", baseEntity.getAttribute9());
        jobj.put("attribute10", baseEntity.getAttribute10());
    }

    private void setAllStationJsons(List<String> baseFieldNameList,
                                    JSONObject parentJson,
                                    Map<String, Object> entityMap,
                                    String parentId) throws IllegalAccessException {
        List<PmWorkStationEntity> pmWorkStationEntityList = (List<PmWorkStationEntity>) entityMap.get("ALL" + PmWorkStationEntity.class.getSimpleName());
        if (pmWorkStationEntityList == null || pmWorkStationEntityList.isEmpty()) {
            return;
        }
        List<PmWorkStationEntity> pmWorkStationEntityListByAreaId = pmWorkStationEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmLineId()))).collect(Collectors.toList());
        if (!pmWorkStationEntityListByAreaId.isEmpty()) {
            JSONArray jsonArray = parentJson.getJSONArray("allWorkStations");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmWorkStationEntity pmWorkStationEntity : pmWorkStationEntityListByAreaId) {
                JSONObject workplace = getJson(pmWorkStationEntity, baseFieldNameList);
                //setAttribsToJson(pmWorkStationEntity,workplace);
                jsonArray.add(workplace);
            }
            parentJson.put("allWorkStations", jsonArray);
        }

    }

    private void setWorkstationOperBookJson(List<String> baseFieldNameList,
                                            JSONObject parent,
                                            Map<String, Object> entityMap,
                                            String parentId) throws IllegalAccessException {
        List<PmWorkstationOperBookEntity> pmWorkstationOperBookEntityList = (List<PmWorkstationOperBookEntity>) entityMap.get(PmWorkstationOperBookEntity.class.getSimpleName());
        if (pmWorkstationOperBookEntityList == null || pmWorkstationOperBookEntityList.isEmpty()) {
            return;
        }
        List<PmWorkstationOperBookEntity> pmWorkstationOperBookEntityListByWorkStationId = pmWorkstationOperBookEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmWorkstationOperBookEntityListByWorkStationId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("workstationOperBooks");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmWorkstationOperBookEntity pmWorkstationOperBookEntity : pmWorkstationOperBookEntityListByWorkStationId) {
                jsonArray.add(getJson(pmWorkstationOperBookEntity, baseFieldNameList));
            }
            parent.put("workstationOperBooks", jsonArray);
        }

    }

    private void setEquipmentJson(List<String> baseFieldNameList,
                                            JSONObject parent,
                                            Map<String, Object> entityMap,
                                            String parentId) throws IllegalAccessException {
        List<PmEquipmentEntity> pmPmEquipmentEntityList = (List<PmEquipmentEntity>) entityMap.get(PmEquipmentEntity.class.getSimpleName());
        if (pmPmEquipmentEntityList == null || pmPmEquipmentEntityList.isEmpty()) {
            return;
        }
        List<PmEquipmentEntity> pmEquipmentEntityListByWorkStationId = pmPmEquipmentEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmEquipmentEntityListByWorkStationId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("equipments");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmEquipmentEntity pmEquipmentEntity : pmEquipmentEntityListByWorkStationId) {
                jsonArray.add(getJson(pmEquipmentEntity, baseFieldNameList));
            }
            parent.put("equipments", jsonArray);
        }

    }

    private void setBopJson(List<String> baseFieldNameList,
                            JSONObject parent,
                            Map<String, Object> entityMap,
                            String parentId) throws IllegalAccessException {
        List<PmBopEntity> pmBopEntityList = (List<PmBopEntity>) entityMap.get(PmBopEntity.class.getSimpleName());
        if (pmBopEntityList == null || pmBopEntityList.isEmpty()) {
            return;
        }
        List<PmBopEntity> pmPullCordEntityListByWorkStationId = pmBopEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmPullCordEntityListByWorkStationId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("bops");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmBopEntity pmBopEntity : pmPullCordEntityListByWorkStationId) {
                jsonArray.add(getJson(pmBopEntity, baseFieldNameList));
            }
            parent.put("bops", jsonArray);
        }

    }

    private void setWorkstationMaterialJson(List<String> baseFieldNameList,
                                            JSONObject parent,
                                            Map<String, Object> entityMap,
                                            String parentId) throws IllegalAccessException {
        List<PmWorkstationMaterialEntity> pmWorkstationEntityList = (List<PmWorkstationMaterialEntity>) entityMap.get(PmWorkstationMaterialEntity.class.getSimpleName());
        if (pmWorkstationEntityList == null || pmWorkstationEntityList.isEmpty()) {
            return;
        }
        List<PmWorkstationMaterialEntity> pmWorkstationMaterialListByWorkStationId = pmWorkstationEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmWorkstationMaterialListByWorkStationId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("workstationMaterials");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmWorkstationMaterialEntity pmWorkstationMaterialEntity : pmWorkstationMaterialListByWorkStationId) {
                jsonArray.add(getJson(pmWorkstationMaterialEntity, baseFieldNameList));
            }
            parent.put("workstationMaterials", jsonArray);
        }

    }

    private void setPullCordJson(List<String> baseFieldNameList,
                                 JSONObject parent,
                                 Map<String, Object> entityMap,
                                 String parentId) throws IllegalAccessException {
        List<PmPullCordEntity> pmPullCordEntityList = (List<PmPullCordEntity>) entityMap.get(PmPullCordEntity.class.getSimpleName());
        if (pmPullCordEntityList == null || pmPullCordEntityList.isEmpty()) {
            return;
        }
        List<PmPullCordEntity> pmPullCordEntityListByToolId = pmPullCordEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmPullCordEntityListByToolId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("pullCords");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmPullCordEntity pmPullcordEntity : pmPullCordEntityListByToolId) {
                jsonArray.add(getJson(pmPullcordEntity, baseFieldNameList));
            }
            parent.put("pullCords", jsonArray);
        }

    }

    private void setToolJson(List<String> baseFieldNameList,
                             JSONObject parent,
                             Map<String, Object> entityMap,
                             String parentId) throws IllegalAccessException {
        List<PmToolEntity> pmToolEntityList = (List<PmToolEntity>) entityMap.get(PmToolEntity.class.getSimpleName());
        if (pmToolEntityList == null || pmToolEntityList.isEmpty()) {
            return;
        }
        List<PmToolEntity> pmToolEntityListByWorkplaceId = pmToolEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmToolEntityListByWorkplaceId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("tools");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmToolEntity pmToolEntity : pmToolEntityListByWorkplaceId) {
                JSONObject json = getJson(pmToolEntity, baseFieldNameList);
                setToolJobJson(baseFieldNameList, parent, entityMap, String.valueOf(pmToolEntity.getId()));
                jsonArray.add(json);
            }
            parent.put("tools", jsonArray);
        }
    }

    private void setToolJobJson(List<String> baseFieldNameList,
                                JSONObject parent,
                                Map<String, Object> entityMap,
                                String parentId) throws IllegalAccessException {
        List<PmToolJobEntity> pmToolJobEntityList = (List<PmToolJobEntity>) entityMap.get(PmToolJobEntity.class.getSimpleName());
        if (pmToolJobEntityList == null || pmToolJobEntityList.isEmpty()) {
            return;
        }
        List<PmToolJobEntity> pmToolJobEntityListByToolId = pmToolJobEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmToolId()))).collect(Collectors.toList());
        if (!pmToolJobEntityListByToolId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("toolJobs");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmToolJobEntity pmToolJobEntity : pmToolJobEntityListByToolId) {
                jsonArray.add(getJson(pmToolJobEntity, baseFieldNameList));
            }
            parent.put("toolJobs", jsonArray);
        }
    }

    private void setWoJson(List<String> baseFieldNameList,
                           JSONObject parent,
                           Map<String, Object> entityMap,
                           String parentId) throws IllegalAccessException {
        List<PmWoEntity> pmWoEntityList = (List<PmWoEntity>) entityMap.get(PmWoEntity.class.getSimpleName());
        if (pmWoEntityList == null || pmWoEntityList.isEmpty()) {
            return;
        }
        List<PmWoEntity> pmWoEntityListByWorkplaceId = pmWoEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmWoEntityListByWorkplaceId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("wos");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmWoEntity pmWoEntity : pmWoEntityListByWorkplaceId) {
                jsonArray.add(getJson(pmWoEntity, baseFieldNameList));
            }
            parent.put("wos", jsonArray);
        }
    }

    private void setOtJson(List<String> baseFieldNameList,
                           JSONObject parent,
                           Map<String, Object> entityMap,
                           String parentId) throws IllegalAccessException {
        List<PmOtEntity> pmOtEntityList = (List<PmOtEntity>) entityMap.get(PmOtEntity.class.getSimpleName());
        if (pmOtEntityList == null || pmOtEntityList.isEmpty()) {
            return;
        }
        List<PmOtEntity> pmOtEntityListByWorkplaceId = pmOtEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmOtEntityListByWorkplaceId.isEmpty()) {
            JSONArray jsonArray = parent.getJSONArray("ots");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmOtEntity pmOtEntity : pmOtEntityListByWorkplaceId) {
                jsonArray.add(getJson(pmOtEntity, baseFieldNameList));
            }
            parent.put("ots", jsonArray);
        }
    }

    private void setAviJson(List<String> baseFieldNameList,
                            JSONObject parentJson,
                            Map<String, Object> entityMap,
                            String parentId) throws IllegalAccessException {
        List<PmAviEntity> allPmAviEntityList = (List<PmAviEntity>) entityMap.get(PmAviEntity.class.getSimpleName());
        if (allPmAviEntityList == null || allPmAviEntityList.isEmpty()) {
            return;
        }
        List<PmAviEntity> pmAviEntityListByAreaId = allPmAviEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmLineId()))).collect(Collectors.toList());
        if (!pmAviEntityListByAreaId.isEmpty()) {
            JSONArray jsonArray = parentJson.getJSONArray("avis");
            if (CollectionUtils.isEmpty(jsonArray)) {
                jsonArray = new JSONArray();
            }
            for (PmAviEntity pmAviEntity : pmAviEntityListByAreaId) {
                jsonArray.add(getJson(pmAviEntity, baseFieldNameList));
            }
            parentJson.put("avis", jsonArray);
        }
    }

    private JSONObject getJson(BaseEntity entity, List<String> baseFieldNameList) throws IllegalAccessException {
        Class entityClazz = entity.getClass();
        Field[] fields = ClassUtil.getDeclaredFields(entityClazz);
        JSONObject childJson = new JSONObject();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (baseFieldNameList.contains(fieldName)) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(entity);
            if(Date.class.equals(field.getType())){
                value = DateUtils.format((Date)value,"yyyy-MM-dd HH:mm:ss");
            }
            childJson.put(fieldName, value == null ? StringUtils.EMPTY : String.valueOf(value));
        }
        setAttribsToJson(entity,childJson);
        return childJson;
    }

    /**
     * 导出模板
     *
     * @return
     */
    private Map<String, Map<String, String>> getPmDic() {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        Map<String, String> mapOfLineAndWorkStation = new HashMap<>(20);
        mapOfLineAndWorkStation.putAll(pmLineService.getExcelHead());
        mapOfLineAndWorkStation.putAll(pmStationService.getExcelHead());
        map.put(PmSheetTableName.PM_ALL, mapOfLineAndWorkStation);
        map.put(PmSheetTableName.PM_LINE, pmLineService.getExcelHead());
        map.put(PmSheetTableName.PM_STATION, pmStationService.getExcelHead());
        map.put(PmSheetTableName.PM_PULL_CORD, pmPullCordService.getExcelHead());
        map.put(PmSheetTableName.PM_OT, pmOtService.getExcelHead());
        map.put(PmSheetTableName.PM_WO, pmWoService.getExcelHead());
        map.put(PmSheetTableName.PM_TOOL, pmToolService.getExcelHead());
        map.put(PmSheetTableName.PM_TOOL_JOB, pmToolJobService.getExcelHead());
        map.put(PmSheetTableName.PM_MATERIAL, pmWorkstationMaterialService.getExcelHead());
        map.put(PmSheetTableName.PM_AVI, pmAviService.getExcelHead());
        map.put(PmSheetTableName.PM_BOP, pmBopService.getExcelHead());
        map.put(PmSheetTableName.PM_EQUIPMENT_AND_POWER, pmEquipmentPowerService.getExcelHead());
        return map;
    }


    private List<ComponentDataDTO> getLocalComponent(String query, LambdaQueryWrapper<PmTraceComponentEntity> predicate) {
        IPmTraceComponentMapper traceComponent = SpringContextUtils.getBean(IPmTraceComponentMapper.class);
        if (query.equals("00000000-0000-0000-0000-000000000000")) {
            query = StringUtils.EMPTY;
        }
        List<PmTraceComponentEntity> data;
        if (!StringUtils.isBlank(query)) {
            predicate.eq(PmTraceComponentEntity::getId, query);
        } else {
            predicate.eq(PmTraceComponentEntity::getTraceComponentCode, query).or().eq(PmTraceComponentEntity::getCategoryDesc, query);
        }
        predicate.orderByDesc(PmTraceComponentEntity::getTraceComponentDescription);
        data = traceComponent.selectList(
                predicate.select(PmTraceComponentEntity::getId, PmTraceComponentEntity::getTraceComponentDescription, PmTraceComponentEntity::getTraceComponentCode));
        List<ComponentDataDTO> datas = data.stream().map(item -> {
            ComponentDataDTO componentDataDTO = new ComponentDataDTO();
            componentDataDTO.setCode(item.getTraceComponentCode());
            componentDataDTO.setId(String.valueOf(item.getId()));
            componentDataDTO.setDescription(item.getTraceComponentDescription());
            return componentDataDTO;
        }).collect(Collectors.toList());
        return datas;
    }

    private PmWorkShopEntity getPmShopEntityByCodeAndVersion(String shopCode, int version) {
        return this.pmShopService.getPmShopEntityByCodeAndVersion(shopCode, version, Boolean.TRUE);
    }

    private PmWorkShopEntity getPmShopEntity(Long shopId) {
        return this.pmShopService.getPmShopEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private PmOrganizationEntity getOrganizationEntity(Long id) {
        return this.pmOrganizationService.getPmOrganization(id, Boolean.FALSE);
    }

    private List<PmLineEntity> getPmAreaEntityByVersion(Long shopId, int version) {
        return this.pmLineService.getPmAreaEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmLineEntity> getPmAreaEntity(Long shopId) {
        return this.pmLineService.getPmAreaEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmWorkStationEntity> getPmStationEntity(Long shopId) {
        return this.pmStationService.getPmStationEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmWorkStationEntity> getPmStationEntityByAll(Long shopId) {
        return this.pmStationService.getPmStationEntityByAll(shopId, 0, Boolean.FALSE);
    }

    private List<PmAviEntity> getPmAviEntityByVersion(Long shopId, int version) {
        return this.pmAviService.getPmAviEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmAviEntity> getPmAviEntity(Long shopId) {
        return this.pmAviService.getPmAviEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmPullCordEntity> getPmPullCordEntityByVersion(Long shopId, int version) {
        return this.pmPullCordService.getPmPullCordEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmPullCordEntity> getPmPullCordEntity(Long shopId) {
        return this.pmPullCordService.getPmPullCordEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmOtEntity> getPmOtEntityByVersion(Long shopId, int version) {
        return this.pmOtService.getPmOtEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmOtEntity> getPmOtEntity(Long shopId) {
        return this.pmOtService.getPmOtEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmWoEntity> getPmWoEntityByVersion(Long shopId, int version) {
        return this.pmWoService.getPmWoEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmWorkstationMaterialEntity> getPmWorkstationMaterialEntityByVersion(Long shopId, int version) {
        return this.pmWorkstationMaterialService.getPmWorkstationEntityByVersion(shopId, version, Boolean.TRUE);
    }


    private List<PmWoEntity> getPmWoEntity(Long shopId) {
        return this.pmWoService.getPmWoEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmToolEntity> getPmToolEntityByVersion(Long shopId, int version) {
        return this.pmToolService.getPmToolEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmToolEntity> getPmToolEntity(Long shopId) {
        return this.pmToolService.getPmToolEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmToolJobEntity> getPmToolJobEntityByVersion(Long shopId, int version) {
        return this.pmToolJobService.getPmToolJobEntityByVersion(shopId, version, Boolean.TRUE);
    }

    private List<PmToolJobEntity> getPmToolJobEntity(Long shopId) {
        return this.pmToolJobService.getPmToolJobEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmBopEntity> getPmBopEntity(Long shopId) {
        return this.pmBopService.getPmBopEntityByVersion(shopId, Boolean.FALSE);
    }

    private List<PmWorkstationMaterialEntity> getPmWorkstationMaterialEntity(Long shopId) {
        return this.pmWorkstationMaterialService.getPmWorkstationEntityByVersion(shopId, 0, Boolean.FALSE);
    }

    private List<PmWorkstationOperBookEntity> getPmWorkstationOperBookEntity(Long shopId) {
        return this.pmWorkStationOperBookService.getPmWorkstationEntity(shopId);
    }

    private List<PmEquipmentEntity> getPmEquipmentEntity(Long shopId) {
        return this.pmEquipmentService.getPmEquipmentEntityByVersion(shopId,0);
    }

    private List<String> getBaseFieldNameList() {
        Field[] fields = ClassUtil.getDeclaredFields(BaseEntity.class);
        if (fields.length > 0) {
            List<String> fieldNameList = new ArrayList<>(fields.length);
            for (Field field : fields) {
                fieldNameList.add(field.getName());
            }
            fieldNameList.add("pmWoEntity");
            fieldNameList.add("pmOtEntity");
            fieldNameList.add("pmPullCordEntity");
            fieldNameList.add("pmToolEntity");
            fieldNameList.add("pmBopEntity");
            fieldNameList.add("pmWorkstationMaterialEntity");
            fieldNameList.add("pmWorkstationOperBookEntity");
            fieldNameList.add("pmEquipmentEntity");
            fieldNameList.add("pmAviEntity");
            fieldNameList.add("pmWorkStationEntity");
            fieldNameList.add("pmToolJobEntity");
            fieldNameList.add("pmLineEntity");
            return fieldNameList;
        }
        return Collections.emptyList();
    }

    private Document getPmShopDocument(Long shopId) throws IllegalAccessException {
        Element root = DocumentHelper.createElement("PM");
        Map<String, Object> entityMap = getEntitiesByShopId(shopId);
        //添加车间
        List<String> baseFieldNameList = getBaseFieldNameList();
        Element shopElement = setAndGetShopElements(baseFieldNameList, entityMap);
        if (shopElement != null) {
            root.add(shopElement);
        }
        return DocumentHelper.createDocument(root);
    }

    private Element getElement(BaseEntity entity, List<String> baseFieldNameList) throws IllegalAccessException {
        Class entityClazz = entity.getClass();
        Field[] fields = ClassUtil.getDeclaredFields(entityClazz);
        String elementName = entityClazz.getSimpleName();
        Element element = DocumentHelper.createElement(elementName);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (baseFieldNameList.contains(fieldName)) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(entity);
            element.add(DocumentHelper.createAttribute(element, fieldName, value == null ? StringUtils.EMPTY : String.valueOf(value)));
        }
        return element;
    }


    /**
     * @param shopId
     * @return
     */
    private Map<String, Object> getEntitiesByShopId(Long shopId) {
        Map<String, Object> target = new HashMap<>();
        PmWorkShopEntity shopEntity = getPmShopEntity(shopId);
        if (shopEntity == null) {
            return target;
        }
        target.put(PmOrganizationEntity.class.getSimpleName(), getOrganizationEntity(shopEntity.getPrcPmOrganizationId()));
        target.put(PmWorkShopEntity.class.getSimpleName(), shopEntity);
        List<PmLineEntity> pmAreaEntityList = getPmAreaEntity(shopId);
        if (pmAreaEntityList.isEmpty()) {
            return target;
        }
        target.put(PmLineEntity.class.getSimpleName(), pmAreaEntityList);
        target.put(PmAviEntity.class.getSimpleName(), getPmAviEntity(shopId));
        List<PmWorkStationEntity> pmStationEntityList = getPmStationEntity(shopId);
        if (pmStationEntityList.isEmpty()) {
            return target;
        }
        List<PmWorkStationEntity> pmStationEntityByAll = getPmStationEntityByAll(shopId);
        if (pmStationEntityByAll.isEmpty()) {
            return target;
        }
        target.put(PmWorkStationEntity.class.getSimpleName(), pmStationEntityList);
        target.put("ALL" + PmWorkStationEntity.class.getSimpleName(), pmStationEntityByAll);
        target.put(PmOtEntity.class.getSimpleName(), getPmOtEntity(shopId));
        target.put(PmWoEntity.class.getSimpleName(), getPmWoEntity(shopId));
        target.put(PmToolEntity.class.getSimpleName(), getPmToolEntity(shopId));
        target.put(PmToolJobEntity.class.getSimpleName(), getPmToolJobEntity(shopId));
        target.put(PmPullCordEntity.class.getSimpleName(), getPmPullCordEntity(shopId));
        target.put(PmBopEntity.class.getSimpleName(), getPmBopEntity(shopId));
        target.put(PmWorkstationMaterialEntity.class.getSimpleName(), getPmWorkstationMaterialEntity(shopId));
        target.put(PmWorkstationOperBookEntity.class.getSimpleName(), getPmWorkstationOperBookEntity(shopId));
        target.put(PmEquipmentEntity.class.getSimpleName(), getPmEquipmentEntity(shopId));
        return target;
    }


    private void setAreaElements(List<String> baseFieldNameList,
                                 Element parentElement,
                                 Map<String, Object> entityMap) throws IllegalAccessException {
        List<PmLineEntity> pmAreaEntities = (List<PmLineEntity>) entityMap.get(PmLineEntity.class.getSimpleName());
        if (pmAreaEntities != null && !pmAreaEntities.isEmpty()) {
            for (PmLineEntity pmAreaEntity : pmAreaEntities) {
                Element areaElement = getElement(pmAreaEntity, baseFieldNameList);
                //设置生产线下面的AVI
                setPmAviElements(baseFieldNameList, areaElement, entityMap, String.valueOf(pmAreaEntity.getId()));
                //设置生产线下面的工位
                setPmStationElements(baseFieldNameList, areaElement, entityMap, String.valueOf(pmAreaEntity.getId()));
                parentElement.add(areaElement);

            }
        }
    }

    private Element setAndGetShopElements(List<String> baseFieldNameList,
                                          Map<String, Object> entityMap) throws IllegalAccessException {
        PmWorkShopEntity pmShopEntity = (PmWorkShopEntity) entityMap.get(PmWorkShopEntity.class.getSimpleName());
        Element shopElement = null;
        if (pmShopEntity != null) {
            shopElement = getElement(pmShopEntity, baseFieldNameList);
            setAreaElements(baseFieldNameList, shopElement, entityMap);
        }
        return shopElement;
    }


    private void setPmAviElements(List<String> baseFieldNameList,
                                  Element parentElement,
                                  Map<String, Object> entityMap,
                                  String parentId) throws IllegalAccessException {
        List<PmAviEntity> allPmAviEntityList = (List<PmAviEntity>) entityMap.get(PmAviEntity.class.getSimpleName());
        if (allPmAviEntityList == null || allPmAviEntityList.isEmpty()) {
            return;
        }
        List<PmAviEntity> pmAviEntityListByAreaId = allPmAviEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmLineId()))).collect(Collectors.toList());
        if (!pmAviEntityListByAreaId.isEmpty()) {
            for (PmAviEntity pmAviEntity : pmAviEntityListByAreaId) {
                parentElement.add(getElement(pmAviEntity, baseFieldNameList));
            }
        }
    }

    private void setPmStationElements(List<String> baseFieldNameList,
                                      Element parentElement,
                                      Map<String, Object> entityMap,
                                      String parentId) throws IllegalAccessException {
        List<PmWorkStationEntity> pmWorkStationEntityList = (List<PmWorkStationEntity>) entityMap.get(PmWorkStationEntity.class.getSimpleName());
        if (pmWorkStationEntityList == null || pmWorkStationEntityList.isEmpty()) {
            return;
        }
        List<PmWorkStationEntity> pmWorkStationEntityListByAreaId = pmWorkStationEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmLineId()))).collect(Collectors.toList());
        if (!pmWorkStationEntityListByAreaId.isEmpty()) {
            for (PmWorkStationEntity pmWorkStationEntity : pmWorkStationEntityListByAreaId) {
                Element workplaceElement = getElement(pmWorkStationEntity, baseFieldNameList);
                //添加ot
                setPmOtElements(baseFieldNameList, workplaceElement, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加Wo
                setPmWoElements(baseFieldNameList, workplaceElement, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加工具
                setPmToolElements(baseFieldNameList, workplaceElement, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                //添加拉绳
                setPmPullCordElements(baseFieldNameList, workplaceElement, entityMap, String.valueOf(pmWorkStationEntity.getId()));
                parentElement.add(workplaceElement);
            }
        }

    }

    private void setPmOtElements(List<String> baseFieldNameList,
                                 Element parentElement,
                                 Map<String, Object> entityMap,
                                 String parentId) throws IllegalAccessException {
        List<PmOtEntity> pmOtEntityList = (List<PmOtEntity>) entityMap.get(PmOtEntity.class.getSimpleName());
        if (pmOtEntityList == null || pmOtEntityList.isEmpty()) {
            return;
        }
        List<PmOtEntity> pmOtEntityListByWorkplaceId = pmOtEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmOtEntityListByWorkplaceId.isEmpty()) {
            for (PmOtEntity pmOtEntity : pmOtEntityListByWorkplaceId) {
                parentElement.add(getElement(pmOtEntity, baseFieldNameList));
            }
        }
    }

    private void setPmWoElements(List<String> baseFieldNameList,
                                 Element parentElement,
                                 Map<String, Object> entityMap,
                                 String parentId) throws IllegalAccessException {
        List<PmWoEntity> pmWoEntityList = (List<PmWoEntity>) entityMap.get(PmWoEntity.class.getSimpleName());
        if (pmWoEntityList == null || pmWoEntityList.isEmpty()) {
            return;
        }
        List<PmWoEntity> pmWoEntityListByWorkplaceId = pmWoEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmWoEntityListByWorkplaceId.isEmpty()) {
            for (PmWoEntity pmWoEntity : pmWoEntityListByWorkplaceId) {
                parentElement.add(getElement(pmWoEntity, baseFieldNameList));
            }
        }
    }

    private void setPmToolElements(List<String> baseFieldNameList,
                                   Element parentElement,
                                   Map<String, Object> entityMap,
                                   String parentId) throws IllegalAccessException {
        List<PmToolEntity> pmToolEntityList = (List<PmToolEntity>) entityMap.get(PmToolEntity.class.getSimpleName());
        if (pmToolEntityList == null || pmToolEntityList.isEmpty()) {
            return;
        }
        List<PmToolEntity> pmToolEntityListByWorkplaceId = pmToolEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmToolEntityListByWorkplaceId.isEmpty()) {
            for (PmToolEntity pmToolEntity : pmToolEntityListByWorkplaceId) {
                Element toolElement = getElement(pmToolEntity, baseFieldNameList);
                setPmToolJobElements(baseFieldNameList, toolElement, entityMap, String.valueOf(pmToolEntity.getId()));
                parentElement.add(toolElement);
            }
        }
    }

    private void setPmToolJobElements(List<String> baseFieldNameList,
                                      Element parentElement,
                                      Map<String, Object> entityMap,
                                      String parentId) throws IllegalAccessException {
        List<PmToolJobEntity> pmToolJobEntityList = (List<PmToolJobEntity>) entityMap.get(PmToolJobEntity.class.getSimpleName());
        if (pmToolJobEntityList == null || pmToolJobEntityList.isEmpty()) {
            return;
        }
        List<PmToolJobEntity> pmToolJobEntityListByToolId = pmToolJobEntityList.stream().filter(v -> Objects.equals(parentId,v.getPrcPmToolId())).collect(Collectors.toList());
        if (!pmToolJobEntityListByToolId.isEmpty()) {
            for (PmToolJobEntity pmToolJobEntity : pmToolJobEntityListByToolId) {
                parentElement.add(getElement(pmToolJobEntity, baseFieldNameList));
            }
        }
    }

    private void setPmPullCordElements(List<String> baseFieldNameList,
                                       Element parentElement,
                                       Map<String, Object> entityMap,
                                       String parentId) throws IllegalAccessException {
        List<PmPullCordEntity> pmPullCordEntityList = (List<PmPullCordEntity>) entityMap.get(PmPullCordEntity.class.getSimpleName());
        if (pmPullCordEntityList == null || pmPullCordEntityList.isEmpty()) {
            return;
        }
        List<PmPullCordEntity> pmPullCordEntityListByToolId = pmPullCordEntityList.stream().filter(v -> parentId.equals(String.valueOf(v.getPrcPmWorkstationId()))).collect(Collectors.toList());
        if (!pmPullCordEntityListByToolId.isEmpty()) {
            for (PmPullCordEntity pmPullcordEntity : pmPullCordEntityListByToolId) {
                parentElement.add(getElement(pmPullcordEntity, baseFieldNameList));
            }
        }

    }


}
