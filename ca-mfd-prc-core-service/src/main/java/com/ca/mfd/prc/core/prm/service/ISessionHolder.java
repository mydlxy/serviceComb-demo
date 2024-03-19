package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.entity.LocalSession;

/**
 * @author tom.wu
 */
public interface ISessionHolder {

    /**
     * 初始化本地session对象
     */
    LocalSession initalSession(String ticket, String userHostAddress) throws Exception;
}