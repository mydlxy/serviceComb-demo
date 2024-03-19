//package com.ca.common.utils;
//
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import Constant;
//import MenuNodeDTO;
//import OnlineUserDTO;
//import TokenUserDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Base64Utils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import static Constant.EMPTY_ID;
//
///**
// * 身份验证帮助类
// *
// * @author eric.zhou@hg2mes.com
// * @date 2023/4/6
// */
//@Component
//public class IdentityHeaderHelper {
//
//
////    @Autowired
////    private RestTemplateUtil restTemplateUtil;
////
//    /**
//     * 存储缓存（5分钟）
//     */
//    private Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5 * 60, TimeUnit.SECONDS).build();
//
////    /**
////     * 获取信息地址
////     */
////    @Value("${inkelink.prm.url:}")
////    private String prmUrl;
//
//
//    /**
//     * 获取domain
//     */
//    public String getDomain() {
//        OnlineUserDTO loginUsr = getLoginUser();
//        return loginUsr == null ? "" : loginUsr.getDomain();
//
//    }
//
//    /**
//     * 获取用户ID
//     *
//     * @return 返回用户ID
//     */
//    public String getUserId() {
//        OnlineUserDTO loginUsr = getLoginUser();
//        return loginUsr == null ? "" : loginUsr.getId();
//    }
//
//    /**
//     * 获取用户名
//     *
//     * @return 返回用户名
//     */
//    public String getUserName() {
//        OnlineUserDTO loginUsr = getLoginUser();
//        return loginUsr == null ? "" : loginUsr.getUserName();
//    }
//
//    /**
//     * 获取登录名
//     *
//     * @return 返回登录名
//     */
//    public String getLoginName() {
//        OnlineUserDTO loginUsr = getLoginUser();
//        return loginUsr == null ? "" : loginUsr.getLoginName();
//    }
//
//    /**
//     * 获取用户信息
//     *
//     * @return 返回登录名
//     */
//    public OnlineUserDTO getLoginUser() {
//        TokenUserDTO loginUsr = getTokenUser();
//        if (loginUsr == null || loginUsr.getUserInfo() == null) {
//            OnlineUserDTO user = new OnlineUserDTO();
//            user.setId(Constant.EMPTY_ID2);
//            user.setUserName("匿名用户");
//            user.setLoginName("anonymity");
//            user.setLanguage("CN");
//            user.setFullName("anonymity");
//            user.setPermissions(new ArrayList<>());
//            return user;
//        }
//        return loginUsr.getUserInfo();
//    }
//
//    /**
//     * 获取用户是否有权限
//     *
//     * @param permission
//     * @return boolean
//     */
//    public boolean hasPermission(String permission) {
//        OnlineUserDTO loginUsr = getLoginUser();
//        if (loginUsr == null || loginUsr.getPermissions() == null) {
//            return false;
//        }
//        return loginUsr.getPermissions().contains(permission);
//    }
//
//    /**
//     * 获取当前登陆token
//     *
//     * @return String
//     */
//    public String getToken() {
//        OnlineUserDTO loginUsr = getLoginUser();
//        return loginUsr == null ? "" : loginUsr.getUserToken();
//    }
//
//    /**
//     * 获取菜单
//     *
//     * @return 返回菜单
//     */
//    public List<MenuNodeDTO> getMenus() {
//        TokenUserDTO loginUsr = getTokenUser();
//        if (loginUsr == null || loginUsr.getMenu() == null) {
//            return null;
//        }
//        return loginUsr.getMenu();
//    }
//
//    /**
//     * 根据TOKEN获取用户信息
//     *
//     * @return 返回登录名
//     */
//    public TokenUserDTO getTokenUser() {
//        HttpServletRequest req = HttpContextUtils.getHttpServletRequest();
//        if (req == null) {
//            log.info("获取用户信息：没有HttpServletRequest");
//            return null;
//        }
//
//        String token = req.getHeader("Authorization");
//        if (StringUtils.isBlank(token)) {
//            token = req.getParameter("Bearer");
//            if (StringUtils.isNotBlank(token)) {
//                token = "Bearer " + token;
//            }
//        }
//
//        if (!StringUtils.isBlank(token)) {
//            TokenUserDTO cacheDto = (TokenUserDTO) cache.getIfPresent(token);
//            if (cacheDto != null) {
//                return cacheDto;
//            }
//        }
//
//        String tokenUser = req.getHeader("userinfo");
//        if (StringUtils.isBlank(tokenUser)) {
//            log.info("获取用户信息：头文件中没有userinfo");
//            return null;
//        }
//
//        TokenUserDTO tokenUserDTO = JSONUtil.toBean(new String(Base64Utils.decodeFromString(tokenUser)), TokenUserDTO.class);
//        cache.put(token, tokenUserDTO);
//        return tokenUserDTO;
//    }
//
//}
