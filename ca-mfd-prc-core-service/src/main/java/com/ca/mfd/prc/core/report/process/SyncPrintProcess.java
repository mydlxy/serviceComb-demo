package com.ca.mfd.prc.core.report.process;

import com.ca.mfd.prc.common.utils.DateUtils;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

@Component
//@ConditionalOnExpression("${inkelink.core.report.enable:false}")
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.report.enable'))}")
public class SyncPrintProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SyncPrintProcess.class);

    private static boolean token = true;

    @Value("${inkelink.core.report.syncInterval:}")
    private String report_syncInterval;
    @Resource
    private TaskExecutor task;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            task.execute(this::manageData);
            task.execute(this::deletePdf);
            logger.info("服务启动成功");
        } catch (Exception ex) {
            logger.error("服务器启动失败，错误消息：" + ex.getMessage());
        }
    }

    private void manageData() {
        IRptSendService rptSendService = SpringContextUtils.getBean(IRptSendService.class);
        IRptFileService rptFileService = SpringContextUtils.getBean(IRptFileService.class);
        IRptPrinterService rptPrinterService = SpringContextUtils.getBean(IRptPrinterService.class);
        IRptPrintQueueService rptPrintQueueService = SpringContextUtils.getBean(IRptPrintQueueService.class);
        while (true) {
            if (!SyncPrintProcess.token) {
                break;
            }
            try {
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
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                int syncInterval = 100;
                if (StringUtils.isNotBlank(report_syncInterval)) {
                    syncInterval = Integer.getInteger(report_syncInterval);
                }
                try {
                    Thread.sleep(syncInterval);
                } catch (InterruptedException ex) {
                    // 在这里处理中断异常
                    // 可以选择重新中断当前线程或执行其他逻辑
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted", ex);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }


    private void deletePdf() {
        //linux 下需要定时删除pdf 生成的文件
        if (SystemUtils.isLinux()) {
            while (true) {
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
                try {
                    Thread.sleep(1000L * 60 * 60 * 23);
                } catch (InterruptedException ex) {
                    // 在这里处理中断异常
                    // 可以选择重新中断当前线程或执行其他逻辑
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted", ex);
                } catch (Exception exception) {
                    logger.error("DeletePdf,错误消息" + exception.getMessage());
                }

                long now = new Date().getTime();
                long lastDay = new GregorianCalendar(9999,11,31).getTime().getTime();
                if(now > lastDay)
                {
                    return;
                }
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

    @Override
    public void destroy() {
        logger.info("关闭执行");
        token = false;
    }
}
