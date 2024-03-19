package com.ca.mfd.prc.pm.remote.app.core;

import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
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
     * 根据参数类型获取参数值
     *
     * @param loginId 登录名
     * @return 返回用户信息
     */
    @GetMapping(value = "/provider/getuserinfobyloginname")
    ResultVO<QueryUserDTO> getUserInfoByLoginName(@RequestParam("loginId") String loginId);
}
