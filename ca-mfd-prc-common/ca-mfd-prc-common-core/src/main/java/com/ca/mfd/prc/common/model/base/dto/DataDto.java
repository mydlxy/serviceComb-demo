package com.ca.mfd.prc.common.model.base.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * DataDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataDto extends ConditionsDto {

    @JsonAlias(value = {"Sorts","sorts"})
    private List<SortDto> sorts = new ArrayList<>();
}
