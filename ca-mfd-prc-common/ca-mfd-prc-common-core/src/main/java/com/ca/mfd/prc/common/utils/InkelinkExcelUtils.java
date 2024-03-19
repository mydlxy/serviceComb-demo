package com.ca.mfd.prc.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.hutool.core.collection.CollUtil;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel工具类
 *
 * @author lty
 * @date 2023/3/31
 */
public class InkelinkExcelUtils {

    private InkelinkExcelUtils(){}

    private static List<ExcelExportEntity> dynaCol(Map<String, String> field) {
        List<ExcelExportEntity> colList = new ArrayList<>();
        field.forEach((key, value) -> colList.add(new ExcelExportEntity(value, key)));
        return colList;
    }

    public static List<Map<String, Object>> getListMap(List<?> list) {
        return getListMap(list, false);
    }

    public static List<Map<String, Object>> getListMap(List<?> list, boolean isSimpleDate) {
        if (list == null) {
            list = new ArrayList<>();
        }
        List<Map<String, Object>> mapList = new ArrayList<>();
        //迭代数据，获取导出字段所需要的数据
        list.forEach(info -> {
            //获取所有属性
            Field[] fields = getAllFields(info.getClass());
            Map<String, Object> map = new HashMap<>(fields.length);
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                try {
                    //获取配置字段的属性
                    if (field.get(info) != null) {
                        map.put(field.getName(), convert(field.get(info), field.getType()));
                        if (isSimpleDate && field.getType().equals(Date.class)) {
                            map.put(field.getName(), simpleDateFiledConvert(field.get(info), field.getType()));
                        }
                    }
                } catch (Exception e) {
                    throw new InkelinkException(e.getMessage());
                }
            }
            mapList.add(map);
        });
        return mapList;
    }

    /**
     * Excel导出
     *
     * @param fieldParam
     * @param mapList
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportByDc(Map<String, String> fieldParam, List<Map<String, Object>> mapList, String fileName, HttpServletResponse response) throws IOException {
        ExportParams excellPms = new ExportParams();
        if (StringUtils.isNotBlank(fileName)) {
            excellPms.setSheetName(URLDecoder.decode(fileName, "UTF-8"));
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }
        List<ExcelExportEntity> beanList = dynaCol(fieldParam);
        beanList.add(0, new ExcelExportEntity("序号", "NO"));
        if (CollUtil.isNotEmpty(mapList)) {
            for (int i = 0; i < mapList.size(); i++) {
                mapList.get(i).put("NO", i + 1);
            }
        }
        if (CollUtil.isEmpty(mapList)) {
            mapList.add(Maps.newHashMapWithExpectedSize(0));
        }
        XSSFWorkbook workbook = (XSSFWorkbook) ExcelExportUtil.exportExcel(excellPms, beanList, mapList);
        fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
      //  response.setHeader("Content-Disposition", "attachment;" + fileName + "; filename*=UTF-8''" + fileName);
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("DownLoadFileName", fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * Excel导出(多sheet,每个sheet表结构相同，sheetname自定义)
     *
     * @param sheetNames
     * @param fieldParam
     * @param mapLists
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportSheets(List<String> sheetNames, Map<String, String> fieldParam,
                                    List<List<Map<String, Object>>> mapLists, String fileName,
                                    HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(fileName)) {
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }
        Workbook workbook = new XSSFWorkbook();
        for (int i = 0; i < mapLists.size(); i++) {
            List<Map<String, Object>> mapList = mapLists.get(i);
            // 添加序号
            List<ExcelExportEntity> beanList = dynaCol(fieldParam);
            beanList.add(0, new ExcelExportEntity("序号", "NO"));
            if (CollUtil.isNotEmpty(mapList)) {
                for (int j = 0; j < mapList.size(); j++) {
                    mapList.get(j).put("NO", j + 1);
                }
            }
            if (CollUtil.isEmpty(mapList)) {
                mapList.add(Maps.newHashMapWithExpectedSize(0));
            }
            //创建sheet参数
            ExportParams excellPms = new ExportParams();
            excellPms.setSheetName(sheetNames.get(i));
            //创建sheet
            ExcelExportService service = new ExcelExportService();
            service.createSheetForMap(workbook, excellPms, beanList, mapList);
        }
        fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
       // response.setHeader("Content-Disposition", "attachment;" + fileName + "; filename*=UTF-8''" + fileName);
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("DownLoadFileName", fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * Excel导出(多sheet,fieldParam定义表结构，fieldParam数量要与sheet数量相同，sheetname自定义)
     *
     * @param sheetNames
     * @param fieldParam
     * @param mapLists
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportSheets(List<String> sheetNames, List<Map<String, String>> fieldParam,
                                    List<List<Map<String, Object>>> mapLists, String fileName,
                                    HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(fileName)) {
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }
        Workbook workbook = new XSSFWorkbook();
        for (int i = 0; i < sheetNames.size(); i++) {
            // 表头添加序号
            List<ExcelExportEntity> beanList = dynaCol(fieldParam.get(i));
            beanList.add(0, new ExcelExportEntity("序号", "NO"));
            List<Map<String, Object>> mapList;
            if (CollUtil.isEmpty(mapLists) || mapLists.size() < i + 1) {
                mapList = new ArrayList<>();
            } else {
                mapList = mapLists.get(i);
                if (CollUtil.isNotEmpty(mapList)) {
                    for (int j = 0; j < mapList.size(); j++) {
                        mapList.get(j).put("NO", j + 1);
                    }
                }
            }

            if (CollUtil.isEmpty(mapList)) {
                mapList.add(Maps.newHashMapWithExpectedSize(0));
            }
            //创建sheet参数
            ExportParams excellPms = new ExportParams();
            excellPms.setSheetName(sheetNames.get(i));
            //创建sheet
            ExcelExportService service = new ExcelExportService();
            service.createSheetForMap(workbook, excellPms, beanList, mapList);
        }
        fileName = URLEncoder.encode(fileName + ".xlsx", "utf-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("DownLoadFileName", fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * Excel导出 用于导入模板
     *
     * @param fieldParam
     * @param mapList
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void createEmptyExcel(Map<String, String> fieldParam, List<Map<String, Object>> mapList, String fileName, HttpServletResponse response) throws IOException {
        ExportParams excellPms = new ExportParams();
        if (StringUtils.isNotBlank(fileName)) {
            excellPms.setSheetName(URLDecoder.decode(fileName, "UTF-8"));
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }
        List<ExcelExportEntity> beanList = dynaCol(fieldParam);
        //构造首行
        if (CollUtil.isEmpty(mapList)) {
            mapList.add(Maps.newHashMapWithExpectedSize(0));
        }
        XSSFWorkbook workbook = (XSSFWorkbook) ExcelExportUtil.exportExcel(excellPms, beanList, mapList);
        fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
       // response.setHeader("Content-Disposition", "attachment;" + fileName + "; filename*=UTF-8''" + fileName);
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("DownLoadFileName", fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * Excel导出 用于导入模板（多Sheet）
     *
     * @param fieldParams
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void createEmptyExcel(List<Map<String, String>> fieldParams, List<String> sheetNames, String fileName, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(fileName)) {
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }

        Workbook workbook = new XSSFWorkbook();
        for (int i = 0; i < fieldParams.size(); i++) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            mapList.add(Maps.newHashMapWithExpectedSize(0));
            // 添加序号
            List<ExcelExportEntity> beanList = dynaCol(fieldParams.get(i));

            //创建sheet参数
            ExportParams excellPms = new ExportParams();
            excellPms.setSheetName(sheetNames.get(i));
            //创建sheet
            ExcelExportService service = new ExcelExportService();
            service.createSheetForMap(workbook, excellPms, beanList, mapList);
        }

        fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
       // response.setHeader("Content-Disposition", "attachment;" + fileName + "; filename*=UTF-8''" + fileName);
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("DownLoadFileName", fileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * Excel导出
     *
     * @param response  response
     * @param fileName  文件名
     * @param list      数据List
     * @param pojoClass 对象Class
     */
    public static void export(HttpServletResponse response, String fileName, Collection<?> list,
                              Class<?> pojoClass) throws IOException {
        ExportParams excellPms = new ExportParams();
        if (StringUtils.isNotBlank(fileName)) {
            excellPms.setSheetName(URLDecoder.decode(fileName, "UTF-8"));
            //当前日期
            fileName = fileName + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        } else {
            fileName = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(excellPms, pojoClass, list);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }

    /**
     * 获取导入模板
     *
     * @param fieldParam
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void getImportTemplate(Map<String, String> fieldParam, String fileName, HttpServletResponse response) throws IOException {
        createEmptyExcel(fieldParam, new ArrayList<>(), fileName, response);
    }

    /**
     * 导入
     *
     * @param is
     * @param fieldParam
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> importExcel(InputStream is, Map<String, String> fieldParam) throws Exception {
        return getDicDataFromExcel(is);
    }

    /**
     * 导入(多Sheet)
     *
     * @param is InputStream
     * @return List
     * @throws Exception
     */
    public static List<List<Map<String, String>>> importMultipleSheetExcel(InputStream is) throws Exception {
        return getDicDataFromMultipleSheetExcel(is);
    }

    /**
     * 导入
     *
     * @param is
     * @param sheets
     * @return
     * @throws Exception
     */
    public static Map<String, List<Map<String, String>>> importExcel(InputStream is, String[] sheets) throws Exception {
        return getDicDataFromExcel(is, sheets);
    }

    /**
     * 字段类型转换
     */
    private static <T> T convert(Object obj, Class<T> type) throws ParseException {
        if (obj != null && StringUtils.isNotBlank(obj.toString())) {
            if (type.equals(String.class)) {
                return (T) obj.toString();
            } else if (type.equals(BigDecimal.class)) {
                return (T) new BigDecimal(obj.toString());
            } else if (type.equals(Double.class)) {
                return (T) Double.valueOf(obj.toString());
            } else if (type.equals(Integer.class)) {
                return (T) Integer.valueOf(obj.toString());
            } else if (type.equals(Date.class)) {
                if (obj != null) {
                    if (obj.getClass().equals(Date.class)) {
                        return (T) obj;
                    } else {
                        String timeStr = String.valueOf(obj);
                        String[] s = timeStr.split("T");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        return (T) sdf.parse(s[0] + " " + s[1]);
                    }
                } else {
                    return null;
                }
            } else {
                //其他类型转换
                return (T) obj.toString();
            }

        }
        return null;
    }

    /**
     * 日期时间类型格式化
     *
     * @param obj  成员
     * @param type 类型
     * @param <T>  类型
     * @return 返回转换后的对象
     */
    private static <T> Object simpleDateFiledConvert(Object obj, Class<T> type) {
        if (type.equals(Date.class)) {
            return DateUtils.format((Date) obj, DateUtils.DATE_TIME_PATTERN);
        }
        return obj;
    }

    private static Map<String, List<Map<String, String>>> getDicDataFromExcel(InputStream is, String[] sheets) throws Exception {
        Map<String, List<Map<String, String>>> mps = new LinkedHashMap<>();
        Workbook workbook = WorkbookFactory.create(is);
        for (String sh : sheets) {
            Sheet sheet = workbook.getSheet(sh);
            if (sheet == null) {
                continue;
            }
            List<Map<String, String>> list = getDicDataFromSheet(sheet);
            mps.put(sh, list);
        }
        return mps;
    }

    private static List<Map<String, String>> getDicDataFromExcel(InputStream is) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        return getDicDataFromSheet(sheet);
    }

    /**
     * 多Sheet情况
     *
     * @param is InputStream
     * @return List
     * @throws IOException e
     */
    private static List<List<Map<String, String>>> getDicDataFromMultipleSheetExcel(InputStream is) throws IOException {
        List<List<Map<String, String>>> list = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            List<Map<String, String>> eachList = getDicDataFromSheet(sheet);
            list.add(eachList);
        }

        return list;
    }

    private static List<Map<String, String>> getDicDataFromSheet(Sheet sheet) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        if (sheet == null) {
            throw new IOException("请上传Excel文件");
        }
        // 获取最后一个非空行的行下标，比如总行数为n，则返回的为n-1
        int rows = sheet.getLastRowNum();

        if (rows > 0) {

            // 获取表头
            Row heard = sheet.getRow(0);
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> dictionary = new HashMap<>(heard.getPhysicalNumberOfCells());
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++) {
                Cell cell = heard.getCell(i);
                if (cell != null) {
                    String value = getCellValue(heard, i).toString();
                    dictionary.put(value, i);
                }
            }
            for (int i = 1; i <= rows; i++) {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                // 判断当前行是否是空行
                if (isRowEmpty(row)) {
                    continue;
                }
                Map<String, String> dictionary2 = new HashMap<>(dictionary.size());
                dictionary.forEach((key, value) -> {

                    String values = getCellValue(row, value).toString();
                    dictionary2.put(key, values);

                });
                list.add(dictionary2);
            }

        }
        return list;
    }


    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    private static Object getCellValue(Row row, int column) {
        if (row == null) {
            return null;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case NUMERIC:
                    case FORMULA:
                        val = cell.getNumericCellValue();
                        if (DateUtil.isCellDateFormatted(cell)) {
                            val = DateUtil.getJavaDate((Double) val);
                        } else {
                            if ((Double) val % 1 != 0) {
                                val = new BigDecimal(val.toString());
                            } else {
                                val = new DecimalFormat("0").format(val);
                            }
                        }
                        break;
                    case STRING:
                        val = cell.getStringCellValue();
                        break;
                    case BOOLEAN:
                        val = cell.getBooleanCellValue();
                        break;
                    case ERROR:
                        val = cell.getErrorCellValue();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    /**
     * 判断是否是空行
     *
     * @param row 判断的行
     * @return boolean
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }
}
