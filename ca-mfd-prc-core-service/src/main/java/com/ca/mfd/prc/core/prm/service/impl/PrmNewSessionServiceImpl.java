package com.ca.mfd.prc.core.prm.service.impl;

import cn.hutool.json.JSONObject;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.entity.AllSession;
import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.entity.MiddleResponse;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.PrmData;
import com.ca.mfd.prc.common.entity.PrmLocalSession;
import com.ca.mfd.prc.common.entity.PrmUserDto;
import com.ca.mfd.prc.common.entity.RoleData;
import com.ca.mfd.prc.common.entity.SessionEntity;
import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.enums.LocalSessionType;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.HttpContextUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.RestClientUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SystemUtils;
import com.ca.mfd.prc.core.prm.controller.SessionController;
import com.ca.mfd.prc.core.prm.dto.SessionDTO;
import com.ca.mfd.prc.core.prm.service.ISessionService;
import com.ca.mfd.prc.core.prm.service.Signature;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tom.wu
 */
@Service
public class PrmNewSessionServiceImpl implements ISessionService {
    private static final Logger logger = LoggerFactory.getLogger(PrmSessionHolderImpl.class);
    @Autowired
    ISysConfigurationService sysConfigurationService;
    @Autowired
    private RestClientUtils restClientUtils;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private SessionController sessionController;

    @Value("${inkelink.coreaddress:ca-mfd-prc-core-service}")
    private String coreServiceDomainKey;

    /**
     * 用户LocalSession信息获取并将相关公共内容同步memcache缓存
     *
     * @param sessionId
     * @return 本地session
     */
    @Override
    public LocalSession getLocalSession(String sessionId) {
        try {
            ResultVO<PrmLocalSession> resp = sessionController.getLocalSession(sessionId);
            PrmLocalSession local = resp.getData();
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            LocalSession result = iniSession(local, null, "");
            return result;
        } catch (InkelinkException ie) {
            logger.info("GetLocalSession:" + ie.getMessage());
            throw new InkelinkException("业务异常:GetLocalSession:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private <T> List<T> parseResultList(MiddleResponse resp, Class<T> clazz) {
        ResultVO<String> requestModel = parseResult(resp);
        String responseStr = requestModel.getData();
        if (StringUtils.isBlank(responseStr)) {
            return null;
        }
        return JsonUtils.parseArray(responseStr, clazz);
    }

    private <T> T parseResultEntity(MiddleResponse resp, Class<T> clazz) {
        ResultVO<String> requestModel = parseResult(resp);
        String responseStr = requestModel.getData();
        if (StringUtils.isBlank(responseStr)) {
            return null;
        }
        return JsonUtils.parseObject(responseStr, clazz);
    }

    private ResultVO<String> parseResult(MiddleResponse resp) {
        ResultVO<String> requestModel = new ResultVO<>();
        if (resp == null || !resp.getSuccess() || StringUtils.isBlank(resp.getBody())) {
            throw new InkelinkException("返回数据空。");
        }

        String responseStr = resp.getBody();
        JSONObject response = JsonUtils.parseObject(responseStr, JSONObject.class);
        requestModel.setCode(response.getInt("code"));
        String datestr = "";
        String dataStr = "Data";
        String dataStr1 = "data";
        if (response.get(dataStr) != null) {
            datestr = response.get("Data").toString();
        } else if (response.get(dataStr1) != null) {
            datestr = response.get("data").toString();
        }
        requestModel.setMessage(response.getStr("message", ""));
        requestModel.setData(datestr);
        if (!requestModel.getSuccess()) {
            if (StringUtils.isBlank(requestModel.getMessage())) {
                throw new InkelinkException("调用接口返回失败。");
            }
            throw new InkelinkException(requestModel.getMessage());
        }
        return requestModel;
    }

    /**
     * 登录
     *
     * @param userName
     * @param pwd
     * @param loginStyle
     * @param rememberType
     * @param extData
     * @param sessionEntity
     * @param userType      = 0
     * @return 本地session
     */
    @Override
    public LocalSession memberLoginAsync(String userName, String pwd, int loginStyle, int rememberType, String extData, SessionEntity sessionEntity, int userType) throws Exception {
        if (StringUtils.isBlank(userName)) {
            throw new InkelinkException("用户名不为空");
        }

        if (StringUtils.isBlank(pwd)) {
            throw new InkelinkException("密码不为空");
        }
        try {
            SessionDTO.MemberLoginModel model = new SessionDTO.MemberLoginModel();
            model.setLoginStyle(loginStyle);
            model.setRememberType(rememberType);
            model.setSessionEntity(sessionEntity);
            model.setUserName(userName);
            model.setPwd(pwd);
            model.setUserType(userType);
            ResultVO<AllSession> resp = sessionController.memberLogin(model);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            AllSession resData = resp.getData();
            LocalSession result = iniSession(resData.getLocalData(), sessionEntity.getNowTime(), extData);
            return result;
        } catch (InkelinkException ie) {
            logger.info("memberLoginAsync:" + ie.getMessage());
            throw new InkelinkException(ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:memberLoginAsync:" + ex.getMessage(), ex);
        }
    }

    /**
     * 自动登录 并同步到异步缓存中去2: string sessionId,
     *
     * @param cookieSession
     * @param userHostAddress
     * @return 本地session
     */
    @Override
    public LocalSession memberAutoLoginAsync(CookieSession cookieSession, String userHostAddress) throws Exception {
        try {
            SessionDTO.MemberAutoLoginModel model = new SessionDTO.MemberAutoLoginModel();
            model.setCookieSession(cookieSession);
            model.setUserHostAddress(userHostAddress);
            ResultVO<AllSession> resp = sessionController.memberAutoLogin(model);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            AllSession resData = resp.getData();

            LocalSession result = iniSession(resData.getLocalData(), null, "");
            return result;
        } catch (InkelinkException ie) {
            logger.info("memberAutoLoginAsync:" + ie.getMessage());
            throw new InkelinkException("业务异常:memberAutoLoginAsync:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:memberAutoLoginAsync:" + ex.getMessage(), ex);
        }
    }

    /**
     * 登出
     *
     * @param cookieSession
     * @param status
     * @return 成功
     */
    @Override
    public Boolean memberLogOut(CookieSession cookieSession, int status) throws Exception {
        try {
            SessionDTO.MemberLogOutModel model = new SessionDTO.MemberLogOutModel();
            model.setCookieSession(cookieSession);
            model.setStatus(status);
            ResultVO<Boolean> resp = sessionController.memberLogOut(model);
            return resp.getSuccess();

        } catch (InkelinkException ie) {
            logger.info("memberLogOut:" + ie.getMessage());
            throw new InkelinkException("业务异常:memberLogOut:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:memberLogOut:" + ex.getMessage(), ex);
        }
    }

    /**
     * 添加用户
     *
     * @param userDatas 添加用户
     * @return 成功
     */
    @Override
    public Boolean prmAddUsersByRequest(List<PrmUserDto> userDatas) {
        return false;
    }


    /**
     * 刷新会话
     *
     * @param userId
     * @return 是否成功
     */
    @Override
    public Boolean refreshSessionAsync(String userId) {
        return true;
    }

    /**
     * 仓储所有失效的会话
     *
     * @return 成功
     */
    @Override
    public Boolean sessionStoreAll() {
        return true;
    }

    /**
     * 仓储所有选中的会话
     *
     * @param ids
     * @return 成功
     */
    @Override
    public Boolean sessionStoreSelected(List<String> ids) {
        return true;
    }

    /**
     * UA专用接口用于刷新TOKEN
     *
     * @param id
     * @param accessToken
     * @param refreshToken
     * @return 用户信息
     */
    @Override
    public Boolean updateUaToken(String id, String accessToken, String refreshToken) {
        return true;
    }

    /**
     * 获取所有用户信息
     *
     * @return 用户信息
     */
    @Override
    public List<OnlineUserDTO> getUsers() throws Exception {
        try {
            ResultVO<List<UserData>> resp = sessionController.getUsers();
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            List<UserData> respDatas = resp.getData();
            if (respDatas == null) {
                respDatas = new ArrayList<>();
            }
            return respDatas.stream().map(c -> userToDto(c)).collect(Collectors.toList());
        } catch (InkelinkException ie) {
            logger.info("GetUsers:" + ie.getMessage());
            throw new InkelinkException("业务异常:GetUsers:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:GetUsers:" + ex.getMessage(), ex);
        }
    }

    private OnlineUserDTO userToDto(UserData c) {
        OnlineUserDTO et = new OnlineUserDTO();
        //et.setId(c.getId()); //TODO
        et.setFullName(c.getFullName());
        et.setUserName(c.getUserName());
        et.setNickName(c.getNickName());
        et.setCode(c.getCode());
        et.setLoginName(c.getNickName());
        et.setLoginTime(new Date());
        et.setEmail(c.getEmail());
        et.setPhone(c.getPhone());
        et.setUserType(c.getUserType());
        et.setGroupName(c.getGroupName());
        et.setDpartmentName(c.getDepartmentName());
        et.setDepartmentCode(c.getDepartmentCode());
        return et;
    }

    /**
     * 所有当前用户信息
     *
     * @param ids
     * @return 用户信息
     */
    @Override
    public List<OnlineUserDTO> getUsersByArrayId(List<String> ids) throws Exception {
        try {
            IdsModel model = new IdsModel();
            model.setIds(ids.toArray(new String[0]));
            ResultVO<List<UserData>> resp = sessionController.getUsersByArrayId(new IdsModel());
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            List<UserData> respDatas = resp.getData();
            if (respDatas == null) {
                respDatas = new ArrayList<>();
            }
            return respDatas.stream().map(c -> userToDto(c)).collect(Collectors.toList());

        } catch (InkelinkException ie) {
            logger.info("getUsersByArrayId:" + ie.getMessage());
            throw new InkelinkException("业务异常:getUsersByArrayId:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getUsersByArrayId:" + ex.getMessage(), ex);
        }
    }

    /**
     * 所有当前用户信息
     *
     * @param ids
     * @return 用户信息
     */
    @Override
    public List<OnlineUserDTO> getUsersByRoleId(List<String> ids) throws Exception {
        try {
            IdsModel model = new IdsModel();
            model.setIds(ids.toArray(new String[0]));
            ResultVO<List<UserData>> resp = sessionController.getUsersByRoleId(new IdsModel());
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            List<UserData> respDatas = resp.getData();
            if (respDatas == null) {
                respDatas = new ArrayList<>();
            }
            return respDatas.stream().map(c -> userToDto(c)).collect(Collectors.toList());
        } catch (InkelinkException ie) {
            logger.info("getUsersByRoleId:" + ie.getMessage());
            throw new InkelinkException("业务异常:getUsersByRoleId:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getUsersByRoleId:" + ex.getMessage(), ex);
        }
    }

    /**
     * 获取所有用户信息
     *
     * @param id
     * @return 用户信息
     */
    @Override
    public OnlineUserDTO getUserById(String id) throws Exception {
        try {
            ResultVO<UserData> resp = sessionController.getUser(id);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            UserData respDatas = resp.getData();
            return userToDto(respDatas);
        } catch (InkelinkException ie) {
            logger.info("getUserById:" + ie.getMessage());
            throw new InkelinkException("业务异常:getUserById:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getUserById:" + ex.getMessage(), ex);
        }
    }

    /**
     * 获取用户信息
     *
     * @param codeOrUserName
     * @return 用户信息
     */
    @Override
    public OnlineUserDTO getUserByCode(String codeOrUserName) throws Exception {
        try {
            ResultVO<UserData> resp = sessionController.getUserByCodeorUserName(codeOrUserName);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            UserData respDatas = resp.getData();
            return userToDto(respDatas);

        } catch (InkelinkException ie) {
            logger.info("getUserByCode:" + ie.getMessage());
            throw new InkelinkException("业务异常:getUserByCode:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getUserByCode:" + ex.getMessage(), ex);
        }
    }

    /**
     * 根基部门id获取用户信息
     *
     * @param ids
     * @return 用户信息
     */
    @Override
    public List<OnlineUserDTO> getDepartmentByUser(List<String> ids) throws Exception {
        try {
            IdsModel model = new IdsModel();
            model.setIds(ids.toArray(new String[0]));
            ResultVO<List<UserData>> resp = sessionController.prmgetUsersByDepartmentIds(model);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            List<UserData> respDatas = resp.getData();
            if (respDatas == null) {
                respDatas = new ArrayList<>();
            }
            return respDatas.stream().map(c -> userToDto(c)).collect(Collectors.toList());
        } catch (InkelinkException ie) {
            logger.info("getDepartmentByUser:" + ie.getMessage());
            throw new InkelinkException("业务异常:getDepartmentByUser:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getDepartmentByUser:" + ex.getMessage(), ex);
        }
    }

    /**
     * 获取用户信息
     *
     * @param ids
     * @return 用户信息
     */
    @Override
    public List<OnlineUserDTO> getUserByDepartmentIds(List<String> ids) throws Exception {
        try {
            IdsModel model = new IdsModel();
            model.setIds(ids.toArray(new String[0]));
            ResultVO<List<UserData>> resp = sessionController.prmgetUsersByDepartmentIds(model);
            if (!resp.getSuccess()) {
                throw new InkelinkException(resp.getMessage());
            }
            List<UserData> respDatas = resp.getData();
            if (respDatas == null) {
                respDatas = new ArrayList<>();
            }
            return respDatas.stream().map(c -> userToDto(c)).collect(Collectors.toList());
        } catch (InkelinkException ie) {
            logger.info("GetUserByDepartmentIds:" + ie.getMessage());
            throw new InkelinkException("业务异常:GetUserByDepartmentIds:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:GetUserByDepartmentIds:" + ex.getMessage(), ex);
        }
    }

    /**
     * 获取用户信息
     *
     * @param base64user
     * @return 用户信息
     */
    @Override
    public OnlineUserDTO getUsersByBase64Request(String base64user) throws Exception {
        try {
            return null;
        } catch (InkelinkException ie) {
            logger.info("getUsersByBase64Request:" + ie.getMessage());
            throw new InkelinkException("业务异常:getUsersByBase64Request:" + ie.getMessage(), ie);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("网络异常:getUsersByBase64Request:" + ex.getMessage(), ex);
        }
    }

    /**
     * 组装用户数据
     */
    private LocalSession iniSession(PrmLocalSession local, String nowTimeStr, String extData) {
        if (extData == null) {
            extData = "";
        }
        //会话初始化
        UserData user = local.getUser(null);
        List<RoleData> role = local.getRole(new ArrayList<>());
        List<PrmData> prm = local.getPrm(new ArrayList<>());
        List<String> roles = role.stream().map(c -> c.getName()).collect(Collectors.toList());
        List<String> prms = prm.stream().map(c -> c.getCode()).collect(Collectors.toList());
        Boolean isExpire = false;
        //判定用户是否开启密码强制过期策略
        Boolean isActiveExpire = false;
        int expireMonth = 0;
        isActiveExpire = Boolean.valueOf(sysConfigurationService.getConfiguration(Constant.IS_ACTIVE_EXPIRE, Constant.USER_PASS_STRATEGY));
        expireMonth = Integer.parseInt(sysConfigurationService.getConfiguration(Constant.EXPIRE_MONTH, Constant.USER_PASS_STRATEGY));

        if (!StringUtils.equalsIgnoreCase(user.getUserName(), Constant.SYSTEM_ADMINROLE)
                && !StringUtils.equalsIgnoreCase(user.getUserName(), Constant.SYSTEM_MANAGER)
                && isActiveExpire
                && System.currentTimeMillis() > DateUtils.addDateMonths(user.getPassExpireDt(), expireMonth).getTime()) {
            //如果当前时间
            isExpire = true;
        }

        Date nowTime = StringUtils.isBlank(nowTimeStr) ? new Date() : DateUtils.parse(nowTimeStr, DateUtils.DATE_TIME_PATTERN_M);
        extData = StringUtils.isBlank(extData) ? local.getExtData() : extData;

        String lang = SystemUtils.getCountry();
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String acceptlanguage = request.getHeader("Accept-Language");
        if (!StringUtils.isBlank(acceptlanguage)) {
            //TODO 多语言筛选
            lang = acceptlanguage.trim().toUpperCase();
        }
        OnlineUserDTO onlineUserInfo = new OnlineUserDTO();
        onlineUserInfo.setId(ConvertUtils.stringToLong(user.getId()));
        onlineUserInfo.setUserType(user.getUserType());
        onlineUserInfo.setFullName(user.getFullName());
        onlineUserInfo.setUserName(user.getUserName());
        onlineUserInfo.setNickName(user.getNickName());
        onlineUserInfo.setCode(user.getCode());
        onlineUserInfo.setLoginName(user.getNickName());
        onlineUserInfo.setDepartmentCode(user.getDepartmentCode());
        onlineUserInfo.setDpartmentName(user.getDepartmentName());
        onlineUserInfo.setRoles(roles);
        onlineUserInfo.setPermissions(prms);
        onlineUserInfo.setLoginTime(new Date());
        onlineUserInfo.setLanguage(lang);
        onlineUserInfo.setUserToken(Signature.encryptToken(local.getSessionId() + "+" + local.getRememberType()));
        onlineUserInfo.setIsExpire(isExpire);
        String domain = "";
        onlineUserInfo.setDomain(domain == null ? "" : domain);
        onlineUserInfo.setExtData(extData);
        onlineUserInfo.setEmail(user.getEmail());
        onlineUserInfo.setPhone(user.getPhone());
        onlineUserInfo.setGroupName(user.getGroupName());


        CookieSession cookieSession = new CookieSession();
        cookieSession.setRememberType(local.getRememberType());
        cookieSession.setSessionGuid(local.getSessionId());
        LocalSession result = new LocalSession();
        result.setSessionType(LocalSessionType.User);
        result.setCookieSession(cookieSession);
        result.setUserInfo(onlineUserInfo);
        result.setApiMd5(local.getApiMd5());
        result.setResult(true);

        local.setLocal(result);
        //本地缓存处理
        localCache.addObject("PrmLocalSession_" + local.getId(), local);
        return result;
    }
}