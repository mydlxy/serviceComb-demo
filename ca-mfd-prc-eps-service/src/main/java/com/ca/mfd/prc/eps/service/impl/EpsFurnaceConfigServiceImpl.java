package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.entity.EpsFurnacePutLogEntity;
import com.ca.mfd.prc.eps.entity.EpsLmsCallButtonEntity;
import com.ca.mfd.prc.eps.mapper.IEpsFurnaceConfigMapper;
import com.ca.mfd.prc.eps.entity.EpsFurnaceConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsFurnaceConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @Description: 熔化炉配置服务实现
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Service
public class EpsFurnaceConfigServiceImpl extends AbstractCrudServiceImpl<IEpsFurnaceConfigMapper, EpsFurnaceConfigEntity> implements IEpsFurnaceConfigService {

    @Override
    public EpsFurnaceConfigEntity getFirstByFurnaceNo(String furnaceNo) {
        QueryWrapper<EpsFurnaceConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsFurnaceConfigEntity::getFurnaceNo, furnaceNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public void beforeInsert(EpsFurnaceConfigEntity model) {
        if (model.getNewRate() != null && model.getReuseRate() != null) {
            double rate = model.getNewRate().doubleValue() + model.getReuseRate().doubleValue();
            if (rate < 100 || rate > 100) {
                throw new InkelinkException("新料比例和回炉料比例相加数必须是100");
            }
        }
    }

    @Override
    public void beforeUpdate(EpsFurnaceConfigEntity model) {
        if (model.getNewRate() != null && model.getReuseRate() != null) {
            double rate = model.getNewRate().doubleValue() + model.getReuseRate().doubleValue();
            if (rate < 100 || rate > 100) {
                throw new InkelinkException("新料比例和回炉料比例相加数必须是100");
            }
        }
    }

}