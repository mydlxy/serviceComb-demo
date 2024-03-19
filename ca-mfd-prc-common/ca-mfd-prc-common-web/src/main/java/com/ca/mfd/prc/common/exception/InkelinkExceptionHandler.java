/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.exception;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 异常处理器
 *
 * @author inkelink
 * @since 1.0.0
 */
@RestControllerAdvice(annotations = RestController.class)
public class InkelinkExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InkelinkExceptionHandler.class);


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(InkelinkException.class)
    public ResultVO handleInkelinkException(InkelinkException exception) {
        int httpCode = exception.getCode();
        ResultVO result = new ResultVO();
        result.error(exception.getCode(), exception.getMsg());
        logger.error("InkelinkException,code:{},msg:{} ",exception.getCode(),exception.getMsg(), exception);
        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultVO handleDuplicateKeyException(DuplicateKeyException exception) {
        ResultVO result = new ResultVO();
        result.error(ErrorCode.DB_RECORD_EXISTS);
        logger.error("DuplicateKeyException,msg:{} ", exception.getMessage() ,exception);
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<Object> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult= e.getBindingResult();
        if (bindingResult != null) {
            if (bindingResult.hasErrors()) {
                List<ObjectError> errorList = bindingResult.getAllErrors();
                List<String> resultList = new ArrayList<>(errorList.size());
                for (ObjectError error : errorList) {
                    resultList.add(error.getDefaultMessage());
                }
                return new ResultVO<>().error(String.join(",", resultList).toString());
            }
        }
        return new ResultVO<>().error("参数校验错误");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> bindingResult = e.getConstraintViolations();
        if (bindingResult != null) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation conviold : bindingResult) {
                sb.append(conviold.getMessage() + ";");
            }
            return new ResultVO<>().error(sb.toString());
        }
        return new ResultVO<>().error("参数校验错误");
    }

    @ExceptionHandler(Exception.class)
    public ResultVO handleException(Exception ex) {
        logger.error("服务内部错误:{}", ex.getMessage(), ex);
        return new ResultVO().error("服务内部错误:" + ex.getMessage());
    }
}