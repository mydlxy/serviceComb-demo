package com.ca.mfd.prc.bdc.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.bdc.dto.CachesItemsVO;
import com.ca.mfd.prc.bdc.dto.LaneCacheVO;
import com.ca.mfd.prc.bdc.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.bdc.dto.RcRouteCacheDTO;
import com.ca.mfd.prc.bdc.dto.RcRouteCacheItemVO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteCacheEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteCacheLogEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePublishEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePublishLogEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteAreaService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteCacheLogService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteCacheService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteLaneService;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePublishService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由缓存Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutecache")
@Tag(name = "路由缓存服务", description = "路由缓存")
public class RcBdcRouteCacheController extends BaseController<RcBdcRouteCacheEntity> {

    @Autowired
    private IRcBdcRouteLaneService rcRouteLaneService;
    @Autowired
    private IRcBdcRouteCacheLogService rcRouteCacheLogService;
    @Autowired
    private IRcBdcRoutePublishService rcBdcRoutePublishService;
    @Autowired
    private IRcBdcRouteAreaService rcBdcRouteAreaService;

    private final IRcBdcRouteCacheService rcBdcRouteCacheService;

    @Autowired
    public RcBdcRouteCacheController(IRcBdcRouteCacheService rcBdcRouteCacheService) {
        this.crudService = rcBdcRouteCacheService;
        this.rcBdcRouteCacheService = rcBdcRouteCacheService;
    }

    @GetMapping("getbyid")
    @Operation(summary = "根据ID获取一条条记录")
    @Override
    public ResultVO getById(@RequestParam(value = "id") String id) {
        RcBdcRouteCacheEntity item = rcBdcRouteCacheService.get(id);
        if (item == null) {
            throw new InkelinkException("数据不存在");
        }
        ResultVO<RcRouteCacheItemVO> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        RcBdcRoutePublishEntity routePublish = rcBdcRoutePublishService.getEntityBySn(item.getSn());
        String publishDisplayNo = routePublish == null ? "0" : routePublish.getDisplayNo().toString();
        RcRouteCacheItemVO data = new RcRouteCacheItemVO();
        data.setId(item.getId());
        data.setSn(item.getSn());
        data.setSnType(item.getSnType());
        data.setLaneId(item.getPrcBdcRouteLaneId());
        data.setLaneCode(item.getLaneCode());
        data.setDisplayNo(item.getDisplayNo().toString());
        data.setPublishDisplayNo(publishDisplayNo);
        data.setAreaId(item.getPrcBdcRouteAreaId());
        data.setBeginDt(item.getBeginDt());
        data.setCreationDate(item.getCreationDate());
        data.setCreatedUser(item.getCreatedUser());
        data.setCreatedBy(item.getCreatedBy());
        data.setLastUpdateDate(item.getLastUpdateDate());
        data.setLastUpdatedUser(item.getLastUpdatedUser());
        data.setLastUpdatedBy(item.getLastUpdatedBy());
        data.setFlag(item.getFlag());
        data.setAttribute1(item.getAttribute1());
        data.setAttribute2(item.getAttribute2());
        data.setAttribute3(item.getAttribute3());
        data.setAttribute4(item.getAttribute4());
        data.setAttribute5(item.getAttribute5());
        data.setAttribute6(item.getAttribute6());
        data.setAttribute7(item.getAttribute7());
        data.setAttribute8(item.getAttribute8());
        data.setAttribute9(item.getAttribute9());
        data.setAttribute10(item.getAttribute10());
        return result.ok(data);
    }

    @PostMapping("edit")
    @Operation(summary = "手动保存缓存队列排序号")
    @Override
    public ResultVO<String> edit(@RequestBody RcBdcRouteCacheEntity model) {
        long cacheCount = rcBdcRouteCacheService.getCountByCodeAndAreaId(model.getSn(), model.getPrcBdcRouteAreaId(), model.getId());
        if (cacheCount > 0) {
            throw new InkelinkException("该车辆已存在");
        }
        RcBdcRouteLaneEntity lane = rcRouteLaneService.getLaneEntityByAreaIdAndCode(model.getPrcBdcRouteAreaId(), model.getLaneCode());
        if (lane == null) {
            throw new InkelinkException("车道号" + model.getLaneCode() + ",在路由中不存在");
        }
        RcBdcRouteAreaEntity area = rcBdcRouteAreaService.get(model.getPrcBdcRouteAreaId());
        if (area == null) {
            throw new InkelinkException("缓存区不存在");
        }

        if (area.getAreaCode() > 1 && "pbs".equalsIgnoreCase(area.getBufferCode())) {
            Date beginDt = new Date();
            Date publishDisplayNo = DateUtils.tryParseExact(model.getPublishDisplayNo().toString(), DateUtils.DATE_TIME_PATTERN_M);
            if (publishDisplayNo == null) {
                beginDt = new Date();
            }
            RcBdcRoutePublishEntity publish = rcBdcRoutePublishService.getEntityBySn(model.getSn());
            if (model.getId() > 0) {
                RcBdcRouteCacheEntity cacheData = rcBdcRouteCacheService.get(model.getId());
                if (cacheData == null) {
                    throw new InkelinkException("用于修改的缓存不存在");
                }
                if (!cacheData.getSn().equalsIgnoreCase(model.getSn())) {
                    publish = rcBdcRoutePublishService.getEntityBySn(cacheData.getSn());
                }
            }
            if (publish == null) {
                RcBdcRoutePublishEntity info = new RcBdcRoutePublishEntity();
                info.setSn(model.getSn());
                info.setBeginDt(beginDt);
                info.setDisplayNo(model.getPublishDisplayNo());
                info.setBufferCode(area.getBufferCode());
                rcBdcRoutePublishService.save(info);
            } else {
                UpdateWrapper<RcBdcRoutePublishEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<RcBdcRoutePublishEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.set(RcBdcRoutePublishEntity::getSn, model.getSn());
                lambdaUpdateWrapper.set(RcBdcRoutePublishEntity::getBeginDt, beginDt);
                lambdaUpdateWrapper.set(RcBdcRoutePublishEntity::getDisplayNo, model.getDisplayNo());
                lambdaUpdateWrapper.set(RcBdcRoutePublishEntity::getBufferCode, area.getBufferCode());
                lambdaUpdateWrapper.eq(RcBdcRoutePublishEntity::getId, publish.getId());
                rcBdcRoutePublishService.update(updateWrapper);
            }
        }

        //if (StringUtils.equals(model.getId(), UUIDUtils.getEmpty())) {
        if (model.getId() <= 0) {
            Long laneCacheCount = rcBdcRouteCacheService.getCountByLaneCodeAndAreaId(model.getPrcBdcRouteAreaId(), model.getLaneCode());
            if (laneCacheCount >= lane.getMaxValue()) {
                throw new InkelinkException("车道号:" + model.getLaneCode() + ",缓存已满，无法插入车辆信息");
            }
            RcBdcRouteCacheEntity rcRouteCacheEntity = new RcBdcRouteCacheEntity();
            //rcRouteCacheEntity.setId(IdGenerator.getId());
            rcRouteCacheEntity.setSn(model.getSn());
            rcRouteCacheEntity.setSnType(model.getSnType());
            rcRouteCacheEntity.setDisplayNo(model.getDisplayNo());
            rcRouteCacheEntity.setPrcBdcRouteAreaId(model.getPrcBdcRouteAreaId());
            rcRouteCacheEntity.setPrcBdcRouteLaneId(lane.getId());
            rcRouteCacheEntity.setLaneCode(model.getLaneCode());
            rcBdcRouteCacheService.save(rcRouteCacheEntity);
        } else {
            UpdateWrapper<RcBdcRouteCacheEntity> updateWrapper = new UpdateWrapper<>();
            LambdaUpdateWrapper<RcBdcRouteCacheEntity> lambdaUpdateWrapper = updateWrapper.lambda();
            lambdaUpdateWrapper.eq(RcBdcRouteCacheEntity::getId, model.getId());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getSn, model.getSn());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getSnType, model.getSnType());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getDisplayNo, model.getDisplayNo());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getPrcBdcRouteAreaId, model.getPrcBdcRouteAreaId());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getPrcBdcRouteLaneId, lane.getId());
            lambdaUpdateWrapper.set(RcBdcRouteCacheEntity::getLaneCode, model.getLaneCode());
            rcBdcRouteCacheService.update(updateWrapper);
        }
        rcBdcRouteCacheService.saveChange();
        return new ResultVO<String>().ok("保存成功");
    }

    @PostMapping("routeout")
    @Operation(summary = "手动删除缓存队列")
    public ResultVO<String> routeOut(@RequestBody RcRouteCacheDTO.RouteOutModel model) {
        RcBdcRouteCacheEntity cache = rcBdcRouteCacheService.get(model.getId());
        if (cache == null) {
            throw new InkelinkException("缓存不存在");
        }
        rcBdcRouteCacheService.delete(cache.getId(), false);
        //插入出车记录
        RcBdcRouteCacheLogEntity info = new RcBdcRouteCacheLogEntity();
        info.setId(cache.getId());
        info.setDisplayNo(cache.getDisplayNo());
        info.setSn(cache.getSn());
        info.setEndDt(new Date());
        info.setPrcBdcRouteAreaId(cache.getPrcBdcRouteAreaId());
        info.setPrcBdcRouteLaneId(cache.getPrcBdcRouteLaneId());
        info.setLaneCode(cache.getLaneCode());
        rcRouteCacheLogService.save(info);

        RcBdcRouteAreaEntity area = rcBdcRouteAreaService.get(cache.getPrcBdcRouteAreaId());
        //  精排区才添加发布记录
        if (area != null && area.getAreaCode() > 1 && "pbs".equalsIgnoreCase(area.getBufferCode())) {
            Boolean cacheAny = rcBdcRouteCacheService.getCacheEntityBySn(cache.getSn(), ConvertUtils.stringToLong(model.getId()));
            RcBdcRoutePublishEntity publish = rcBdcRoutePublishService.getEntityBySn(cache.getSn());
            if (publish != null && cacheAny) {
                rcBdcRoutePublishService.delete(publish.getId());
                RcBdcRoutePublishLogEntity logInfo = new RcBdcRoutePublishLogEntity();
                logInfo.setId(publish.getId());
                logInfo.setDisplayNo(publish.getDisplayNo());
                logInfo.setSn(publish.getSn());
                logInfo.setEndDt(new Date());
                logInfo.setBeginDt(publish.getBeginDt());
                logInfo.setBufferCode(publish.getBufferCode());
            }
        }
        rcRouteCacheLogService.saveChange();
        return new ResultVO<String>().ok("成功！", "删除成功");
    }

    @PostMapping("lanecache")
    @Operation(summary = "查询车道和缓存信息")
    public ResultVO<List<LaneCacheVO>> laneCache(@RequestBody RcRouteCacheDTO.LaneModel model) {
        RcRouteAreaItemVO area = rcBdcRouteAreaService.getAreaEntityByAreaId(ConvertUtils.stringToLong(model.getAreaId()));
        if (area == null) {
            throw new InkelinkException("区域不存在");
        }
        List<RcBdcRouteLaneEntity> laneList = rcRouteLaneService.getLaneEntityByAreaId(model.getAreaId());
        List<RcBdcRouteCacheEntity> cacheList = rcBdcRouteCacheService.getCacheEntityByAreaId(model.getAreaId());
        List<LaneCacheVO> data = new ArrayList<>();
        for (RcBdcRouteLaneEntity item : laneList) {
            LaneCacheVO laneCacheVO = new LaneCacheVO();
            laneCacheVO.setId(item.getId());
            laneCacheVO.setLaneCode(item.getLaneCode());
            laneCacheVO.setLaneName(item.getLaneName());
            laneCacheVO.setDisplayNo(item.getDisplayNo());
            laneCacheVO.setPrcRcBdcRouteAreaId(item.getPrcBdcRouteAreaId());
            laneCacheVO.setRcRouteAreaCode(area.getAreaCode());
            laneCacheVO.setRcRouteAreaName(area.getAreaName());
            laneCacheVO.setMaxValue(item.getMaxValue());
            laneCacheVO.setLaneType(item.getLaneType());
            laneCacheVO.setBdcArea(item.getBdcArea());
            laneCacheVO.setBdcStorage(item.getBdcStorage());
            laneCacheVO.setPositionX(item.getPositionX());
            laneCacheVO.setPositionY(item.getPositionY());
            laneCacheVO.setPositionZ(item.getPositionZ());
            //laneCacheVO.setMaxLevel(item.getMaxLevel());
            laneCacheVO.setLastUpdatedUser(item.getLastUpdatedUser());
            laneCacheVO.setLastUpdatedBy(item.getLastUpdatedBy());
            laneCacheVO.setLastUpdateDate(item.getLastUpdateDate());
            laneCacheVO.setCreatedUser(item.getCreatedUser());
            laneCacheVO.setCreatedBy(item.getCreatedBy());
            laneCacheVO.setCreationDate(item.getCreationDate());
            List<CachesItemsVO> cachesList = cacheList.stream().filter(s -> Objects.equals(s.getPrcBdcRouteLaneId(), item.getId()))
                    .map(c -> {
                        CachesItemsVO et = new CachesItemsVO();
                        et.setId(c.getId());
                        et.setRcRouteAreaId(c.getPrcBdcRouteAreaId());
                        et.setPrcRcBdcRouteLaneId(c.getPrcBdcRouteLaneId());
                        et.setLaneCode(c.getLaneCode());
                        et.setSn(c.getSn());
                        et.setSnType(c.getSnType());
                        et.setDisplayNo(c.getDisplayNo());
                        et.setLastUpdatedUser(c.getLastUpdatedUser());
                        et.setLastUpdatedBy(c.getLastUpdatedBy());
                        et.setLastUpdateDate(c.getLastUpdateDate());
                        et.setCreatedUser(c.getCreatedUser());
                        et.setCreatedBy(c.getCreatedBy());
                        et.setCreationDate(c.getCreationDate());
                        return et;
                    }).collect(Collectors.toList());
            laneCacheVO.setCaches(cachesList);
            data.add(laneCacheVO);
        }
        return new ResultVO<List<LaneCacheVO>>().ok(data, "成功！");
    }
}