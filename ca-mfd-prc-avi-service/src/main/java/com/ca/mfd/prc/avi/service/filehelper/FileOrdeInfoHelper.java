package com.ca.mfd.prc.avi.service.filehelper;

import com.ca.mfd.prc.avi.dto.LastPushPlcOrderEntryInfo;
import com.ca.mfd.prc.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @Author: joel
 * @Date: 2023-08-17-17:30
 * @Description:
 */
public class FileOrdeInfoHelper {

    private static final Logger logger = LoggerFactory.getLogger(FileOrdeInfoHelper.class);


    public static void writeLastPlcOrder(String fileName, LastPushPlcOrderEntryInfo model) {
        try {
            String contentRootPath = ResourceUtils.getURL("classpath:").getPath();
            File d = new File(contentRootPath, "lastOrfder");
            if (!d.isDirectory()) {
                d.mkdir();
            }
            String lastOrderString = JsonUtils.toJsonString(model);
            File file = new File(d.getPath(), fileName + ".data");


            if (file.createNewFile()) {
                try (OutputStream out = new FileOutputStream(file);
                     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                ) {
                    bw.write(lastOrderString);
                    bw.flush();
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static LastPushPlcOrderEntryInfo readerLastPlcOrder(String fileName) {
        LastPushPlcOrderEntryInfo model = new LastPushPlcOrderEntryInfo();
        try {
            String path = ResourceUtils.getURL("classpath:").getPath();
            File d = new File(path, "lastOrfder");
            if (!d.isDirectory()) {
                d.mkdir();
            }
            File file = new File(d.getPath(), fileName + ".data");
            if (file.exists()) {

                try (InputStream is = new FileInputStream(file.getPath());) {
                    int iAvail = is.available();
                    byte[] bytes = new byte[iAvail];
                    if (is.read(bytes) > 0) {
                        model = JsonUtils.parseObject(new String(bytes), LastPushPlcOrderEntryInfo.class);
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return model;
    }
}
