package com.ca.mfd.prc.core.report.dto;


import com.ca.mfd.prc.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;

public class AppConfig {
    private PrintOption Print;

    public AppConfig(String setingPrint) {
        getReportAppConfig(setingPrint);
    }

    public PrintOption getPrint() {
        return this.Print;
    }

    public void setPrint(PrintOption option) {
        this.Print = option;
    }

    private void getReportAppConfig(String setingPrint) {
        if (StringUtils.isNotBlank(setingPrint)) {
            PrintOption option = JsonUtils.parseObject(setingPrint, PrintOption.class);
            setPrint(option);
        } else {
            setPrint(new PrintOption());
        }
    }

}
