package com.ca.mfd.prc.avi.uwb.uwbhelper;

import com.ca.mfd.prc.avi.uwb.dto.UwbOptionModel;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

/**
 * UwbToken
 *
 * @author eric.zhou
 * @date 2023/05/18
 */
public class UwbToken {

    private static final String cacheName = "uwbToken";
    private static final Object lockObj = new Object();

    public static UwbOptionModel getUwbOptionModel() {
        return SpringContextUtils.getBean(UwbOptionModel.class);
    }

    public static String getUwbToken() {
        LocalCache localCache = SpringContextUtils.getBean(LocalCache.class);
        String datas = localCache.getObject(cacheName);
        if (datas == null) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null) {
                    try {
                        //var uwbModel = scope.ServiceProvider.GetRequiredService<IOptions<UWBOptionModel>>().Value;
                        RestTemplateUtil restTemplateUtil = SpringContextUtils.getBean(RestTemplateUtil.class);
                        UwbOptionModel uWbOptionModel = getUwbOptionModel();

                        String pwdmd5 = DigestUtils.md5Hex(uWbOptionModel.getPassWord().getBytes(StandardCharsets.UTF_8)) + "abcdefghijklmnopqrstuvwxyz20191107salt";
                        String md5 = DigestUtils.md5Hex(pwdmd5.getBytes(StandardCharsets.UTF_8));
                        MultiValueMap<String, Object> par = new LinkedMultiValueMap<>();
                        par.add("username", uWbOptionModel.getUserName());
                        par.add("password", md5);

                        ResponseEntity<String> resultHttpPost = restTemplateUtil.postFromUrlencodedResponse(uWbOptionModel.getLoginUrl(), par, null);
                        if (resultHttpPost.getStatusCode() == HttpStatus.OK) {
                            String interfaceresult = resultHttpPost.getBody();
                            UwbResult result = JsonUtils.parseObject(interfaceresult, UwbResult.class);
                            datas = result.getToken();
                        }
                        localCache.addObject(cacheName, datas, 6 * 60 * 10 * 7);
                    } catch (Exception exe) {
                        throw exe;
                    }
                }
            }
        }
        return datas;
    }
}
