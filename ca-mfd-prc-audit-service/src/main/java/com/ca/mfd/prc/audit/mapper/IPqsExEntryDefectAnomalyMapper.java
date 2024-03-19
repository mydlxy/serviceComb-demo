package com.ca.mfd.prc.audit.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.audit.dto.AuditDefectAnomalyReponse;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.entity.PqsExEntryDefectAnomalyEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @Description: 精致工艺缺陷记录Mapper
 * @author inkelink
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Mapper
public interface IPqsExEntryDefectAnomalyMapper extends IBaseMapper<PqsExEntryDefectAnomalyEntity> {
    /**
     * 获取
     *
     * @param para
     * @return
     */
    Page<AuditDefectAnomalyReponse> getVehicleDefectAnomalyList(Page<AuditDefectAnomalyReponse> page, @Param("para") GetAuditDefectAnomalyRequest para);
}