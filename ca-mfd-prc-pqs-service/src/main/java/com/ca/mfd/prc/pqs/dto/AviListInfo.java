package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-18-9:55
 * @Description:
 */
@Data
public class AviListInfo {

    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    private String code;

    private String name;
}
