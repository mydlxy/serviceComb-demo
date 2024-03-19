package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsWorkplaceTimeLogMapper;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceTimeLogService;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceTimeLogEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位时间日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsWorkplaceTimeLogServiceImpl extends AbstractCrudServiceImpl<IEpsWorkplaceTimeLogMapper, EpsWorkplaceTimeLogEntity> implements IEpsWorkplaceTimeLogService {

    /**
     * 获取列表
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    @Override
    public List<EpsWorkplaceTimeLogEntity> getBySn(String workstationCode, String sn) {
        QueryWrapper<EpsWorkplaceTimeLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsWorkplaceTimeLogEntity::getWorkstationCode, workstationCode).eq(EpsWorkplaceTimeLogEntity::getSn, sn).orderByDesc(EpsWorkplaceTimeLogEntity::getInsertWorkplaceDt);
        return getData(qry, false);
    }


}