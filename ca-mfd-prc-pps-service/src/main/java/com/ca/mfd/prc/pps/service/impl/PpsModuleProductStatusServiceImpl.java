package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.mapper.IPpsModuleProductStatusMapper;
import com.ca.mfd.prc.pps.entity.PpsModuleProductStatusEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleProductStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 模组相关产品状态服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsModuleProductStatusServiceImpl extends AbstractCrudServiceImpl<IPpsModuleProductStatusMapper, PpsModuleProductStatusEntity> implements IPpsModuleProductStatusService {

    /**
     * 获取
     *
     * @param barcodes
     * @return
     * */
    @Override
    public List<PpsModuleProductStatusEntity> getListByBarCodes(List<String> barcodes) {
        QueryWrapper<PpsModuleProductStatusEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsModuleProductStatusEntity::getProductBarcode, barcodes);
        return selectList(qry);
    }

}