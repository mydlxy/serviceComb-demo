package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class PqsQgCheckedListDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工位
     */
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 结果;0 未处理 1:OK 2:NG 3ByPass
     */
    private Integer result = 0;
}
