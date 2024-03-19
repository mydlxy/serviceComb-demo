///**
// * Copyright (c) 2023 依柯力 All rights reserved.
// *
// * https://www.inkelink.com
// *
// * 版权所有，侵权必究！
// */
//
//package com.ca.common.handler;
//
//import com.baomidou.mybatisplus.core.enums.SqlMethod;
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import OnlineUserDTO;
//import IdentityHelper;
//import UUIDUtils;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Objects;
//import java.util.UUID;
//
//import static Constant.EMPTY_ID;
//
///**
// * 公共字段，自动填充值
// *
// * @author inkelink
// */
//@Component
//public class FieldMetaObjectHandler implements MetaObjectHandler {
//    private final static String ID = "id";
//    private final static String CREATE_DATE = "createDt";
//    private final static String CREATOR = "creater";
//    private final static String CREATOR_ID = "createUserId";
//    private final static String UPDATE_DATE = "updateDt";
//    private final static String UPDATER = "updater";
//    private final static String UPDATER_ID = "updateUserId";
//
//    @Autowired
//    IdentityHelper identityHelper;
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//
//        OnlineUserDTO loginUser = identityHelper.getLoginUser();
//        String userid = loginUser.getId();
//        String userName = loginUser.getUserName();
//        Date now = new Date();
//
//        //创建者
//        fillId(metaObject, UUID.randomUUID().toString());
//        //创建者
//        fill(metaObject, CREATOR, userName);
//        //创建时间
//        fill(metaObject, CREATE_DATE, now);
//        //创建者ID
//        fill(metaObject, CREATOR_ID, userid);
//        //更新者
//        fill(metaObject, UPDATER, userName);
//        //更新时间
//        fill(metaObject, UPDATE_DATE, now);
//        //更新者ID
//        fill(metaObject, UPDATER_ID, userid);
//
//        String[] getterNames = metaObject.getGetterNames();
//        for (String getterName : getterNames) {
//            Object obj = metaObject.getValue(getterName);
//            if (Objects.isNull(obj)) {
//                if (metaObject.getGetterType(getterName) == String.class) {
//                    metaObject.setValue(getterName, "");
//                } else if (metaObject.getGetterType(getterName) == Integer.class) {
//                    metaObject.setValue(getterName, 0);
//                } else if (metaObject.getGetterType(getterName) == Boolean.class) {
//                    metaObject.setValue(getterName, false);
//                }
//            }
//        }
//
//    }
//
//    MetaObjectHandler fillId(MetaObject metaObject, String fieldVal) {
//        Object object = getFieldValByName(ID, metaObject);
//        if (object == null) {
//            setFieldValByName(ID, fieldVal, metaObject);
//        } else {
//            String value = object.toString();
//            if (UUIDUtils.isGuidEmpty(value)) {
//                setFieldValByName(ID, fieldVal, metaObject);
//            }
//        }
//
//        return this;
//    }
//
//    MetaObjectHandler fill(MetaObject metaObject, String fieldName, Object fieldVal) {
//        if (Objects.nonNull(fieldVal)) {
//            setFieldValByName(fieldName, fieldVal, metaObject);
//        }
//        return this;
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        OnlineUserDTO loginUser = identityHelper.getLoginUser();
//        String userid = loginUser.getId();
//        String userName = loginUser.getUserName();
//        Date now = new Date();
//        //更新者
//        fill(metaObject, UPDATER, userName);
//        //更新时间
//        fill(metaObject, UPDATE_DATE, now);
//        //更新者ID
//        fill(metaObject, UPDATER_ID, userid);
//    }
//}