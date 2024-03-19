package com.ca.mfd.prc.gateway.exception;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.gateway.utils.ResultConstant;
import io.netty.channel.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * <p>功能描述: 全局统一异常处理</p>
 *
 * @author wujc
 * @version 2021年8月13日
 */
@Component
public class GateWayExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GateWayExceptionHandlerAdvice.class);

    @ExceptionHandler(value = {ResponseStatusException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<String> handle(ResponseStatusException ex) {
        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Gateway Exception" + ex.getMessage());
        logger.error("网关异常" + ex.getMessage(), ex);
        return rs;
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResultVO<String> handle(ConnectTimeoutException ex) {
        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Gateway TimeOut:" + ex.getMessage());
        logger.error("网关超时:" + ex.getMessage(), ex);
        return rs;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultVO<String> handle(NotFoundException ex) {
        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Unknown service：" + ex.getMessage());
        logger.error("Unknown service：" + ex.getMessage(), ex);
        return rs;
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<String> handle(RuntimeException ex) {

        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Running Exception:" + ex.getMessage());
        logger.error("Running Exception:" + ex.getMessage(), ex);
        return rs;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<String> handle(Exception ex) {
        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Gateway Error:" + ex.getMessage());
        logger.error("Gateway Error 500:" + ex.getMessage(), ex);
        return rs;
    }
	    
	   /*
	    @ExceptionHandler(value = {BlockException.class})
	    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	    public ResultVO<String> handle(BlockException ex) {
	        ResultVO<String> rs=new ResultVO<String>();
	        rs.setCode(ResultConstant.ERROR);
			rs.setMessage("系统忙(限流)，请稍后重试！");
	        //logger.error("服务未找到429:"+ ex.getMessage(),ex);
	        return rs;
	    }*/


    @ExceptionHandler(value = {TimeoutException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResultVO<String> handle(TimeoutException ex) {
        ResultVO<String> rs = new ResultVO<String>();
        rs.setCode(ResultConstant.ERROR);
        rs.setMessage("Gateway Timeout:" + ex.getMessage());
        logger.error("Gateway Timeout:" + ex.getMessage(), ex);
        return rs;
    }


    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<String> handle(Throwable throwable) {
        ResultVO<String> result = new ResultVO<String>();
        result.setCode(ResultConstant.ERROR);
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
	        /*}else if (BlockException.isBlockException(throwable)) {
	            result = handle((BlockException) throwable);*/
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof TimeoutException) {
            result = handle((TimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            result = handle((NotFoundException) throwable);
        } else if (throwable instanceof RuntimeException) {
            result = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            result = handle((Exception) throwable);
        }
        return result;
    }
}
