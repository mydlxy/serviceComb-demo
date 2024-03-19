package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.avi.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.avi.dto.OrderSequenceDTO;
import com.ca.mfd.prc.avi.dto.ResetQueueParaDTO;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.mapper.IAviQueueReleaseMapper;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 队列发布数据表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviQueueReleaseServiceImpl extends AbstractCrudServiceImpl<IAviQueueReleaseMapper, AviQueueReleaseEntity> implements IAviQueueReleaseService {

    @Autowired
    PpsOrderProvider ppsOrderProvider;

    @Autowired
    PpsEntryProvider ppsEntryProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    PmVersionProvider pmVersionProvider;

    /**
     * 查找指定队列的未处理数据
     *
     * @param queueName
     * @return
     */
    @Override
    public List<AviQueueReleaseEntity> getNoSendByQuee(String queueName) {
        QueryWrapper<AviQueueReleaseEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(AviQueueReleaseEntity::getQueueCode, queueName)
                .eq(AviQueueReleaseEntity::getIsSend, false)
                .orderByAsc(AviQueueReleaseEntity::getInsertDt);
        return selectList(qry);
    }

    /**
     * 查找指定队列的未处理数据
     *
     * @param queueName
     * @param top
     * @return
     */
    @Override
    public List<AviQueueReleaseEntity> getNoSendByQuee(String queueName,Integer top) {
        QueryWrapper<AviQueueReleaseEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(AviQueueReleaseEntity::getQueueCode, queueName)
                .eq(AviQueueReleaseEntity::getIsSend, false)
                .orderByAsc(AviQueueReleaseEntity::getDisplayNo);
        return getTopDatas(top,qry);
    }

    /**
     * 整车数据(原代码未实现)
     */
    @Override
    public PageData<OrderSequenceDTO> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize) {
        PageData<OrderSequenceDTO> page = new PageData<OrderSequenceDTO>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        // TODO 原代码未实现
        throw new InkelinkException("原代码未实现");
    }

    /**
     * 重置队列
     */
    public void resetQueueBak(ResetQueueParaDTO para) {
        //PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderBySnOrBarcode(para.getSn());
        PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(para.getSn());
        if (order == null) {
            throw new InkelinkException("VIN码不存在");
        } else {
            para.setSn(order.getSn());
        }
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("QUEUE_CODE", para.getQueueCode(), ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("SN", para.getSn(), ConditionOper.Equal));
        AviQueueReleaseEntity queueInfo = getData(conditionDtos).stream().findFirst().orElse(null);
        if (queueInfo != null) {
            List<ConditionDto> conditionQueFalse = new ArrayList<>();
            conditionQueFalse.add(new ConditionDto("QUEUE_CODE", para.getQueueCode(), ConditionOper.Equal));
            conditionQueFalse.add(new ConditionDto("IS_SEND", "0", ConditionOper.Equal));
            conditionQueFalse.add(new ConditionDto("InsertDt", DateUtils.format(queueInfo.getInsertDt(), DateUtils.DATE_TIME_PATTERN), ConditionOper.LessThan));
            List<AviQueueReleaseEntity> lessThan = getData(conditionQueFalse);
            for (AviQueueReleaseEntity item : lessThan) {
                LambdaUpdateWrapper<AviQueueReleaseEntity> upset = new LambdaUpdateWrapper<>();
                upset.set(AviQueueReleaseEntity::getIsSend, true);
                upset.eq(AviQueueReleaseEntity::getId, item.getId());
                update(upset);
            }
            List<ConditionDto> conditionQueTrue = new ArrayList<>();
            conditionQueTrue.add(new ConditionDto("Code", para.getQueueCode(), ConditionOper.Equal));
            conditionQueTrue.add(new ConditionDto("IsSend", "1", ConditionOper.Equal));
            conditionQueTrue.add(new ConditionDto("InsertDt", DateUtils.format(queueInfo.getInsertDt(), DateUtils.DATE_TIME_PATTERN), ConditionOper.LessThanEqual));
            List<AviQueueReleaseEntity> greaterThan = getData(conditionQueTrue);
            for (AviQueueReleaseEntity item : greaterThan) {
                LambdaUpdateWrapper<AviQueueReleaseEntity> upset = new LambdaUpdateWrapper<>();
                upset.set(AviQueueReleaseEntity::getIsSend, false);
                upset.eq(AviQueueReleaseEntity::getId, item.getId());
                update(upset);
            }
        }
    }

    /**
     * 重置队列
     */
    @Override
    public void resetQueue(ResetQueueParaDTO para) {
        PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(para.getSn());
        if (order != null) {
            para.setSn(order.getSn());
        }
        AviQueueReleaseEntity queueInfo = getEntityByCodeAndSn(para.getQueueCode(), para.getSn());
        if (queueInfo != null) {
            List<AviQueueReleaseEntity> lessThan = getLessThanByCode(para.getQueueCode(), queueInfo.getDisplayNo());
            for (AviQueueReleaseEntity item : lessThan) {
                updateIsSendStatus(Boolean.TRUE, item.getId());
            }
            List<AviQueueReleaseEntity> greaterThan = getGreaterThanListByCode(para.getQueueCode(), queueInfo.getDisplayNo());
            for (AviQueueReleaseEntity item : greaterThan) {
                updateIsSendStatus(Boolean.FALSE, item.getId());
            }
        }
    }

    private AviQueueReleaseEntity getEntityByCodeAndSn(String code, String sn) {
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, code);
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getSn, sn);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    private List<AviQueueReleaseEntity> getLessThanByCode(String code, Integer displayNo) {
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, code);
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
        lambdaQueryWrapper.lt(AviQueueReleaseEntity::getDisplayNo, displayNo);
        return selectList(queryWrapper);
    }

    private List<AviQueueReleaseEntity> getGreaterThanListByCode(String code, Integer displayNo) {
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, code);
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, true);
        lambdaQueryWrapper.ge(AviQueueReleaseEntity::getDisplayNo, displayNo);
        return selectList(queryWrapper);
    }

    public void updateIsSendStatus(Boolean isSend, Long id) {
        UpdateWrapper<AviQueueReleaseEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<AviQueueReleaseEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(AviQueueReleaseEntity::getIsSend, isSend);
        lambdaUpdateWrapper.eq(AviQueueReleaseEntity::getId, id);
        this.update(updateWrapper);
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("insertDt") && data.getOrDefault("insertDt", null) != null) {
                data.put("insertDt", DateUtils.format((Date) data.get("insertDt"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("isSend") && data.getOrDefault("isSend", null) != null) {
                if ("true".equals(data.get("isSend"))) {
                    data.put("isSend", "是");
                } else {
                    data.put("isSend", "否");
                }
            }
        }
    }

    /**
     * 下发lms车辆队列
     *
     * @return 车辆过点信息
     */
    @Override
    public List<MidLmsAviQueueDto> getLmsAviQueueList() {
        String queueStr = sysConfigurationProvider.getConfiguration("LmsQueueReleaseSet", "LmsAviQueue");
        //        List<SysConfigurationEntity> queues = sysConfigurationProvider.getSysConfigurations("LmsAviQueue");
        //        if (queues.isEmpty()) {
        //            return Collections.emptyList();
        //        }
        //        SysConfigurationEntity sysConfigurationInfo = queues.stream().findFirst().orElse(null);
        //        String queueStr = sysConfigurationInfo.getValue();
        String[] queueArr = queueStr.split(",");
        if (queueArr.length == 0) {
            return Collections.emptyList();
        }
        List<String> sendQueues = Arrays.stream(queueArr).collect(Collectors.toList());
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();
        String orgCode = pmAllDTO.getOrganization().getOrganizationCode();
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
        lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, sendQueues);
        List<AviQueueReleaseEntity> resList = selectList(queryWrapper);
        List<MidLmsAviQueueDto> lmsAviQueueList = new ArrayList<>();
        for (AviQueueReleaseEntity item : resList) {
            PpsOrderEntity ppsOrderInfo = ppsOrderProvider.getPpsOrderInfo(item.getSn());
            MidLmsAviQueueDto info = new MidLmsAviQueueDto();
            info.setOrgCode(orgCode);
            info.setWorkshopCode(item.getWorkshopCode());
            info.setLineCode(item.getLineCode());
            info.setVid("");
            info.setVin(item.getSn());
            String orderCategory = item.getOrderCategory() == "1" ? "1" : "2";
            info.setProductType(orderCategory);
            //item.getOrderCategory()
            info.setProductCode(ppsOrderInfo.getProductCode());
            info.setOneId("");
            info.setManager("");
            info.setPassTime(item.getInsertDt());
            info.setAviCode(item.getAviCode());
            info.setOrderSign(ppsOrderInfo.getOrderSign());
            info.setUniqueCode(item.getId());
            lmsAviQueueList.add(info);
        }
        return lmsAviQueueList;
    }

    /**
     * 更新队列发送标识
     * @param ids 队列 ids
     */
    @Override
    public void updateLmsAviQueueStatus(List<Long> ids) {
        UpdateWrapper<AviQueueReleaseEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<AviQueueReleaseEntity> lambdaWrapper = wrapper.lambda();
        lambdaWrapper.set(AviQueueReleaseEntity::getIsSend, true);
        lambdaWrapper.in(AviQueueReleaseEntity::getId, ids);
        this.update(wrapper);
    }

    @Override
    public List<MidLmsAviQueueDto> getLmsAviQueueListBak() {
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();
        String orgCode = pmAllDTO.getOrganization().getOrganizationCode();
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
        List<AviQueueReleaseEntity> resList = selectList(queryWrapper);
        List<MidLmsAviQueueDto> lmsAviQueueList = new ArrayList<>();
        for (AviQueueReleaseEntity item : resList) {
            PpsOrderEntity ppsOrderInfo = ppsOrderProvider.getPpsOrderInfo(item.getSn());
            MidLmsAviQueueDto info = new MidLmsAviQueueDto();
            info.setOrgCode(orgCode);
            info.setWorkshopCode(item.getWorkshopCode());
            info.setLineCode(item.getLineCode());
            info.setVid("");
            info.setVin(item.getSn());
            String orderCategory = item.getOrderCategory() == "1" ? "1" : "2";
            info.setProductType(orderCategory);
            //item.getOrderCategory()
            info.setProductCode(ppsOrderInfo.getProductCode());
            info.setOneId("");
            info.setManager("");
            info.setPassTime(item.getInsertDt());
            info.setAviCode(item.getAviCode());
            info.setOrderSign(ppsOrderInfo.getOrderSign());
            info.setUniqueCode(item.getId());
            lmsAviQueueList.add(info);
        }
        return lmsAviQueueList;
    }

}