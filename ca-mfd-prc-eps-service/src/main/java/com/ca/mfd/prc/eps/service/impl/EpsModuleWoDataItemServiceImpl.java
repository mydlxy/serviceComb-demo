package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsModuleWoDataItemMapper;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataItemEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleWoDataItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 模组工艺数据项服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsModuleWoDataItemServiceImpl extends AbstractCrudServiceImpl<IEpsModuleWoDataItemMapper, EpsModuleWoDataItemEntity> implements IEpsModuleWoDataItemService {

    @Override
    public List<EpsModuleWoDataItemEntity> getByWoDataId(Long woDataId) {
        QueryWrapper<EpsModuleWoDataItemEntity> qryItem = new QueryWrapper<>();
        qryItem.lambda().eq(EpsModuleWoDataItemEntity::getPrcEpsModuleWoDataId, woDataId);

        return selectList(qryItem);
    }
}