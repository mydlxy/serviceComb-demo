package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取扩展对象
 *
 * @author inkelink
 * @date 2023年4月4日
 * @变更说明 BY eric.zhou At 2023年4月4日
 */
public class BasicExtensions {
    private BasicExtensions() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getBasicObj(PrmLocalSession local, String basicKey, Class<T> clazz, T defaultValue) {
        if (local.getBasic().containsKey(basicKey)) {
            String result = local.getBasic().getOrDefault(basicKey, null);
            if (StringUtils.isNotBlank(result)) {
                try {
                    return JsonUtils.parseObject(result, clazz);
                } catch (Exception exe) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    public static <T> List<T> getBasicList(PrmLocalSession local, String basicKey, Class<T> clazz, List<T> defaultValue) {
        if (local.getBasic().containsKey(basicKey)) {
            String result = local.getBasic().getOrDefault(basicKey, null);
            if (StringUtils.isNotBlank(result)) {
                try {
                    return JsonUtils.parseArray(result, clazz);
                } catch (Exception exe) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("unused")
    public static UserData getUser(PrmLocalSession local, UserData defaultValue) {
        UserData result = getBasicObj(local, BasicConstant.USER, UserData.class, defaultValue);
        result.setId(local.getId());
        return result;
    }
    @SuppressWarnings("unused")
    public static List<OpenData> getOpen(PrmLocalSession local, List<OpenData> defaultValue) {
        return getBasicList(local, BasicConstant.OPEN, OpenData.class, defaultValue);
    }
    @SuppressWarnings("unused")
    public static List<RoleData> getRole(PrmLocalSession local, List<RoleData> defaultValue) {
        return getBasicList(local, BasicConstant.ROLE, RoleData.class, defaultValue);
    }

    public static List<PrmData> getPrm(PrmLocalSession local, List<PrmData> defaultValue) {
        List<PrmData> prmList = getBasicList(local, BasicConstant.PRM, PrmData.class, defaultValue);
        if (prmList != null) {
            prmList = prmList.stream().filter(p -> p.getRecycleDt() == null
                    || p.getRecycleDt().compareTo(new Date()) > 0).collect(Collectors.toList());
        }
        return prmList;
    }

    @SuppressWarnings("unused")
    public static TokenDataInfo getTokenData(PrmLocalSession local, TokenDataInfo defaultValue) {
        return getBasicObj(local, BasicConstant.TOKEN, TokenDataInfo.class, defaultValue);
    }
}
