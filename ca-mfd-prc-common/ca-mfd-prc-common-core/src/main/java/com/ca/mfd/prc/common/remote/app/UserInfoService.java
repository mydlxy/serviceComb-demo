package com.ca.mfd.prc.common.remote.app;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * IntegratedService
 *
 * @author inkelink mason
 * @since 1.0.0 2023-09-23
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "member/integrated", contextId = "getlocaluserinfobytoken")
public interface UserInfoService {

    /**
     * 获取用户信息(兼容本地和门户模式)
     *
     * @return 用户信息
     */
    @GetMapping(value = "/provider/getallbytoken")
    ResultVO<Map> getAllByToken(@RequestParam("token") String token);

    @Component
    class AccountServiceFallbackFactory implements FallbackFactory<UserInfoService> {
        private static final Logger logger = LoggerFactory.getLogger(AccountServiceFallbackFactory.class);

        @Override
        public UserInfoService create(Throwable cause) {
            logger.error("inkelink-core-integrated feign request error:" + cause.getMessage(), cause);
            return new UserInfoService() {

                /**
                 * 获取用户信息(兼容本地和门户模式)
                 *
                 * @return 用户信息
                 */
                @Override
                public ResultVO getAllByToken(String token) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }
            };
        }
    }
}
