package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.exception.InkelinkException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

/**
 * @author 阳波
 * @ClassName ClassUtil
 * @description:
 * @date 2023年09月21日
 * @version: 1.0
 */
public class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);
    public static void validNullByNullAnnotation(Object bean) {
        if(bean == null){
            return;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object val = getAttributeValue(bean,field.getName());
            if(field.getType().equals(String.class)
                && field.isAnnotationPresent(NotBlank.class)
                && StringUtils.isBlank((String)val)){
                NotBlank notBlank = field.getAnnotation(NotBlank.class);
                throw new InkelinkException(notBlank.message());
            }else if(field.getType().equals(Integer.class)
                    && (field.isAnnotationPresent(NotNull.class))
                    && val == null){
                NotNull notNull = field.getAnnotation(NotNull.class);
                throw new InkelinkException(notNull.message());
            }
        }

    }



    public static Object getAttributeValue(Object bean,String fieldName) {
        Class clazz = bean.getClass();
        Field field;
        try{
            field = clazz.getDeclaredField(fieldName);
        }catch (NoSuchFieldException e){
            throw new InkelinkException(String.format("字段[%s]在实体[%s]里不存在",fieldName,bean.getClass().getSimpleName()));
        }
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, bean);
    }

    public static void setAttributeValue(Object bean,String fieldName,Object val){
        Class clazz = bean.getClass();
        Field field;
        try{
            field = clazz.getDeclaredField(fieldName);
        }catch (NoSuchFieldException e){
            throw new InkelinkException(String.format("字段[%s]在实体[%s]里不存在",fieldName,bean.getClass().getSimpleName()));
        }
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, val);
    }

    public static <T1,T2> void copeVal(T1 source, T2 target, Map<String,String> mapping) {
        if(source == null || target == null || mapping == null || mapping.isEmpty()){
            return;
        }
        for(Map.Entry<String,String> item : mapping.entrySet()){
            setAttributeValue(target,item.getKey(),getAttributeValue(source,item.getValue()));
        }
    }

    public static  <T extends Object> void setDefaultValue(T obj){
        if(obj != null){
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field : fields){
                Object val = ClassUtil.getAttributeValue(obj,field.getName());
                setDefaultValue(val,field);
            }
        }
    }

    private static void setDefaultValue(Object val,Field field){
        if(val != null ){
            if(List.class.equals(field.getType())){
                setListType(field.getType());
            }else{
                ClassUtil.defaultValue(val);
            }
        }
    }

    private static void setListType(Object val){
        List<Object> listVal = (List<Object>) val;
        if(!listVal.isEmpty()){
            for(Object eachObj : listVal){
                ClassUtil.defaultValue(eachObj);
            }
        }
    }

    /**
     * 属性值为null和空的时候设置默认值
     *
     * @param demo 实体类
     */
    public static void defaultValue(Object demo) {
        try {
            Class<?> aClass = demo.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            List<String> fieldName = new ArrayList<>();
            for (Field field : declaredFields) {
                ReflectionUtils.makeAccessible(field);
                String name = field.getName();
                if (StringUtils.isNotBlank(name)) {
                    fieldName.add(name);
                }
            }
            for (String s : fieldName) {
                Method method = aClass.getMethod("get" + s.substring(0, 1).toUpperCase() + s.substring(1));
                Object invoke = method.invoke(demo);
                if (invoke == null || "".equals(invoke)) {
                    Field field = aClass.getDeclaredField(s);
                    ReflectionUtils.makeAccessible(field);
                    String typeName = field.getGenericType().getTypeName();
                    Object val =  ReflectionUtils.getField(field, demo);
                    if(val == null){
                        if (typeName.contains("String")) {
                            ReflectionUtils.setField(field, demo, StringUtils.EMPTY);
                        } else if (typeName.contains("Integer")) {
                            ReflectionUtils.setField(field, demo, 0);
                        } else if (typeName.contains("Long")) {
                            ReflectionUtils.setField(field, demo, 0);
                        } else if (typeName.contains("Double")) {
                            ReflectionUtils.setField(field, demo, 0.0);
                        } else if (typeName.contains("BigDecimal")) {
                            ReflectionUtils.setField(field, demo, 0);
                        } else if (typeName.contains("Date")) {
                            ReflectionUtils.setField(field, demo, new Date());
                        } else if (typeName.contains("Boolean")) {
                            ReflectionUtils.setField(field, demo, false);
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }




}
