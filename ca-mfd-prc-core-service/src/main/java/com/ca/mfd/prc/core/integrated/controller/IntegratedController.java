package com.ca.mfd.prc.core.integrated.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.integrated.service.TokenService;
import com.ca.mfd.prc.core.integrated.service.UserService;
import com.ca.mfd.prc.core.prm.service.ISessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author inkelink
 */
@RestController
@RequestMapping("member/integrated")
@Tag(name = "门户集成相关方法")
public class IntegratedController extends BaseApiController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private ISessionHolder sessionHolder;
    @Value("${inkelink.gateway.isOpenCaTokenCheck:false}")
    private boolean isOpenCaTokenCheck;

    @GetMapping(value = "getuserinfo", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "令牌校验-获取用户信息")
    public ResultVO getUserInfo(String token, String refreshToken) {
        ResultVO result = new ResultVO<>();
        OnlineUserDTO userInfo = tokenService.getUserInfo(token, refreshToken);
        if(userInfo==null){
            return result.error("用户令牌校验失败");
        }
        Map mp = new HashMap(20);
        mp.put("token", userInfo.getToken());
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
        mp.put("menuPermissions", userInfo.getMenuPermissions());
        mp.put("menuName", userInfo.getMenuName());
        //licese未实现
        mp.put("isLicenseExpire", false);
        mp.put("licenseExpireDt", DateUtils.format(DateUtils.addDateYears(new Date(), 10), DateUtils.DATE_TIME_PATTERN));
        return result.ok(mp);
    }

    @GetMapping(value = "/provider/getlocaluserinfobytoken")
    @Operation(summary = "获取本地存储的用户信息")
    public ResultVO<OnlineUserDTO> getlocaluserinfobytoken(String token) {
        ResultVO<OnlineUserDTO> result = new ResultVO<>();
        OnlineUserDTO userDTO = tokenService.getlocaluserinfobytoken(token);
        return result.ok(userDTO);
    }

    @GetMapping(value = "/provider/getuserinfobyloginname")
    @Operation(summary = "根据登录账号获取门户用户信息")
    public ResultVO<QueryUserDTO> getUserInfoByLoginName(String loginId) {
        ResultVO<QueryUserDTO> result = new ResultVO<>();
        if(isOpenCaTokenCheck){
            QueryUserDTO userDTO = userService.queryUser(null,loginId);
            return result.ok(userDTO);
        }
        return result.ok(new QueryUserDTO());
    }

    @GetMapping(value = "/provider/getuserinfobytoken")
    @Operation(summary = "根据Token获取用户信息")
    public ResultVO<OnlineUserDTO> getUserInfoByToken(String token,String refreshToken) {
        ResultVO<OnlineUserDTO> result = new ResultVO<>();
        OnlineUserDTO userDTO = tokenService.getlocaluserinfobytoken(token);
        if(userDTO!=null){
            return result.ok(userDTO);
        }
        //本地没获取到数据，重新拉取门户
        userDTO=tokenService.checkToken(token,refreshToken);
        return result.ok(userDTO);
    }

    @GetMapping(value = "/provider/getallbytoken")
    @Operation(summary = "获取用户信息(兼容本地和门户模式)")
    public ResultVO<Map> getAllByToken(HttpServletRequest request,String token) throws Exception {
        ResultVO<Map> resultVo = new ResultVO<>();
        if (isOpenCaTokenCheck) {
            OnlineUserDTO userDTO = tokenService.getlocaluserinfobytoken(token);
            Map mp = new HashMap(5);
            mp.put("userInfo", userDTO);
            return resultVo.ok(mp);
        } else {
            String ip = IpUtils.getIpAddr(request);
            LocalSession session = sessionHolder.initalSession(token, ip);
            if (session == null) {
                return new ResultVO<Map>().error(401, "没有登陆");
            }
            Map mp = new HashMap(5);
            mp.put("userInfo", session.getUserInfo());
            return resultVo.ok(mp);
        }
    }
}
