package com.ca.mfd.prc.gateway.remote.app;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IntegratedService
 *
 * @author inkelink mason
 * @since 1.0.0 2023-09-23
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "member/integrated", contextId = "inkelink-core-integrated")
public interface IntegratedService {
    /**
     * 根据token获取本地用户信息
     * @param token
     * @return
     */
    @GetMapping(value = "/provider/getlocaluserinfobytoken")
    ResultVO getlocaluserinfobytoken(@RequestParam("token") String token);

    /**
     * 根据token获取用户信息
     * @param token
     * @param refreshToken
     * @return
     */
    @GetMapping(value = "/provider/getuserinfobytoken")
    ResultVO getUserInfoByToken(@RequestParam("token") String token,@RequestParam("refreshToken") String refreshToken);

    /**
     * 用户令牌校验-调用门户接口
     * @param token
     * @param refreshToken
     * @return
     */
    @GetMapping(value = "getUserInfo")
    ResultVO getUserInfo(@RequestParam("token") String token,@RequestParam("refreshToken") String refreshToken);

    @Component
    class AccountServiceFallbackFactory implements FallbackFactory<IntegratedService> {
        private static final Logger logger = LoggerFactory.getLogger(AccountServiceFallbackFactory.class);

        @Override
        public IntegratedService create(Throwable cause) {
            logger.error("inkelink-core-integrated feign request error:" + cause.getMessage(), cause);
            return new IntegratedService() {
                @Override
                public ResultVO getlocaluserinfobytoken(String token) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                @Override
                public ResultVO getUserInfoByToken(String token, String refreshToken) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                @Override
                public ResultVO getUserInfo(String token, String refreshToken) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

            };
        }
    }
}
