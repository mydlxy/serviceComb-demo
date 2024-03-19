package com.ca.mfd.prc.core.main.service.impl;

import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.main.service.ILocalizationService;
import com.ca.mfd.prc.core.main.service.ISysLocalizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 国际化业务实现
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class LocalizationServiceImpl implements ILocalizationService {

    @Autowired
    private IdentityHelper identityHelper;

    /**
     * 国际化
     *
     * @param content 国际化内容
     * @param agrs    参数
     * @return 输出国际化后的内容
     */
    @Override
    public String getLocalization(String content, String[] agrs) {
        return getLocalizationContent(content, identityHelper.getLoginUser().getLanguage(), agrs);
    }

    /**
     * 国际化
     *
     * @param content  国际化内容
     * @param language 翻译语言
     * @param agrs     参数
     * @return 输出国际化后的内容
     */
    @Override
    public String getLocalizationContent(String content, String language, String[] agrs) {
        ISysLocalizationService sysLocalizationService = SpringContextUtils.getBean(ISysLocalizationService.class);

        String finalContent = content;
        String data = sysLocalizationService.getAllDatas().stream().filter(o ->
                        StringUtils.equals(o.getLang(), language)
                                && StringUtils.equals(o.getCn(), finalContent)).map(o -> o.getEn())
                .findFirst().orElse(null);
        if (!StringUtils.isBlank(data)) {
            content = data;
        }
        if (agrs == null || agrs.length == 0) {
            return content;
        }
        //TODO
        return String.format(content, agrs);
    }
}