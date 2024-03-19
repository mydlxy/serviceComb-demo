package com.ca.mfd.prc.pqs.dto;

import cn.hutool.crypto.digest.DigestUtil;
import com.ca.mfd.prc.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * QMS接口对数据头数据块
 *
 * @Author: joel
 * @Date: 2023-04-25-14:21
 * @Description:
 */
@Data
@Schema(description = "QMS接口对数据头数据块")
public class QmsApiHeaderInfo {
    private String sender;

    private String receiver;

    private String recid;

    private String dtsend = DateUtils.format(new Date(), "yyyyMMddHHmmss");

    private String busid = "";

    public String createRecId() {
        return DigestUtil.md5Hex(sender + receiver + dtsend + busid);
    }
}
