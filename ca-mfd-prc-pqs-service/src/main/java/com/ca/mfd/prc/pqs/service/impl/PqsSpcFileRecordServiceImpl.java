package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsSpcFileRecordMapper;
import com.ca.mfd.prc.pqs.service.IPqsSpcFileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Description: 服务实现
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@Service
public class PqsSpcFileRecordServiceImpl extends AbstractCrudServiceImpl<IPqsSpcFileRecordMapper, PqsSpcFileRecordEntity> implements IPqsSpcFileRecordService {


    @Autowired
    private IPqsSpcFileRecordMapper pqsSpcFileRecordMapper;

    @Override
    public PqsSpcFileRecordEntity getFileRecordByMd5(String fileMd5) {
        QueryWrapper<PqsSpcFileRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcFileRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getFileMd5, fileMd5);
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getIsDelete, false);

        return this.pqsSpcFileRecordMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PqsSpcFileRecordEntity getFileRecordById(Long id) {
        QueryWrapper<PqsSpcFileRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcFileRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getId, id);
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getIsDelete, false);

        return this.pqsSpcFileRecordMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PqsSpcFileRecordEntity getFileRecordByMd5AndName(String fileMd5, String fileName) {
        QueryWrapper<PqsSpcFileRecordEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsSpcFileRecordEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getFileMd5, fileMd5);
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getOriFileName, fileName);
        lambdaQueryWrapper.eq(PqsSpcFileRecordEntity::getIsDelete, false);

        return this.pqsSpcFileRecordMapper.selectList(queryWrapper).stream().findFirst().orElse(null);
    }
}