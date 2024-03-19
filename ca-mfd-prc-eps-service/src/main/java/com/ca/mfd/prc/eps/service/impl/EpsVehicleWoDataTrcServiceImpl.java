package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoDataTrcMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataTrcService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 追溯操作记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleWoDataTrcServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoDataTrcMapper, EpsVehicleWoDataTrcEntity> implements IEpsVehicleWoDataTrcService {

    @Override
    public PageData<EpsVehicleWoDataTrcEntity> page(PageDataDto model) {
        PageData<EpsVehicleWoDataTrcEntity> result = new PageData<>();
        result.setPageIndex(model.getPageIndex());
        result.setPageSize(model.getPageSize());
        ConditionDto conditionDelete = null;
        if (model.getConditions() != null) {
            conditionDelete = model.getConditions().stream().filter(c -> StringUtils.equals(c.getColumnName(), "IS_DELETE")).findFirst().orElse(null);
        }
        boolean isDelete = false;
        if (conditionDelete != null) {
            model.getConditions().remove(conditionDelete);
            isDelete = true;
        }
        PageDataDto page = new PageDataDto();
        page.setPageIndex(model.getPageIndex());
        page.setPageSize(model.getPageSize());
        page.setConditions(model.getConditions());
        page.setSorts(model.getSorts());
        IPage<EpsVehicleWoDataTrcEntity> pageData = super.getDataByPage(model, isDelete);
        result.setTotal((int) pageData.getTotal());
        result.setDatas(pageData.getRecords());
        return result;
    }

}