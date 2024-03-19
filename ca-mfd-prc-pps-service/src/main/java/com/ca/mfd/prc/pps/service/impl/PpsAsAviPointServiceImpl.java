package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.pps.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.pps.dto.AsSendRptDTO;
import com.ca.mfd.prc.pps.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.extend.IPpsOrderExtendService;
import com.ca.mfd.prc.pps.extend.IPpsPlanPartsExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsAsAviPointMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.eps.entity.EpsBodySparePartTrackEntity;
import com.ca.mfd.prc.pps.remote.app.eps.provider.EpsBodySparePartTrackProvider;
import com.ca.mfd.prc.pps.remote.app.eps.provider.EpsSpareBindingDetailProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS车辆实际过点服务实现
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Service
public class PpsAsAviPointServiceImpl extends AbstractCrudServiceImpl<IPpsAsAviPointMapper, PpsAsAviPointEntity> implements IPpsAsAviPointService {

    @Autowired
    private IPpsAsAviPointMapper midAsAviPointMapper;

    @Autowired
    private IPpsOrderExtendService ppsOrderExtendService;

    @Autowired
    private IPpsPlanPartsExtendService ppsPlanPartsExtendService;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    private EpsBodySparePartTrackProvider epsBodySparePartTrackProvider;

    @Autowired
    private EpsSpareBindingDetailProvider epsSpareBindingDetailProvider;

    @Autowired
    private PmShcCalendarProvider pmShcCalendarProvider;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    /**
     * 获取未发送数据(车辆过点数据。整车)
     *
     * @return
     */
    @Override
    public List<PpsAsAviPointEntity> getNoSendList(Integer top) {
        QueryWrapper<PpsAsAviPointEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsAsAviPointEntity::getAsSendFlag, 0)
                .eq(PpsAsAviPointEntity::getOrderCategory, "1")
                .orderByAsc(PpsAsAviPointEntity::getCreationDate);
        return getTopDatas(top, qry);
        //   where a.AS_SEND_FLAG=0 and a.ORDER_CATEGORY='1'
        //   and a.IS_DELETE=0 and c.IS_DELETE=0 limit #{top}
        //return midAsAviPointMapper.getNoSendList(top);
    }

   /* *//**
     * 获取未发送数据(车辆过点数据。整车)
     *
     * @return
     *//*
    @Override
    public List<PpsAsAviPointEntity> getPlanNoSendList(Integer top) {
        QueryWrapper<PpsAsAviPointEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsAsAviPointEntity::getAsSendFlag, 0)
                .eq(PpsAsAviPointEntity::getOrderCategory, "1")
                .orderByAsc(PpsAsAviPointEntity::getCreationDate);
        return getTopDatas(top, qry);
        //   where a.AS_SEND_FLAG=0 and a.ORDER_CATEGORY='1'
        //   and a.IS_DELETE=0 and c.IS_DELETE=0 limit #{top}
        //return midAsAviPointMapper.getNoSendList(top);
    }*/

    @Override
    public List<MidLmsAviQueueDto> getNoLmsSendList(Integer top) {
        return midAsAviPointMapper.getLmsNoSendList(top);
    }

    @Override
    public List<AsSendRptDTO> getAsSendRpt(Map pms) {
        return midAsAviPointMapper.getAsSendRpt(pms);
    }

    /**
     * 获取未发送数据(批次反馈数据)
     *
     * @return
     */
    @Override
    public List<PpsAsAviPointEntity> getNoAsBatchPieces(Integer top) {
        QueryWrapper<PpsAsAviPointEntity> qry = new QueryWrapper<>();
        qry.lambda().ne(PpsAsAviPointEntity::getOrderCategory, "1")
                .eq(PpsAsAviPointEntity::getAsSendFlag, 0);
        List<PpsAsAviPointEntity> list = getTopDatas(top, qry);
        return list;
    }

    @Override
    public List<PpsAsAviPointEntity> getPartSendList() {
        String queueStr = sysConfigurationProvider.getConfiguration("LmsPartQueueSet", "LmsPartQueue");
        String[] queueArr = queueStr.split(",");
        if (queueArr.length == 0) {
            return Collections.emptyList();
        }
        List<String> sendQueues = Arrays.stream(queueArr).collect(Collectors.toList());
        QueryWrapper<PpsAsAviPointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsAsAviPointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsAsAviPointEntity::getLmsSendFlag, false);
        lambdaQueryWrapper.in(PpsAsAviPointEntity::getAviCode, sendQueues);
        lambdaQueryWrapper.ne(PpsAsAviPointEntity::getOrderCategory, 1);
        return selectList(queryWrapper);
    }


    /**
     * 更新发送状态
     *
     * @param ids 主键ids
     */
    @Override
    public void updatePartSendStatus(List<Long> ids) {
        UpdateWrapper<PpsAsAviPointEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsAsAviPointEntity> lambdaWrapper = wrapper.lambda();
        lambdaWrapper.set(PpsAsAviPointEntity::getLmsSendFlag, true);
        lambdaWrapper.in(PpsAsAviPointEntity::getId, ids);
        this.update(wrapper);
    }

    /**
     * 更新AS发送状态
     *
     * @param ids 主键ids
     */
    @Override
    public void updateAsSendStatus(List<Long> ids) {
        UpdateWrapper<PpsAsAviPointEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsAsAviPointEntity> lambdaWrapper = wrapper.lambda();
        lambdaWrapper.set(PpsAsAviPointEntity::getAsSendFlag, true);
        lambdaWrapper.in(PpsAsAviPointEntity::getId, ids);
        this.update(wrapper);
    }


    /**
     * 写入as中间表的过点，报工数据
     *
     * @param data
     */
    @Override
    public void insertAsAviPoint(InsertAsAviPointInfo data) {
        if (StringUtils.isBlank(data.getPlanNo()) && StringUtils.isBlank(data.getVin())) {
            throw new InkelinkException("请填充计划号或VIN属性");
        }

        if (StringUtils.isBlank(data.getLineCode()) && StringUtils.isBlank(data.getAviCode()) && !"S".equalsIgnoreCase(data.getScanType()) && !"X".equalsIgnoreCase(data.getScanType())) {
            throw new InkelinkException("请填充线体编码或站点编码");
        }
        if (StringUtils.isBlank(data.getPlanNo())) {
            PpsOrderEntity orderInfo = ppsOrderExtendService.getPpsOrderBySn(data.getVin());
            if (orderInfo == null) {
                throw new InkelinkException("无效的产品唯一码" + data.getVin());
            }
            data.setPlanNo(orderInfo.getPlanNo());
        }
        String orderCategory = StringUtils.EMPTY;

        if (!StringUtils.isBlank(data.getVin())) {
            //PpsOrderEntity orderInfo = ppsOrderExtendService.get(data.getVin());
            PpsOrderEntity orderInfo = ppsOrderExtendService.getPpsOrderBySn(data.getVin());
            orderCategory = orderInfo.getOrderCategory();
        } else {
            PpsPlanPartsEntity planPartsInfo = ppsPlanPartsExtendService.getFirstByPlanNo(data.getPlanNo());
            orderCategory = String.valueOf(planPartsInfo.getOrderCategory());
        }

        if (StringUtils.isBlank(data.getAviCode()) && StringUtils.isBlank(data.getLineCode()) && "S".equalsIgnoreCase(data.getScanType())) {
            PpsPlanPartsEntity planPartsInfo = ppsPlanPartsExtendService.getFirstByPlanNo(data.getPlanNo());
            data.setAviCode(planPartsInfo.getStartAvi());
        }

        if (StringUtils.isBlank(data.getAviCode()) && StringUtils.isBlank(data.getLineCode()) && "X".equalsIgnoreCase(data.getScanType())) {
            PpsPlanPartsEntity planPartsInfo = ppsPlanPartsExtendService.getFirstByPlanNo(data.getPlanNo());
            data.setAviCode(planPartsInfo.getEndAvi());
        }

        if (StringUtils.isBlank(data.getAviCode())) {
            PmLineEntity avilineInfo = pmVersionProvider.getObjectedPm().getLines().stream().filter(c -> StringUtils.equals(c.getLineCode(), data.getLineCode()))
                    .findFirst().orElse(null);
            if (avilineInfo == null) {
                throw new InkelinkException("无效线体编码" + data.getLineCode());
            }
            if ("S".equalsIgnoreCase(data.getScanType())) {
                PmAviEntity aviInfos = pmVersionProvider.getObjectedPm().getAvis().stream()
                        .filter(c -> Objects.equals(c.getPrcPmLineId(), avilineInfo.getId()) && c.getAviType().contains("2"))
                        .findFirst().orElse(null);
                if (aviInfos == null) {
                    throw new InkelinkException("未找到线体" + avilineInfo.getLineCode() + "的上线点");
                }
                data.setAviCode(aviInfos.getAviCode());
            }
            if ("X".equalsIgnoreCase(data.getScanType())) {
                PmAviEntity aviInfox = pmVersionProvider.getObjectedPm().getAvis().stream()
                        .filter(c -> Objects.equals(c.getPrcPmLineId(), avilineInfo.getId()) && c.getAviType().contains("3"))
                        .findFirst().orElse(null);
                if (aviInfox == null) {
                    throw new InkelinkException("未找到" + avilineInfo.getLineCode() + "线体的下线点");
                }
                data.setAviCode(aviInfox.getAviCode());
            }
        }

        if (StringUtils.isBlank(data.getLineCode())) {
            PmAviEntity aviInfol = pmVersionProvider.getObjectedPm().getAvis().stream()
                    .filter(c -> StringUtils.equals(c.getAviCode(), data.getAviCode()))
                    .findFirst().orElse(null);
            if (aviInfol == null) {
                throw new InkelinkException("无效站点编码" + data.getAviCode());
            }
            PmLineEntity linex = pmVersionProvider.getObjectedPm().getLines().stream()
                    .filter(c -> Objects.equals(c.getId(), aviInfol.getPrcPmLineId()))
                    .findFirst().orElse(null);
            data.setLineCode(linex.getLineCode());
        }

        PpsAsAviPointEntity model = new PpsAsAviPointEntity();
        PmLineEntity lineInfo = pmVersionProvider.getObjectedPm().getLines().stream()
                .filter(c -> StringUtils.equals(c.getLineCode(), data.getLineCode()))
                .findFirst().orElse(null);

        PmWorkShopEntity shopInfo = pmVersionProvider.getObjectedPm().getShops().stream()
                .filter(c -> Objects.equals(c.getId(), lineInfo.getPrcPmWorkshopId()))
                .findFirst().orElse(null);

        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis().stream()
                .filter(c -> StringUtils.equals(c.getAviCode(), data.getAviCode()))
                .findFirst().orElse(null);

        model.setOrgCode(pmVersionProvider.getObjectedPm().getOrganization().getOrganizationCode());
        model.setPlanNo(data.getPlanNo());
        model.setOrderCategory(orderCategory);
        model.setVin(data.getVin());
        model.setWorkshopCode(shopInfo.getWorkshopCode());
        model.setLineCode(lineInfo.getLineCode());
        model.setAviName(aviInfo.getAviName());
        model.setAviCode(aviInfo.getAviCode());
        model.setScanType(data.getScanType());

        ShiftDTO shift = pmShcCalendarProvider.getCurrentShiftInfo(model.getLineCode());
        //TODO getAsShiftCode
        model.setActualShift(shift == null ? "" : shift.getShiftCode());
        model.setQualifiedCount(data.getQualifiedCount());
        model.setBadCount(data.getBadCount());

        this.insert(model);
    }

    /**
     * 写入as中间表的过点 AVI过点补录
     *
     * @param vehicleSn
     * @param aviCode
     * @param AviType
     */
    public void insertDataAsAviPoint(String vehicleSn, String aviCode, int AviType) {
        List<String> sns = new ArrayList<>();
        if (vehicleSn.startsWith("TP")) {
            EpsBodySparePartTrackEntity sparePartTrackInfo = epsBodySparePartTrackProvider.getEntityByVirtualVin(vehicleSn);
            if (sparePartTrackInfo != null) {
                //备件过点
                sns = epsSpareBindingDetailProvider.getPartVirtualVinByPartTrackId(sparePartTrackInfo.getId().toString());
            }
        } else {
            sns.add(vehicleSn);
        }
        for (String sn : sns) {
            PmAllDTO pm = pmVersionProvider.getObjectedPm();
            PmAviEntity pmAviInfo = pm.getAvis().stream().filter(s -> s.getAviCode().equals(aviCode)).findFirst().orElse(null);
            PmLineEntity line = pm.getLines().stream().filter(s -> s.getId().equals(pmAviInfo.getPrcPmLineId())).findFirst().orElse(null);
            PmWorkShopEntity workShop = pm.getShops().stream().filter(s -> s.getId().equals(pmAviInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
            if (StringUtils.isBlank(sn)) {
                throw new InkelinkException("产品编码是空");
            }
            PpsOrderEntity entry = ppsOrderExtendService.getPpsOrderBySnOrBarcode(sn);
            if (entry == null) {
                throw new InkelinkException("产品编码是空");
            }
            InsertAsAviPointInfo asAviPointInfo = new InsertAsAviPointInfo();
            asAviPointInfo.setLineCode(line.getLineCode());
            asAviPointInfo.setPlanNo(entry.getPlanNo());
            if (pmAviInfo.getAviType().contains("2")) {
                asAviPointInfo.setScanType("S");
            } else if (pmAviInfo.getAviType().contains("3")) {
                asAviPointInfo.setScanType("X");
            } else if (AviType == 0) {
                asAviPointInfo.setScanType("1");
            } else if (AviType == 1) {
                asAviPointInfo.setScanType("3");
            } else if (AviType == 2) {
                asAviPointInfo.setScanType("2");
            }
            asAviPointInfo.setAviCode(pmAviInfo.getAviCode());
            asAviPointInfo.setVin(sn);
            insertAsAviPoint(asAviPointInfo);
        }
    }
}