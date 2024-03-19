package com.ca.mfd.prc.common.model.base.dto;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.enums.DataType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * ConditionDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@JsonDeserialize()
public class ConditionDto {
    /**
     * Summary:字段名称
     **/
    @JsonAlias(value = {"columnName", "ColumnName"})
    private String columnName;
    /**
     * Summary:操作类型
     **/
    private ConditionOper operator;
    /**
     * Summary:操作关系
     **/
    private ConditionRelation relation;
    /**
     * Summary:字段的类型
     **/
    private DataType dataType;
    /**
     * Summary:分组
     **/
    private String group;
    /**
     * Summary:分组间的关系
     **/
    private ConditionRelation groupRelation;
    /**
     * Summary:表别名
     **/
    private String alias;
    /**
     * Summary:比较值
     **/
    @JsonAlias(value = {"value", "Value"})
    private String value;
    /**
     * Summary:索引号
     **/
    private Integer index;
    /**
     * Summary:是否查询
     **/
    private Boolean isQuery;
    /**
     * Summary:数据库使用值
     **/
    @JsonIgnore
    private Object dbval;

    /**
     * Summary:构造函数
     **/
    public ConditionDto() {
        this.operator = ConditionOper.Equal;
        this.relation = ConditionRelation.And;
        this.groupRelation = ConditionRelation.And;
        this.isQuery = true;
    }

    public ConditionDto(String columnName, String value, ConditionOper operator) {
        this.operator = operator;
        this.relation = ConditionRelation.And;
        this.groupRelation = ConditionRelation.And;
        this.columnName = columnName;
        this.value = value;
        this.isQuery = true;
    }

    public ConditionDto(String columnName, String value, ConditionOper operator, ConditionRelation relation) {
        this.operator = operator;
        this.relation = relation;
        this.groupRelation = ConditionRelation.And;
        this.columnName = columnName;
        this.value = value;
        this.isQuery = true;
    }

    public ConditionDto(String columnName, String value, ConditionOper operator, ConditionRelation relation, DataType dataType) {
        this.operator = operator;
        this.relation = relation;
        this.groupRelation = ConditionRelation.And;
        this.columnName = columnName;
        this.value = value;
        this.dataType = dataType;
        this.isQuery = true;
    }

    public ConditionDto(String columnName, String value, ConditionOper operator, ConditionRelation relation, String group) {
        this.operator = operator;
        this.relation = relation;
        this.groupRelation = ConditionRelation.And;
        this.columnName = columnName;
        this.value = value;
        this.group = group;
        this.isQuery = true;
    }
}
