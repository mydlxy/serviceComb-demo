/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.exception;


import com.ca.mfd.prc.common.utils.MessageUtils;

/**
 * 自定义异常
 *
 * @author inkelink
 */
public class InkelinkException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public InkelinkException(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public InkelinkException(int code, String... params) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public InkelinkException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public InkelinkException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public InkelinkException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public InkelinkException(String msg, int code) {
        this.code = code;
        this.msg = msg;
    }

    public InkelinkException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}