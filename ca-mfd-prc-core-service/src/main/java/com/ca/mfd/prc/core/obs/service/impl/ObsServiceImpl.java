package com.ca.mfd.prc.core.obs.service.impl;


import com.ca.mfd.prc.common.obs.config.ObsClientStrategy;
import com.ca.mfd.prc.common.obs.model.CaTemporarySignatureResponse;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.obs.service.IObsService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Obs使用测试类
 *
 * @author xxx
 */
@Service
public class ObsServiceImpl implements IObsService {

    public static final String DOWNLOAD_FAIL_MSG = "文件下载失败:";
    private static final Logger logger = LoggerFactory.getLogger(ObsServiceImpl.class);
    @Autowired
    @Lazy
    private ObsClientStrategy obsClientStrategy;

    private static final String PUBLIC_LEVEL = "public";

    @Override
    public ResultVO<String> uploadFile(MultipartFile multipartFile) {
        // 文件上传
        InputStream inputStream = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            inputStream = multipartFile.getInputStream();
            ObsClient obsClient = obsClientStrategy.getClientByCode(PUBLIC_LEVEL);
            PutObjectResult putObjectResult = obsClient.putObject(obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL), fileName, inputStream);
            logger.warn("文件上传结果：{}", putObjectResult);
        } catch (ObsException e) {
            // 文件上传失败
            logger.error("文件上传失败，OBS异常:", e);
        } catch (IOException e) {
            logger.error("文件上传失败，IOException异常: ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return ResultVO.ok();
    }

    @Override
    public void downFile(HttpServletResponse response, String fileName) {
        ObsClient obsClient = obsClientStrategy.getClientByCode(PUBLIC_LEVEL);
        ObsObject obsObject = obsClient.getObject(obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL), fileName);
        InputStream content = null;
        OutputStream outputStream = null;
        try {
            content = obsObject.getObjectContent();
            outputStream = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(content, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error(DOWNLOAD_FAIL_MSG, e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(DOWNLOAD_FAIL_MSG, e);
                }
            }
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.error(DOWNLOAD_FAIL_MSG, e);
                }
            }
        }

    }


    @Override
    public CaTemporarySignatureResponse getClientSing(String contentType, String fileName) {
        TemporarySignatureRequest request = new TemporarySignatureRequest();
        request.setBucketName(obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL));
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if (StringUtils.isBlank(fileName)) {
            String guid = UUID.randomUUID().toString();
            String objectKey = currentDate + "/" + guid;
            request.setObjectKey(objectKey);
        } else {
            String objectKey = currentDate + "/" + fileName;
            request.setObjectKey(objectKey);
            request.setMethod(HttpMethodEnum.PUT);
            request.setExpires(3600);
            if (StringUtils.isBlank(contentType)) {
                request.getHeaders().put("Content-type", "multipart/form-data");
            } else {
                request.getHeaders().put("Content-type", contentType);
            }
            ObsClient obsClient = obsClientStrategy.getClientByCode(PUBLIC_LEVEL);
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            CaTemporarySignatureResponse createResponse = new CaTemporarySignatureResponse();
            createResponse.setUrl(response.getSignedUrl());
            createResponse.setHeaders(response.getActualSignedRequestHeaders());
            return createResponse;
        }
        return null;
    }
}
