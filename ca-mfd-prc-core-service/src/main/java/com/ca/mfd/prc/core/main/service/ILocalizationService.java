package com.ca.mfd.prc.core.main.service;


/**
 * 系统业务库同步表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ILocalizationService {

    /**
     * 国际化
     *
     * @param content 国际化内容
     * @param agrs    参数
     * @return 输出国际化后的内容
     */
    String getLocalization(String content, String[] agrs);

    /**
     * 国际化
     *
     * @param content  国际化内容
     * @param language 翻译语言
     * @param agrs     参数
     * @return 输出国际化后的内容
     */
    String getLocalizationContent(String content, String language, String[] agrs);
}