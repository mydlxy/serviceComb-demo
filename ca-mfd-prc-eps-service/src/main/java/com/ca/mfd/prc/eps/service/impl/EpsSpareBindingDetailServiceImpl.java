package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsSpareBindingDetailMapper;
import com.ca.mfd.prc.eps.service.IEpsSpareBindingDetailService;
import com.ca.mfd.prc.eps.entity.EpsSpareBindingDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 备件绑定明细服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsSpareBindingDetailServiceImpl extends AbstractCrudServiceImpl<IEpsSpareBindingDetailMapper, EpsSpareBindingDetailEntity> implements IEpsSpareBindingDetailService {

    /**
     * 获取撬上面的备件VIN号集合
     *
     * @param virtualVin
     * @return
     */
    @Override
    public List<String> getSpareParVins(String virtualVin) {
        QueryWrapper<EpsSpareBindingDetailEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsSpareBindingDetailEntity::getPartVirtualVin, virtualVin).select(EpsSpareBindingDetailEntity::getSpareVin);
        return selectList(qry).stream().map(EpsSpareBindingDetailEntity::getSpareVin).collect(Collectors.toList());
    }

    /**
     * 备件过点 查询虚拟VIN号
     *
     * @param id 主键
     * @return 虚拟VIN号集合
     */
    @Override
    public List<String> getPartVirtualVinByPartTrackId(String id) {
        QueryWrapper<EpsSpareBindingDetailEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<EpsSpareBindingDetailEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(EpsSpareBindingDetailEntity::getPrcEpsBodySparePartTrackId, id);
        List<EpsSpareBindingDetailEntity> list = this.selectList(queryWrapper);
        return list.stream().map(EpsSpareBindingDetailEntity::getPartVirtualVin).collect(Collectors.toList());
    }
}