package com.ca.mfd.prc.common.utils;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.enums.DataType;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MybatisPlus的SQL工具类
 *
 * @author inkelink ERIC.ZHOU
 */
public class MpSqlUtils {
    private static final String SQL_OR_GP = "OR_0_";
    private static final String SQL_AND_GP = "AND_0_";
    private static final String SQL_OR_CP = "OR_";

    /**
     * 获取lamq表达式的字段名
     */
    public static <T> String getColumnName(SFunction<T, ?> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fname = meta.getImplMethodName();
        String fieldName = PropertyNamer.methodToProperty(fname);
        return fieldName;
    }

    /**
     * 获取实体的字段名
     */
    public static String getColumnName(List<TableFieldInfo> fields, String fname, TableInfo tableInfo) {
        if (StringUtils.isBlank(fname)) {
            return "";
        }
        if (StringUtils.equalsIgnoreCase(fname, tableInfo.getKeyProperty())) {
            return tableInfo.getKeyColumn();
        }
        TableFieldInfo tableFieldInfo = getColumnField(fields, fname);
        return tableFieldInfo == null ? "" : tableFieldInfo.getColumn();
    }

    /**
     * 获取实体的字段名
     */
    public static TableFieldInfo getColumnField(List<TableFieldInfo> fields, String fname) {
        if (StringUtils.isBlank(fname)) {
            return null;
        }
        List<String> vnames = Arrays.asList(fname.split("[.]"));
        String vname = vnames.size() > 1 ? vnames.get(1) : vnames.get(0);
        TableFieldInfo tableFieldInfo = fields.stream().filter(field -> field.getProperty().equalsIgnoreCase(vname) || field.getColumn().equalsIgnoreCase(vname))
                .findFirst().orElse(null);
        return tableFieldInfo;
    }

    /**
     * 获取分组关联字
     */
    public static String getGroupRel(String groupkey) {
        if (StringUtils.isBlank(groupkey)) {
            return "";
        }
        if (groupkey.trim().startsWith(SQL_OR_GP) || groupkey.trim().startsWith(SQL_AND_GP)) {
            return "";
        }
        if (groupkey.trim().startsWith(SQL_OR_CP)) {
            return "or";
        }
        return "and";
    }

    /**
     * 获取like符号
     */
    public static String getLikeStr(String val, String left, String right) {
        if (StringUtils.isNotBlank(val)) {
            return (left == null ? "" : left) + val + (right == null ? "" : right);
        }
        return val;
    }

    /**
     * 获取分页开始序号
     */
    public static Integer getPageStart(Integer pageindex, Integer pagesize) {
        if (pageindex < 1) {
            pageindex = 1;
        }
        if (pagesize < 1) {
            pagesize = 1;
        }
        Integer start = (pageindex - 1) * pagesize;
        return start;
    }

    /**
     * 获取排序sql
     */
    public static String getSortStr(List<SortDto> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" order by ");
        int snum = 0;
        for (SortDto dto : sorts) {
            if (dto == null || StringUtils.isBlank(dto.getColumnName())) {
                continue;
            }
            if (snum > 0) {
                sb.append(",");
            }
            String sortStr = "";
            sortStr = sortStr + dto.getColumnName();
            if (dto.getDirection() != null && dto.getDirection() == ConditionDirection.DESC) {
                sortStr = sortStr + " desc";
            } else {
                sortStr = sortStr + " asc";
            }
            sb.append(sortStr);
            snum++;
        }
        return sb.toString();
    }

    /**
     * 获取值
     */
    public static Object getVal(ConditionDto dto) {
        if (dto != null) {
            if (dto.getOperator() == ConditionOper.None) {
                return null;
            } else if (dto.getOperator() == ConditionOper.In || dto.getOperator() == ConditionOper.NotIn) {
                if (StringUtils.isBlank(dto.getValue())) {
                    return null;
                } else if (dto.getDataType() == DataType.String) {
                    List<String> lst = Arrays.asList(dto.getValue().split("\\|"));
                    return (lst == null || lst.isEmpty()) ? null : lst;
                } else {
                    List<Object> arrs = new ArrayList<>();
                    Arrays.asList(dto.getValue().split("\\|")).forEach(c -> {
                        ConditionDto et = new ConditionDto();
                        et.setValue(c);
                        et.setDataType(dto.getDataType());
                        Object val = convertVal(et);
                        if (val != null) {
                            arrs.add(val);
                        }
                    });
                    if (arrs.isEmpty()) {
                        return null;
                    }
                    return arrs;
                }

            } else {
                return convertVal(dto);
            }
        }
        return null;
    }

    /**
     * 值转换
     */
    public static Object convertVal(ConditionDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getDataType() == DataType.Boolean) {
            if (StringUtils.isBlank(dto.getValue())) {
                return null;
            }
            return StringUtils.equalsIgnoreCase(dto.getValue().trim(), "true")
                    || StringUtils.equals(dto.getValue().trim(), "1");
        } else if (dto.getDataType() == DataType.Decimal) {
            if (StringUtils.isBlank(dto.getValue())) {
                return null;
            }
            return BigDecimal.valueOf(Double.parseDouble(dto.getValue().trim()));
        } else if (dto.getDataType() == DataType.Int) {
            if (StringUtils.isBlank(dto.getValue())) {
                return null;
            }
            return Integer.parseInt(dto.getValue().trim());
        } else if (dto.getDataType() == DataType.DateTime) {
            if (StringUtils.isBlank(dto.getValue())) {
                return null;
            }
            return DateUtils.parse(dto.getValue().trim(), dto.getValue().trim().length() == 10 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
        } else {
            return dto.getValue();
        }
    }

    /**
     * 获取值数组
     */
    public static List<Object> getSplitVal(ConditionDto dto) {
        if (dto == null || StringUtils.isBlank(dto.getValue())) {
            return new ArrayList<>();
        }
        return Arrays.asList(dto.getValue().split("\\|"));
    }

    /**
     * 获取Relation
     */
    public static String getRelation(ConditionDto dto, Integer sindex) {
        if (sindex != null && sindex == 0) {
            return "";
        }
        if (dto != null && dto.getRelation() == ConditionRelation.Or) {
            return "or";
        }
        return "and";
    }

    /**
     * 过滤条件组
     */
    public static List<ConditionDto> filtrationCondition(List<ConditionDto> conditions, String alias) {
        List<ConditionDto> models = conditions.stream().filter(c -> c.getIsQuery() != null && c.getIsQuery()
                && !StringUtils.isBlank(c.getValue())).collect(Collectors.toList());
        //  && !StringUtils.isBlank(c.getValue()) TODO 删除确认
        if (models.isEmpty()) {
            return new ArrayList<>();
        }

        for (ConditionDto model : models) {
            if (StringUtils.isBlank(model.getGroup())) {
                model.setGroup("");
            }
            if (alias != null) {
                model.setAlias(alias);
            }
            if (StringUtils.isNotBlank(model.getAlias()) && !model.getColumnName().contains(".")) {
                model.setColumnName(model.getAlias() + "." + model.getColumnName());
            }
        }
        return models;
    }

    /**
     * 设置条件组的DateType(处理boolean的查询问题)
     */
    public static void setConditionDataType(List<TableFieldInfo> fields, List<ConditionDto> models) {
        for (ConditionDto model : models) {
            TableFieldInfo field = getColumnField(fields, model.getColumnName());
            if (field != null && model.getDataType() == null && field.getField() != null) {
                String typeName = field.getField().getType().getName();
                if (StringUtils.equals("java.lang.Boolean", typeName)) {
                    model.setDataType(DataType.Boolean);
                }
            }
        }
    }

    /**
     * 过滤条件组
     */
    public static List<ConditionDto> filtrationCondition(Class<?> clazz, List<ConditionDto> conditions, String alias) {
        return filtrationCondition(clazz, conditions, alias, false);
    }

    /**
     * 过滤条件组
     */
    public static List<ConditionDto> filtrationCondition(Class<?> clazz, List<ConditionDto> conditions, String alias, Boolean isRemoveEmptyField) {
        if (clazz == null || conditions == null || conditions.isEmpty()) {
            return new ArrayList<>();
        }
        List<ConditionDto> models = conditions.stream().filter(c -> c.getIsQuery() != null && c.getIsQuery()
                && !StringUtils.isBlank(c.getValue())).collect(Collectors.toList());
        if (models.isEmpty()) {
            return new ArrayList<>();
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if(tableInfo == null) {
            return models;
        }
        List<TableFieldInfo> fields = tableInfo.getFieldList();
        setConditionDataType(fields, models);
        List<ConditionDto> removes = new ArrayList<>();
        for (ConditionDto model : models) {
            String columnName = getColumnName(fields, model.getColumnName(), tableInfo);
            if (StringUtils.isNotBlank(columnName)) {
                model.setColumnName(columnName);
            } else {
                if (Boolean.TRUE.equals(isRemoveEmptyField)) {
                    removes.add(model);
                    continue;
                }
            }
            if (StringUtils.isBlank(model.getGroup())) {
                model.setGroup("");
            }
            if (alias != null) {
                model.setAlias(alias);
            }
            if (StringUtils.isNotBlank(model.getAlias()) && !model.getColumnName().contains(".")) {
                model.setColumnName(model.getAlias() + "." + model.getColumnName());
            }
        }

        for (ConditionDto model : removes) {
            models.remove(model);
        }
        return models;
    }

    /**
     * 添加刪除条件
     */
    public static List<ConditionDto> addDelCondition(List<ConditionDto> models, Boolean isDelete) {
        if (models == null) {
            models = new ArrayList<>();
        }
        ConditionDto delcon = models.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getColumnName(), "isDelete")
                || StringUtils.equalsIgnoreCase(c.getColumnName(), "IS_Delete")).findFirst().orElse(null);
        if (delcon != null) {
            delcon.setValue(Boolean.TRUE.equals(isDelete) ? "1" : "0");
        } else {
            delcon = new ConditionDto("IS_Delete", Boolean.TRUE.equals(isDelete) ? "1" : "0", ConditionOper.Equal);
            models.add(delcon);
        }
        boolean hasOr = models.stream().filter(c -> c.getRelation() == ConditionRelation.Or).count() > 0;
        if (hasOr) {
            delcon.setGroup("delgroup");
            delcon.setGroupRelation(ConditionRelation.And);
        }
        return models;
    }

    /**
     * 过滤排序组
     */
    public static List<SortDto> filtrationSort(List<SortDto> models, String alias) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        for (SortDto model : models) {
            if (alias != null) {
                model.setAlias(alias);
            }
            if (StringUtils.isNotBlank(model.getAlias()) && !model.getColumnName().contains(".")) {
                model.setColumnName(model.getAlias() + "." + model.getColumnName());
            }
        }
        return new ArrayList<>(models);
    }

    /**
     * 过滤排序组
     */
    public static List<SortDto> filtrationSort(Class<?> clazz, List<SortDto> models, String alias) {
       return filtrationSort(clazz, models, alias, false);
    }

    /**
     * 过滤排序组
     */
    public static List<SortDto> filtrationSort(Class<?> clazz, List<SortDto> models, String alias, Boolean isRemoveEmptyField) {
        if (clazz == null || models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo == null) {
            return new ArrayList<>(models);
        }
        List<TableFieldInfo> fields = tableInfo.getFieldList();
        List<SortDto> removes = new ArrayList<>();
        for (SortDto model : models) {
            String columnName = getColumnName(fields, model.getColumnName(), tableInfo);
            if (StringUtils.isNotBlank(columnName)) {
                model.setColumnName(columnName);
            } else {
                if (Boolean.TRUE.equals(isRemoveEmptyField)) {
                    removes.add(model);
                    continue;
                }
            }
            if (alias != null) {
                model.setAlias(alias);
            }
            if (StringUtils.isNotBlank(model.getAlias()) && !model.getColumnName().contains(".")) {
                model.setColumnName(model.getAlias() + "." + model.getColumnName());
            }
        }
        for (SortDto model : removes) {
            models.remove(model);
        }
        return new ArrayList<>(models);
    }


    /**
     * 获取值查询条件分组
     */
    public static Map<String, List<ConditionDto>> getConditionGroup(List<ConditionDto> models) {
        if (models == null || models.isEmpty()) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        // 过滤空值和非查询条件
        List<ConditionDto> where = models.stream().filter(c -> c.getIsQuery() != null && c.getIsQuery() && c.getValue() != null).collect(Collectors.toList());
        if (where.isEmpty()) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        for (ConditionDto con : where) {
            if (StringUtils.isBlank(con.getGroup())) {
                con.setGroup("");
            }
            if (con.getIndex() == null) {
                con.setIndex(0);
            }
            con.setDbval(getVal(con));
        }
        where = where.stream().filter(c -> c.getDbval() != null).collect(Collectors.toList());

        where.sort(Comparator.comparingInt(ConditionDto::getIndex));
        Map<String, List<ConditionDto>> map = Maps.newHashMapWithExpectedSize(0);
        int nindex = 0;
        for (Map.Entry<String, List<ConditionDto>> kv : where.stream().collect(Collectors.groupingBy(ConditionDto::getGroup)).entrySet()) {
            if (kv.getValue() != null && !kv.getValue().isEmpty()) {
                String rel = "AND_";
                ConditionDto con = kv.getValue().stream().filter(c -> c.getGroupRelation() != null).findFirst().orElse(null);
                if (con != null) {
                    rel = con.getGroupRelation().value() == ConditionRelation.Or.value() ? "OR_" : "AND_";
                }
                rel = rel + nindex + "_";
                map.put(rel + (kv.getKey() == null ? "" : kv.getKey()), kv.getValue());
                nindex++;
            }
        }
        return map;
    }

}
