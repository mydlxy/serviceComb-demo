package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.TokenUserDTO;
import com.ca.mfd.prc.common.redis.RedisKeys;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 身份验证帮助类
 *
 * @author eric.zhou@hg2mes.com
 * @date 2023/4/6
 */
@Component
public class IdentityHelper {

    private static final Logger logger = LoggerFactory.getLogger(IdentityHelper.class);

    /**
     * 存储缓存（30分钟）
     */
    private final Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5 * 60, TimeUnit.SECONDS).build();

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取domain
     */
    public String getDomain() {
        //TODO 多租户暂不实现
        return "";
        //OnlineUserDTO loginUsr = getLoginUser();
        //return loginUsr == null ? "" : loginUsr.getDomain();

    }

    /**
     * 获取用户ID
     *
     * @return 返回用户ID
     */
    public Long getUserId() {
        OnlineUserDTO loginUsr = getLoginUser();
        return loginUsr == null ? Constant.DEFAULT_USER_ID : loginUsr.getId();
    }

    /**
     * 获取用户名
     *
     * @return 返回用户名
     */

    public String getUserName() {
        OnlineUserDTO loginUsr = getLoginUser();
        return loginUsr == null ? "" : loginUsr.getUserName();
    }

    /**
     * 获取登录名
     *
     * @return 返回登录名
     */
    public String getLoginName() {
        OnlineUserDTO loginUsr = getLoginUser();
        return loginUsr == null ? "" : loginUsr.getLoginName();
    }

    /**
     * 获取用户信息
     *
     * @return 返回登录名
     */
    public OnlineUserDTO getLoginUser() {
        TokenUserDTO loginUsr = getTokenUser();
        if (loginUsr == null || loginUsr.getUserInfo() == null) {
            OnlineUserDTO user = new OnlineUserDTO();
            user.setId(Constant.DEFAULT_USER_ID);
            user.setUserName("匿名用户");
            user.setLoginName("anonymity");
            user.setLanguage("CN");
            user.setFullName("anonymity");
            user.setPermissions(new ArrayList<>());
            return user;
        }
        return loginUsr.getUserInfo();
    }

    /**
     * 获取用户是否有权限
     *
     * @param permission
     * @return boolean
     */
    public boolean hasPermission(String permission) {
        OnlineUserDTO loginUsr = getLoginUser();
        if (loginUsr == null || loginUsr.getPermissions() == null) {
            return false;
        }
        return loginUsr.getPermissions().contains(permission);
    }

    /**
     * 获取当前登陆token
     *
     * @return String
     */
    public String getToken() {
        OnlineUserDTO loginUsr = getLoginUser();
        return loginUsr == null ? "" : loginUsr.getUserToken();
    }

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

    /**
     * 根据TOKEN获取用户信息
     *
     * @return 返回登录名
     */
    public TokenUserDTO getTokenUser() {
        HttpServletRequest req = HttpContextUtils.getHttpServletRequest();
        if (req != null) {
            String tokenString = req.getHeader("token");
            if (StringUtils.isNotBlank(tokenString)) {
                //从内存取
                TokenUserDTO cacheDto = (TokenUserDTO) cache.getIfPresent(tokenString);
                if (cacheDto != null) {
                    return cacheDto;
                }
                //从redis获取
                Object tokenObj = redisUtils.get(RedisKeys.getToken(tokenString));
                if (tokenObj != null) {
                    Object userObj = redisUtils.get(RedisKeys.getTokenUserId(Long.valueOf(tokenObj.toString())));
                    if (userObj != null) {
                        OnlineUserDTO onlineUserDTO = JsonUtils.parseObject(JsonUtils.toJsonString(userObj), OnlineUserDTO.class);
                        if (onlineUserDTO != null) {
                            cacheDto = new TokenUserDTO();
                            cacheDto.setUserInfo(onlineUserDTO);
                            cache.put(tokenString, cacheDto);
                            return cacheDto;
                        }
                    }
                }
                logger.info("获取用户信息失败：token值：" + tokenString);
            } else {
                logger.info("获取用户信息失败：没有token值");
            }
        } else {
            logger.info("获取用户信息：没有HttpServletRequest");
        }
        return null;
    }


    /**
     * 获取头的token
     *
     * @return String
     */
    public String getHeaderToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        //通过header获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.isBlank(token)) {
            token = token.replace("Bearer ", "").replace("bearer ", "").trim();
        }
        if (!StringUtils.isBlank(token)) {
            return token;
        }
//        //通过cookie获取token
//        if (request.getCookies() != null && request.getCookies().containsKey("ACCESS-TOKEN")) {
//            HttpCookie tokenCookie = request.getCookies().getFirst("ACCESS-TOKEN");
//            if (tokenCookie != null) {
//                token = tokenCookie.getValue();
//            }
//        }
//        if (!StringUtils.isBlank(token)) {
//            token = token.replace("Bearer", "").replace("bearer", "").trim();
//        }
//        if (!StringUtils.isBlank(token)) {
//           return Arrays.asList(token,"cookie");
//        }
        //通过url参数获取
        token = request.getParameter("bearer");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("Bearer");
        }
        if (!StringUtils.isBlank(token)) {
            logger.info("====获取到query中token参数：" + token);
            token = token.trim();
            return token;
        }
        return null;
    }

}
