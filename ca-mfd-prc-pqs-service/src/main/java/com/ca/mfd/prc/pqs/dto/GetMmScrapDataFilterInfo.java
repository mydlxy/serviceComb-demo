package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class GetMmScrapDataFilterInfo extends PageDataDto {

    /**
     * 筛选Key
     */
    private String key;
}
