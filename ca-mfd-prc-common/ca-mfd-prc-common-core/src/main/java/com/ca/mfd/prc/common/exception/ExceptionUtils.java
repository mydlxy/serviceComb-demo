/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.exception;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception工具类
 *
 * @author inkelink
 */
public class ExceptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);
    /**
     * 获取异常信息
     *
     * @param ex 异常
     * @return 返回异常信息
     */
    public static String getErrorMessage(Exception ex) {
        String errMsg = ex.getMessage();
        if (StringUtils.isBlank(errMsg)) {
            errMsg = getErrorStackTrace(ex);
            String[] errs = errMsg.split("\r\n\t");
            return errs[0];
        } else {
            return errMsg;
        }
    }

    /**
     * 获取异常信息
     *
     * @param expectEx 异常
     * @return 返回异常信息
     */
    public static String getErrorStackTrace(Exception expectEx) {

        try (StringWriter sw = new StringWriter();
             PrintWriter  pw = new PrintWriter(sw, true);
        ) {
            logger.error(expectEx.getMessage());
            return sw.toString();
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }
}