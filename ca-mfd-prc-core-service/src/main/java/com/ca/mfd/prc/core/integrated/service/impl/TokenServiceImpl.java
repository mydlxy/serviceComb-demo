package com.ca.mfd.prc.core.integrated.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.dto.PortalBaseDto;
import com.ca.mfd.prc.common.entity.CheckTokenDTO;
import com.ca.mfd.prc.common.entity.DepartmentDTO;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.entity.QueryUserPermissionDTO;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.redis.RedisKeys;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.integrated.config.IntegratedConfig;
import com.ca.mfd.prc.core.integrated.service.TokenService;
import com.ca.mfd.prc.core.integrated.service.UserService;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRedisTokenUserIdEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRedisUserIdUserInfoEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRedisTokenUserIdService;
import com.ca.mfd.prc.core.prm.service.IPrmRedisUserIdUserInfoService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.main.service.ISysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
    private static Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String SUCCESS_CODE = "00000";  // 统一门户验证成功的代码
    @Autowired
    private IntegratedConfig integratedConfig;
    @Autowired
    @Qualifier("casdkrestemplate")
    @Lazy
    private RestTemplate casdkrestemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IPrmRedisTokenUserIdService prmRedisTokenUserIdService;
    @Autowired
    private IPrmRedisUserIdUserInfoService prmRedisUserIdUserInfoService;
    @Autowired
    private IPrmUserService prmUserService;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private IPrmPermissionService prmPermissionService;
    @Autowired
    private UserService userService;

    @Override
    public OnlineUserDTO getlocaluserinfobytoken(String token) {
        OnlineUserDTO onlineUserDTO;

        //先从redis获取数据
        Object tokenObj = redisUtils.get(RedisKeys.getToken(token));
        if (tokenObj != null) {
            Object userObj = redisUtils.get(RedisKeys.getTokenUserId(Long.valueOf(tokenObj.toString())));
            if (userObj != null) {
                onlineUserDTO = JsonUtils.parseObject(JsonUtils.toJsonString(userObj), OnlineUserDTO.class);
                return onlineUserDTO;
            }
        }

        //从数据库中获取
        PrmRedisTokenUserIdEntity tokenUser = prmRedisTokenUserIdService.getLocaluserByToken(token);
        if (tokenUser != null) {
            PrmRedisUserIdUserInfoEntity userInfoEntity = prmRedisUserIdUserInfoService.getUserInfoByUserId(tokenUser.getCaUserId());
            if (userInfoEntity != null) {
                if (isBase64Encoded(userInfoEntity.getUserInfo())) {
                    byte[] decodedBytes = Base64.getDecoder().decode(userInfoEntity.getUserInfo());
                    String userInfo = StringUtils.toEncodedString(decodedBytes, StandardCharsets.UTF_8);
                    userInfoEntity.setUserInfo(userInfo);
                }
                onlineUserDTO = JsonUtils.parseObject(userInfoEntity.getUserInfo(), OnlineUserDTO.class);

                if (onlineUserDTO != null) {
                    //获取到数据，重写到redis
                    redisUtils.set(RedisKeys.getToken(token), tokenUser.getCaUserId());
                    redisUtils.set(RedisKeys.getTokenUserId(tokenUser.getCaUserId()), onlineUserDTO);
                }

                return onlineUserDTO;
            }
        }

        return null;
    }

    @Override
    public OnlineUserDTO getUserInfo(String token, String refreshToken) {
        OnlineUserDTO onlineUserDTO;

        //先从redis获取数据
        Object tokenObj = redisUtils.get(RedisKeys.getToken(token));
        if (tokenObj != null) {
            Object userObj = redisUtils.get(RedisKeys.getTokenUserId(Long.valueOf(tokenObj.toString())));
            if (userObj != null) {
                onlineUserDTO = JsonUtils.parseObject(JsonUtils.toJsonString(userObj), OnlineUserDTO.class);
                return onlineUserDTO;
            }
        }

        //从数据库中获取
        PrmRedisTokenUserIdEntity tokenUser = prmRedisTokenUserIdService.getUserIdByToken(token);
        if (tokenUser != null) {
            PrmRedisUserIdUserInfoEntity userInfoEntity = prmRedisUserIdUserInfoService.getUserInfoByUserId(tokenUser.getCaUserId());
            if (userInfoEntity != null) {
                if (isBase64Encoded(userInfoEntity.getUserInfo())) {
                    byte[] decodedBytes = Base64.getDecoder().decode(userInfoEntity.getUserInfo());
                    String userInfo = StringUtils.toEncodedString(decodedBytes, StandardCharsets.UTF_8);
                    userInfoEntity.setUserInfo(userInfo);
                }
                onlineUserDTO = JsonUtils.parseObject(userInfoEntity.getUserInfo(), OnlineUserDTO.class);
                return onlineUserDTO;
            }
        }

        /*
          redis、数据库都获取不到用户信息，就重新发起门户token校验获
         */
        onlineUserDTO = checkToken(token, refreshToken);
        return onlineUserDTO;
    }

    /**
     * 调用门户-token验证
     *
     * @param token
     * @param refreshToken
     * @return
     */
    @Override
    public OnlineUserDTO checkToken(String token, String refreshToken) {
        OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getCheckTokenUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString())
                .queryParam("token", token).queryParam("refreshToken", refreshToken);
        ParameterizedTypeReference<PortalBaseDto<CheckTokenDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<CheckTokenDTO>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<CheckTokenDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("token验证失效 | token: {}, msg: {}", token, dto.getMsg());
                throw new InkelinkException("token验证失效 | token:" + token + "msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("token验证成功:" + JSONObject.toJSON(response.getBody()));
            CheckTokenDTO tokenDTO = (CheckTokenDTO) dto.getData();
            Long expire;
            try {
                expire = DateUtils.getTimeTotalSeconds(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN), tokenDTO.getTokenExpiration());
            } catch (ParseException e) {
                logger.info("====计算token过期时间失败");
                throw new InkelinkException("====计算token过期时间失败");
            }
            //token验证接口信息组装
            onlineUserDTO.setToken(token);
            onlineUserDTO.setRefreshToken(refreshToken);
            onlineUserDTO.setLoginTime(new Date());

            //获取用户信息进行组装
            QueryUserDTO userDTO = userService.queryUser(tokenDTO.getUserId(), null);
            if (userDTO != null) {
                onlineUserDTO.setId(userDTO.getUserId());
                onlineUserDTO.setLoginName(userDTO.getLoginId());
                onlineUserDTO.setUserName(userDTO.getUserName());
                onlineUserDTO.setFullName(userDTO.getUserName() + "/" + userDTO.getLoginId());
                onlineUserDTO.setNickName(userDTO.getUserName());
                //获取用户部门信息
                List<DepartmentDTO> departList = userService.departmentQuery(userDTO.getDepartmentId());
                if (departList != null && departList.size() > 0) {
                    //取得对第一个部门信息
                    DepartmentDTO departDto = departList.get(0);
                    onlineUserDTO.setDepartmentCode(departDto.getDepartmentCode());
                    onlineUserDTO.setDpartmentName(departDto.getDepartmentName());
                }
            }

            //获取用户权限列表
            QueryUserPermissionDTO userPermissionDTO = userService.queryUserPermission(tokenDTO.getUserId());
            if (userPermissionDTO != null) {
                onlineUserDTO.setBtnPermission(userPermissionDTO.getBtnPermission());
                onlineUserDTO.setUriPermission(userPermissionDTO.getUriPermission());
            }

            //组装MOM系统相关字段数据
            assemnbleUserData(onlineUserDTO);

            //数据存入redis中
            redisUtils.set(RedisKeys.getToken(token), tokenDTO.getUserId(), expire);
            redisUtils.set(RedisKeys.getTokenUserId(tokenDTO.getUserId()), onlineUserDTO, expire);

            //token跟用户id信息插入数据库
            PrmRedisTokenUserIdEntity tokenUserId = prmRedisTokenUserIdService.getUserIdByToken(token);
            if (tokenUserId != null) {
                tokenUserId.setCaUserId(tokenDTO.getUserId());
                prmRedisTokenUserIdService.updateById(tokenUserId);
            } else {
                PrmRedisTokenUserIdEntity tokenEntity = new PrmRedisTokenUserIdEntity();
                tokenEntity.setToken(token);
                tokenEntity.setCaUserId(tokenDTO.getUserId());
                tokenEntity.setExpireDt(DateUtils.parse(tokenDTO.getTokenExpiration(), DateUtils.DATE_TIME_PATTERN));
                prmRedisTokenUserIdService.insert(tokenEntity);
            }

            //用户id跟用户dto信息插入数据库
            PrmRedisUserIdUserInfoEntity userInfo = prmRedisUserIdUserInfoService.getUserInfoByUserId(tokenDTO.getUserId());

            //对用户信息(json格式)数据进行Base64编码加密
            byte[] bytes = JsonUtils.toJsonString(onlineUserDTO).getBytes(StandardCharsets.UTF_8);
            String userinfo = Base64.getEncoder().encodeToString(bytes);
            if (userInfo != null) {
                userInfo.setUserInfo(userinfo);
                prmRedisUserIdUserInfoService.updateById(userInfo);

            } else {
                PrmRedisUserIdUserInfoEntity userInfoEntity = new PrmRedisUserIdUserInfoEntity();
                userInfoEntity.setCaUserId(tokenDTO.getUserId());
                userInfoEntity.setUserInfo(userinfo);
                prmRedisUserIdUserInfoService.insert(userInfoEntity);
            }


            prmRedisTokenUserIdService.saveChange();
            prmRedisUserIdUserInfoService.saveChange();
        } catch (Exception e) {
            logger.info("用户令牌校验-调用门户接口异常:" + e.getMessage());
            return null;
        }
        return onlineUserDTO;
    }

    /**
     * 用户权限，菜单相关信息
     *
     * @return
     */
    public OnlineUserDTO assemnbleUserData(OnlineUserDTO onlineUserDTO) {
        //获取superadmin的信息
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("USER_NAME", "superadmin", ConditionOper.Equal));
        PrmUserEntity userEntity = prmUserService.getData(conditions).stream().findFirst().orElse(null);
        if (userEntity != null) {
            onlineUserDTO.setMenuName(SpringContextUtils.getConfiguration("User:SysMenu" + userEntity.getUserType(), "AdminMenu"));
        }
        onlineUserDTO.setMenuPermissions(sysMenuService.getMenuItemsPremissms());
        onlineUserDTO.setPermissions(prmPermissionService.getData(null).stream().map(PrmPermissionEntity::getPermissionCode).collect(Collectors.toList()));
        return onlineUserDTO;
    }

    /**
     * 判断是否被加密过
     */
    public static boolean isBase64Encoded(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        return str.matches(base64Pattern);
    }
}
