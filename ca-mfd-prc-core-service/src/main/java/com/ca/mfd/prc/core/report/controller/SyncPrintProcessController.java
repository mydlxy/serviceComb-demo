package com.ca.mfd.prc.core.report.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.SystemUtils;
import com.ca.mfd.prc.core.report.entity.RptFileEntity;
import com.ca.mfd.prc.core.report.entity.RptPrintQueueEntity;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.entity.RptSendEntity;
import com.ca.mfd.prc.core.report.service.IRptFileService;
import com.ca.mfd.prc.core.report.service.IRptPrintQueueService;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import com.ca.mfd.prc.core.report.service.IRptSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("report/syncprintprocess")
@Tag(name = "生成报表打印队列定调度", description = "生成报表打印队列调度")
public class SyncPrintProcessController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(SyncPrintProcessController.class);

    @GetMapping(value = "excute")
    @Operation(summary = "生成报表打印队列")
    public ResultVO<String> excute() {
        try {
            manageData();
            deletePdf();
        } catch (Exception ex) {
            logger.error("执行打印服务异常，错误消息：" + ex.getMessage());
        }
        return new ResultVO<String>().ok("", "处理成功");
    }
    private void manageData() {
        IRptSendService rptSendService = SpringContextUtils.getBean(IRptSendService.class);
        IRptFileService rptFileService = SpringContextUtils.getBean(IRptFileService.class);
        IRptPrinterService rptPrinterService = SpringContextUtils.getBean(IRptPrinterService.class);
        IRptPrintQueueService rptPrintQueueService = SpringContextUtils.getBean(IRptPrintQueueService.class);

        List<RptSendEntity> list = rptSendService.getListByStatus();
        List<RptPrintQueueEntity> printQueues = new ArrayList<>();
        for (RptSendEntity item : list) {
            int sendTime = item.getSendTimes() + 1;
            RptPrinterEntity printer = rptPrinterService.getInfoByBizCode(item.getBizCode());
            if (printer == null) {
                int status = 0;
                if (sendTime > 3) {
                    status = 2;
                }
                rptSendService.updateDataByStatus(status, sendTime, "打印代码不存在", item.getId());
                rptSendService.saveChange();
                continue;
            }
            RptFileEntity file = rptFileService.get(printer.getPrcRptFileId());
            if (file == null) {
                int status = 0;
                if (sendTime > 3) {
                    status = 2;
                }
                rptSendService.updateDataByStatus(status, sendTime, "打印报表文件不存在", item.getId());
                rptSendService.saveChange();
                continue;
            }

            try {
                int printNumber = printer.getPrintNumber();
                if (item.getPrintNumber() > 0) {
                    printNumber = item.getPrintNumber();
                }
                RptPrintQueueEntity data = new RptPrintQueueEntity();
                data.setPrcRptFileId(file.getId());
                data.setPrcRptPrinterId(printer.getId());
                data.setTargetId(item.getTargetId());
                data.setBizCode(printer.getBizCode());
                data.setDisplayName(StringUtils.isNotBlank(printer.getBizName()) ? printer.getBizName() : "");
                data.setPrintDt(item.getPrintDt());
                data.setPrintName(StringUtils.isNotBlank(printer.getBizName()) ? printer.getPrintName() : "");
                data.setBizName(StringUtils.isNotBlank(printer.getPrintName()) ? printer.getBizName() : "");
                //String parameters = JsonUtils.toJsonString(item.getParameters());
                String parameters = item.getParameters();
                if (StringUtils.isBlank(parameters)) {
                    parameters = "[]";
                }
                data.setParamaters(parameters);
                data.setIp(printer.getIp());
                data.setModel(StringUtils.isNotBlank(printer.getModel()) ? printer.getModel() : "");
                data.setPath(file.getPath());
                data.setPrintQty(0);
                data.setRemark(StringUtils.isNotBlank(item.getRemark()) ? item.getRemark() : "");
                data.setStatus(1);
                data.setStatusRemark("");
                data.setPrintNumber(printNumber);
                rptPrintQueueService.save(data);
                rptSendService.updateSendTimesById(1, new Date(), sendTime, "处理成功", item.getId());
                printQueues.add(data);
            } catch (Exception exInner) {
                logger.error(exInner.getMessage(), exInner);
            }
            rptSendService.saveChange();
        }
    }
    private void deletePdf() {
        if (SystemUtils.isLinux()) {
            try {
                String dt = DateUtils.format(DateUtils.addDateDays(new Date(), -7), DateUtils.DATE_PATTERN_C);
                String filespath = System.getProperty("user.dir") + "/wwwroot" + "/pdf" + dt;
                if (StringUtils.isNotBlank(filespath)) {
                    File file = new File(filespath);
                    //删除空文件夹
                    deletedire(file);
                    //删除过期文件
                    handleclearfiles(file);
                }
            } catch (Exception exe) {
                logger.error("DeletePdf,错误消息" + exe.getMessage());
            }

            long now = new Date().getTime();
            long lastDay = new GregorianCalendar(9999,11,31).getTime().getTime();
            if(now > lastDay)
            {
                return;
            }
        }
    }

    private boolean isDirectoryEmpty(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            return Objects.requireNonNull(file.list()).length == 0;
        } else {
            return false;
        }
    }
    private void handleclearfiles(File filepath) {
        File[] directorys = filepath.listFiles();
        if (directorys != null) {
            for (File dirs : directorys) {
                if (dirs.isDirectory()) {
                    handleclearfiles(dirs);
                }
                if (dirs.isFile()) {
                    String path = dirs.getAbsolutePath();
                    clearfiles(path);
                }
            }
        }
    }

    private void deletedire(File filepath) throws IOException {
        File[] directorys = filepath.listFiles();
        if (directorys != null) {
            for (File dirs : directorys) {
                if (dirs.isDirectory()) {
                    boolean isempty = isDirectoryEmpty(dirs);
                    if (isempty) {
                        Files.delete(dirs.toPath());
                    }
                }
            }
        }
    }
    private void clearfiles(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                Files.delete(file.toPath());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
