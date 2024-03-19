package com.ca.mfd.prc.gateway.controller;

import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.TokenUserDTO;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.gateway.remote.app.IAccountService;
import com.ca.mfd.prc.gateway.remote.app.IntegratedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页提示
 *
 * @author inkelink
 */
@RestController
public class IndexController {

    @Autowired
    private Environment env;
    @Autowired
    private IntegratedService integratedService;

    @Autowired
    private IAccountService accountService;
    @Value("${inkelink.gateway.isOpenCaTokenCheck:false}")
    private boolean isOpenCaTokenCheck;

    @GetMapping("/")
    public String index() {

        String tips = "你好，inkelink-gateway 已启动！";
//        String tips = String.format("你好，inkelink-gateway 已启动！\n %s \n %s \n %s \n %s  \n %s",
//                env.getProperty("spring.cloud.gateway.routes[13].id"),
//                env.getProperty("spring.cloud.gateway.routes[13].uri"),
//                env.getProperty("spring.cloud.gateway.routes[13].filters[0]"),
//                env.getProperty("spring.cloud.gateway.routes[13].filters[1]"),
//                env.getProperty("spring.cloud.gateway.routes[13].predicates[0]"));
        return tips;
    }

    @GetMapping("/getuserinfobytoken")
    public ResultVO<OnlineUserDTO> getUserInfoByToken(String token, String refreshToken) {
        OnlineUserDTO user;
        //走公司那一套
        if (!isOpenCaTokenCheck) {
            //根据token从缓存中获取
            ResultVO account = accountService.info("Bearer " + token);
            if (account == null || account.getData() == null) {
                return new ResultVO<OnlineUserDTO>().error("获取用户信息失败");
            }
            TokenUserDTO userDTO = JsonUtils.parseObject(JsonUtils.toJsonString(account.getData()), TokenUserDTO.class);
            if (userDTO.getUserInfo() == null) {
                return new ResultVO<OnlineUserDTO>().error("获取用户信息转换失败：");
            }
            return new ResultVO<OnlineUserDTO>().ok(userDTO.getUserInfo());
        }
        //走门户集成验证token
        else {
            ResultVO userInfo = integratedService.getUserInfoByToken(token.replace("\"", "").trim(), refreshToken);
            if (userInfo == null || userInfo.getData() == null) {
                return new ResultVO<OnlineUserDTO>().error("获取用户信息失败");
            } else {
                user = JsonUtils.parseObject(JsonUtils.toJsonString(userInfo.getData()), OnlineUserDTO.class);
            }
            return new ResultVO<OnlineUserDTO>().ok(user);
        }
    }

}
