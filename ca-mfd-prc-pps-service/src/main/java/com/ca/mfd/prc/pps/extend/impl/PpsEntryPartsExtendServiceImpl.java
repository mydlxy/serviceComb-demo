package com.ca.mfd.prc.pps.extend.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.extend.IPpsEntryPartsExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsEntryPartsMapper;
import org.springframework.stereotype.Service;

/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsEntryPartsExtendServiceImpl extends AbstractCrudServiceImpl<IPpsEntryPartsMapper, PpsEntryPartsEntity> implements IPpsEntryPartsExtendService {

    /**
     * 获取
     *
     * @param entryNo
     * @return
     * */
    public PpsEntryPartsEntity getByEntryNo(String entryNo) {
        QueryWrapper<PpsEntryPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryPartsEntity::getEntryNo, entryNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}
