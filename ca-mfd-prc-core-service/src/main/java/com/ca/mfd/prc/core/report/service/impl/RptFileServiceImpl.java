package com.ca.mfd.prc.core.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.report.entity.RptFileEntity;
import com.ca.mfd.prc.core.report.mapper.IRptFileMapper;
import com.ca.mfd.prc.core.report.service.IRptFileService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 报表文件存储服务实现
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Service
public class RptFileServiceImpl extends AbstractCrudServiceImpl<IRptFileMapper, RptFileEntity> implements IRptFileService {

    /**
     * 关键字查询
     *
     * @param keys 关键字
     * @return 列表
     */
    @Override
    public List<RptFileEntity> getListByName(String keys) {
        QueryWrapper<RptFileEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptFileEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.like(RptFileEntity::getDisplayName, keys);
        lambdaQueryWrapper.orderByAsc(RptFileEntity::getDisplayName);
        return selectList(queryWrapper);
    }

    /**
     * 根据配置名查询
     *
     * @param displayName
     * @return
     */
    @Override
    public String getByDisplayName(String displayName) {
        QueryWrapper<RptFileEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptFileEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptFileEntity::getDisplayName, displayName);

        String result = StringUtils.EMPTY;
        List<RptFileEntity> rptFileEntities = selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(rptFileEntities)) {
            RptFileEntity rptFileEntity = rptFileEntities.stream()
                    .filter(r -> StringUtils.equals(r.getDisplayName(), displayName))
                    .findFirst().orElse(null);
            if (rptFileEntity != null) {
                result = rptFileEntity.getPath();
            }
        }

        return result;
    }
}