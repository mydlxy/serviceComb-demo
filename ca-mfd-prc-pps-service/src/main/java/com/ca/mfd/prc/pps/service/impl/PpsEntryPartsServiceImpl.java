package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.pps.dto.OutsourceEntryAreaDTO;
import com.ca.mfd.prc.pps.dto.SplitEntryPara;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;
import com.ca.mfd.prc.pps.enums.PlanStatusEnum;
import com.ca.mfd.prc.pps.mapper.IPpsEntryPartsMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsEntryReportPartsService;
import com.ca.mfd.prc.pps.service.IPpsProcessRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 工单-零部件服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsEntryPartsServiceImpl extends AbstractCrudServiceImpl<IPpsEntryPartsMapper, PpsEntryPartsEntity> implements IPpsEntryPartsService {

    @Autowired
    private IPpsProcessRelationService ppsProcessRelationService;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IPpsAsAviPointService ppsAsAviPointService;
    @Autowired
    private IPpsEntryReportPartsService ppsEntryReportPartsService;


    @Override
    public PpsEntryPartsEntity getFirstByEntryNo(String entryNo) {
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryPartsEntity::getEntryNo, entryNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 根据计划单号或工单号查询
     *
     * @param planNoOrEntryNo
     * @return
     */
    @Override
    public PpsEntryPartsEntity getFirstByPlanNoOrEntryNo(String planNoOrEntryNo) {
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryPartsEntity::getPlanNo, planNoOrEntryNo)
                .or().eq(PpsEntryPartsEntity::getEntryNo, planNoOrEntryNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryPartsEntity getFirstByEntryNoAndCategory(String entryNo, Integer orderCategory) {
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryPartsEntity::getOrderCategory, orderCategory)
                .eq(PpsEntryPartsEntity::getEntryNo, entryNo)
                .orderByDesc(PpsEntryPartsEntity::getCreationDate);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 计划冻结
     *
     * @param ppsPlanIds
     */
    @Override
    public void freeze(List<Long> ppsPlanIds) {
        for (Long planId : ppsPlanIds) {
            PpsEntryPartsEntity ppsPlanInfo = get(planId);
            if (ppsPlanInfo == null) {
                return;
            }
            if (ppsPlanInfo.getStatus() >= 2) {
                throw new InkelinkException(ppsPlanInfo.getPlanNo() + "该工单状态不正确，不能冻结！");
            }
            if (ppsPlanInfo.getIsFreeze()) {
                throw new InkelinkException(ppsPlanInfo.getPlanNo() + "该工单编号已冻结,不需冻结！");
            }
            UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsEntryPartsEntity::getIsFreeze, true)
                    .eq(PpsEntryPartsEntity::getId, planId);
            update(upset);
        }
    }

    /**
     * 取消工单冻结
     *
     * @param ppsPlanIds
     */
    @Override
    public void unFreeze(List<Long> ppsPlanIds) {
        for (Long planId : ppsPlanIds) {
            PpsEntryPartsEntity ppsPlanInfo = get(planId);
            if (ppsPlanInfo == null) {
                return;
            }
            if (!ppsPlanInfo.getIsFreeze()) {
                throw new InkelinkException("'" + ppsPlanInfo.getPlanNo() + "'该工单编号未冻结,不需解冻.");
            }
            UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsEntryPartsEntity::getIsFreeze, false)
                    .eq(PpsEntryPartsEntity::getId, planId);
            update(upset);
        }
    }


    /**
     * 统计合格数量
     *
     * @param planNo 计划编码
     * @return 统计合格数量
     */
    @Override
    public int getEntryPartNumByPlanNo(String planNo) {
        QueryWrapper<PpsEntryPartsEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryPartsEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryPartsEntity::getPlanNo, planNo);
        List<PpsEntryPartsEntity> list = selectList(queryWrapper);
        return list.stream().mapToInt(PpsEntryPartsEntity::getQualifiedQuantity).sum();
    }


    /**
     * ------------离散业务(start)-----------------
     */
    /**
     * 修改工单数量
     *
     * @param entryNo
     * @param count
     * @return
     */
    @Override
    public void changeWorkOrderCount(String entryNo, Integer count) {
        PpsEntryPartsEntity entry = getFirstByEntryNo(entryNo);
        if (entry == null) {
            throw new InkelinkException("工单不存在");
        }
        if (entry.getStatus() > 4) {
            throw new InkelinkException("工单已经关闭，无法修改数量");
        }
        if (count <= 0) {
            throw new InkelinkException("无效的生产数量");
        }
        //if (entry.PlanCount > count)
        //{
        //    throw new InkelinkException("修改数量必须大于当前工单数量，如果需要减少数量请关闭后重新创建工单");
        //}

        //判断修改工单数量不能小于已生产数量
        Integer productionCount = entry.getPlanQuantity() - entry.getQualifiedQuantity() - entry.getScrapQuantity();
        if (productionCount > count) {
            throw new InkelinkException("工单数量不能小于已生产数量");
        }

        //修改产量,并将是否处理调整为false，重新进行下发。
        UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsEntryPartsEntity::getPlanQuantity, count)
                .set(PpsEntryPartsEntity::getIsSend, false)
                .eq(PpsEntryPartsEntity::getId, entry.getId());
        update(upset);
    }

    /**
     * 关闭工单
     *
     * @param entryNo
     */
    @Override
    public void closeEntry(List<Long> entryNo) {
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsEntryPartsEntity::getId, entryNo);
        List<PpsEntryPartsEntity> entryList = selectList(qry);
        for (PpsEntryPartsEntity entry : entryList) {
            //工单已关闭，跳过
            if (entry.getStatus() >= 90) {
                return;
            }
            UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsEntryPartsEntity::getStatus, 90)
                    .set(PpsEntryPartsEntity::getActualEndDt, new Date())
                    .eq(PpsEntryPartsEntity::getEntryNo, entry.getEntryNo());
            update(upset);
        }
    }

    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    @Override
    public List<OutsourceEntryAreaDTO> getOutsourceEntryArea(String entryNo) {
        PpsEntryPartsEntity ppsEntry = getFirstByEntryNo(entryNo);
        if (ppsEntry == null) {
            throw new InkelinkException("未找到对应工单！");
        }
        //返回匹配的生产区域
        List<OutsourceEntryAreaDTO> list = new ArrayList<>();
        List<PpsProcessRelationEntity> processRes = ppsProcessRelationService.getListByOrderCategory(ppsEntry.getOrderCategory(), ppsEntry.getProcessCode());
        for (PpsProcessRelationEntity pro : processRes) {
            OutsourceEntryAreaDTO et = new OutsourceEntryAreaDTO();
            et.setLineName(pro.getLineName());
            et.setLineCode(pro.getLineCode());
            list.add(et);
        }
        return list;
    }


    /**
     * 拆分工单
     *
     * @param request
     */
    @Override
    public void splitEntry(SplitEntryPara request) {
        if (request.getSplitCount() <= 0) {
            throw new InkelinkException("无效的拆分数量");
        }
        PpsEntryPartsEntity oldEntryInfo = getFirstByEntryNo(request.getEntryNo());
        if (oldEntryInfo == null) {
            throw new InkelinkException("无效的生产工单信息");
        }
        if (oldEntryInfo.getIsFreeze()) {
            throw new InkelinkException("工单已冻结，无法操作！");
        }
        Integer surplusCount = oldEntryInfo.getPlanQuantity() - oldEntryInfo.getQualifiedQuantity() - oldEntryInfo.getScrapQuantity();
        if (surplusCount < request.getSplitCount()) {
            throw new InkelinkException("该生产工单剩余生产数量,不满足拆分数量");
        }
        if (request.getStartTime() != null && request.getEndTime() != null
                && request.getStartTime().getTime() > request.getEndTime().getTime()) {
            throw new InkelinkException("预计开始时间不能大于预计完成时间");
        }

        //获取唯一码KEY
        String snKey = "ppsEntrySn";
        switch (oldEntryInfo.getOrderCategory()) {
            //压铸
            case 3:
                snKey = "ppsEntryParts_Cast";
                break;
            //机加工
            case 4:
                snKey = "ppsEntryParts_Machining";
                break;
            //冲压
            case 5:
                snKey = "ppsEntryParts_Stamping";
                break;
            //电池盖板
            case 6:
                snKey = "ppsEntryParts_CoverPlate";
                break;
            default:
                break;
        }
        //排除数量=0的计划单

        //生成工单号
        Map<String, String> snpara = new LinkedHashMap<>();
        snpara.put("PlanNo", oldEntryInfo.getPlanNo());
        snpara.put("LineCode", request.getLineCode());
        snpara.put("ProcessNo", oldEntryInfo.getProcessCode());
        String entryNo = sysSnConfigProvider.createSnBypara(snKey, snpara);

        PpsProcessRelationEntity lineInfo = ppsProcessRelationService.getFirstByLineCode(request.getLineCode(),1);

        PpsEntryPartsEntity entryInfo = new PpsEntryPartsEntity();
        entryInfo.setPlanNo(oldEntryInfo.getPlanNo());
        entryInfo.setEntryNo(entryNo);
        entryInfo.setProcessCode(oldEntryInfo.getProcessCode());
        entryInfo.setProcessName(oldEntryInfo.getProcessName());
        entryInfo.setOrderCategory(oldEntryInfo.getOrderCategory());
        entryInfo.setEstimatedStartDt(request.getStartTime());
        entryInfo.setEstimatedEndDt(request.getEndTime());
        entryInfo.setWorkshopCode(lineInfo.getShopCode());
        entryInfo.setLineCode(request.getLineCode());
        entryInfo.setLineName(lineInfo.getLineName());
        entryInfo.setModel(oldEntryInfo.getModel());
        entryInfo.setPlanQuantity(request.getSplitCount());
        entryInfo.setMaterialNo(oldEntryInfo.getMaterialNo());
        entryInfo.setMaterialCn(oldEntryInfo.getMaterialCn());
        entryInfo.setStatus(1);

        insert(entryInfo);

        //更新原工单数量
        UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryPartsEntity> upsetLmp = upset.lambda();
        upsetLmp.setSql(" PLAN_QUANTITY = PLAN_QUANTITY - " + request.getSplitCount());
        //订单被拆完了，设置为完成(这里不需要设置计划结束，因为拆单了，肯定工单未完成)
        if (surplusCount.equals(request.getSplitCount())) {
            upsetLmp.set(PpsEntryPartsEntity::getStatus, 20)
                    .set(PpsEntryPartsEntity::getActualEndDt, new Date());
        }
        upsetLmp.eq(PpsEntryPartsEntity::getId, oldEntryInfo.getId());
        update(upset);
    }

    /**
     * 没有锁定的工单
     *
     * @param status
     * @param pageIndex
     * @param pageSize
     * @param orderCategory
     * @return
     */
    @Override
    public IPage<PpsEntryPartsEntity> getNoLockWith(Integer status,int pageIndex, int pageSize, String orderCategory) {
        if(status == null){
            return new Page<PpsEntryPartsEntity>();
        }
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().lt(PpsEntryPartsEntity::getStatus, status)
                .eq(PpsEntryPartsEntity::getOrderCategory, orderCategory)
                //.isNotNull(PpsEntryPartsEntity::getEstimatedStartDt)
                .orderByAsc(PpsEntryPartsEntity::getEstimatedStartDt);
        return getDataByPage(qry, pageIndex, pageSize);
    }

    /**
     * 预备生产
     *
     * @param status
     * @param entryId
     */
    @Override
    public void entryLock(Integer status,Long entryId) {
        PpsEntryPartsEntity entry = get(entryId);
        if (entry.getStatus() != 1) {
            throw new InkelinkException("当前工单状态不允许操作");
        }
        if (entry.getOrderCategory() == 5 && status ==2) {
            preLock(entryId);
            return;
        }

        UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsEntryPartsEntity::getStatus, status)
                .eq(PpsEntryPartsEntity::getId, entryId);


        update(upset);
    }

    /**
     * 预备生产
     *
     * @param entryId
     */
    @Override
    public void preLock(Long entryId) {
        PpsEntryPartsEntity entry = get(entryId);
        if (entry.getStatus() != 1) {
            throw new InkelinkException("当前工单状态不允许操作");
        }
        //获取配置的数量
        SysConfigurationEntity lockConfig = sysConfigurationProvider.getSysConfigurations("EntryPartsPreLockCount")
                .stream().findFirst().orElse(null);
        int prelockCount = Integer.parseInt(lockConfig == null ? "1" : lockConfig.getValue());
        //已锁定数量
        QueryWrapper<PpsEntryPartsEntity> qryCnt = new QueryWrapper<>();
        qryCnt.lambda().eq(PpsEntryPartsEntity::getOrderCategory, 5)
                .eq(PpsEntryPartsEntity::getStatus, 3);
        Long currentLockCount = selectCount(qryCnt);
        if (currentLockCount >= prelockCount) {
            throw new InkelinkException("已超过最大可锁定数量！");
        }
        //冲压订单
        if (entry.getOrderCategory() != 5) {
            throw new InkelinkException("当前工单不允许预锁定！");
        }
        UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsEntryPartsEntity::getStatus, 2)
                .eq(PpsEntryPartsEntity::getId, entryId);
        update(upset);
    }

    /**
     * 开始生产
     *
     * @param id
     */
    @Override
    public PpsEntryPartsEntity beginProduct(Long id) {
        PpsEntryPartsEntity entryInfo = get(id);
        if (entryInfo == null) {
            throw new InkelinkException("未找到生产工单！");
        }
        /*if (entryInfo.getOrderCategory() == 5 && entryInfo.getStatus() < 2) {
            throw new InkelinkException("请先进行备料锁定！");
        }*/
        //验证是否冻结
        if (entryInfo.getIsFreeze()) {
            throw new InkelinkException("该工单已被冻结，请确认！");
        }
        if (entryInfo.getStatus() < 10) {
            UpdateWrapper<PpsEntryPartsEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PpsEntryPartsEntity::getStatus, 10)
                    .set(PpsEntryPartsEntity::getActualStartDt,new Date())
                    .lt(PpsEntryPartsEntity::getStatus, 10)
                    .eq(PpsEntryPartsEntity::getId, id);
            update(upset);
            //未上线的，进行上线

            InsertAsAviPointInfo inAvi = new InsertAsAviPointInfo();
            inAvi.setPlanNo(entryInfo.getPlanNo());
            inAvi.setLineCode(entryInfo.getLineCode());
            inAvi.setScanType("S");
            ppsAsAviPointService.insertAsAviPoint(inAvi);

        }
        return entryInfo;
    }

    /**------------离散业务(end)-----------------*/

}