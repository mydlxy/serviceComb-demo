package com.ca.mfd.prc.rc.rcbs.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.rc.rcbs.dto.CachesItemsVO;
import com.ca.mfd.prc.rc.rcbs.dto.LaneCacheVO;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteCacheDTO;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteCacheItemVO;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteCacheLogEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteLaneEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePublishLogEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteCacheLogService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteCacheService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteLaneService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePublishService;
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
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbsroutecache")
@Tag(name = "路由缓存服务", description = "路由缓存")
public class RcBsRouteCacheController extends BaseController<RcBsRouteCacheEntity> {
    @Autowired
    private IRcBsRouteLaneService rcRouteLaneService;
    @Autowired
    private IRcBsRouteCacheLogService rcRouteCacheLogService;
    @Autowired
    private IRcBsRoutePublishService rcBsRoutePublishService;
    @Autowired
    private IRcBsRouteAreaService rcBsRouteAreaService;
    private final IRcBsRouteCacheService rcRouteCacheService;

    @Autowired
    public RcBsRouteCacheController(IRcBsRouteCacheService rcRouteCacheService) {
        this.crudService = rcRouteCacheService;
        this.rcRouteCacheService = rcRouteCacheService;
    }

    @GetMapping("getbyid")
    @Operation(summary = "获取条记录")
    @Override
    public ResultVO getById(@RequestParam(value = "id") String id) {
        RcBsRouteCacheEntity item = rcRouteCacheService.get(id);
        if (item == null) {
            throw new InkelinkException("数据不存在");
        }
        ResultVO<RcRouteCacheItemVO> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        RcBsRoutePublishEntity routePublish = rcBsRoutePublishService.getEntityBySn(item.getSn());
        String publishDisplayNo = routePublish == null ? "0" : routePublish.getDisplayNo().toString();
        RcRouteCacheItemVO data = new RcRouteCacheItemVO();
        data.setId(item.getId());
        data.setSn(item.getSn());
        data.setLaneId(item.getPrcRcBsRouteLaneId());
        data.setLaneCode(item.getLaneCode());
        data.setDisplayNo(item.getDisplayNo().toString());
        data.setPublishDisplayNo(publishDisplayNo);
        data.setAreaId(item.getPrcRcBsRouteAreaId());
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
    public ResultVO<String> edit(@RequestBody RcBsRouteCacheEntity model) {
        long cacheCount = rcRouteCacheService.getCountByCodeAndAreaId(model.getSn(), model.getPrcRcBsRouteAreaId(), model.getId());
        if (cacheCount > 0) {
            throw new InkelinkException("该车辆已存在");
        }
        RcBsRouteLaneEntity lane = rcRouteLaneService.getLaneEntityByAreaIdAndCode(model.getPrcRcBsRouteAreaId(), model.getLaneCode());
        if (lane == null) {
            throw new InkelinkException("车道号" + model.getLaneCode() + ",在路由中不存在");
        }
        RcBsRouteAreaEntity area = rcBsRouteAreaService.get(model.getPrcRcBsRouteAreaId());
        if (area == null) {
            throw new InkelinkException("缓存区不存在");
        }
        if (area.getAreaCode() > 1 && "pbs".equalsIgnoreCase(area.getBufferCode())) {
            Date beginDt = new Date();
            Date publishDisplayNo = DateUtils.tryParseExact(model.getPublishDisplayNo().toString(), DateUtils.DATE_TIME_PATTERN_M);
            if (publishDisplayNo == null) {
                beginDt = new Date();
            }
            RcBsRoutePublishEntity publish = rcBsRoutePublishService.getEntityBySn(model.getSn());
            if (model.getId() > 0) {
                RcBsRouteCacheEntity cacheData = rcRouteCacheService.get(model.getId());
                if (cacheData == null) {
                    throw new InkelinkException("用于修改的缓存不存在");
                }
                if (!cacheData.getSn().equalsIgnoreCase(model.getSn())) {
                    publish = rcBsRoutePublishService.getEntityBySn(cacheData.getSn());
                }
            }
            if (publish == null) {
                RcBsRoutePublishEntity info = new RcBsRoutePublishEntity();
                info.setSn(model.getSn());
                info.setBeginDt(beginDt);
                info.setDisplayNo(model.getPublishDisplayNo());
                info.setBufferCode(area.getBufferCode());
                rcBsRoutePublishService.save(info);
            } else {
                UpdateWrapper<RcBsRoutePublishEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<RcBsRoutePublishEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.set(RcBsRoutePublishEntity::getSn, model.getSn());
                lambdaUpdateWrapper.set(RcBsRoutePublishEntity::getBeginDt, beginDt);
                lambdaUpdateWrapper.set(RcBsRoutePublishEntity::getDisplayNo, model.getDisplayNo());
                lambdaUpdateWrapper.set(RcBsRoutePublishEntity::getBufferCode, area.getBufferCode());
                lambdaUpdateWrapper.eq(RcBsRoutePublishEntity::getId, publish.getId());
                rcBsRoutePublishService.update(updateWrapper);
            }
        }

        if (model.getId() <= 0) {
            Long laneCacheCount = rcRouteCacheService.getCountByLaneCodeAndAreaId(model.getPrcRcBsRouteAreaId(), model.getLaneCode());
            if (laneCacheCount >= lane.getMaxValue()) {
                throw new InkelinkException("车道号:" + model.getLaneCode() + ",缓存已满，无法插入车辆信息");
            }
            RcBsRouteCacheEntity rcRouteCacheEntity = new RcBsRouteCacheEntity();
            //rcRouteCacheEntity.setId(IdGenerator.getId());
            rcRouteCacheEntity.setSn(model.getSn());
            rcRouteCacheEntity.setDisplayNo(model.getDisplayNo());
            rcRouteCacheEntity.setPrcRcBsRouteAreaId(model.getPrcRcBsRouteAreaId());
            rcRouteCacheEntity.setPrcRcBsRouteLaneId(lane.getId());
            rcRouteCacheEntity.setLaneCode(model.getLaneCode());
            rcRouteCacheService.save(rcRouteCacheEntity);
        } else {
            UpdateWrapper<RcBsRouteCacheEntity> updateWrapper = new UpdateWrapper<>();
            LambdaUpdateWrapper<RcBsRouteCacheEntity> lambdaUpdateWrapper = updateWrapper.lambda();
            lambdaUpdateWrapper.eq(RcBsRouteCacheEntity::getId, model.getId());
            lambdaUpdateWrapper.set(RcBsRouteCacheEntity::getSn, model.getSn());
            lambdaUpdateWrapper.set(RcBsRouteCacheEntity::getDisplayNo, model.getDisplayNo());
            lambdaUpdateWrapper.set(RcBsRouteCacheEntity::getPrcRcBsRouteAreaId, model.getPrcRcBsRouteAreaId());
            lambdaUpdateWrapper.set(RcBsRouteCacheEntity::getPrcRcBsRouteLaneId, lane.getId());
            lambdaUpdateWrapper.set(RcBsRouteCacheEntity::getLaneCode, model.getLaneCode());
            rcRouteCacheService.update(updateWrapper);
        }

        rcRouteCacheService.saveChange();
        return new ResultVO<String>().ok("成功！", "保存成功");
    }


    @PostMapping("routeout")
    @Operation(summary = "手动删除缓存队列")
    public ResultVO<String> routeOut(@RequestBody RcRouteCacheDTO.RouteOutModel model) {
        RcBsRouteCacheEntity cache = rcRouteCacheService.get(model.getId());
        if (cache == null) {
            throw new InkelinkException("缓存不存在");
        }
        rcRouteCacheService.delete(cache.getId(), false);
        //插入出车记录
        RcBsRouteCacheLogEntity info = new RcBsRouteCacheLogEntity();
        info.setId(cache.getId());
        info.setDisplayNo(cache.getDisplayNo());
        info.setSn(cache.getSn());
        info.setEndDt(new Date());
        info.setPrcRcBsRouteAreaId(cache.getPrcRcBsRouteAreaId());
        info.setPrcRcBsRouteLaneId(cache.getPrcRcBsRouteLaneId());
        info.setLaneCode(cache.getLaneCode());
        rcRouteCacheLogService.save(info);

        RcBsRouteAreaEntity area = rcBsRouteAreaService.get(cache.getPrcRcBsRouteAreaId());
        //  精排区才添加发布记录
        if (area != null && area.getAreaCode() > 1 && "pbs".equalsIgnoreCase(area.getBufferCode())) {
            Boolean cacheAny = rcRouteCacheService.getCacheEntityBySn(cache.getSn(), ConvertUtils.stringToLong(model.getId()));
            RcBsRoutePublishEntity publish = rcBsRoutePublishService.getEntityBySn(cache.getSn());
            if (publish != null && cacheAny) {
                rcBsRoutePublishService.delete(publish.getId());
                RcBsRoutePublishLogEntity logInfo = new RcBsRoutePublishLogEntity();
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
        RcRouteAreaItemVO area = rcBsRouteAreaService.getAreaEntityByAreaId(ConvertUtils.stringToLong(model.getAreaId()));
        if (area == null) {
            throw new InkelinkException("区域不存在");
        }
        List<RcBsRouteLaneEntity> laneList = rcRouteLaneService.getLaneEntityByAreaId(model.getAreaId());
        List<RcBsRouteCacheEntity> cacheList = rcRouteCacheService.getCacheEntityByAreaId(model.getAreaId());
        List<LaneCacheVO> data = new ArrayList<>();
        for (RcBsRouteLaneEntity item : laneList) {
            LaneCacheVO laneCacheVO = new LaneCacheVO();
            laneCacheVO.setId(item.getId());
            laneCacheVO.setPrcRcBsRouteAreaId(item.getPrcRcBsRouteAreaId());
            laneCacheVO.setLaneCode(item.getLaneCode());
            laneCacheVO.setLaneName(item.getLaneName());
            laneCacheVO.setLaneType(item.getLaneType());
            laneCacheVO.setMaxLevel(item.getMaxLevel());
            laneCacheVO.setMaxValue(item.getMaxValue());
            laneCacheVO.setDisplayNo(item.getDisplayNo());
            laneCacheVO.setLastUpdatedUser(item.getLastUpdatedUser());
            laneCacheVO.setLastUpdatedBy(item.getLastUpdatedBy());
            laneCacheVO.setLastUpdateDate(item.getLastUpdateDate());
            laneCacheVO.setCreatedUser(item.getCreatedUser());
            laneCacheVO.setCreatedBy(item.getCreatedBy());
            laneCacheVO.setCreationDate(item.getCreationDate());
            List<CachesItemsVO> cachesList = cacheList.stream().filter(s -> Objects.equals(s.getPrcRcBsRouteLaneId(), item.getId()))
                    .map(c -> {
                        CachesItemsVO et = new CachesItemsVO();
                        et.setPrcRcBsRouteLaneId(c.getPrcRcBsRouteLaneId());
                        et.setId(c.getId());
                        et.setSn(c.getSn());
                        et.setDisplayNo(c.getDisplayNo());
                        et.setRcRouteAreaId(c.getPrcRcBsRouteAreaId());
                        et.setLaneCode(c.getLaneCode());
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