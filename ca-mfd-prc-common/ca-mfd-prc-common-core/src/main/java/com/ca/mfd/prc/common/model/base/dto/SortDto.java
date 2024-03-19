package com.ca.mfd.prc.common.model.base.dto;


import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * SortDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@JsonDeserialize()
public class SortDto {
    /**
     * Summary:表的字段名
     **/
    private String columnName;

    /**
     * Summary:排序方向
     **/
    @JsonAlias(value = {"direction", "Direction"})
    private ConditionDirection direction;

    /**
     * Summary:表别名
     **/
    private String alias;

    /**
     * Summary:构造函数
     **/
    public SortDto() {
        direction = ConditionDirection.ASC;
    }

    /**
     * Summary:构造函数
     **/
    public SortDto(String columnName, ConditionDirection direction) {
        this.columnName = columnName;
        this.direction = direction;
    }
}