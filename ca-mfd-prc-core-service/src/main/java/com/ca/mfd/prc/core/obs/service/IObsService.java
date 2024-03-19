package com.ca.mfd.prc.core.obs.service;

import com.ca.mfd.prc.common.obs.model.CaTemporarySignatureResponse;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IObsService {
    /**
     * obs对象存储示例
     *
     * @param multipartFile 文件
     * @return ResultVO<String>
     */
    ResultVO<String> uploadFile(MultipartFile multipartFile);

    /**
     * obs文件下载示例
     *
     * @param response 文件下载
     * @param fileName 文件名称
     */
    void downFile(HttpServletResponse response, String fileName);

    /**
     * 上传临时签名
     *
     * @param contentType 文件/媒体类型
     * @param fileName    文件名
     * @return 临时签名
     */
    CaTemporarySignatureResponse getClientSing(String contentType, String fileName);
}
