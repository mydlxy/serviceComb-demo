package com.ca.mfd.prc.common.dto;

/**
 * 统一门户接口返回对象
 *
 * @author qiujun
 * @date 2023/9/8 10:42
 */
public class PortalBaseDto<T> {

    /**
     * 当为"00000"时，则为成功，其他code值则为失败
     */
    private String code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
