package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgPartAuditDetailFilterInfo extends PageDataDto {
    /**
     * 工位
     */
    private String workstationCode;

    /**
     * 筛选Key
     */
    private String key;
}
