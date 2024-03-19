package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;


}
