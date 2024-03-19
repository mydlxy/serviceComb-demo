package com.ca.mfd.prc.pqs.dto;

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
public class ShowQgCheckListDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 分组
     */
    private String group = StringUtils.EMPTY;

    /**
     * 顺序
     */
    private String displayNo = StringUtils.EMPTY;

    /**
     * 检查内容
     */
    private String content = StringUtils.EMPTY;

    /**
     * 状态
     */
    private Integer result = 0;
}
