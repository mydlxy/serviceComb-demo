package com.ca.mfd.prc.common.model.base.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PageDataDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PageDataDto extends DataDto {
    @JsonAlias(value = {"pageSize","PageSize"})
    private Integer pageSize;
    @JsonAlias(value = {"pageIndex","PageIndex"})
    private Integer pageIndex = 0;
}
