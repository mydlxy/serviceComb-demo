package com.ca.mfd.prc.pqs.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsSpcOperationRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsSpcOperationRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsSpcOperationRecordService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @Description: SPC模块_操作记录表服务实现
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@Service
public class PqsSpcOperationRecordServiceImpl extends AbstractCrudServiceImpl<IPqsSpcOperationRecordMapper, PqsSpcOperationRecordEntity> implements IPqsSpcOperationRecordService {
@Autowired
IPqsSpcOperationRecordMapper pqsSpcOperationRecordMapper;

    @Override
    public PqsSpcOperationRecordEntity queryByFileAndOperationMd5(Long fileId, String operationMd5) {
        // 1.查询是否存在数据
        QueryWrapper<PqsSpcOperationRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcOperationRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getPrcPqsSpcFileRecordId, fileId);
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getOperationParamsMd5, operationMd5);
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getIsDelete, false);
        PqsSpcOperationRecordEntity pqsSpcOperationRecordEntity = pqsSpcOperationRecordMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
        if (pqsSpcOperationRecordEntity !=null){
            //修改查找次数
            LambdaUpdateWrapper<PqsSpcOperationRecordEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(PqsSpcOperationRecordEntity::getId,pqsSpcOperationRecordEntity.getId())
                    .eq(PqsSpcOperationRecordEntity::getIsDelete,false)
                    .set(PqsSpcOperationRecordEntity::getOperationCount,Integer.parseInt(pqsSpcOperationRecordEntity.getOperationCount())+1);
            this.update(lambdaUpdateWrapper);
            this.saveChange();

        }
        return pqsSpcOperationRecordEntity;
    }

    @Override
    public List<PqsSpcOperationRecordEntity> getResultByFileId(Long id) {
        QueryWrapper<PqsSpcOperationRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcOperationRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getPrcPqsSpcFileRecordId, id);
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getIsDelete, false);
        List<PqsSpcOperationRecordEntity> pqsSpcOperationRecordEntityList = pqsSpcOperationRecordMapper.selectList(queryWrapper);
        return pqsSpcOperationRecordEntityList;

    }

    @Override
    public PqsSpcOperationRecordEntity queryByFileAndResultName(Long id, String resultName) {
        QueryWrapper<PqsSpcOperationRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcOperationRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getPrcPqsSpcFileRecordId, id);
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getAttribute1, resultName);
        lambdaQueryWrapper.eq(PqsSpcOperationRecordEntity::getIsDelete, false);
        PqsSpcOperationRecordEntity pqsSpcOperationRecordEntity = pqsSpcOperationRecordMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
        return pqsSpcOperationRecordEntity;

    }
}