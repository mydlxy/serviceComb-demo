//package com.ca.common.interceptor;
//
//import IdentityHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 身份验证信息初始化
// *
// * @author eric.zhou@hg2mes.com
// * @date 2023/4/19
// */
//@Service
//public class IdentityInterceptor implements HandlerInterceptor {
//    @Resource
//    private IdentityHelper identityHelper;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        try {
//            identityHelper.getTokenUser();
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("获取身份验证信息失败", e);
//        }
//        return true;
//    }
//
//}
