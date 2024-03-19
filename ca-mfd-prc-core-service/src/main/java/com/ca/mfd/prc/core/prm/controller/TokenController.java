package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.entity.AllSession;
import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.entity.BasicConstant;
import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.LocalSessionDs;
import com.ca.mfd.prc.common.entity.PrmLocalSession;
import com.ca.mfd.prc.common.entity.RemoteSession;
import com.ca.mfd.prc.common.entity.TokenDataInfo;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.TokenDTO;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmTokenPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 获取TOKEN
 *
 * @author inkelink
 */
@RestController
@RequestMapping("prm/token")
@Tag(name = "获取TOKEN")
public class TokenController extends BaseApiController {
    @Autowired
    IPrmTokenService prmTokenService;
    @Autowired
    IPrmTokenPermissionService prmTokenPermissionService;
    @Autowired
    IPrmPermissionService prmPermissionService;
    @Autowired
    IPrmInterfacePermissionService prmInterfacePermissionService;
    @Autowired
    RedisUtils redisUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
    @PostMapping("gettokendata")
    @Operation(summary = "登陆")
    public ResultVO getTokenData(@RequestBody TokenDTO.GetTokenModel model) {
        PrmTokenEntity app = prmTokenService.getTokenEntityByAppId(model.getAppId());
        if (app == null) {
            return null;
        }
        Function<RemoteSession, Void> func = (remoteSession) -> {
            redisUtils.set(redisUtils.getRemoteSessionKey(remoteSession.getId()), JsonUtils.toJsonString(remoteSession));
            return null;
        };
        AllSession data = iniSession(model.getNowTime(), app, model.getRememberType(), func);
        return new ResultVO<AllSession>().ok(data);
    }

    private AllSession iniSession(Date nowTime, PrmTokenEntity token, int rememberType, Function<RemoteSession, Void> func) {
        String nowTimeString = DateUtils.format(nowTime, DateUtils.DATE_TIME_PATTERN_M);
        PrmLocalSession localSession = iniLocalSession(token, rememberType);
        boolean locked = token.getTokenEnable();
        if (!locked && token.getExpireDt() != null) {
            Date current = new Date();
            locked = current.compareTo(token.getExpireDt()) < 0;
        }
        RemoteSession remoteSession = new RemoteSession();
        remoteSession.setId(token.getTokenName());
        remoteSession.setKey(token.getId().toString());
        remoteSession.setTime(nowTimeString);
        remoteSession.setLocked(locked);
        remoteSession.setLocalMd5(localSession.getMd5());
        try {
            //更新缓存
            //func(remoteSession);
            func.apply(remoteSession);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
        //设置账户在浏览器保存的数据
        CookieSession cookieSession = new CookieSession();
        cookieSession.setSessionGuid(token.getId().toString());
        cookieSession.setRememberType(rememberType);

        AllSession result = new AllSession();
        result.setCookieData(cookieSession);
        result.setLocalData(localSession);
        result.setRemoteData(remoteSession);

        return result;
    }

    private PrmLocalSession iniLocalSession(PrmTokenEntity data, int rememberType) {

        List<String> permissions = prmTokenPermissionService.getPermissionQuery(data.getId())
                .stream().map(UserRoleMsg::getName).collect(Collectors.toList());

        List<String> paths = prmTokenPermissionService.getPathQuery(data.getId())
                .stream().map(UserRoleMsg::getName).collect(Collectors.toList());
        TokenDataInfo token = new TokenDataInfo();
        token.setAppId(data.getTokenName());
        token.setSecret(data.getToken());
        token.setSecretEnable(data.getTokenEnable());
        token.setGroupName(data.getGroupName());
        token.setPermissions(permissions);
        token.setPaths(paths);
        Map<String, String> basic = new HashMap<>(5);
        String tokenStr = JsonUtils.toJsonString(token);
        if (StringUtils.isNotBlank(tokenStr)) {
            basic.put(BasicConstant.TOKEN, tokenStr);
        } else {
            basic.put(BasicConstant.TOKEN, "{}");
        }
        List<ApiSession> apiList = prmInterfacePermissionService.getApiSession();
        String apiMd5 = EncryptionUtils.md5(JsonUtils.toJsonString(apiList));
        PrmLocalSession localSession = new PrmLocalSession();
        localSession.setId(data.getId().toString());
        localSession.setSessionId(data.getTokenName());
        localSession.setRememberType(rememberType);
        localSession.setUserType(99);
        localSession.setBasic(basic);
        localSession.setApiMd5(apiMd5);

        LocalSessionDs localSessionDs = new LocalSessionDs();
        localSessionDs.setId(localSession.getId());
        localSessionDs.setUserType(localSession.getUserType());
        localSessionDs.setBasic(localSession.getBasic());
        localSessionDs.setApiMd5(apiMd5);

        String dsMd5 = EncryptionUtils.md5(JsonUtils.toJsonString(localSessionDs));
        localSession.setMd5(dsMd5);
        return localSession;
    }
}
