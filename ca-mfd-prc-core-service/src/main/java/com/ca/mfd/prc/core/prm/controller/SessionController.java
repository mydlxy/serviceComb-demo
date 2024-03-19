package com.ca.mfd.prc.core.prm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.entity.AllSession;
import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.entity.BasicConstant;
import com.ca.mfd.prc.common.entity.BasicExtensions;
import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.LocalDync;
import com.ca.mfd.prc.common.entity.LocalSessionDs;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.OpenMsgConstant;
import com.ca.mfd.prc.common.entity.PrmData;
import com.ca.mfd.prc.common.entity.PrmLocalSession;
import com.ca.mfd.prc.common.entity.PrmMsgConstant;
import com.ca.mfd.prc.common.entity.RemoteSession;
import com.ca.mfd.prc.common.entity.SessionEntity;
import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.entity.UserMsgConstant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionView;
import com.ca.mfd.prc.core.prm.dto.SessionDTO;
import com.ca.mfd.prc.core.prm.dto.UserRoleMsg;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmSessionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserOpenEntity;
import com.ca.mfd.prc.core.prm.enums.LoginStyle;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentJoinUserService;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentService;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmSessionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserOpenService;
import com.ca.mfd.prc.core.prm.service.IPrmUserPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmUserRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.prm.utils.LoginLockVerificationUtils;
import com.ca.mfd.prc.core.prm.service.Signature;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户登录Controller
 *
 * @author inkelink
 */
@RestController
@RequestMapping("prm/session")
@Tag(name = "用户登录信息")
public class SessionController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private static final Map<Integer, LoginStyle> loginStyle = new HashMap<>();

    static {
        for (LoginStyle value : LoginStyle.values()) {
            loginStyle.put(value.value(), value);
        }
    }

    @Autowired
    IPrmSessionService prmSessionService;
    @Autowired
    IPrmPermissionService prmPermissionService;
    @Autowired
    IPrmUserService prmUserService;
    @Autowired
    IPrmUserPermissionService prmUserPermissionService;
    @Autowired
    IPrmUserOpenService prmUserOpenService;
    @Autowired
    IPrmUserRoleService prmUserRoleService;
    @Autowired
    IPrmDepartmentService prmDepartmentService;
    @Autowired
    IPrmDepartmentJoinUserService prmDepartmentJoinUserService;
    @Autowired
    IPrmRoleService prmRoleService;
    @Autowired
    IPrmRolePermissionService prmRolePermissionService;
    @Autowired
    IPrmInterfacePermissionService prmInterfacePermissionService;
    @Autowired
    ISysConfigurationService sysConfigurationService;
    @Autowired
    RedisUtils redisUtils;
    private final String appKey = "";
    @Autowired
    private IdentityHelper identityHelper;
    /**
     * 和.net互通的sessionid(。net库中的ID) 70019690-2a3a-4248-9b95-765eb8bba708
     */
    @Value("${inkelink.session.id: }")
    private String sessionId;

    @PostMapping("memberlogin")
    @Operation(summary = "用户登陆")
    public ResultVO<AllSession> memberLogin(@RequestBody SessionDTO.MemberLoginModel model) {
        if (StringUtils.isBlank(model.getUserName())) {
            throw new InkelinkException("用户名不为空");
        }
        if (StringUtils.isBlank(model.getPwd())) {
            throw new InkelinkException("密码不为空");
        }
        String pwd = EncryptionUtils.md5(model.getPwd());
        String userName = model.getUserName();
        List<PrmUserEntity> userList = null;
        LoginStyle enumTypes = loginStyle.get(model.getLoginStyle());
        switch (enumTypes) {
            case MesLogin:
                userList = prmUserService.getMesLogin(pwd, userName);
                break;
            case AdLogin:
            case AuthOpenLogin:
                userList = prmUserService.getAuthOpenLogin(userName);
                break;
            default:
                userList = prmUserService.getMesLogin(EncryptionUtils.md5(pwd), userName);
                break;
        }
        if (model.getUserType() > 0) {
            userList = userList.stream().filter(w -> w.getUserType().equals(model.getUserType())).collect(Collectors.toList());
        }
        PrmUserEntity user = userList.stream().findFirst().orElse(null);
        boolean loginLock = false;
        String resData = sysConfigurationService.getConfiguration("LoginLock", Constant.USER_PASS_STRATEGY);
        loginLock = Boolean.parseBoolean(resData);
        if (user == null && loginLock) {
            // todo  这个逻辑需确定
            if (!userName.equals(Constant.SYSTEM_MANAGER) ||
                    !userName.equals(Constant.SYSTEM_ADMINROLE)) {
                int loginLockTime = 0;
                String resTimeData = sysConfigurationService.getConfiguration("LoginLockTime", Constant.USER_PASS_STRATEGY);
                loginLockTime = Integer.parseInt(resTimeData);
                Integer min = 60;
                if (LoginLockVerificationUtils.verificationLock(userName, loginLockTime * min)) {
                    prmUserService.updateEntityFrozenDt(loginLockTime, userName);
                    prmSessionService.saveChange();
                    throw new InkelinkException(model.getUserName() + "账户已被锁定" + loginLockTime);
                }
            }
        }
        AllSession data = doMemeberLogin(user, model.getRememberType(), model.getSessionEntity());
        prmSessionService.saveChange();
        LoginLockVerificationUtils.removeVerificationLock(userName);
        return new ResultVO<AllSession>().ok(data);
    }

    @PostMapping("memberautologin")
    @Operation(summary = "用户自动登陆")
    public ResultVO memberAutoLogin(@RequestBody SessionDTO.MemberAutoLoginModel req) {
        //通过cookie创建一个对象，但是这个时候用户的基本信息对象是没有相关用户信息的
        // 此model是上次用户登录时保存于用户端的识别码，用于后续访问的自动登录。不是本次访问的相关信息
        Integer sdStatus = 2;
        if (req.getCookieSession() == null
                || UUIDUtils.isGuidEmpty(req.getCookieSession().getSessionGuid())
                || req.getCookieSession().getRememberType() == null
                || req.getCookieSession().getRememberType() <= 0) {
            throw new InkelinkException("票据无效");
        }

        //如果在数据库中找到了相应的记录，则说明可以自动登录
        Boolean isExpire = "true".equals(SpringContextUtils.getConfiguration("EQuality:Prm:IsExpire", "false"));
        PrmSessionEntity session = prmSessionService.getPrmSessionEntityByIdStatus(req.getCookieSession().getSessionGuid()
                , isExpire);
        if (session == null) {
            throw new InkelinkException("会话失效");
        }

        //自动登录
        PrmUserEntity user = prmUserService.get(session.getPrcPrmUserId());
        if (user != null && user.getIsDelete() == true) {
            user = null;
        }
        if (user == null) {
            throw new InkelinkException("用户不存在");
        }
        if (!user.getIsActive()) {
            //throw new InkelinkException("用户未激活");
            //7激活状态改变导致登出
            SessionDTO.MemberLogOutModel logOutReq = new SessionDTO.MemberLogOutModel();
            logOutReq.setCookieSession(req.getCookieSession());
            logOutReq.setStatus(7);
            return memberLogOut(logOutReq);
        }
        //用户状态1、正常,2、冻结,3、锁定
        if (user.getStatus() == null || user.getStatus().equals(sdStatus)) {
            //0不能用，1可用,2用户登出,3用户切换登出4用户修改密码5密码修改联动触发的登录会话失效
            SessionDTO.MemberLogOutModel logOutReq = new SessionDTO.MemberLogOutModel();
            logOutReq.setCookieSession(req.getCookieSession());
            logOutReq.setStatus(6);
            return memberLogOut(logOutReq);
        }
        Date nowTime = new Date();

        //触发时间 超过24小时，自动锁定
        if (session.getStatus() == 1
                && session.getActiveDt().getTime()
                < DateUtils.addDateHours(nowTime, -24).getTime()
                && session.getType() != 8760) //非一年帐号
        {
            session.setStatus(0);
        }
        //自动登录 更新session
        session.setActiveCount(session.getActiveCount() + 1);
        session.setActiveDt(nowTime);
        session.setExpireDt(DateUtils.addDateHours(nowTime, session.getType()));

        session.setIp(req.getUserHostAddress());
        UpdateWrapper<PrmSessionEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PrmSessionEntity::getStatus, session.getStatus())
                .set(PrmSessionEntity::getActiveCount, session.getActiveCount())
                .set(PrmSessionEntity::getActiveDt, session.getActiveDt())
                .set(PrmSessionEntity::getExpireDt, session.getExpireDt())
                .set(PrmSessionEntity::getIp, session.getIp())
                .eq(PrmSessionEntity::getId, session.getId());
        prmSessionService.update(upset);

        List<PrmUserOpenEntity> tokens = prmUserOpenService.getTokens(user.getId());
        PrmDepartmentEntity dep = prmDepartmentJoinUserService.getPrmDepartmentByUserId(user.getId());
        if (dep != null) {
            user.setDepartmentName(dep.getDepartmentName());
            user.setDepartmentId(dep.getId().toString());
            user.setDepartmentCode(dep.getDepartmentCode());
        }
        Function<Map, Void> func = (map) -> {
            RemoteSession remoteSession = (RemoteSession) map.get("remoteSession");
            LocalDync localDyncinfo = (LocalDync) map.get("localDyncs");
            boolean isCompatible = Boolean.parseBoolean(SpringContextUtils.getConfiguration("EQuality:Prm:IsCompatible", "false"));
            if (isCompatible) {
                redisUtils.set(appKey + session.getId() + "{User}", localDyncinfo, 3600);
            }
            String rmoteJson = JsonUtils.toJsonString(remoteSession);
            if (StringUtils.isBlank(rmoteJson)) {
                rmoteJson = "{}";
            }
            redisUtils.set(redisUtils.getRemoteSessionKey(remoteSession.getId()), rmoteJson, 3600);
            return null;
        };

        AllSession data = iniSession(nowTime,
                user,
                session,
                tokens,
                func);
        prmSessionService.saveChange();
        return new ResultVO<AllSession>().ok(data);
    }

    @PostMapping("memberfrozen")
    @Operation(summary = "根据账号冻结用户")
    public ResultVO<Boolean> memberFrozen(String userName, Integer userType) {
        prmUserService.updateMemberFrozen(userName, userType);
        prmUserService.saveChange();
        return new ResultVO<Boolean>().ok(true);
    }

    @GetMapping("getuser")
    @Operation(summary = "根据ID获取用户基础信息")
    public ResultVO<UserData> getUser(String id) {
        PrmUserEntity user = prmUserService.get(id);
        if (user == null) {
            throw new InkelinkException("用户不存在");
        }
        UserData data = getUserData(user);
        return new ResultVO<UserData>().ok(data);
    }

    @GetMapping("getuserbycodeorusername")
    @Operation(summary = "根据code或者名称获取用户信息")
    public ResultVO<UserData> getUserByCodeorUserName(String codeorUserName) {
        PrmUserEntity user = prmUserService.getAllUser().stream().filter(s -> s.getJobNo().equals(codeorUserName)
                || s.getUserName().equals(codeorUserName) || s.getEmail().equals(codeorUserName)
                || s.getNickName().equals(codeorUserName)).findFirst().orElse(null);
        if (user == null) {
            throw new InkelinkException("用户不存在");
        }
        UserData data = getUserData(user);
        return new ResultVO<UserData>().ok(data);
    }

    @PostMapping("getusersbyarrayid")
    @Operation(summary = "根据用户id获取用户信息")
    public ResultVO<List<UserData>> getUsersByArrayId(@RequestBody IdsModel ids) {
        if (ids == null || ids.getIds().length == 0) {
            List<UserData> data = new ArrayList<>();
            return new ResultVO<List<UserData>>().ok(data);
        }
        List<PrmUserEntity> data = prmUserService.getAllUser();
        List<PrmUserEntity> findlist = new ArrayList<>();
        for (PrmUserEntity userEntity : data) {
            if (Arrays.asList(ids.getIds()).contains(userEntity.getId())) {
                findlist.add(userEntity);
            }
        }
        List<UserData> userDataList = new ArrayList<>();
        for (PrmUserEntity userEntity : findlist) {
            UserData userData = getUserData(userEntity);
            userDataList.add(userData);
        }
        return new ResultVO<List<UserData>>().ok(userDataList);
    }

    @PostMapping("prmgetusersbydepartmentids")
    @Operation(summary = "根据部门ID获取用户")
    public ResultVO<List<UserData>> prmgetUsersByDepartmentIds(@RequestBody IdsModel ids) {
        if (ids == null || ids.getIds().length == 0) {
            List<UserData> data = new ArrayList<>();
            return new ResultVO<List<UserData>>().ok(data);
        }
        List<UserData> data = prmDepartmentJoinUserService.getPrmDepartmentJoinUser(Arrays.asList(ids.getIds()));
        return new ResultVO<List<UserData>>().ok(data);
    }

    @PostMapping("getusersbyroleid")
    @Operation(summary = "根据角色ID获取用户")
    public ResultVO<List<UserData>> getUsersByRoleId(@RequestBody IdsModel ids) {
        if (ids == null || ids.getIds().length == 0) {
            List<UserData> data = new ArrayList<>();
            return new ResultVO<List<UserData>>().ok(data);
        }
        List<UserData> userDataList = prmUserService.getUsersByRoleId(Arrays.asList(ids.getIds()));
        return new ResultVO<List<UserData>>().ok(userDataList);
    }

    @GetMapping("getusers")
    @Operation(summary = "获取用户基础信息")
    public ResultVO<List<UserData>> getUsers() {
        List<PrmUserEntity> userEntityList = prmUserService.getAllUser();
        List<UserData> userDataList = new ArrayList<>();
        for (PrmUserEntity prmUserEntity : userEntityList) {
            UserData userData = getUserData(prmUserEntity);
            userDataList.add(userData);
        }
        return new ResultVO<List<UserData>>().ok(userDataList);
    }

    @GetMapping("getlocalsession")
    @Operation(summary = "获取本地会话")
    public ResultVO<PrmLocalSession> getLocalSession(String id) {
        PrmSessionEntity session = prmSessionService.getPrmSessionEntityById(id);
        if (session == null) {
            throw new InkelinkException("没有找到可用的会话");
        }
        PrmUserEntity user = prmUserService.get(session.getPrcPrmUserId());
        if (user == null) {
            throw new InkelinkException("用户不存在");
        }
        if (!user.getIsActive()) {
            throw new InkelinkException("用户未激活");
        }
        List<PrmUserOpenEntity> tokens = prmUserOpenService.getTokens(user.getId());
        PrmDepartmentEntity dep = prmDepartmentJoinUserService.getPrmDepartmentByUserId(user.getId());
        if (dep != null) {
            user.setDepartmentName(dep.getDepartmentName());
            user.setDepartmentId(dep.getId().toString());
            user.setDepartmentCode(dep.getDepartmentCode());
        }
        PrmLocalSession localSession = iniLocalSession(user, session, tokens);
        return new ResultVO<PrmLocalSession>().ok(localSession);
    }

    @PostMapping("memberlogout")
    @Operation(summary = "Cookie登出")
    public ResultVO<Boolean> memberLogOut(@RequestBody SessionDTO.MemberLogOutModel req) {
        if (StringUtils.equals(req.getCookieSession().getSessionGuid(), UUIDUtils.getEmpty())) {
            throw new InkelinkException("Cookie不存在");
        }
        prmSessionService.updateStatusById(req.getCookieSession().getSessionGuid(), req.getStatus());
        prmSessionService.saveChange();
        redisUtils.delete(redisUtils.getRemoteSessionKey(req.getCookieSession().getSessionGuid()));
        return new ResultVO<Boolean>().ok(true);
    }

    @PostMapping("memberlock")
    @Operation(summary = "根据Cookie锁定用户")
    public ResultVO<String> memberLock(@RequestBody CookieSession cookieSession) {
        if (cookieSession.getSessionGuid().equals(UUIDUtils.getEmpty()) || cookieSession.getRememberType() <= 0) {
            throw new InkelinkException("会话不存在");
        }
        String remoteSessionJson = String.valueOf(redisUtils.get("RemoteSession" + cookieSession.getSessionGuid()));
        RemoteSession remoteSession = JsonUtils.parseObject(remoteSessionJson, RemoteSession.class);
        int actCount = prmSessionService.updateStatusByExpireDt(cookieSession.getSessionGuid(), 0);
        //远程会话存在，直接更新数据库和当前状态
        if (remoteSession != null && !remoteSession.getLocked() && actCount > 0) {
            remoteSession.setLocked(true);
            String redisKey = "RemoteSession" + cookieSession.getSessionGuid();
            if (redisUtils.hasKey(redisKey)) {
                redisUtils.set(redisKey, JsonUtils.toJsonString(remoteSession));
            }
        }
        prmSessionService.saveChange();
        return new ResultVO<String>().ok("","操作成功");
    }

    @PostMapping("memberunlock")
    @Operation(summary = "用户Cookie解锁")
    public ResultVO<String> memberUnLock(@RequestBody SessionDTO.MemberUnLockModel req) {
        if (req.getCookieSession().getSessionGuid().equals(UUIDUtils.getEmpty())
                || req.getCookieSession().getRememberType() <= 0) {
            throw new InkelinkException("会话不存在");
        }
        PrmSessionEntity sessionEntity = prmSessionService.getPrmSessionEntityByExpireDt(req.getCookieSession().getSessionGuid());
        if (sessionEntity == null) {
            throw new InkelinkException("会话不存在");
        }
        PrmUserEntity account = prmUserService.getPrmUserInfo(sessionEntity.getUserName(), req.getPassword());
        if (account == null) {
            throw new InkelinkException("用户不存在");
        }
        ////远程会话存在，直接更新数据库和当前状态
        String remoteSessionJson = String.valueOf(redisUtils.get("RemoteSession" + req.getCookieSession().getSessionGuid()));
        RemoteSession remoteSession = JsonUtils.parseObject(remoteSessionJson, RemoteSession.class);
        int actCount = prmSessionService.updateStatusByExpireDt(req.getCookieSession().getSessionGuid(), 1);
        if (remoteSession != null && remoteSession.getLocked() && actCount > 0) {
            remoteSession.setLocked(false);
            String redisKey = "RemoteSession" + req.getCookieSession().getSessionGuid();
            if (redisUtils.hasKey(redisKey)) {
                redisUtils.set(redisKey, JsonUtils.toJsonString(remoteSession));
            }
        }
        prmSessionService.saveChange();
        return new ResultVO<String>().ok("","操作成功");
    }

    private PrmLocalSession iniLocalSession(PrmUserEntity user, PrmSessionEntity session, List<PrmUserOpenEntity> tokenList) {

        Map<String, String> userMsgs = getUserMsgsMaps(user);

        List<Map<String, String>> openMsgs = getOpenMsgsMaps(tokenList);

        List<String> roleMsgs = prmUserRoleService.getRoleMsgs(user.getId()).stream().map(UserRoleMsg::getName).collect(Collectors.toList());
        Map<String, String> basic = new HashMap<>(50);
        String userMsgsJson = JsonUtils.toJsonString(userMsgs);
        if (StringUtils.isBlank(userMsgsJson)) {
            userMsgsJson = "{}";
        }
        basic.put(BasicConstant.USER, userMsgsJson);

        String openMsgsJson = JsonUtils.toJsonString(openMsgs);
        if (StringUtils.isBlank(openMsgsJson)) {
            openMsgsJson = "[]";
        }
        basic.put(BasicConstant.OPEN, JsonUtils.toJsonString(openMsgsJson));

        String roleMsgsJson = JsonUtils.toJsonString(roleMsgs);
        if (StringUtils.isBlank(roleMsgsJson)) {
            roleMsgsJson = "[]";
        }
        basic.put(BasicConstant.ROLE, JsonUtils.toJsonString(roleMsgsJson));

        if (!user.getUserName().equalsIgnoreCase(Constant.SYSTEM_ADMINROLE)
                && !user.getUserName().equalsIgnoreCase(Constant.SYSTEM_MANAGER)) {
            List<String> rolePermissions = prmUserRoleService.getRolePermissions(user.getId()).stream()
                    .map(UserRoleMsg::getName).collect(Collectors.toList());

            List<PrmUserPermissionView> userPermissions = prmUserPermissionService.getUserPermissions(user.getId());
            List<Map<String, String>> permissions = new ArrayList<>();
            for (String items : rolePermissions) {
                Map<String, String> subMap = new HashMap<>(5);
                subMap.put(PrmMsgConstant.CODE, items);
                subMap.put(PrmMsgConstant.RECYCLEDT, "");
                permissions.add(subMap);
            }

            for (PrmUserPermissionView view : userPermissions) {
                PrmUserPermissionView up = userPermissions.stream().
                        filter(s -> StringUtils.equals(s.getCode(), view.getCode())).findFirst().orElse(null);
                if (up != null) {
                    continue;
                }
                Map<String, String> subMap = new HashMap<>(5);
                subMap.put(PrmMsgConstant.CODE, view.getCode());
                subMap.put(PrmMsgConstant.RECYCLEDT, DateUtils.format(view.getRecycleDt(), DateUtils.DATE_TIME_PATTERN));
                permissions.add(subMap);
            }
            basic.put(BasicConstant.PRM, JsonUtils.toJsonString(permissions));
        } else {
            List<PrmPermissionEntity> list = prmPermissionService.getAllDatas();
            List<Map<String, String>> permissions = new ArrayList<>();
            for (PrmPermissionEntity entity : list) {
                Map<String, String> maplist = new HashMap<>(5);
                maplist.put(PrmMsgConstant.CODE, entity.getPermissionCode());
                maplist.put(PrmMsgConstant.RECYCLEDT, "");
                permissions.add(maplist);
            }
            basic.put(BasicConstant.PRM, JsonUtils.toJsonString(permissions));
        }
        List<ApiSession> apiList = prmInterfacePermissionService.getApiSession();
        String apiMd5 = EncryptionUtils.md5(JsonUtils.toJsonString(apiList));
        PrmLocalSession localSession = new PrmLocalSession();
        localSession.setId(user.getId().toString());
        localSession.setSessionId(session.getId().toString());
        localSession.setRememberType(session.getType());
        localSession.setUserType(user.getUserType());
        localSession.setBasic(basic);
        localSession.setExtData(session.getUserInfoVersion());
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

    private List<Map<String, String>> getOpenMsgsMaps(List<PrmUserOpenEntity> tokenList) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (PrmUserOpenEntity item : tokenList) {
            Map<String, String> maps = new HashMap<>(10);
            maps.put(OpenMsgConstant.ACCESSTOKEN, item.getAccessToken());
            maps.put(OpenMsgConstant.EXPIRESIN, item.getExpiresIn().toString());
            maps.put(OpenMsgConstant.OPENID, item.getOpenCode());
            maps.put(OpenMsgConstant.OPENTYPE, item.getType().toString());
            maps.put(OpenMsgConstant.REFRESHTOKEN, item.getRefreshToken());
            maps.put(OpenMsgConstant.SCOPE, item.getScope());
            maps.put(OpenMsgConstant.UNIONCODE, item.getUnionCode());
            maps.put(OpenMsgConstant.EXTDATA, item.getExtData());
        }
        return mapList;
    }

    private Map<String, String> getUserMsgsMaps(PrmUserEntity user) {
        Map<String, String> userMsgs = new HashMap<>(50);
        String code = StringUtils.isBlank(user.getJobNo()) ? "" : user.getJobNo();
        userMsgs.put(UserMsgConstant.CODE, code);

        String userName = StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : "";
        userMsgs.put(UserMsgConstant.USERNAME, userName);

        String nickName = StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : "";
        userMsgs.put(UserMsgConstant.NICKNAME, nickName);

        String cnName = StringUtils.isNotBlank(user.getCnName()) ? user.getCnName() : "";
        userMsgs.put(UserMsgConstant.CNNAME, cnName);

        String cnGroupName = StringUtils.isNotBlank(user.getCnName()) ? user.getCnName() : "";
        userMsgs.put(UserMsgConstant.CNGROUPNAME, cnGroupName);

        String enName = StringUtils.isNotBlank(user.getEnName()) ? user.getEnName() : "";
        userMsgs.put(UserMsgConstant.ENNAME, enName);

        String enGroupName = StringUtils.isNotBlank(user.getEnGroupName()) ? user.getEnGroupName() : "";
        userMsgs.put(UserMsgConstant.ENGROUPNAME, enGroupName);

        String fullName = user.getNickName() + "/" + user.getUserName();
        userMsgs.put(UserMsgConstant.FULLNAME, fullName);

        String phone = StringUtils.isNotBlank(user.getPhone()) ? user.getPhone() : "";
        userMsgs.put(UserMsgConstant.PHONE, phone);

        String email = StringUtils.isNotBlank(user.getEmail()) ? user.getEmail() : "";
        userMsgs.put(UserMsgConstant.EMAIL, email);

        String idCard = StringUtils.isNotBlank(user.getIdCard()) ? user.getIdCard() : "";
        userMsgs.put(UserMsgConstant.IDCARD, idCard);

        String groupName = StringUtils.isNotBlank(user.getGroupName()) ? user.getGroupName() : "";
        userMsgs.put(UserMsgConstant.GROUPNAME, groupName);

        String no = StringUtils.isNotBlank(user.getNo()) ? user.getNo() : "";
        userMsgs.put(UserMsgConstant.NO, no);

        userMsgs.put(UserMsgConstant.PASSEXPIREDT, DateUtils.format(user.getPassExpireDt(), DateUtils.DATE_TIME_PATTERN));

        userMsgs.put(UserMsgConstant.CREATEDT, DateUtils.format(user.getCreationDate(), DateUtils.DATE_TIME_PATTERN));

        userMsgs.put(UserMsgConstant.FROZENDT, DateUtils.format(user.getFrozenDt(), DateUtils.DATE_TIME_PATTERN));

        userMsgs.put(UserMsgConstant.ISACTIVE, user.getIsActive().toString());

        userMsgs.put(UserMsgConstant.ISEDIT, user.getIsEdit().toString());

        String remark = StringUtils.isNotBlank(user.getRemark()) ? user.getRemark() : "";
        userMsgs.put(UserMsgConstant.REMARK, remark);

        String departmentCode = StringUtils.isNotBlank(user.getDepartmentCode()) ? user.getDepartmentCode() : "";
        userMsgs.put(UserMsgConstant.DEPARTMENTCODE, departmentCode);

        userMsgs.put(UserMsgConstant.STATUS, user.getStatus().toString());

        userMsgs.put(UserMsgConstant.USERTYPE, user.getUserType().toString());

        String expiredDt = user.getExpiredDt() != null ? DateUtils.format(user.getExpiredDt(), DateUtils.DATE_TIME_PATTERN) : "";
        userMsgs.put(UserMsgConstant.EXPIREDDT, expiredDt);
        return userMsgs;
    }

    private UserData getUserData(PrmUserEntity user) {
        UserData data = new UserData();
        data.setId(user.getId().toString());
        data.setCode(user.getJobNo());
        data.setEmail(user.getEmail());
        data.setFrozenDt(user.getFrozenDt());
        data.setIsEdit(user.getIsEdit());
        data.setUserName(user.getUserName());
        data.setCreateDt(user.getCreationDate());
        data.setStatus(user.getStatus());
        data.setUserType(user.getUserType());
        data.setPhone(user.getPhone());
        data.setEnGroupName(user.getEnGroupName());
        data.setCnName(user.getCnName());
        data.setCnGroupName(user.getCnGroupName());
        data.setIdCard(user.getIdCard());
        data.setIsActive(user.getIsActive());
        data.setNickName(user.getNickName());
        data.setNo(user.getNo());
        data.setRemark(user.getRemark());
        data.setDepartmentCode(user.getDepartmentCode());
        data.setDepartmentName(user.getDepartmentName());
        return data;
    }

    private AllSession iniSession(Date nowTime, PrmUserEntity user, PrmSessionEntity session, List<PrmUserOpenEntity> tokens, Function<Map, Void> fun) {
        String nowTimeString = DateUtils.format(nowTime, DateUtils.DATE_TIME_PATTERN_T);
        PrmLocalSession localSession = iniLocalSession(user, session, tokens);
        RemoteSession remoteSession = new RemoteSession();
        remoteSession.setId(session.getId().toString());
        remoteSession.setKey(user.getId().toString());
        remoteSession.setTime(nowTimeString);
        remoteSession.setLocked(session.getStatus() == 0);
        if (localSession != null) {
            remoteSession.setLocalMd5(localSession.getMd5());
        }
        try {
            boolean isExpire = false;
            //判定用户是否开启密码强制过期策略
            boolean isActiveExpire = false;
            int expireMonth = 0;
            isActiveExpire = Boolean.parseBoolean(sysConfigurationService.getConfiguration(Constant.IS_ACTIVE_EXPIRE, Constant.USER_PASS_STRATEGY));
            expireMonth = Integer.parseInt(sysConfigurationService.getConfiguration(Constant.EXPIRE_MONTH, Constant.USER_PASS_STRATEGY));
            if (!Constant.SYSTEM_ADMINROLE.equals(user.getUserName())
                    || !Constant.SYSTEM_MANAGER.equals(user.getUserName())) {
                if (isActiveExpire && new Date().compareTo(DateUtils.addDateMonths(user.getPassExpireDt(), expireMonth)) > 0) {
                    isExpire = true;
                }
            }

            List<PrmData> prmList = BasicExtensions.getPrm(localSession, null);
            List<String> userPermission = prmList.stream().map(PrmData::getCode).collect(Collectors.toList());
            OnlineUserDTO onlineUserInfo = new OnlineUserDTO();
            onlineUserInfo.setId(user.getId()); //TODO
            onlineUserInfo.setFullName(user.getNickName() + "/" + user.getUserName());
            onlineUserInfo.setUserName(user.getUserName());
            onlineUserInfo.setNickName(user.getNickName());
            onlineUserInfo.setCode(user.getJobNo());
            onlineUserInfo.setDpartmentName(user.getDepartmentName());
            onlineUserInfo.setDepartmentCode(user.getDepartmentCode());
            onlineUserInfo.setLoginName(user.getNickName());
            List<String> rolesList = prmRoleService.getByIds(user.getRoles()).stream().map(PrmRoleEntity::getRoleName).collect(Collectors.toList());
            onlineUserInfo.setRoles(rolesList);
            onlineUserInfo.setPermissions(userPermission);
            onlineUserInfo.setLoginTime(new Date());
            onlineUserInfo.setLanguage(session.getText());
            String token = session.getId().toString() + session.getType();
            onlineUserInfo.setUserToken(Signature.encryptToken(token));
            onlineUserInfo.setIsExpire(isExpire);
            onlineUserInfo.setDomain(identityHelper.getDomain());
            onlineUserInfo.setExtData(session.getUserInfoVersion());

            CookieSession cookieSession = new CookieSession();
            cookieSession.setSessionGuid(session.getId().toString());
            cookieSession.setRememberType(session.getType());

            LocalDync localDyncs = new LocalDync();
            localDyncs.setCookieSession(cookieSession);
            localDyncs.setUserInfo(onlineUserInfo);
            localDyncs.setResult(true);
            // 缓存
            Map map = new HashMap(5);
            map.put("remoteSession", remoteSession);
            map.put("localDyncs", localDyncs);
            fun.apply(map);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        CookieSession cookieSession = new CookieSession();
        cookieSession.setSessionGuid(session.getId().toString());
        cookieSession.setRememberType(session.getType());

        AllSession result = new AllSession();
        result.setCookieData(cookieSession);
        result.setLocalData(localSession);
        result.setRemoteData(remoteSession);
        return result;
    }

    private AllSession doMemeberLogin(PrmUserEntity user, int rememberType, SessionEntity sessionEntity) {
        Integer sdStatus = 2;
        if (user == null) {
            throw new InkelinkException("用户名不存在或密码错误");
        }
        if (!user.getIsActive()) {
            throw new InkelinkException("用户未激活");
        }
        if (user.getStatus().equals(sdStatus)) {
            throw new InkelinkException("用户锁定中,请联系管理员解锁");
        }
        if (user.getFrozenDt().compareTo(new Date()) > 0) {
            throw new InkelinkException("用户冻结中请稍后再试");
        }
        if (user.getExpiredDt() != null && user.getExpiredDt().compareTo(new Date()) < 0) {
            throw new InkelinkException("用户已经过期，请联系管理员修改过期时间");
        }
        int sessionType = rememberType;
        Date nowTime = DateUtils.stringToDate(sessionEntity.getNowTime(), DateUtils.DATE_TIME_PATTERN_M);
        //TODO 临时方案，和.net不同库互通
        PrmSessionEntity session = prmSessionService.get(sessionId);
        if (session == null) {
            session = new PrmSessionEntity();
            session.setId(IdGenerator.getId()); //sessionId
            session.setPrcPrmUserId(user.getId());
            session.setUserType(user.getUserType());
            session.setUserName(user.getUserName());
            session.setText(user.getNickName());
            session.setApp(sessionEntity.getApp());
            session.setIp(sessionEntity.getUserHostAddress());
            session.setPort(sessionEntity.getPort());
            session.setActiveCount(1);
            session.setActiveDt(nowTime);
            session.setLoginDt(nowTime);
            session.setType(sessionType);
            session.setExpireDt(DateUtils.addDateHours(nowTime, sessionType));
            session.setStatus(1);
            session.setUserInfoVersion(sessionEntity.getExtData());
            prmSessionService.insert(session);
        }

        List<PrmUserOpenEntity> tokens = prmUserOpenService.getTokens(user.getId());

        PrmDepartmentEntity dep = prmDepartmentJoinUserService.getPrmDepartmentByUserId(user.getId());
        if (dep != null) {
            user.setDepartmentName(dep.getDepartmentName());
            user.setDepartmentId(dep.getId().toString());
            user.setDepartmentCode(dep.getDepartmentCode());
        }
        PrmSessionEntity finalSession = session;
        Function<Map, Void> funcCache = (map) -> {
            RemoteSession remoteSession = (RemoteSession) map.get("remoteSession");
            LocalDync localDyncinfo = (LocalDync) map.get("localDyncs");
            boolean isCompatible = Boolean.parseBoolean(SpringContextUtils.getConfiguration("EQuality:Prm:IsCompatible", "false"));
            if (isCompatible) {
                redisUtils.set(appKey + finalSession.getId() + "{User}", localDyncinfo, 3600);
            }
            String rmoteJson = JsonUtils.toJsonString(remoteSession);
            if (StringUtils.isBlank(rmoteJson)) {
                rmoteJson = "{}";
            }
            redisUtils.set(redisUtils.getRemoteSessionKey(remoteSession.getId()), rmoteJson, 3600);
            return null;
        };
        return iniSession(nowTime, user, session, tokens, funcCache);
    }
}
