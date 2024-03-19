package com.ca.mfd.prc.gateway.remote.app;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * ISysConfigurationService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-08-17
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "member/account", contextId = "inkelink-core-account")
public interface IAccountService {
    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping(value = "info")
    ResultVO info(@RequestHeader("Authorization") String authorization);

    @Component
    class AccountServiceFallbackFactory implements FallbackFactory<IAccountService> {
        private static final Logger logger = LoggerFactory.getLogger(IAccountService.AccountServiceFallbackFactory.class);

        @Override
        public IAccountService create(Throwable cause) {
            logger.error("inkelink-core-account feign request error:" + cause.getMessage(), cause);
            return new IAccountService() {
                /**
                 * 获取用户信息
                 *
                 * @return 用户信息
                 */
                @Override
                public ResultVO info(String authorization) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

            };
        }
    }
}
