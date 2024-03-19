package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.JsonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PrmLocalSession class
 *
 * @author inkelink
 * @date 2023/04/03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrmLocalSession")
public class PrmLocalSession implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PrmLocalSession.class);

    @Schema(title = "当前时间")
    private String id = StringUtils.EMPTY;
    @Schema(title = "当前时间")
    private String sessionId;
    @Schema(title = "当前时间")
    private Integer rememberType;
    @Schema(title = "当前时间")
    private Integer userType;
    @Schema(title = "当前时间")
    private Map<String, String> basic;
    @Schema(title = "当前时间")
    private String extData;
    @Schema(title = "当前时间")
    private String md5;
    @Schema(title = "当前时间")
    private String apiMd5;

    @Schema(title = "当前时间")
    private LocalSession local;
    @Schema(title = "当前时间")
    private TokenDataInfo app;

    public String getBasicObj(String basicKey) {
        if (basic == null || !basic.containsKey(basicKey)) {
            return null;
        }
        return basic.get(basicKey);
    }

    public <T> T getBasicObj(String basicKey, T defaultValue, Class<T> clazz) {
        String value = getBasicObj(basicKey);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return JsonUtils.parseObject(value, clazz);
        } catch (Exception ex) {
            logger.error("", ex);
            return defaultValue;
        }
    }

    public <T> List<T> getBasicObj(String basicKey, List<T> defaultValue, Class<T> clazz) {
        String value = getBasicObj(basicKey);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        if (StringUtils.equals(value, Constant.SPLIT_ARRAYS)) {
            value = "[]";
        }
        try {
            return JsonUtils.parseArray(value, clazz);
        } catch (Exception ex) {
            logger.error("", ex);
            return defaultValue;
        }
    }

    public UserData getUser(UserData defaultValue) {
        UserData result = getBasicObj("user", defaultValue, UserData.class);
        result.setId(getId());
        return result;
    }

    public List<OpenData> getOpen(List<OpenData> defaultValue) {
        return getBasicObj("open", defaultValue, OpenData.class);
    }

    public List<RoleData> getRole(List<RoleData> defaultValue) {
        return getBasicObj("role", defaultValue, RoleData.class);
    }

    public List<PrmData> getPrm(List<PrmData> defaultValue) {
        List<PrmData> prmList = getBasicObj("prm", defaultValue, PrmData.class);
        if (prmList != null) {
            prmList = prmList.stream().filter(p -> p.getRecycleDt() == null
                            || p.getRecycleDt().getTime() > System.currentTimeMillis())
                    .collect(Collectors.toList());
        }
        return prmList;
    }

    public TokenDataInfo getTokenData(TokenDataInfo defaultValue) {
        return getBasicObj("token", defaultValue, TokenDataInfo.class);
    }
}
