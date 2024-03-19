package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName PmBusinessBaseEntity
 * @description:业务父类
 * @date 2023年08月26日
 * @version: 1.0
 */
@Data
public class CmcPmBusinessBaseVo {

    private Integer source = 1;


    /**
     * 来源ID
     */
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;


}
