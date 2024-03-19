 package com.ca.mfd.prc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

 /**
  *
  * <p>功能描述: ApiPlatResponseUtil.java</p>
  * @author   wujc
  * @version  2023年7月5日
  */
 public class ApiPlatResponseUtil {
     private static final Logger logger = LoggerFactory.getLogger(ApiPlatResponseUtil.class);

     public static void traceResponse(ResponseEntity<?> response) {

         if (logger.isInfoEnabled()) {
             logger.info("============================response begin==========================================");
             logger.info("Status code  : {}", response.getStatusCode());

             logger.info("=======================response end=================================================");
         }
     }
 }
