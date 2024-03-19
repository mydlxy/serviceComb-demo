package com.ca.mfd.prc.common.model.base.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * ConditionsDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConditionsDto {
    @JsonAlias(value = {"conditions","Conditions"})
    private List<ConditionDto> conditions = new ArrayList<>();
}