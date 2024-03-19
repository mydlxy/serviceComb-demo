package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.entity.EpsBodySparePartTrackEntity;
import com.ca.mfd.prc.eps.entity.EpsBodySparePartTrackLogEntity;
import com.ca.mfd.prc.eps.entity.EpsSpareBindingDetailEntity;
import com.ca.mfd.prc.eps.mapper.IEpsBodySparePartTrackMapper;
import com.ca.mfd.prc.eps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsBodySparePartTrackLogService;
import com.ca.mfd.prc.eps.service.IEpsBodySparePartTrackService;
import com.ca.mfd.prc.eps.service.IEpsSpareBindingDetailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 焊装车间备件运输跟踪服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsBodySparePartTrackServiceImpl extends AbstractCrudServiceImpl<IEpsBodySparePartTrackMapper, EpsBodySparePartTrackEntity> implements IEpsBodySparePartTrackService {
    private static final String CACHE_NAME = "PRC_EPS_BODY_SPARE_PART_TRACK";
    private static final Logger logger = LoggerFactory.getLogger(EpsBodySparePartTrackServiceImpl.class);
    @Autowired
    private LocalCache localCache;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IEpsSpareBindingDetailService epsSpareBindingDetailService;
    @Autowired
    private IEpsBodySparePartTrackLogService epsBodySparePartTrackLogService;

    @Override
    public void afterInsert(EpsBodySparePartTrackEntity entity) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterUpdate(EpsBodySparePartTrackEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsBodySparePartTrackEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Wrapper<EpsBodySparePartTrackEntity> queryWrapper) {
        removeCache();
    }

    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<EpsBodySparePartTrackEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsBodySparePartTrackEntity>> getDataFunc = (obj) -> {
                List<EpsBodySparePartTrackEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsBodySparePartTrackEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 呼叫空撬
     *
     * @param workstationCode
     * @return
     */
    @Override
    public void callingCarrier(String workstationCode) {
        String qcVin = sysSnConfigProvider.createSn("TPVIN");
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmWorkStationEntity stationInfo = pmall.getStations().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), workstationCode)).findFirst().orElse(null);
        EpsBodySparePartTrackEntity data = new EpsBodySparePartTrackEntity();
        data.setVirtualVin(qcVin);
        data.setBindingWorkstationCode(workstationCode);
        data.setBindingWorkstationName(stationInfo.getWorkstationName());
        data.setTrackStatus(1);

        insert(data);

        EpsBodySparePartTrackLogEntity log = new EpsBodySparePartTrackLogEntity();
        log.setPrcEpsBodySparePartTrackId(data.getId());
        log.setVirtualVin(qcVin);
        log.setTrackStatus(1);
        log.setOpTime(new Date());
        epsBodySparePartTrackLogService.insert(log);
    }

    /**
     * 获取等待下发数据
     *
     * @return
     */
    public EpsBodySparePartTrackEntity getDownCallingCarrier() {
        QueryWrapper<EpsBodySparePartTrackEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsBodySparePartTrackEntity::getTrackStatus, 1).orderByAsc(EpsBodySparePartTrackEntity::getCreationDate);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 更新等待下发数据
     *
     * @param virtualVin
     * @return
     */
    public void downWaitData(String virtualVin) {
        QueryWrapper<EpsBodySparePartTrackEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsBodySparePartTrackEntity::getVirtualVin, virtualVin);
        EpsBodySparePartTrackEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);

        UpdateWrapper<EpsBodySparePartTrackEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsBodySparePartTrackEntity::getTrackStatus, 2)
                .eq(EpsBodySparePartTrackEntity::getId, data.getId());
        update(upset);

        EpsBodySparePartTrackLogEntity log = new EpsBodySparePartTrackLogEntity();
        log.setPrcEpsBodySparePartTrackId(data.getId());
        log.setVirtualVin(data.getVirtualVin());
        log.setTrackStatus(2);
        log.setOpTime(new Date());
        epsBodySparePartTrackLogService.insert(log);
    }

    /**
     * 绑定之后放撬
     *
     * @param virtualVin
     * @return
     */
    @Override
    public void releasePassSuccess(String virtualVin) {
        QueryWrapper<EpsBodySparePartTrackEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsBodySparePartTrackEntity::getVirtualVin, virtualVin);
        EpsBodySparePartTrackEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (data == null || !data.getIsBindingSparePart()) {
            throw new InkelinkException("空撬的VIN" + virtualVin + "还未绑定备件，无法接收放行信号");
        }

        UpdateWrapper<EpsBodySparePartTrackEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsBodySparePartTrackEntity::getTrackStatus, 3)
                .eq(EpsBodySparePartTrackEntity::getId, data.getId());
        update(upset);

        EpsBodySparePartTrackLogEntity log = new EpsBodySparePartTrackLogEntity();
        log.setPrcEpsBodySparePartTrackId(data.getId());
        log.setVirtualVin(virtualVin);
        log.setTrackStatus(3);
        log.setOpTime(new Date());
        epsBodySparePartTrackLogService.insert(log);

    }

    /**
     * 绑定备件
     *
     * @param virtualVin
     * @param sparePartVin 备件VIN号-队列编码
     * @return
     */
    public void bindingSparePart(String virtualVin, Map<String, String> sparePartVin) {
        QueryWrapper<EpsBodySparePartTrackEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsBodySparePartTrackEntity::getVirtualVin, virtualVin)
                .lt(EpsBodySparePartTrackEntity::getTrackStatus, 3);
        EpsBodySparePartTrackEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);

        if (data == null) {
            throw new InkelinkException("空撬的虚拟VIN:" + virtualVin + "状态不正确，无法绑定备件");
        }
        List<PpsOrderEntity> orderInfos = ppsOrderProvider.getListByBarcodes(sparePartVin.keySet().stream().collect(Collectors.toList()));

        List<EpsSpareBindingDetailEntity> details = new ArrayList<>();

        for (PpsOrderEntity orderInfo : orderInfos) {
            EpsSpareBindingDetailEntity et = new EpsSpareBindingDetailEntity();
            et.setPrcEpsBodySparePartTrackId(data.getId());
            et.setPartVirtualVin(data.getVirtualVin());
            et.setSpareVin(orderInfo.getBarcode());
            et.setMaterialName(orderInfo.getCharacteristic4());
            et.setMaterialCode(orderInfo.getCharacteristic5());
            et.setAviQueueCode(sparePartVin.get(orderInfo.getSn()));
            details.add(et);
        }

        epsSpareBindingDetailService.insertBatch(details);

        UpdateWrapper<EpsBodySparePartTrackEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsBodySparePartTrackEntity::getIsBindingSparePart, true)
                .eq(EpsBodySparePartTrackEntity::getId, data.getId());
        update(upset);
    }

    /**
     * 判断是否锁线
     *
     * @param tpvin
     * @param workStationCode 备件VIN号-队列编码
     * @return true 锁线  false 放行
     */
    public Boolean lockLine(String tpvin, String workStationCode) {
        //判断改托盘是否要在该岗位配置了装备件
        EpsBodySparePartTrackEntity data = getAllDatas().stream().filter(c ->
                StringUtils.equals(c.getVirtualVin(), tpvin)
                        && StringUtils.equals(c.getBindingWorkstationCode(), workStationCode)).findFirst().orElse(null);
        if (data == null) {
            return false;
        }
        //判断是否已装过备件
        data = get(data.getId());
        if (data == null) {
            return false;
        }

        return data.getTrackStatus() == 2;
    }

    /**
     * 获取撬上面的备件VIN号集合
     *
     * @param virtualVin
     * @return
     */
    public List<String> getSpareParVins(String virtualVin) {
        return epsSpareBindingDetailService.getSpareParVins(virtualVin);
    }

    @Override
    public PageData<EpsBodySparePartTrackEntity> page(PageDataDto model) {
        IPage<EpsBodySparePartTrackEntity> page = this.getDataByPage(model);
        PageData<EpsBodySparePartTrackEntity> result = getPageData(page, this.currentModelClass());
        if (model.getPageIndex() != null) {
            result.setPageIndex(model.getPageIndex());
        }
        if (model.getPageSize() != null) {
            result.setPageSize(model.getPageSize());
        }
        //生产控制redis使用
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        for (EpsBodySparePartTrackEntity item : result.getDatas()) {
            if (item.getTrackStatus() < 3) {
                PmWorkStationEntity stationInfo = pmall.getStations().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), item.getBindingWorkstationCode())).findFirst().orElse(null);
                PmLineEntity lineInfo = pmall.getLines().stream().filter(c -> Objects.equals(c.getId(), stationInfo.getPrcPmLineId())).findFirst().orElse(null);
                PmWorkShopEntity shopInfo = pmall.getShops().stream().filter(c -> Objects.equals(c.getId(), stationInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
                String itemKey = "OT:" + shopInfo.getWorkshopCode() + ":" + lineInfo.getLineCode() + ":" + stationInfo.getWorkstationCode();
                Object itemVal = redisUtils.hGet(itemKey, "AutoProductCode");
                String productCode = itemVal == null ? "" : itemVal.toString();
                logger.info("redis-hash:" + itemKey + ":" + productCode);
                if (StringUtils.equals(productCode, item.getVirtualVin())) {
                    item.setIsEnter(true);
                }
            }
        }
        return result;
    }

    /**
     * 根据 虚拟VIN号 查询备件运输跟踪
     *
     * @param vehicleSn 虚拟VIN号
     * @return 返回 焊装车间备件运输跟踪
     */
    @Override
    public EpsBodySparePartTrackEntity getEntityByVirtualVin(String vehicleSn) {
        QueryWrapper<EpsBodySparePartTrackEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<EpsBodySparePartTrackEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(EpsBodySparePartTrackEntity::getVirtualVin, vehicleSn);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

}