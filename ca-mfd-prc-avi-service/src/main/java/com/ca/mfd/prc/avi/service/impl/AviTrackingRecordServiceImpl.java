package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.dto.SnAviTrackingRecordVO;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.enums.AviTrackingEnum;
import com.ca.mfd.prc.avi.mapper.IAviTrackingRecordMapper;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.eps.entity.EpsBodySparePartTrackEntity;
import com.ca.mfd.prc.avi.remote.app.eps.provider.EpsBodySparePartTrackProvider;
import com.ca.mfd.prc.avi.remote.app.eps.provider.EpsSpareBindingDetailProvider;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsAsAviPointServiceProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviOperationLogService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 产品过点信息
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviTrackingRecordServiceImpl extends AbstractCrudServiceImpl<IAviTrackingRecordMapper, AviTrackingRecordEntity> implements IAviTrackingRecordService {

    @Autowired
    //private IPpsOrderService ppsOrderService;
    PpsOrderProvider ppsOrderProvider;

    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    PpsAsAviPointServiceProvider ppsAsAviPointServiceProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    EpsBodySparePartTrackProvider epsBodySparePartTrackProvider;
    @Autowired
    EpsSpareBindingDetailProvider epsSpareBindingDetailProvider;
    @Autowired
    //private IPmShcCalendarService pmShcCalendarService;
    private PmShcCalendarProvider pmShcCalendarProvider;
    @Autowired
    private IAviOperationLogService aviOperationLogService;
    @Resource
    private IAviTrackingRecordMapper aviTrackingRecordDao;

    private PmAllDTO getObjectedPm() {
        return pmVersionProvider.getObjectedPm();
    }

    /**
     * 报错AS过点记录
     *
     * @param vehicleSn
     * @param aviCode
     * @param AviType
     */
    public void saveAsPointData(String vehicleSn, String aviCode, int AviType) {
        ppsAsAviPointServiceProvider.insertDataAsAviPoint(vehicleSn, aviCode, AviType);
    }

    /**
     * 第三方过点数据
     *
     * @param sn              车辆识别码
     * @param aviCode
     * @param aviType
     * @param avitrackingenum
     * @param isProcess
     * @param passTime
     */
    @Override
    public void saveThirdPointData(String sn, String aviCode, Integer aviType, AviTrackingEnum avitrackingenum, Boolean isProcess, Date passTime) {
        if (aviType == null) {
            aviType = 0;
        }
        if (isProcess == null) {
            isProcess = false;
        }
        if (avitrackingenum == null) {
            avitrackingenum = AviTrackingEnum.自动过点;
        }
        // if (!ValdExistsTracking(sn, aviId))
        //  {
        insertData(sn, aviCode, aviType, avitrackingenum, isProcess, passTime);
        //  }
    }


    /**
     * 手动过点数据（web）
     *
     * @param tpsCode 车辆识别码
     * @param aviId
     * @return Result<PpsOrderEntity>
     */
    @Override
    public ResultVO<PpsOrderEntity> saveManualPointWeb(String tpsCode, String aviId) {
        ResultVO<PpsOrderEntity> result = new ResultVO<PpsOrderEntity>();
        result.setMessage("过点保存成功");
        PpsOrderEntity ppsorder = null;

        PmAllDTO pm = getObjectedPm();
        PmAviEntity pmAviInfo = pm.getAvis().stream().filter(w -> Objects.equals(w.getId(), ConvertUtils.stringToLong(aviId))).findFirst().orElse(null);
        if (pmAviInfo != null) {
            ppsorder = ppsOrderProvider.getPpsOrderInfo(tpsCode);
            String sn = ppsorder == null ? "" : ppsorder.getSn();
            if (StringUtils.isNotBlank(sn)) {
                saveManualPoint(sn, pmAviInfo.getAviCode());
                aviOperationLogService.insertAviLog(pmAviInfo.getAviCode(), "产品手动过点", "产品信息" + sn + "过点成功");
                saveChange();
            } else {
                throw new InkelinkException("未找到【" + sn + "】对应的产品信息!");
            }
            ppsorder = ppsOrderProvider.getPpsOrderInfo(sn);
        } else {
            result.setCode(-1);
            result.setMessage("过点保存失败，AVI站点不存在");
            return result;
        }
        return result.ok(ppsorder);
    }

    /**
     * 保存手动过点数据
     *
     * @param tpsCode 车辆识别码
     * @param aviCode aviCode
     */
    @Override
    public void saveManualPoint(String tpsCode, String aviCode) {
        saveManualPoint(tpsCode, aviCode, 0, AviTrackingEnum.手动过点);
    }

    /**
     * 保存手动过点数据
     *
     * @param tpsCode         车辆识别码
     * @param aviCode         aviCode
     * @param aviType         aviType
     * @param avitrackingenum avitrackingenum
     */
    @Override
    public void saveManualPoint(String tpsCode, String aviCode, int aviType, AviTrackingEnum avitrackingenum) {
        // if (!ValdExistsTracking(tpsCode, aviId))
        // {
        insertData(tpsCode, aviCode, aviType, avitrackingenum, false, null);
        // }
    }

    /**
     * 保存Pack过点记录数据
     *
     * @param barCode 车辆识别码
     * @param aviCode
     */
    @Override
    public void savePackPoint(String barCode, String aviCode) {
        savePackPoint(barCode, aviCode, 0, AviTrackingEnum.手动过点);
    }

    /**
     * 保存Pack过点记录数据
     *
     * @param barCode         车辆识别码
     * @param aviCode
     * @param aviType         4 Pack过点
     * @param avitrackingenum
     */
    @Override
    public void savePackPoint(String barCode, String aviCode, int aviType, AviTrackingEnum avitrackingenum) {
        // if (!ValdExistsTracking(tpsCode, aviId))
        // {
        insertData(barCode, aviCode, aviType, avitrackingenum, false, null);
        // }
    }

    /**
     * 保存过点数据
     *
     * @param tpsCode 车辆识别码
     * @param aviCode
     */
    @Override
    public void savePointData(String tpsCode, String aviCode) {
        savePointData(tpsCode, aviCode, 0, AviTrackingEnum.自动过点, false);
    }

    /**
     * 保存过点数据
     *
     * @param tpsCode         车辆识别码
     * @param aviCode
     * @param aviType         4 过点
     * @param avitrackingenum
     * @param isProcess
     */
    @Override
    public void savePointData(String tpsCode, String aviCode, int aviType, AviTrackingEnum avitrackingenum, boolean isProcess) {
        // if (!ValdExistsTracking(tpsCode, aviId))
        // {
        insertData(tpsCode, aviCode, aviType, avitrackingenum, isProcess, null);
        // }
    }

    /**
     * 生成过点记录
     *
     * @param vehicleSn       车辆识别码
     * @param aviCode
     * @param aviType
     * @param avitrackingenum
     * @param pressTime
     */
    void insertData(String vehicleSn, String aviCode, int aviType, AviTrackingEnum avitrackingenum, boolean isProcess, Date pressTime) {
        List<String> sns = new ArrayList<>();
        // 托盘过点
        if (vehicleSn.startsWith("TP")) {
            EpsBodySparePartTrackEntity sparePartTrackInfo = epsBodySparePartTrackProvider.getEntityByVirtualVin(vehicleSn);
            if (sparePartTrackInfo != null) {
                sns = epsSpareBindingDetailProvider.getPartVirtualVinByPartTrackId(sparePartTrackInfo.getId().toString());
            }
        } else {
            sns.add(vehicleSn);
        }

        PmAllDTO pm = getObjectedPm();
        PmAviEntity pmAviInfo = pm.getAvis().stream().filter(w -> StringUtils.equals(w.getAviCode(), aviCode)).findFirst().orElse(null);
        PmLineEntity pmArea = pm.getLines().stream().filter(w -> Objects.equals(w.getId(), pmAviInfo.getPrcPmLineId())).findFirst().orElse(null);
        PmWorkShopEntity pmShopInfo = pm.getShops().stream().filter(w -> Objects.equals(w.getId(), pmArea.getPrcPmWorkshopId())).findFirst().orElse(null);
        for (String sn : sns) {
            if (StringUtils.isBlank(sn)) {
                throw new InkelinkException("产品编码是空");
            }
            PpsOrderEntity entry = ppsOrderProvider.getPpsOrderBySnOrBarcode(sn);
            if (entry == null) {
                throw new InkelinkException("订单未找到");
            }
            String shcCalendarId = StringUtils.EMPTY;
            ShiftDTO shiftInfo = pmShcCalendarProvider.getCurrentShiftInfo(String.valueOf(pmArea.getLineCode()));
            if (shiftInfo != null) {
                shcCalendarId = shiftInfo.getShcCalenderId();
            }
            // boolean isProcess = false;
            AviTrackingRecordEntity aviTrackingRecordInfo = new AviTrackingRecordEntity();
            aviTrackingRecordInfo.setInsertDt(pressTime == null ? new Date() : pressTime);
            aviTrackingRecordInfo.setMode(avitrackingenum.value());
            aviTrackingRecordInfo.setIsProcess(isProcess);
            aviTrackingRecordInfo.setSn(sn);
            /* 通过BarCode 查询到工单，通过工单得到对象类型 */
            aviTrackingRecordInfo.setOrderCategory(entry.getOrderCategory());
            aviTrackingRecordInfo.setLineCode(pmArea.getLineCode());
            aviTrackingRecordInfo.setAviCode(pmAviInfo.getAviCode());
            aviTrackingRecordInfo.setAviName(pmAviInfo.getAviName());
            aviTrackingRecordInfo.setWorkshopCode(pmShopInfo.getWorkshopCode());
            aviTrackingRecordInfo.setPrcPpsShcShiftId(ConvertUtils.stringToLong(shcCalendarId));
            aviTrackingRecordInfo.setAviTrackIngRecordType(aviType);
            insert(aviTrackingRecordInfo);
        }
    }

    @Override
    public void getAviTrackingRecordInfo(PageData<AviTrackingRecordEntity> pageInfo, List<ConditionDto> cons, List<SortDto> sorts) {
        if (cons == null) {
            cons = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("a.LAST_UPDATE_DATE");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        List<ConditionDto> conditions = MpSqlUtils.filtrationCondition(currentModelClass(), cons, "a");
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(currentModelClass(), sorts, "a");
        map.put("wheresa", conditions);
        map.put("order", sortsNew);

        //微服务查询
        PageDataDto pageAvi = new PageDataDto();
        pageAvi.setPageIndex(pageInfo.getPageIndex());
        pageAvi.setPageSize(pageInfo.getPageSize());
        pageAvi.setConditions(conditions);
        pageAvi.setSorts(sorts);
        IPage<AviTrackingRecordEntity> pdata = getDataByPage(pageAvi);
        //查询关联ppsOrder
        if (pdata.getRecords() != null && pdata.getRecords().size() > 0) {
            List<ConditionDto> conditionppsOrde = new ArrayList<>();
            List<String> sns = pdata.getRecords().stream().filter(c -> StringUtils.isNotBlank(c.getSn())).distinct().map(AviTrackingRecordEntity::getSn).collect(Collectors.toList());
            conditionppsOrde.add(new ConditionDto("sn", String.join("|", sns), ConditionOper.In));
            List<PpsOrderEntity> ppsOrders = ppsOrderProvider.getData(conditionppsOrde);
            if (ppsOrders != null && ppsOrders.size() > 0) {
                for (AviTrackingRecordEntity et : pdata.getRecords()) {
                    PpsOrderEntity ppsOrderEt = ppsOrders.stream().filter(c -> StringUtils.equals(c.getSn(), et.getSn())).findFirst().orElse(null);
                    if (ppsOrderEt != null) {
                        // b.BARCODE,b.SEQUENCE_NO as SeqNo,
                        // b.CHARACTERISTIC1,b.CHARACTERISTIC2,b.CHARACTERISTIC3,b.CHARACTERISTIC4,
                        // b.PLAN_NO,b.ORDER_NO
                        et.setCharacteristic1(ppsOrderEt.getCharacteristic1());
                        et.setCharacteristic2(ppsOrderEt.getCharacteristic2());
                        et.setCharacteristic3(ppsOrderEt.getCharacteristic3());
                        et.setCharacteristic4(ppsOrderEt.getCharacteristic4());
                        et.setBarcode(ppsOrderEt.getBarcode());
                        et.setSeqNo(ppsOrderEt.getDisplayNo());
                        et.setPlanNo(ppsOrderEt.getPlanNo());
                        et.setOrderNo(ppsOrderEt.getOrderNo());
                        et.setProductionNo(ppsOrderEt.getProductionNo());
                    }
                }
            }
        }
        pageInfo.setDatas(pdata.getRecords());
        pageInfo.setTotal((int) pdata.getTotal());
    }

    /**
     * 整车订单
     */
    @Override
    public void getVehiceOrderPageDatas(List<ConditionDto> conditions, List<SortDto> sorts, PageData<AviTrackingRecordEntity> page) {
        //, boolean isDelete  参数未用
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("a.LAST_UPDATE_DATE");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(currentModelClass(), sorts, "a");
        //avi
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(4);
        List<ConditionDto> conditionAviTrackingRecord = conditions.stream().filter(o -> StringUtils.isBlank(o.getAlias()) || "evw".equalsIgnoreCase(o.getAlias())).collect(Collectors.toList());
        conditionAviTrackingRecord = MpSqlUtils.filtrationCondition(currentModelClass(), conditionAviTrackingRecord, "");
        map.put("where_a", conditionAviTrackingRecord);

        //pps
        List<ConditionDto> conditionppsOrde = conditions.stream().filter(o -> o.getAlias() != null && "pe".equalsIgnoreCase(o.getAlias())).collect(Collectors.toList());
        conditionppsOrde = MpSqlUtils.filtrationCondition(PpsOrderEntity.class, conditionppsOrde, "");
        map.put("where_b", conditionppsOrde);
        map.put("order", sortsNew);

        //微服务查询
        PageDataDto pageAvi = new PageDataDto();
        pageAvi.setPageIndex(page.getPageIndex());
        pageAvi.setPageSize(page.getPageSize());
        pageAvi.setConditions(conditionAviTrackingRecord);
        pageAvi.setSorts(sorts);
        IPage<AviTrackingRecordEntity> pdata = getDataByPage(pageAvi);
        //查询关联ppsOrder
        if (pdata.getRecords() != null && pdata.getRecords().size() > 0) {
            if (conditionppsOrde == null) {
                conditionppsOrde = new ArrayList<>();
            }
            List<String> sns = pdata.getRecords().stream().filter(c -> StringUtils.isNotBlank(c.getSn())).distinct().map(AviTrackingRecordEntity::getSn).collect(Collectors.toList());
            conditionppsOrde.add(new ConditionDto("sn", String.join("|", sns), ConditionOper.In));
            List<PpsOrderEntity> ppsOrders = ppsOrderProvider.getData(conditionppsOrde);
            if (ppsOrders != null && ppsOrders.size() > 0) {
                for (AviTrackingRecordEntity et : pdata.getRecords()) {
                    PpsOrderEntity ppsOrderEt = ppsOrders.stream().filter(c -> StringUtils.equals(c.getSn(), et.getSn())).findFirst().orElse(null);
                    if (ppsOrderEt != null) {
                        et.setCharacteristic1(ppsOrderEt.getCharacteristic1());
                        et.setCharacteristic2(ppsOrderEt.getCharacteristic2());
                        et.setCharacteristic3(ppsOrderEt.getCharacteristic3());
                        et.setCharacteristic4(ppsOrderEt.getCharacteristic4());
                        et.setBarcode(ppsOrderEt.getBarcode());
                        et.setSeqNo(ppsOrderEt.getDisplayNo());
                        et.setPlanNo(ppsOrderEt.getPlanNo());
                        et.setOrderNo(ppsOrderEt.getOrderNo());
                    }
                }
            }
        }
        page.setDatas(pdata.getRecords());
        page.setTotal((int) pdata.getTotal());
    }

    /**
     * 获取自定车辆过点记录
     */
    @Override
    public List<AviPassedRecordDTO> getAviPassedRecord(String aviCode) {
        return aviTrackingRecordDao.getAviPassedRecord(aviCode);
    }

    @Override
    public AviTrackingRecordEntity getTopAviPassedRecord(String sn, String avicode) {
        QueryWrapper<AviTrackingRecordEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(AviTrackingRecordEntity::getSn, sn)
                .eq(AviTrackingRecordEntity::getAviCode, avicode)
                .orderByDesc(AviTrackingRecordEntity::getInsertDt);

        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public List<AviTrackingRecordEntity> getTopAviPassedRecord(Boolean isProcess, Integer top) {
        QueryWrapper<AviTrackingRecordEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(AviTrackingRecordEntity::getIsProcess, isProcess)
                .orderByAsc(AviTrackingRecordEntity::getInsertDt);
        return getTopDatas(top, qry);
    }


    @Override
    public PageData<AviTrackingRecordEntity> getVehiceOrderPageDatas(List<ConditionDto> conditions, List<SortDto> sorts,
                                                                     int pageIndex, int pageSize) {
        PageDataDto model = new PageDataDto();
        model.setPageIndex(pageIndex);
        model.setPageSize(pageSize);
        model.setConditions(conditions);
        model.setSorts(sorts);
        return super.page(model);
    }

    /**
     * 根据sn / aviCode 查询列表
     *
     * @param sn      产品唯一标识
     * @param aviCode avicode
     * @return 返回列表
     */
    @Override
    public List<SnAviTrackingRecordVO> getEntityBySnAndAviCode(List<String> sn, List<String> aviCode) {
        QueryWrapper<AviTrackingRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviTrackingRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(AviTrackingRecordEntity::getSn, sn);
        lambdaQueryWrapper.in(AviTrackingRecordEntity::getAviCode, aviCode);
        List<AviTrackingRecordEntity> list = selectList(queryWrapper);
        return list.stream().map(s -> {
            SnAviTrackingRecordVO items = new SnAviTrackingRecordVO();
            items.setPmAviCode(s.getAviCode());
            items.setSn(s.getSn());
            items.setInsertDt(s.getInsertDt());
            return items;
        }).collect(Collectors.toList());
    }


    @Override
    public void beforeInsert(AviTrackingRecordEntity model) {
        valid(model);
    }

    @Override

    public void beforeUpdate(AviTrackingRecordEntity model) {
        valid(model);
    }

    private void valid(AviTrackingRecordEntity model) {
        if (StringUtils.isBlank(model.getAviCode())) {
            throw new InkelinkException("请选择AVI站点");
        }
        if (model.getInsertDt() != null) {
            if (model.getInsertDt().after(new Date())) {
                throw new InkelinkException("过点时间不能大于当前时间");
            }
        }

        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();

        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo != null) {
            model.setAviName(aviInfo.getAviName());
        }

        PmLineEntity lineEntity;
        if (aviInfo != null) {
            lineEntity = pmAllDtoResultVo.getLines().stream().filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId()))
                    .findFirst().orElse(null);
            if (lineEntity != null) {
                model.setLineCode(lineEntity.getLineCode());
            }
        } else {
            lineEntity = null;
        }

        PmWorkShopEntity shopEntity = null;
        if (lineEntity != null) {
            shopEntity = pmAllDtoResultVo.getShops().stream().filter(s -> Objects.equals(s.getId(), lineEntity.getPrcPmWorkshopId()))
                    .findFirst().orElse(null);
            if (shopEntity != null) {
                model.setWorkshopCode(shopEntity.getWorkshopCode());
            }
        }

    }


    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("insertDt") && data.getOrDefault("insertDt", null) != null) {
                data.put("insertDt", DateUtils.format((Date) data.get("insertDt"), DateUtils.DATE_TIME_PATTERN));
            }

            if (data.containsKey("orderCategory") && data.getOrDefault("orderCategory", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("orderCategory").toString(), "EntrySource");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("orderCategory", keyValues);
                } else {
                    data.put("orderCategory", "");
                }
            }

            if (data.containsKey("aviTrackIngRecordType") && data.getOrDefault("aviTrackIngRecordType", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("aviTrackIngRecordType").toString(), "AviRecordType");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("aviTrackIngRecordType", keyValues);
                } else {
                    data.put("aviTrackIngRecordType", "");
                }
            }

            if (data.containsKey("mode") && data.getOrDefault("mode", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("mode").toString(), "AviRecordMode");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("mode", keyValues);
                } else {
                    data.put("mode", "");
                }
            }

        }
    }

}