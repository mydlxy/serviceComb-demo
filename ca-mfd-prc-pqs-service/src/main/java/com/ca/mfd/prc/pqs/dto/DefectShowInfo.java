package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-10-15:50
 * @Description:
 */
@Data
public class DefectShowInfo {
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = IdGenerator.getId();

    private String code;

    private String description;
}
