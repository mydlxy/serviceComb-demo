package com.ca.mfd.prc.core.report.process;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.NetworkUtils;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.report.dto.AppConfig;
import com.ca.mfd.prc.core.report.dto.PrintPackageItem;
import com.ca.mfd.prc.core.report.dto.PrinterStatusItems;
import com.ca.mfd.prc.core.report.dto.ReportPrintQueueDycVO;
import com.ca.mfd.prc.core.report.dto.ReportPrinterDyc;
import com.ca.mfd.prc.core.report.dto.StatusCheckItem;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.service.IRptPrintQueueService;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.report.enable'))}")
public class PrintProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(PrintProcess.class);
    private static boolean token = true;
    private static final ConcurrentMap<String, ConcurrentLinkedQueue<ReportPrintQueueDycVO>> reportPrintQueue
            = new ConcurrentHashMap<>();
    @Resource
    private TaskExecutor task;

    @Value("${inkelink.report.queueInterval:100}")
    private String print_queueInterval;

    @Value("${inkelink.report.threadsleep:100}")
    private String print_threadsleep;

    @Value("${inkelink.core.rptfile.path:}")
    private String rptFilePath;


    @Autowired
    private RestTemplateUtil _httpHelper;
    private final ConcurrentMap<String, Long> cacheQueue = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        IRptPrinterService rptPrinterService = SpringContextUtils.getBean(IRptPrinterService.class);
        List<RptPrinterEntity> lists = rptPrinterService.getListAll();
        String resultStr = getReportConfigure();
        AppConfig appConfig = new AppConfig(resultStr);
        List<ReportPrinterDyc> datas = lists.stream().map(s -> {
            ReportPrinterDyc info = new ReportPrinterDyc();
            info.setId(s.getId());
            info.setPrintName(s.getPrintName());
            info.setIp(s.getIp());
            info.setModel(s.getModel());
            return info;
        }).collect(Collectors.toList());

        if (appConfig.getPrint() != null && appConfig.getPrint().getPrinter() != null && appConfig.getPrint().getPrinter().size() > 0) {
            String[] array = appConfig.getPrint().getPrinter().toArray(new String[appConfig.getPrint().getPrinter().size()]);
            datas = datas.stream().filter(s -> ArrayUtil.containsIgnoreCase(array, s.getPrintName())).collect(Collectors.toList());
        }
        Map<String, List<ReportPrinterDyc>> map = datas.stream().collect(Collectors.groupingBy(ReportPrinterDyc::getIp));
        for (Map.Entry<String, List<ReportPrinterDyc>> kv : map.entrySet()) {
            reportPrintQueue.put(kv.getKey(), new ConcurrentLinkedQueue<>());
        }
        task.execute(() -> {
            try {
                queueProcces();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
        for (Map.Entry<String, List<ReportPrinterDyc>> kv : map.entrySet()) {
            try {
                task.execute(() -> reportPrint(kv.getKey()));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void queueProcces() throws InterruptedException {
        IRptPrintQueueService rptPrintQueueService = SpringContextUtils.getBean(IRptPrintQueueService.class);
        IRptPrinterService rptPrinterService = SpringContextUtils.getBean(IRptPrinterService.class);
        while (true) {
            try {
                if (!PrintProcess.token) {
                    break;
                }
                Long printQueueQty = rptPrintQueueService.getCountNumberByStatus();
                if (printQueueQty <= 0) {
                    int queueInterval = 100;
                    if (StringUtils.isNotBlank(print_queueInterval)) {
                        queueInterval = Integer.parseInt(print_queueInterval);
                    }
                    Thread.sleep(queueInterval);
                    continue;
                }
                List<String> ips = new ArrayList<>();
                // 使用for-each循环遍历map中的元素
                for (Map.Entry<String, ConcurrentLinkedQueue<ReportPrintQueueDycVO>> entry : reportPrintQueue.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        ips.add(entry.getKey());
                    }
                }
                if (ips.isEmpty()) {
                    int queueInterval = 100;
                    if (StringUtils.isNotBlank(print_queueInterval)) {
                        queueInterval = Integer.parseInt(print_queueInterval);
                    }
                    Thread.sleep(queueInterval);
                    continue;
                }
                List<ReportPrintQueueDycVO> allList = rptPrintQueueService.queueList(ips);
                for (Map.Entry<String, ConcurrentLinkedQueue<ReportPrintQueueDycVO>> entry : reportPrintQueue.entrySet()) {
                    if (ips.contains(entry.getKey())) {
                        List<ReportPrintQueueDycVO> list = allList.stream().filter(s -> StringUtils.equals(s.getIp(), entry.getKey()))
                                .sorted(Comparator.comparing(ReportPrintQueueDycVO::getPrintDt)).collect(Collectors.toList());
                        if (list.isEmpty()) {
                            logger.info(entry.getKey(), "没有要推送打印的记录");
                            continue;
                        }
                        logger.info(entry.getKey(), "待推送打印总条数:" + list.size());
                        for (ReportPrintQueueDycVO dataItem : list) {
                            entry.getValue().offer(dataItem);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                // 在这里处理中断异常
                // 可以选择重新中断当前线程或执行其他逻辑
                Thread.currentThread().interrupt();
                logger.error("Sleep interrupted", ex);
            }  catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                int queueInterval = 100;
                if (StringUtils.isNotBlank(print_queueInterval)) {
                    queueInterval = Integer.parseInt(print_queueInterval);
                }
                try {
                    Thread.sleep(queueInterval);
                } catch (InterruptedException ex) {
                    // 在这里处理中断异常
                    // 可以选择重新中断当前线程或执行其他逻辑
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted", ex);
                }
            }
        }
    }

    private void reportPrint(String ip) {
        String resultStr = getReportConfigure();
        AppConfig appConfig = new AppConfig(resultStr);
        while (true) {
            try {
                if (!PrintProcess.token) {
                    break;
                }
                ConcurrentLinkedQueue<ReportPrintQueueDycVO> value;
                if (!reportPrintQueue.containsKey(ip) || reportPrintQueue.getOrDefault(ip, null).isEmpty()) {
                    int printInterval = 100;
                    if (StringUtils.isNotBlank(print_threadsleep)) {
                        printInterval = Integer.parseInt(print_queueInterval);
                    }
                    Thread.sleep(printInterval);
                    continue;
                } else {
                    value = reportPrintQueue.get(ip);
                }
                IRptPrintQueueService reportPrintQueueService = SpringContextUtils.getBean(IRptPrintQueueService.class);
                try {
                    while (!value.isEmpty() && value.peek() != null) {
                        ReportPrintQueueDycVO item = value.peek();
                        if (item != null && item.getData() != null && item.getData().getType() == 1) {
                            processPrint(item, appConfig, reportPrintQueueService);
                            value.remove(item); //移除队列
                        } else {
                            boolean isPrint = processPrintMark(item, reportPrintQueueService);
                            if (isPrint) {
                                value.remove(item);
                            }
                        }
                    }
                } catch (Exception ex) {
                    value.clear();
                    throw new InkelinkException(ex.getMessage());
                }
            } catch (InterruptedException ex) {
                // 在这里处理中断异常
                // 可以选择重新中断当前线程或执行其他逻辑
                Thread.currentThread().interrupt();
                logger.error("Sleep interrupted", ex);
            } catch (Exception ex) {
                logger.error(ip + ":" + ex.getMessage(), ex);
            } finally {
                int printInterval = 100;
                if (StringUtils.isNotBlank(print_threadsleep)) {
                    printInterval = Integer.parseInt(print_threadsleep);
                }
                try {
                    Thread.sleep(printInterval);
                } catch (InterruptedException ex) {
                    // 在这里处理中断异常
                    // 可以选择重新中断当前线程或执行其他逻辑
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted", ex);
                }  catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private void processPrint(ReportPrintQueueDycVO item, AppConfig appConfig, IRptPrintQueueService rptPrintQueueService) {
        try {
            PrinterStatusItems res = tryGetPrinterStatus(item, appConfig);
            if (Boolean.FALSE.equals(res.getResult())) {
                throw new InkelinkException(res.getMessage());
            }
            long id = cacheQueue.getOrDefault(item.getIp(), 0L);
            if (id != 0 || id != item.getId()) {
                print(item, appConfig);
                cacheQueue.put(item.getIp(), item.getId());
            }
            int count = item.getPrintCount() + 1;
            rptPrintQueueService.updatePrintQtyById(2, count, item.getId());
            rptPrintQueueService.saveChange();
        } catch (Exception exinner) {
            throw new InkelinkException(exinner.getMessage());
        }
    }

    private String getReportConfigure() {
        String resultStr = StringUtils.EMPTY;
        ISysConfigurationService sysConfigurationService = SpringContextUtils.getBean(ISysConfigurationService.class);
        List<SysConfigurationEntity> list = sysConfigurationService.getSysConfigurations("ReportConfigure");
        if (!CollectionUtils.isEmpty(list)) {
            SysConfigurationEntity info = list.get(0);
            if (!Objects.isNull(info)) {
                resultStr = info.getValue();
            }
        }
        return resultStr;
    }

    private void print(ReportPrintQueueDycVO item, AppConfig appConfig) throws Exception {
        String reportPath = item.getPath();
        reportPath = rptFilePath + "ureport/" + "pdf?_u=file:" + reportPath;
        if (!Strings.isNullOrEmpty(item.getParamaters())) {
            HashMap hashMap = JsonUtils.parseObject(item.getParamaters(), HashMap.class);
            if (hashMap != null && hashMap.size() > 0) {
                if (reportPath.contains("?")) {
                    reportPath = reportPath + "&" + Joiner.on("&")
                            .useForNull("")
                            .withKeyValueSeparator("=")
                            .join(hashMap);
                } else {
                    reportPath = reportPath + "?" + Joiner.on("&")
                            .useForNull("")
                            .withKeyValueSeparator("=")
                            .join(hashMap);
                }
            }
        }
        InputStream inputStream = null;
        try {
            //获取报表路径的流
            inputStream = downLoadByUrl(reportPath);
            //调用打印
            CupsClient cupsClient = new CupsClient(item.getIp(), 631);
            CupsPrinter cupsPrinter = cupsClient.getPrinter(item.getPrintName());
            Map<String, String> attr = new HashMap<>();
            attr.put("document-format", "application/octet-stream");
            attr.put("attributes-charset", "utf-8");
            boolean portrait = false;
            PrintPackageItem printPackageItem = appConfig.getPrint().getPageOrientation().stream().findFirst().filter(s -> s.getPrintName().equalsIgnoreCase(item.getPrintName()) && s.getCode().equalsIgnoreCase(item.getCode())).orElse(null);
            if (printPackageItem != null && printPackageItem.getOrientation() == 1) {
                portrait = true;
            }
            String pageFormat = "iso-a4";
            boolean itemNullCheck = printPackageItem != null
                    && printPackageItem.getPaper() != null
                    && !Strings.isNullOrEmpty(printPackageItem.getPaper().getName());
            boolean widthAndHeightCheck = printPackageItem.getPaper().getWidth() > 0
                    && printPackageItem.getPaper().getHeight() > 0;
            if (itemNullCheck && widthAndHeightCheck) {
                pageFormat = printPackageItem.getPaper().getHeight() + "x" + printPackageItem.getPaper().getWidth() + "mm";
            }
            //组装打印参数
            PrintJob printJob = new PrintJob.Builder(inputStream)
                    .jobName(item.getCode())
                    .copies(item.getPrintNumber())
                    .duplex(false)   //双面
                    .portrait(portrait)    //纵向
                    .color(false)
                    .pageFormat(pageFormat)
                    .attributes(attr)
                    .build();
            cupsPrinter.print(printJob);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private boolean processPrintMark(ReportPrintQueueDycVO item, IRptPrintQueueService rptPrintQueueService) {
        //循环中任何打印报异常直接抛出，下次循环继续尝试推送打印队列
        try {
            boolean isPrint = false;

            Long id = cacheQueue.getOrDefault(item.getIp(), 0L);

            if (id != 0 || id != item.getId()) {
                for (int i = 0; i < item.getPrintNumber(); i++) {
                    //isPrint = 斑马打印
                    isPrint = sendStringToPrinter(item.getIp(), item.getData().getContent());
                }
                //避免出现数据库异常导致的重复触发打印
                cacheQueue.put(item.getIp(), item.getId());
            } else {
                isPrint = true;
            }
            if (isPrint) {
                int count = item.getPrintCount() + 1;
                rptPrintQueueService.updatePrintQtyById(2, count, item.getId());
                rptPrintQueueService.saveChange();
            }
            return isPrint;
        } catch (Exception exinner) {
            throw new InkelinkException(exinner.getMessage());
        }
    }

    private boolean sendStringToPrinter(String ip, String content) {
        boolean result = false;
        try {
            com.zebra.sdk.comm.Connection connection = new TcpConnection(ip, 9100);
            if (!connection.isConnected()) {
                connection.open();
            }
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            printer.sendCommand(content);
            connection.close();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private PrinterStatusItems tryGetPrinterStatus(ReportPrintQueueDycVO data, AppConfig appConfig) {
        String key = appConfig.getPrint().getStatusCheck().keySet().stream()
                .filter(s -> {
                    if (data != null) {
                        return s.equalsIgnoreCase(data.getModel());
                    }
                    return false;
                }).findFirst().orElse("");
        if (StringUtils.isNotBlank(key)) {
            StatusCheckItem item = appConfig.getPrint().getStatusCheck().get(key);
            int times = 1;
            do {
                String statusText = "未获取正确的xpath";
                try {
                    boolean res = NetworkUtils.ping(data.getIp());
                    if (!res) {
                        break;
                    }
                    ResponseEntity requestTxt = _httpHelper.getStringForResponse(data.getIp() + item.getPath());
                    if (!requestTxt.getStatusCode().is2xxSuccessful() || requestTxt.getBody() == null) {
                        break;
                    }
                    String html = requestTxt.getBody().toString();
                    if (StringUtils.isBlank(html)) {
                        break;
                    }
                    Document document = Jsoup.parse(html);
                    for (String xpath : item.getXpaths()) {
                        Elements node = document.select(xpath);
                        if (node.isEmpty()) {
                            continue;
                        }
                        statusText = node.text();
                        for (String str : item.getReplaces()) {
                            statusText = StrUtil.replace(statusText, str, "", true);
                        }
                        if (StringUtils.isNotBlank(statusText)) {
                            for (String ok : item.getOks()) {
                                Pattern pattern = Pattern.compile(ok);
                                Matcher matcher = pattern.matcher(statusText);
                                if (statusText.equalsIgnoreCase(ok) || StringUtils.containsIgnoreCase(statusText, ok) || matcher.matches()) {
                                    return getResultItems(true, statusText);
                                }
                            }
                        }
                    }
                    return getResultItems(false, statusText);
                } catch (Exception ex) {
                    statusText = "异常";
                    logger.info(data.getIp() + ",QueueId:(" + data.getId() + "),第" + times + "次尝试获取打印机(" + data.getPrintName() + ")状态:" + statusText, ex);
                }
                logger.info(data.getIp() + ",QueueId:(" + data.getId() + "),第" + times + "次尝试获取打印机(" + data.getPrintName() + ")状态:" + statusText);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    // 在这里处理中断异常
                    // 可以选择重新中断当前线程或执行其他逻辑
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted", ex);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
                times++;
            } while (times <= 3);
        } else {
            try {
                // 当不是HP打印机，而是其他打印机，如标签打印机，我们只需要ping通即可
                boolean prep = NetworkUtils.ping(data.getIp());
                // 如果能ping通
                if (prep) {
                    logger.info(data.getIp(), "QueueId:(" + data.getId() + "),尝试获取打印机(" + data.getPrintName() + "),状态:就绪");
                    return getResultItems(true, "就绪");
                }
                logger.info(data.getIp(), "QueueId:(" + data.getId() + "),尝试获取打印机(" + data.getPrintName() + "),状态:离线");
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
        return getResultItems(false, "未知");
    }

    private PrinterStatusItems getResultItems(Boolean result, String message) {
        PrinterStatusItems resItems = new PrinterStatusItems();
        resItems.setResult(result);
        resItems.setMessage(message);
        return resItems;
    }

    @Override
    public void destroy() {
        logger.info("关闭执行");
        token = false;
    }

    private InputStream downLoadByUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Content-Type", "application/octet-stream; charset=ISO8859-1");
        //得到输入流
        return conn.getInputStream();
    }
}
