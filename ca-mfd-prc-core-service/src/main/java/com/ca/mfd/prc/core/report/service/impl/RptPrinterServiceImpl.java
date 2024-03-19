package com.ca.mfd.prc.core.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.report.dto.EditStatusDto;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.mapper.IRptPrinterMapper;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 报表打印机服务实现
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Service
public class RptPrinterServiceImpl extends AbstractCrudServiceImpl<IRptPrinterMapper, RptPrinterEntity> implements IRptPrinterService {

    /**
     * 查询所有数据
     *
     * @return 数据列表
     */
    @Override
    public List<RptPrinterEntity> getListAll() {
        QueryWrapper<RptPrinterEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrinterEntity> lambdaQueryWrapper = queryWrapper.lambda();
        return selectList(queryWrapper);
    }

    /**
     * 根据ip集合查询
     *
     * @param ips ip集合
     * @return 列表
     */
    @Override
    public List<RptPrinterEntity> getListByIps(List<String> ips) {
        QueryWrapper<RptPrinterEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrinterEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RptPrinterEntity::getIp, ips);
        return selectList(queryWrapper);
    }

    /**
     * 根据ID 更新状态
     *
     * @param ids         主键集合
     * @param printStatus 状态
     */
    @Override
    public void updatePrintStatusByIds(List<Long> ids, String printStatus) {
        UpdateWrapper<RptPrinterEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptPrinterEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptPrinterEntity::getPrintStatus, printStatus);
        lambdaUpdateWrapper.in(RptPrinterEntity::getId, ids);
        lambdaUpdateWrapper.ne(RptPrinterEntity::getPrintStatus, printStatus);
        this.update(updateWrapper);
    }

    /**
     * 更新打印状态
     *
     * @param printStatus 状态
     * @param id          主键
     */
    @Override
    public void updateQueuePrintStatus(String printStatus, Long id) {
        UpdateWrapper<RptPrinterEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptPrinterEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptPrinterEntity::getPrintStatus, printStatus);
        lambdaUpdateWrapper.eq(RptPrinterEntity::getId, id);
        this.update(updateWrapper);
    }

    /**
     * 根据打印代码查询报表
     *
     * @param bizCode 打印代码
     * @return 报表
     */
    @Override
    public RptPrinterEntity getInfoByBizCode(String bizCode) {
        QueryWrapper<RptPrinterEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrinterEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptPrinterEntity::getBizCode, bizCode);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public void editStatus(EditStatusDto dto) {
        if(dto!=null&& dto.getId()!=null&&StringUtils.isNotEmpty(dto.getStatus())){
            UpdateWrapper<RptPrinterEntity> updateWrapper = new UpdateWrapper<>();
            LambdaUpdateWrapper<RptPrinterEntity> lambdaUpdateWrapper = updateWrapper.lambda();
            lambdaUpdateWrapper.set(RptPrinterEntity::getPrintStatus, dto.getStatus());
            lambdaUpdateWrapper.eq(RptPrinterEntity::getId, dto.getId());
            lambdaUpdateWrapper.eq(RptPrinterEntity::getIsDelete, 0);
            this.update(updateWrapper);
        }
    }

    @Override
    public List<RptPrinterEntity> getData(String bizCode) {
        if(StringUtils.isEmpty(bizCode)){
            return new ArrayList<>();
        }
        List<String> strings = Arrays.asList(bizCode.split(","));
        QueryWrapper<RptPrinterEntity> wrapper=new QueryWrapper<>();
        wrapper.lambda().in(RptPrinterEntity::getBizCode,strings);
        List<RptPrinterEntity> data = this.getData(wrapper, false);
        return data;
    }
}