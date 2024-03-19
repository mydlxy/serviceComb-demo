package com.ca.mfd.prc.pps.extend;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;

import java.util.List;

/**
 * 工单扩展
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsPlanPartsExtendService extends ICrudService<PpsPlanPartsEntity> {

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    PpsPlanPartsEntity getFirstByPlanNo(String planNo);

}