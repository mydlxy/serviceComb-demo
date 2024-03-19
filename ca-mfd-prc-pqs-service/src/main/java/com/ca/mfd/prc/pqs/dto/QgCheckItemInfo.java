package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgCheckItemInfo {

    /**
     * 围堵明细ID
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 状态
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int result = 0;

    /**
     * 唯一码
     */
    private String sn = StringUtils.EMPTY;

    /**
     * 车型
     */
    private String model = StringUtils.EMPTY;
}
