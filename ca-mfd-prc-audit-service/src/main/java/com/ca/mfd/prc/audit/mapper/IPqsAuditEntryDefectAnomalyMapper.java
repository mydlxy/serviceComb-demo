package com.ca.mfd.prc.audit.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.audit.dto.AuditDefectAnomalyReponse;
import com.ca.mfd.prc.audit.dto.GetAuditDefectAnomalyRequest;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryDefectAnomalyEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author inkelink
 * @Description: AUDIT缺陷记录Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsAuditEntryDefectAnomalyMapper extends IBaseMapper<PqsAuditEntryDefectAnomalyEntity> {
    /**
     * 获取
     *
     * @param para
     * @return
     */
    Page<AuditDefectAnomalyReponse> getVehicleDefectAnomalyList(Page<AuditDefectAnomalyReponse> page, @Param("para") GetAuditDefectAnomalyRequest para);
}