package com.ca.mfd.prc.core.prm.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.core.MenuItemsPremissmsInfo;
import com.ca.mfd.prc.common.dto.core.OauthLoginInfo;
import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.GraphicCodeDTO;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.entity.OauthLoginDTO;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.SessionEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.HttpContextUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.integrated.service.TokenService;
import com.ca.mfd.prc.core.integrated.service.UserService;
import com.ca.mfd.prc.core.prm.dto.AccountDTO;
import com.ca.mfd.prc.core.prm.entity.PrmAuthorizeEntity;
import com.ca.mfd.prc.core.prm.enums.ERememberType;
import com.ca.mfd.prc.core.prm.service.IPrmAuthorizeService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.prm.utils.LoginLockVerificationUtils;
import com.ca.mfd.prc.core.prm.service.ISessionHolder;
import com.ca.mfd.prc.core.prm.service.ISessionService;
import com.ca.mfd.prc.core.main.service.ISysMenuService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * 角色
 *
 * @author inkelink ${email}
 * @date 2023-07-24
 */
@RestController
@RequestMapping("member/account")
@Tag(name = "角色")
public class AccountController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private static final String whiteKey = "WhiteAccessAuthorization";
    @Autowired
    private IPrmUserService prmUserService;
    @Autowired
    private IPrmAuthorizeService prmAuthorizeService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISessionService sessionService;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private ISessionHolder sessionHolder;
    @Autowired
    private RedisUtils redisUtils;
    @Value("${inkelink.gateway.isOpenCaTokenCheck:false}")
    private boolean isOpenCaTokenCheck;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    public AccountController() throws NoSuchAlgorithmException {
    }

    /**
     * 登陆 验证码的开启 默认是false
     * 系统配置需要添加如下配置
     * 【WhiteAccessAuthorization 分类】
     * 【key-》 IsOpenWhite， true】
     * 【key IsOpenWhiteIp  192。。。1,192。。。2  白名单IP】
     */
    @PostMapping(value = "login")
    @Operation(summary = "登陆 验证码的开启 默认是false ")
    //@Async
    public ResultVO login(@RequestBody AccountDTO.LogOnModel model, HttpServletRequest request) throws Exception {
        ///验证是否处于锁定状态
        LoginLockVerificationUtils.verificationisLock(model.getUserName());
        model.setApp(Constant.EQUALITY);
        int rememberType = model.getRememberMe() ? ERememberType.OneMonth.value() : ERememberType.Normal.value();
        //解密(为了兼容明文和加密两种模式
        try {
            model.setUserName(EncryptionUtils.decryptByAesForCryptoJs(model.getUserName()));
            model.setPassword(EncryptionUtils.decryptByAesForCryptoJs(model.getPassword()));
        } catch (Exception e) {
            logger.error("参加加密失败", e);
            //throw new InkelinkException("参加加密失败");
        }
        try {
            if (!isOpenCaTokenCheck) {
                String ip = IpUtils.getIpAddr(request);
                String token = identityHelper.getHeaderToken(request);
                //token = "NCtmMTJkNGM2ZjY5ZGYtMDExOC0zM2U0LWIwODYtZjNiZjJiNDE="; //TODO test
                ResultVO result = loginSign(token, ip, model.getApp(), model.getUserName(),
                        model.getPassword(), model.getLoginStyle().value(),
                        rememberType, model.getUserType(), model.getExtData());
                return result;
            } else {
                //统一门户授权登录
                OauthLoginInfo oauthLoginInfo = new OauthLoginInfo();
                oauthLoginInfo.setUsername(model.getUserName());
                oauthLoginInfo.setPassword(model.getPassword().trim());
                oauthLoginInfo.setCaptchaCode(model.getVerificationCode());
                oauthLoginInfo.setCaptchaToken(model.getHasCode());
                ResultVO oauthLogin = userService.oauthLogin(oauthLoginInfo);
                if (oauthLogin.getData() == null) {
                    return oauthLogin;
                }
                OauthLoginDTO oauthLoginDTO = (OauthLoginDTO) oauthLogin.getData();
                //根据token获取用户信息
                ResultVO result = new ResultVO();
                OnlineUserDTO userInfo = tokenService.getUserInfo(oauthLoginDTO.getAccess_token(), oauthLoginDTO.getRefresh_token());
                if (userInfo == null) {
                    result.setCode(-1);
                    result.setMessage("统一门户登录授权：获取用户信息失败：");
                    return result;
                }
                result.setMessage("登录成功");
                result.ok(userInfo);
                return result;
            }
        } catch (Exception exe) {
            ResultVO result = new ResultVO();
            result.setCode(110);
            Map mp = new LinkedHashMap();
            mp.put("hasCode", model.getHasCode());
            mp.put("content", getverfyCode(model.getHasCode()));
            result.setData(mp);
            result.setMessage(exe.getMessage());
            return result;
        }
    }

    //@Async
    protected ResultVO loginSign(String token, String ip, String app, String userName, String pwd, Integer loginStyle, Integer rememberType, Integer userType, String extData) {
        if (loginStyle == null) {
            loginStyle = 1;
        }
        if (rememberType == null) {
            rememberType = ERememberType.Normal.value();
        }
        if (userType == null) {
            userType = 0;
        }
        if (extData == null) {
            extData = "";
        }

        try {
            LocalSession localSession = sessionHolder.initalSession(token, ip);
            //如果当前有用户登录，先登出该用户
            if (localSession != null) {
                this.memberLogOut(3, token, ip);
            }

            LocalSession all = loginSignProcess(app, userName, pwd, loginStyle, rememberType, userType, extData);

            if (all.getResult()) {
                OnlineUserDTO userInfo = all.getUserInfo();
                localCache.removeObject("USER_PERMISSIONS_" + userInfo.getId());
                Integer newuserType = userInfo.getUserType();
                String menuName = SpringContextUtils.getConfiguration("User:SysMenu" + newuserType, "AdminMenu");
                String defaultPwd = SpringContextUtils.getConfiguration("User:Pwd", "123456");
                ResultVO<Map> result = new ResultVO<>();
                result.setMessage("登录成功");
                Map mp = new HashMap(50);
                mp.put("token", userInfo.getUserToken());
                mp.put("id", userInfo.getId().toString());
                mp.put("code", userInfo.getCode());
                mp.put("fullName", userInfo.getFullName());
                mp.put("userName", userInfo.getUserName());
                mp.put("isExpire", userInfo.getIsExpire());
                mp.put("loginTime", DateUtils.format(userInfo.getLoginTime(), DateUtils.DATE_TIME_PATTERN));
                mp.put("nickName", userInfo.getNickName());
                mp.put("permissions", userInfo.getPermissions());
                mp.put("extData", userInfo.getExtData());
                mp.put("productCode", userInfo.getProductCode());
                mp.put("groupName", userInfo.getGroupName());

                mp.put("departmentCode", userInfo.getDepartmentCode());
                mp.put("departmentName", userInfo.getDpartmentName());
                mp.put("isDefaultPwd", StringUtils.equalsIgnoreCase(defaultPwd, pwd));

                mp.put("menuPermissions", getAbstractDynamic());
                mp.put("ip", ip);
                mp.put("menuName", menuName);

                //licese未实现
                mp.put("isLicenseExpire", false);
                mp.put("licenseExpireDt", DateUtils.format(DateUtils.addDateYears(new Date(), 10), DateUtils.DATE_TIME_PATTERN));

                return result.ok(mp);
            } else if (!all.getResult()) {
                ResultVO result = new ResultVO();
                result.setCode(-1);
                result.setMessage("登录失败:" + all.getMeessage());
                return result;
            } else {
                logger.info("登录失败");
                ResultVO result = new ResultVO();
                result.setCode(-1);
                result.setMessage("系统繁忙,请稍后再试~~!");
                return result;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ResultVO result = new ResultVO();
            result.setCode(-1);
            result.setMessage(ex.getMessage());
            return result;
        }
    }

    @PostMapping(value = "getabstractdynamic")
    @Operation(summary = "获取菜单权限")
    public List<MenuItemsPremissmsInfo> getAbstractDynamic() {
        return sysMenuService.getMenuItemsPremissms();
    }


    void memberLogOut(int status, String token, String ip) throws Exception {
        LocalSession localsession = sessionHolder.initalSession(token, ip);
        //LocalSession localsession = User.GetAllSession();
        if (localsession == null) {
            throw new InkelinkException("用户已登出", 200);
        }
        CookieSession cookieSession = localsession.getCookieSession();
        if (cookieSession == null || StringUtils.isBlank(cookieSession.getSessionGuid())) {
            return;
        }
        sessionService.memberLogOut(cookieSession, status);
    }

    LocalSession loginSignProcess(String app, String userName, String pwd, int loginStyle, int rememberType, int userType, String extData) throws Exception {
        userName = userName.trim();
        pwd = pwd.trim();

        Date nowTime = new Date();
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String userAgent = request.getHeader("user-agent");
        String ip = IpUtils.getIpAddr(request);
        String remark = userAgent == null ? "[]" : userAgent;
        if (remark.length() > 1000) {
            remark = remark.substring(0, 1000);
        }

        SessionEntity entity = new SessionEntity();
        entity.setApp(app);
        entity.setRemark(remark);
        entity.setNowTime(DateUtils.format(nowTime, DateUtils.DATE_TIME_PATTERN_M));
        entity.setUserHostAddress(ip);
        entity.setPort(String.valueOf(request.getRemotePort()));
        entity.setExtData(extData);

        return sessionService.memberLoginAsync(userName, pwd, loginStyle, rememberType, extData, entity, userType);
    }

    private String getverfyCode(String hascode) {
        String ip = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
        String verfyCode = createVerifyCode();
        redisUtils.set(whiteKey + ":" + ip + ":" + hascode, verfyCode, 3 * 60);
        return getVerfyData(verfyCode);
    }

    String getVerfyData(String verfyCode) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try {
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(verfyCode.length() * 16, 27);
            lineCaptcha.createImage(verfyCode);
            lineCaptcha.write(byteOutput);
            byte[] bytes = byteOutput.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            logger.error("获取验证码失败", ex);
        } finally {
            try {
                byteOutput.close();
            } catch (Exception ecx) {
                logger.error("获取验证码失败", ecx);
            }
        }
        return null;
    }


    @GetMapping(value = "getverifycode")
    @Operation(summary = "默认获取验证码")
    //@Async
    public ResultVO getVerifyCode() {
        ResultVO<Map> result = new ResultVO<>();
        Map mp = new HashMap(10);
        String hascode;
        String pic;
        if (!isOpenCaTokenCheck) {
            hascode = UUIDUtils.getGuid();
            String ip = IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());
            String verfyCode = createVerifyCode();
            redisUtils.set(whiteKey + ":" + ip + ":" + hascode, verfyCode, 3 * 60);
            pic = getVerfyData(verfyCode);
            if (StringUtils.isBlank(pic)) {
                return result.error(-1, "生成验证码失败");
            }
        } else {
            GraphicCodeDTO codeDTO = userService.getGraphicCode();
            if (codeDTO == null) {
                return result.error(-1, "调用门户接口:获取验证码失败");
            }
            hascode = codeDTO.getCaptchaToken();
            pic = codeDTO.getCaptchaCode();
        }
        result.setMessage("获取成功");
        mp.put("hasCode", hascode);
        mp.put("content", pic);
        return result.ok(mp);
    }

    private Random rand = SecureRandom.getInstanceStrong();

    private String createVerifyCode() {
        String[] verifyString = new String[]{"1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "E", "F", "G", "H", "i", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            //int rd = rand.nextInt(33);
            int rd = (int) (Math.random() * 33);
            verifyBuilder.append(verifyString[rd]);
        }
        String verifyCode = verifyBuilder.toString();
        return verifyCode;
    }

    @GetMapping(value = "refreshverifycode")
    @Operation(summary = "验证码刷新")
    //@Async
    public ResultVO refVerifyCode(String hascode) {
        String pic = getverfyCode(hascode);
        ResultVO<Map> result = new ResultVO<>();
        Map mp = new HashMap(5);
        mp.put("hasCode", hascode);
        mp.put("content", pic);
        return result.ok(mp, "刷新成功");
    }

    @PostMapping(value = "authorize")
    @Operation(summary = "授权检查")
    public ResultVO authorize(@RequestBody AccountDTO.AuthorizeModel model) throws Exception {
        model.setApp(Constant.EQUALITY);
        OnlineUserDTO userInfo = new OnlineUserDTO();
        //公司MOM授权
        if (!isOpenCaTokenCheck) {
            Integer rememberType = ERememberType.Normal.value();
            LocalSession all = loginSignProcess(model.getApp(), model.getUserName(), model.getPassword(), model.getLoginStyle().value()
                    , rememberType, model.getUserType(), "");
            //var token = $"{all.CookieSession.SessionGuid}+{rememberType}".EncryptToken();
            if (all == null || !all.getResult()) {
                ResultVO result = new ResultVO();
                result.setCode(-1);
                result.setMessage(all.getMeessage());
                result.setData(new HashMap(5));
                return result;
            }
            userInfo = all.getUserInfo();

        } else {
            //统一门户授权登录
            OauthLoginInfo oauthLoginInfo = new OauthLoginInfo();
            oauthLoginInfo.setUsername(model.getUserName());
            oauthLoginInfo.setPassword(model.getPassword().trim());
            oauthLoginInfo.setCaptchaCode(model.getCaptchaCode());
            oauthLoginInfo.setCaptchaToken(model.getCaptchaToken());
            ResultVO resultVO = userService.oauthLogin(oauthLoginInfo);
            if (resultVO.getData() == null) {
                throw new InkelinkException(model.getUserName() + "统一门户授权：登录失败");
            }
            //根据token获取用户信息
            OauthLoginDTO oauthLoginDTO = (OauthLoginDTO) resultVO.getData();
            userInfo = tokenService.getUserInfo(oauthLoginDTO.getAccess_token(), oauthLoginDTO.getRefresh_token());
            if (userInfo == null) {
                throw new InkelinkException(model.getUserName() + "统一门户授权：获取用户信息失败");
            }
        }
        Boolean success = userInfo.getPermissions().contains(model.getPermission() == null ? "" : model.getPermission());
        if (StringUtils.isBlank(model.getPermission())) {
            success = true;
        }
        if (success) {
            //var userId = userInfo.UserName;
            //var updater = userInfo;
            String name = userInfo.getCode() + "/" + userInfo.getUserName() + "/" + userInfo.getNickName();
            PrmAuthorizeEntity auth = new PrmAuthorizeEntity();
            auth.setId(IdGenerator.getId());
            auth.setPrcPrmUserId(userInfo.getId());
            auth.setApp(model.getApp() == null ? "" : model.getApp());
            auth.setAuthorizeDt(new Date());
            auth.setCreationDate(new Date());
            auth.setCreatedBy(userInfo.getId());
            auth.setCreatedUser(userInfo.getUserName());
            auth.setIpAddress(model.getIpAddress() == null ? "" : model.getIpAddress());
            auth.setModule(model.getModule() == null ? "" : model.getModule());
            auth.setName(name);
            auth.setPermission(model.getPermission());
            auth.setLastUpdatedBy(userInfo.getId());
            auth.setLastUpdatedUser(userInfo.getUserName());
            auth.setLastUpdateDate(new Date());
            //auth.setUpdateUserId(userInfo.getId());
            prmAuthorizeService.insert(auth);
            prmAuthorizeService.saveChange();
        } else {
            throw new InkelinkException(model.getUserName() + "用户不具备该功能的授权能力");
        }
        ResultVO<Map> result = new ResultVO<>();
        Map mp = new HashMap(5);
        mp.put("userInfo", userInfo);
        if (success) {
            return result.ok(mp, "授权通过");
        } else {
            return result.error(-1, "授权驳回");
        }
    }

    @GetMapping(value = "logout")
    @Operation(summary = "登出")
    public ResultVO logOut() throws Exception {
        memberLogOut(2, "", "");
        ResultVO<String> result = new ResultVO<>();
        return result.ok("", "登出成功");
    }

    @GetMapping(value = "info")
    @Operation(summary = "已放弃该接口")
    public ResultVO info(HttpServletRequest request) throws Exception {
        ResultVO<Map> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        String ip = IpUtils.getIpAddr(request);
        String token = identityHelper.getHeaderToken(request);
        LocalSession session = sessionHolder.initalSession(token, ip);
        if (session == null) {
            return new ResultVO<String>().error(401, "没有登陆");
        }
        Map mp = new HashMap(5);
        mp.put("userInfo", session.getUserInfo());
        //mp.put("menu", sysMenuService.getMenuTree("AdminMenu"));
        //var session = User.GetAllSession();
        return result.ok(mp);
    }


}