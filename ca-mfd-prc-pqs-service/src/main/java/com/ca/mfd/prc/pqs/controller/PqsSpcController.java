package com.ca.mfd.prc.pqs.controller;


import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.obs.config.LocalUploadProperties;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.ControlChartsConstantsDto;
import com.ca.mfd.prc.pqs.dto.FeatureInfo;
import com.ca.mfd.prc.pqs.dto.FeatureInfoDtos;
import com.ca.mfd.prc.pqs.dto.FeaturePointsDto;
import com.ca.mfd.prc.pqs.dto.FeatureResDto;
import com.ca.mfd.prc.pqs.dto.FeatureSaveDto;
import com.ca.mfd.prc.pqs.dto.FeatureValueDto;
import com.ca.mfd.prc.pqs.dto.FileSaveDto;
import com.ca.mfd.prc.pqs.dto.PairDto;
import com.ca.mfd.prc.pqs.dto.SpcDto;
import com.ca.mfd.prc.pqs.dto.SpcFileDto;
import com.ca.mfd.prc.pqs.dto.SpcQueryParamDto;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileRecordEntity;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileUserLogEntity;
import com.ca.mfd.prc.pqs.entity.PqsSpcOperationRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsSpcFileRecordService;
import com.ca.mfd.prc.pqs.service.IPqsSpcFileUserLogService;
import com.ca.mfd.prc.pqs.service.IPqsSpcOperationRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Floats;
import io.swagger.v3.oas.annotations.Operation;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.nd4j.linalg.indexing.NDArrayIndex.interval;

/**
 * spc
 *
 * @author inkelink
 */
@RestController
@RequestMapping("pqsspc")
public class PqsSpcController {


    private final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;
    @Autowired
    private LocalUploadProperties localUploadProperties;
    @Autowired
    private IPqsSpcFileRecordService pqsSpcFileRecordService;
    @Autowired
    private IPqsSpcFileUserLogService pqsSpcFileUserLogService;
    @Autowired
    private IPqsSpcOperationRecordService pqsSpcOperationRecordService;


    @Value("${inkelink.host:http://localhost}")
    private String ip;

    @Autowired
    public PqsSpcController(ObjectMapper mapper, ResourceLoader resourceLoader) throws NoSuchAlgorithmException {
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
    }

    //    @PostMapping(value = "/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResultVO<List<SpcDto>> uploadJsonFile(@RequestParam("jsonFile") MultipartFile file) throws IOException {
//        Path path = Files.createTempFile(null, null);
//        file.transferTo(path);
//        List<SpcDto> SpcDtoList = mapper.readValue(path.toFile(), new TypeReference<List<SpcDto>>() {});
//        String uploadJson = mapper.writeValueAsString(SpcDtoList);
//        String dsMd5 = EncryptionUtils.md5(uploadJson);
//        // 在这里处理解析后的JSON数据
//
//        return new ResultVO<List<SpcDto>>().ok(SpcDtoList, "获取数据成功");
//    }

    @PostMapping("/saveoperationrecord")
    @Operation(summary = "")
    public ResultVO saveOperationRecord(@RequestBody FeatureSaveDto featureSaveDto) throws IOException {
        if (featureSaveDto == null) {
            throw new InkelinkException("保存请求参数不能为空");
        }
        String fileName = featureSaveDto.getFileName();
        FeatureInfo featureInfo = featureSaveDto.getFeatureInfo();
        if (featureInfo == null) {
            throw new InkelinkException("保存请求结果参数不能为空");
        }
        String resultJson = mapper.writeValueAsString(featureInfo);
        String resultName = featureSaveDto.getResultName();

        if (StringUtils.isBlank(resultName)) {
            throw new InkelinkException("结果名称不能为空");
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = "默认";
        }
        List<SpcDto> spcDtoList = featureInfo.getSpcDtoList();
        List<SpcQueryParamDto> QueryParamList = spcDtoList.stream().map(e -> e.getOperationParam()).collect(Collectors.toList());
        for (SpcDto spcDto : spcDtoList) {
            spcDto.setOperationParam(null);
        }
        String fileJson = mapper.writeValueAsString(spcDtoList);
        String fileMd5 = EncryptionUtils.md5(fileJson);
        String operationJson = mapper.writeValueAsString(QueryParamList.get(0));
        String operationMd5 = EncryptionUtils.md5(operationJson);
        //查询该文件此次操作是否存在
        PqsSpcFileRecordEntity pqsSpcFileRecordEntity = pqsSpcFileRecordService.getFileRecordByMd5AndName(fileMd5, fileName);
        if (pqsSpcFileRecordEntity == null) {
            throw new InkelinkException("远端文件不存在，请检查");
        }
//        String pqsrusult = mediaProvider.postfiles("pqsrusult", mapper.writeValueAsString(featureInfo));
        PqsSpcOperationRecordEntity resultNameEntity = pqsSpcOperationRecordService.queryByFileAndResultName(pqsSpcFileRecordEntity.getId(), resultName);
        if (resultNameEntity != null) {
            throw new InkelinkException("结果名已存在,请修改后再次保存");
        }
        PqsSpcOperationRecordEntity pqsSpcOperationRecordEntity = pqsSpcOperationRecordService.queryByFileAndOperationMd5(pqsSpcFileRecordEntity.getId(), operationMd5);
        String message = "保存结果已存在，无需继续保存";
        if (pqsSpcOperationRecordEntity == null) {
            pqsSpcOperationRecordEntity = new PqsSpcOperationRecordEntity();
            pqsSpcOperationRecordEntity.setPrcPqsSpcFileRecordId(pqsSpcFileRecordEntity.getId());
            pqsSpcOperationRecordEntity.setOperationParams(operationJson);
            pqsSpcOperationRecordEntity.setOperationParamsMd5(operationMd5);
            pqsSpcOperationRecordEntity.setOperationCount("1");
            pqsSpcOperationRecordEntity.setBackFilePath(resultJson);
            pqsSpcOperationRecordEntity.setAttribute1(resultName);
            pqsSpcOperationRecordService.insert(pqsSpcOperationRecordEntity);
            pqsSpcOperationRecordService.saveChange();
            message = "保存完成";
        }
        return new ResultVO<>().ok("", message);

    }

    @PostMapping(value = "/saverecord")
    @Operation(summary = "保存文件记录")
    ResultVO saveRecord(@RequestBody FileSaveDto fileSaveDto) throws IOException {
        String fileName = fileSaveDto.getFileName();
        if (StringUtils.isBlank(fileName)) {
            fileName = "默认";
        }
        String filepath = fileSaveDto.getFilepath();
        if (StringUtils.isBlank(filepath)) {
            throw new InkelinkException("请输入上传文件地址");
        }
        String remark = fileSaveDto.getRemark();
        String httpPath = ip + filepath;
        XSSFWorkbook workbook = downloadWorkbookFromUrl(httpPath);
        ArrayList<SpcFileDto> spcFileDtos = new ArrayList<>();
        try {
            spcFileDtos = xlsxData(workbook);
        } catch (Exception e) {
            throw new InkelinkException("请按规范上传文件: " + e.getMessage());
        }
        workbook.close();
        if (CollectionUtils.isEmpty(spcFileDtos)){
            throw new InkelinkException("请按规范上传文件");
        }
        ArrayList<SpcDto> spcDtos = spcFileToSpcDto(spcFileDtos);
        //保存文件表
        String uploadJson = mapper.writeValueAsString(spcDtos);
        String fileMd5 = EncryptionUtils.md5(uploadJson);
        PqsSpcFileRecordEntity pqsSpcFileRecordEntity = pqsSpcFileRecordService.getFileRecordByMd5AndName(fileMd5, fileName);
        if (pqsSpcFileRecordEntity == null) {
            pqsSpcFileRecordEntity = new PqsSpcFileRecordEntity();
            pqsSpcFileRecordEntity.setFileMd5(fileMd5);
            pqsSpcFileRecordEntity.setBackFileName(httpPath);
            pqsSpcFileRecordEntity.setSourse(1);
            pqsSpcFileRecordEntity.setState(0);
            pqsSpcFileRecordEntity.setOriFileName(fileName);
            pqsSpcFileRecordEntity.setAttribute1(remark);
            pqsSpcFileRecordService.insert(pqsSpcFileRecordEntity);
            pqsSpcFileRecordService.saveChange();
        }
        Long id = pqsSpcFileRecordEntity.getId();
        //上传操作记录
        PqsSpcFileUserLogEntity pqsSpcFileUserLogEntity = new PqsSpcFileUserLogEntity();
        pqsSpcFileUserLogEntity.setPrcPqsSpcFileRecordId(id);
        pqsSpcFileUserLogEntity.setCustomerName(fileName);
        pqsSpcFileUserLogService.insert(pqsSpcFileUserLogEntity);
        pqsSpcFileUserLogService.saveChange();
        return new ResultVO<>().ok("", "保存文件成功");
    }

    @GetMapping(value = "/getrecord")
    @Operation(summary = "获取文件记录json")
    ResultVO getRecord(@RequestParam("id") Long id) throws IOException {
        PqsSpcFileRecordEntity pqsSpcFileRecordEntity = pqsSpcFileRecordService.getFileRecordById(id);
        if (pqsSpcFileRecordEntity == null) {
            return new ResultVO<>().error("获取文件json失败");
        }
        XSSFWorkbook workbook = downloadWorkbookFromUrl(pqsSpcFileRecordEntity.getBackFileName());
        ArrayList<SpcFileDto> spcFileDtos = new ArrayList<>();
        spcFileDtos = xlsxData(workbook);
        workbook.close();
        ArrayList<SpcDto> spcDtos = spcFileToSpcDto(spcFileDtos);

        return new ResultVO<ArrayList<SpcDto>>().ok(spcDtos, "操作成功");
    }

    @GetMapping(value = "/getresult")
    @Operation(summary = "获取文件结果json")
    ResultVO getResult(@RequestParam("id") Long id) throws IOException {
        List<PqsSpcOperationRecordEntity> pqsSpcOperationRecordEntityList = pqsSpcOperationRecordService.getResultByFileId(id);
        return new ResultVO<List<PqsSpcOperationRecordEntity>>().ok(pqsSpcOperationRecordEntityList, "成功");
    }

    private ArrayList<SpcDto> spcFileToSpcDto(ArrayList<SpcFileDto> spcDtos) {
        Map<String, List<SpcFileDto>> groupedspcDtos = spcDtos.stream()
                .collect(Collectors.groupingBy(SpcFileDto::getFeatureName));
        ArrayList<SpcDto> spcDtoList = new ArrayList<>();
        for (Map.Entry<String, List<SpcFileDto>> entry : groupedspcDtos.entrySet()) {
            SpcDto spcDto = new SpcDto();
            List<SpcFileDto> spcFileDtos = entry.getValue();
            SpcFileDto spcFileDto = spcFileDtos.get(0);
            String unit = spcFileDto.getUnit();
            String vin = spcFileDto.getVin();
            String featureName = spcFileDto.getFeatureName();
            Float upperLimit = spcFileDto.getUpperLimit();
            if (upperLimit == null) {
                throw new InkelinkException("特征值首行上限不能为空");
            }
            Float lowerLimit = spcFileDto.getLowerLimit();
            if (lowerLimit == null) {
                throw new InkelinkException("特征值首行下限不能为空");
            }
            spcDto.setFeatureName(featureName);
            spcDto.setLowerLimit(lowerLimit);
            spcDto.setUpperLimit(upperLimit);
            spcDto.setUnit(unit);
            spcDto.setVin(vin);
            ArrayList<FeatureValueDto> featureValueDtoList = new ArrayList<>();
            for (SpcFileDto spcFile : spcFileDtos) {
                FeatureValueDto featureValueDto = new FeatureValueDto();
                featureValueDto.setFeatureValue(spcFile.getFeatureValue());
                featureValueDto.setConditionValue(spcFile.getConditionValue());
                featureValueDto.setIsSpcParam(spcFile.getIsSpcParam());
                featureValueDtoList.add(featureValueDto);
            }
            spcDto.setFeatureValueList(featureValueDtoList);
            spcDtoList.add(spcDto);
        }
        return spcDtoList;
    }

    @PostMapping("/queryspc")
    @Operation(summary = "")
    public ResultVO querySpc(@RequestBody ArrayList<SpcDto> spcDtoList) throws Exception {
        //调试判断文件操作是否保存，c
//        List<SpcDto> spcDtos = new ArrayList<>();
//        for (SpcDto spcDto : spcDtoList) {
//            spcDtos.add(spcDto.clone());
//        }
//        List<SpcQueryParamDto> QueryParamList = spcDtos.stream().map(e -> e.getOperationParam()).collect(Collectors.toList());
//        for (SpcDto spcDto : spcDtos) {
//            spcDto.setOperationParam(null);
//        }
//        String fileJson = mapper.writeValueAsString(spcDtos);
//        String fileMd5 = EncryptionUtils.md5(fileJson);
//        String operationJson = mapper.writeValueAsString(QueryParamList);
//        String operationMd5 = EncryptionUtils.md5(operationJson);
//        //查询该文件此次操作是否存在
//        PqsSpcFileRecordEntity pqsSpcFileRecordEntity = pqsSpcFileRecordService.getFileRecordByMd5(fileMd5);
//        if (pqsSpcFileRecordEntity == null) {
//            return new ResultVO<>().error("远端文件不存在，请检查");
//        }
//
//        PqsSpcOperationRecordEntity pqsSpcOperationRecordEntity = pqsSpcOperationRecordService.queryByFileAndOperationMd5(pqsSpcFileRecordEntity.getId(), operationMd5);
//       if (pqsSpcOperationRecordEntity!=null){
//           return new ResultVO<String>().ok(pqsSpcOperationRecordEntity.getBackFilePath(),"返回结果地址成功");
//       }
///////
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setSpcDtoList(spcDtoList);
        List<FeatureResDto> featureResponseDtos = new ArrayList<>();

        //遍历特征值
        for (SpcDto spcDto : spcDtoList) {
            List<FeatureValueDto> featureValueList = spcDto.getFeatureValueList();
            SpcQueryParamDto operationParam = spcDto.getOperationParam();
            //1.判断操作条件是否为空
            if (operationParam == null) {
                throw new InkelinkException("操作条件不能为空");
            }
            //2.判断是否存在筛选最大值
            if (StringUtils.isNotBlank(operationParam.getUpperFeatureValue())) {
                featureValueList = featureValueFilter(featureValueList, operationParam.getUpperFeatureValue(), "upper");
            }
            //3.判断是否存在筛选最小值
            if (StringUtils.isNotBlank(operationParam.getLowerFeatureValue())) {
                featureValueList = featureValueFilter(featureValueList, operationParam.getLowerFeatureValue(), "lower");
            }
            //4.通过结果条件筛选
            String resultType = operationParam.getResultType();
            if ("pass".equals(resultType) || "fail".equals(resultType)) {
                featureValueList = resultFilter(featureValueList, resultType, spcDto.getLowerLimit(), spcDto.getUpperLimit());
            }
            //5.通过时间筛选
            featureValueList = timeQuery(featureValueList, operationParam.getQueryValue());


            //7。通过抽样条件筛选
            String sampleType = operationParam.getSampleType();
            String sampleNum = operationParam.getSampleNum();
            //0.全部样本，1.固定抽样，2.每N抽1，3.每时抽N，4.剔除末尾
            switch (sampleType) {
                case "1":
                    //2017-01-01 00:00:00,2017-12-31 00:00:00
                    featureValueList = randomQuery(featureValueList, sampleNum);
                    break;
                case "2":
                    featureValueList = sampleQuery(featureValueList, sampleNum);
                    break;
                case "3":
                    featureValueList = hourQuery(featureValueList, sampleNum);
                    break;
                case "4":
                    featureValueList = removeEndQuery(featureValueList, sampleNum);
                    break;
                default:
                    break;
            }
            float CLM = Float.parseFloat(operationParam.getClm());


            if (CollectionUtils.isNotEmpty(featureValueList)) {

                List<Float> tp = featureValueList.stream().map(e -> e.getFeatureValue()).collect(Collectors.toList());
                List<String> ds = featureValueList.stream().map(e -> e.getConditionValue()).collect(Collectors.toList());

                float[] featureArray = Floats.toArray(tp);
                String[] timeArray = ds.toArray(new String[0]);
                INDArray arr = Nd4j.create(featureArray);
                long featureLength = arr.length();
                float[] featureMean = arr.mean().data().asFloat();
                float[] featureMax = arr.max().data().asFloat();
                float[] featureMin = arr.min().data().asFloat();
                float[] featureSigma = Floats.toArray(Arrays.stream(new Double[]{allStd(arr)}).collect(Collectors.toList()));

                float LCL = featureMean[0] - CLM * featureSigma[0];
                float UCL = featureMean[0] + CLM * featureSigma[0];
                float LSL = spcDto.getLowerLimit();
                float USL = spcDto.getUpperLimit();

                //最小值和最大值分别向扩大或缩小20%
                float fBeign = featureMin[0] - Math.abs(featureMin[0] * 0.2F);
                float fEnd = featureMax[0] + Math.abs(featureMax[0] * 0.2F);

                Tuple2<float[], float[]> f = simpleHistogram(featureArray);
                INDArray fBoxY = Nd4j.create(f._1);
                INDArray fBoxX = Nd4j.create(f._2);

                float fMx = fBoxX.get(interval(1, fBoxX.size(0))).sub(fBoxX.get(interval(0, fBoxX.size(0) - 1))).mean().data().asFloat()[0] / 2;
                fBoxX = fBoxX.add(fMx);
                //构建f_histogram 图
                List<PairDto> fHistogram = new ArrayList<PairDto>();
                float[] testTempF_histogramArray_x = fBoxX.get(interval(0, fBoxX.size(0) - 1)).data().asFloat();
                float[] testTempF_histogramArray_y = fBoxY.data().asFloat();
                for (int i = 0; i < Math.min(testTempF_histogramArray_x.length, testTempF_histogramArray_y.length); i++) {
                    PairDto pairDto = new PairDto();
                    pairDto.setAttribute1(testTempF_histogramArray_x[i]);
                    pairDto.setAttribute2(testTempF_histogramArray_y[i]);
                    fHistogram.add(pairDto);
                }
                //
                float fDx = fMx * 2;
                float nXBegin = Math.min(fBeign, LSL);
                nXBegin = nXBegin - Math.abs(nXBegin * 0.2F);
                float nXEnd = Math.max(fEnd, USL);
                nXEnd = nXEnd + Math.abs(nXEnd * 0.2F);
                float nXrange = nXEnd - nXBegin;
                nXBegin = Math.min(featureMean[0] - nXrange / 2F, nXBegin);
                nXEnd = Math.max(featureMean[0] + nXrange / 2F, nXEnd);
                //
                int nSC = 1000;
                float nDX = (nXEnd - nXBegin) / nSC;
                float[] nx = new float[nSC];
                for (int i3 = 0; i3 < nSC; i3++) {
                    nx[i3] = nXBegin + i3 * nDX;
                }
                ;
                //注意调整featureSigma
                float[] ny = normFun(nx, featureMean[0], featureSigma[0]);
                List<PairDto> nPoints = new ArrayList<PairDto>();
                for (int i4 = 0; i4 < Math.min(ny.length, nx.length); i4++) {
                    PairDto pairDto = new PairDto();
                    pairDto.setAttribute1(nx[i4]);
                    pairDto.setAttribute2(ny[i4]);
                    nPoints.add(pairDto);
                }
                FeatureResDto featureResDto = new FeatureResDto();
                //用featurePoints
                FeaturePointsDto featurePoints = new FeaturePointsDto();
                featurePoints.setPoints(featureValueList);
                featurePoints.setFeatureLength((int) featureLength);
                featurePoints.setMean(featureMean[0]);
                featurePoints.setSigma(featureSigma[0]);
                featurePoints.setMin(featureMin[0]);
                featurePoints.setMax(featureMax[0]);
                featurePoints.setLCL(LCL);
                featurePoints.setUCL(UCL);
                featurePoints.setLSL(LSL);
                featurePoints.setUSL(USL);
                featurePoints.setHistogramPoints(fHistogram);
                featurePoints.setDX(fDx);
                featurePoints.setNPoints(nPoints);
                featureResDto.setFeaturePoints(featurePoints);
                //分组大小：分组数量：
                int groupSize;
                int groupLength;
                try {
                    groupSize = StringUtils.isNotBlank(operationParam.getGroupSize()) ? Integer.parseInt(operationParam.getGroupSize()) : null;
                } catch (Exception e) {
                    groupSize = 0;
                }
                try {
                    groupLength = StringUtils.isNotBlank(operationParam.getGroupLength()) ? Integer.parseInt(operationParam.getGroupLength()) : null;
                } catch (Exception e) {
                    groupLength = 0;
                }

                if (groupSize != 0) {
                    groupSize = Math.max(Math.min(groupSize, 25), 2);
                    groupLength = (int) ((double) featureLength / groupSize);
                } else if (groupLength != 0) {
                    groupSize = (int) ((double) featureLength / groupLength);
                    groupSize = Math.max(Math.min(groupSize, 25), 2);
                    //没看懂的原逻辑
//                        if (groupLength * groupSize > featureLength) {
//                            continue;
//                        } else {
//                            groupLength = groupLength;
//                        }
                } else {
                    groupSize = Math.max(groupSize, 2);
                    groupLength = (int) ((double) featureLength / groupSize);
                }
                int groupDataCount = groupLength * groupSize;

                float[] featureRArray = Arrays.copyOfRange(featureArray, 0, groupDataCount);
                float[] featureTArray = Arrays.copyOfRange(featureArray, groupDataCount, featureArray.length);
                INDArray featureRINDArray = Nd4j.create(featureRArray);
                featureRINDArray = featureRINDArray.reshape(groupLength, groupSize);
                List<List<Float>> featureRList = new ArrayList<>();
                for (int i = 0; i < featureRINDArray.rows(); i++) {
                    List<Float> newRow = new ArrayList<>();
                    for (int j = 0; j < featureRINDArray.columns(); j++) {
                        newRow.add(featureRINDArray.getFloat(i, j));
                    }
                    featureRList.add(newRow);
                }
                Boolean isLoseTail = operationParam.getIsLoseTail() != null ? operationParam.getIsLoseTail() : true;

                if (!isLoseTail && featureTArray.length > 0) {
                    List<Float> featureTList = new ArrayList<>();
                    for (float value : featureTArray) {
                        featureTList.add(value);
                    }
                    featureRList.add(featureTList);
                }
                //转化为二维数组
                float[][] groupData = listToArray(featureRList);
                float[] featureGArray;
                if (isLoseTail) {
                    featureGArray = featureRArray;
                } else {
                    featureGArray = featureArray;
                }
                int featureGLength = featureGArray.length;
                float[] featureGMean;
                float[] featureGMax;
                float[] featureGMin;
                float[] featureGSigma;
                float yBegin = 0f;
                float yEnd = 0f;
                FeatureInfoDtos featureInfoDtos = new FeatureInfoDtos();
                if (featureGLength > 0) {
                    ArrayList<FeatureValueDto> featureFValueList = new ArrayList<>();
                    for (int i = 0; i < featureGLength; i++) {
                        featureFValueList.add(featureValueList.get(i));
                    }
                    INDArray featureGINDArray = Nd4j.create(featureGArray);
                    featureGMean = featureGINDArray.mean().data().asFloat();
                    featureGMax = featureGINDArray.max().data().asFloat();
                    featureGMin = featureGINDArray.min().data().asFloat();
                    featureGSigma = Floats.toArray(Arrays.stream(new Double[]{allStd(arr)}).collect(Collectors.toList()));
                    yBegin = Math.min(featureGMin[0], LSL != 0 ? LSL : featureGMin[0]);
                    yEnd = Math.max(featureGMax[0], LSL != 0 ? LSL : featureGMax[0]);

                    String[] timeRArray = Arrays.copyOfRange(timeArray, 0, groupDataCount);
                    String[] timeTArray = Arrays.copyOfRange(timeArray, groupDataCount, featureArray.length);
                    //timeRArray分组并取出每组第一个时间
                    List<String> firsttimeGroup = getFirstTime(timeRArray, groupSize);
                    if (!isLoseTail && timeTArray.length > 0) {
                        firsttimeGroup.add(timeTArray[0]);
                    }
                    List<float[]> groupMRS = new ArrayList<>();
                    for (float[] groupDatum : groupData) {
                        float[] noZeroArray = noZeroArray(groupDatum);
                        INDArray groupDatumINDArray = Nd4j.create(noZeroArray);
                        float max = Nd4j.max(groupDatumINDArray).getFloat(0);
                        float min = Nd4j.min(groupDatumINDArray).getFloat(0);
                        //domension参数可能有问题
                        float sigma = Nd4j.std(groupDatumINDArray, 0).getFloat(0);
                        float mean = Nd4j.mean(groupDatumINDArray).getFloat(0);
                        float r = max - min;
                        float[] mrs = {mean, r, sigma};
                        groupMRS.add(mrs);
                    }

                    float[][] array = groupMRS.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                    INDArray groupMRSIndArray = Nd4j.create(array);

                    Function<INDArray, INDArray> testmean = x -> x.mean();
                    INDArray tempIndArray = applyAlongAxis(testmean, 0, groupMRSIndArray);
                    float groupMean = tempIndArray.data().asFloat()[0];
                    float groupRMean = tempIndArray.data().asFloat()[1];
                    float groupSigmaMean = tempIndArray.data().asFloat()[2];

//
                    Map<String, Double> ccc = ControlChartsConstantsDto.result(groupSize);
//
                    LCL = featureGMean[0] - 3 * featureGSigma[0];
                    UCL = featureGMean[0] + 3 * featureGSigma[0];
//


                    float gLcl = groupMean - (float) (ccc.get("A") * groupRMean);
                    float gUcl = groupMean + (float) (ccc.get("A") * groupRMean);
                    float sigmaC = 0;
                    if (groupSize > 6) {
                        sigmaC = (float) (groupSigmaMean / ccc.get("c4"));
                    } else {
                        sigmaC = (float) (groupRMean / ccc.get("d2"));
                    }
                    float sigmaP = 0;
                    if (featureGLength > 1) {
                        sigmaP = Nd4j.std(Nd4j.create(featureGArray)).data().asFloat()[0];
                    }
                    double CP = 0;
                    double CPL = 0;
                    double CPU = 0;
                    double CPK = 0;
                    if (sigmaC > 0) {
                        CP = (USL - LSL) / (6 * sigmaC);
                        CPL = (groupMean - LSL) / (3 * sigmaC);
                        CPU = (USL - groupMean) / (3 * sigmaC);
                        CPK = Math.min(CPU, CPL);
                    }
                    double PP = 0;
                    double PPL = 0;
                    double PPU = 0;
                    double PPK = 0;
                    if (sigmaP > 0) {
                        PP = (USL - LSL) / (6 * sigmaP);
                        PPL = (groupMean - LSL) / (3 * sigmaP);
                        PPU = (USL - groupMean) / (3 * sigmaP);
                        PPK = Math.min(PPU, PPL);
                    }
                    double xBarRMean = groupMean;
                    double xBarRLcl = xBarRMean - ccc.get("A2") * groupRMean;
                    double xBarRUcl = xBarRMean + ccc.get("A2") * groupRMean;
                    double rLcl = ccc.get("D3") * groupRMean;
                    double rUcl = ccc.get("D4") * groupRMean;
                    double xbarSMean = groupMean;
                    double xBarSLcl = xbarSMean - ccc.get("A3") * groupSigmaMean;
                    double xBarSUcl = xbarSMean + ccc.get("A3") * groupSigmaMean;
                    double stdLcl = ccc.get("B3") * groupSigmaMean;
                    double stdUcl = ccc.get("B4") * groupSigmaMean;
                    double sigmaR = groupRMean / ccc.get("d2");
                    double sigmaStd = groupSigmaMean / ccc.get("c4");
                    double MRFeatureArrayP1single = LSL + (USL - LSL) / 2;
                    float[] extentedMrFeatureArrayP1 = Arrays.copyOfRange(featureGArray, 0, featureGArray.length - 1);
                    List<Double> MRFeatureArrayP1arrayList = new ArrayList<Double>();
                    MRFeatureArrayP1arrayList.add(MRFeatureArrayP1single);
                    for (float v : extentedMrFeatureArrayP1) {
                        MRFeatureArrayP1arrayList.add((double) v);
                    }
                    double[] MrFeatureArrayP1 = MRFeatureArrayP1arrayList.stream().mapToDouble(Double::doubleValue).toArray();
                    INDArray MR = Nd4j.math().sub(Nd4j.create(featureGArray), Nd4j.create(MRFeatureArrayP1arrayList));
                    INDArray MRMean = MR.mean();
                    INDArray mrUCL = MRMean.mul(ccc.get("D4"));
                    INDArray mrLCL = MRMean.mul(ccc.get("D3"));

                    float[][] doubleFloat = new float[groupMRS.size()][3];
                    for (int i = 0; i < groupMRS.size(); i++) {
                        float[] teamp = groupMRS.get(i);
                        for (int i1 = 0; i1 < 3; i1++) {
                            doubleFloat[i][i1] = teamp[i1];
                        }
                    }
                    INDArray groupMrsT = Nd4j.create(doubleFloat).transpose();
                    INDArray teampGroupMrsT0 = groupMrsT.get(NDArrayIndex.point(0), NDArrayIndex.all()).sub(0);
                    INDArray teampGroupMrsT1 = groupMrsT.get(NDArrayIndex.point(1), NDArrayIndex.all()).sub(0);
                    INDArray teampGroupMrsT2 = groupMrsT.get(NDArrayIndex.point(2), NDArrayIndex.all()).sub(0);
//                        INDArray[] GroupMeanList = {
//                                Nd4j.concat(1, Nd4j.create(firsttimeGroup), teampGroupMrsT0),
//                                Nd4j.concat(1, Nd4j.create(firsttimeGroup), teampGroupMrsT1),
//                                Nd4j.concat(1, Nd4j.create(firsttimeGroup), teampGroupMrsT2)
//                        };
                    // List<Tuple2<String,Float>> partArray = featureValueList.stream().map(e ->  new Tuple2<String,Float>(e.getConditionValue(),e.getFeatureValue())).collect(Collectors.toList());
                    float[][] partArrayGroupData = groupData;
                    INDArray groupMeanArray = teampGroupMrsT0;
                    INDArray groupMeanDistance = groupMeanArray.sub(groupMean);
                    float groupMeanSigma = Float.parseFloat(String.valueOf(allStd(groupMeanArray)));

                    //groupMeanArray.std().data().asFloat()[0];
                    List<String> result = new ArrayList<String>();
                    boolean[] resultIndex;
                    int resultLength;
                    if (!isLoseTail && featureTArray.length > 0) {
                        resultIndex = new boolean[groupLength + 1];
                        resultLength = groupLength + 1;
                    } else {
                        resultIndex = new boolean[groupLength];
                        resultLength = groupLength;
                    }
                    INDArray resultIndexINDArray = Nd4j.create(resultIndex);


                    //八项判异
                    String minitable = operationParam.getMinitable();
                    if (StringUtils.isBlank(minitable)) {
                        minitable = ",,,,,,,";
                    }
                    String[] minitableArray = minitableToArray(minitable);
//                        int result_length = 1;
//                        List<String> result = new ArrayList<>();
                    if (StringUtils.isNotBlank(minitableArray[0]) && resultLength > 0) {
                        int k = Integer.parseInt(minitableArray[0]);
                        INDArray u3stdA = groupMeanArray.add(-groupMean).add(-k * groupMeanSigma).gt(0).castTo(Nd4j.dataType().INT);
                        INDArray u3stdB = groupMeanArray.add(groupMean).add(k * groupMeanSigma).lt(0).castTo(Nd4j.dataType().INT);
                        INDArray u3std = u3stdA.add(u3stdB);
                        if (u3std.sumNumber().intValue() > 0) {
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(u3std);
                            List<Integer> trueValues = Arrays.stream(u3std.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            result.add(String.format("1. 距中心线大于%d个标准差的任意一点，[%d]。", k, trueValues.size()));
                        }
                    }
                    //第二个数据
                    if (StringUtils.isNotBlank(minitableArray[1]) && resultLength >= Integer.parseInt(minitableArray[1])) {
                        int k = Integer.parseInt(minitableArray[1]);
                        INDArray mlA = groupMeanDistance.gt(0).castTo(Nd4j.dataType().INT);
                        INDArray mlB = groupMeanDistance.lt(0).castTo(Nd4j.dataType().INT);
                        INDArray ml = mlA.sub(mlB);
                        float[] input = ml.toFloatVector();
                        float[] kernel = new float[k];
                        Arrays.fill(kernel, 1);
                        float[] convolution = convolution(input, kernel);
                        INDArray convolutionINDArray = Nd4j.create(convolution);
                        INDArray convolutionINDArrayA = convolutionINDArray.eq(k).castTo(Nd4j.dataType().INT);
                        INDArray convolutionINDArrayB = convolutionINDArray.eq(-k).castTo(Nd4j.dataType().INT);
                        convolutionINDArray = convolutionINDArrayA.add(convolutionINDArrayB);
                        convolutionINDArray = convolutionINDArray.get(interval(0, resultLength));
                        if (convolutionINDArray.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                INDArray shiftedU9mlIndex = Nd4j.concat(0, convolutionINDArray.get(interval(1, convolutionINDArray.length())), Nd4j.createFromArray(0));
                                convolutionINDArray = convolutionINDArray.add(shiftedU9mlIndex);
                            }
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(convolutionINDArray);
                            List<Integer> trueValues = Arrays.stream(convolutionINDArray.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());

                            result.add(String.format("2. 连续%d个点在中心线同一侧，[%d]。", k, trueValues.size()));
                        }
                    }

                    if (StringUtils.isNotBlank(minitableArray[2]) && resultLength >= Integer.parseInt(minitableArray[2])) {
                        int k = Integer.parseInt(minitableArray[2]);
                        INDArray plf = groupMeanDistance.get(interval(1, groupMeanDistance.length())).sub(groupMeanDistance.get(interval(0, groupMeanDistance.length() - 1)));
                        INDArray plfA = plf.gt(0).castTo(Nd4j.dataType().INT);
                        INDArray plfB = plf.lt(0).castTo(Nd4j.dataType().INT);
                        plf = plfA.sub(plfB);
                        float[] input = plf.toFloatVector();
                        float[] kernel = new float[k];
                        Arrays.fill(kernel, 1);
                        float[] u6plf = convolution(input, kernel);
                        INDArray u6plfINDArray = Nd4j.create(u6plf);
                        INDArray u6plfINDArrayA = u6plfINDArray.eq(k).castTo(Nd4j.dataType().INT);
                        INDArray u6plfINDArrayB = u6plfINDArray.eq(-k).castTo(Nd4j.dataType().INT);
                        u6plfINDArray = u6plfINDArrayA.add(u6plfINDArrayB);
                        u6plfINDArray = u6plfINDArray.get(interval(0, resultLength));
                        if (u6plfINDArray.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                INDArray shiftedU9mlIndex = Nd4j.concat(0, u6plfINDArray.get(interval(1, u6plfINDArray.length())), Nd4j.createFromArray(0));
                                u6plfINDArray = u6plfINDArray.add(shiftedU9mlIndex);
                            }
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(u6plfINDArray);
                            List<Integer> trueValues = Arrays.stream(u6plfINDArray.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());

                            result.add(String.format("3. 连续%d个点全部递增或递减，[%d]。", k, trueValues.size()));
                        }
                    }
                    //待进一步测试
                    if (StringUtils.isNotBlank(minitableArray[3]) && resultLength >= Integer.parseInt(minitableArray[3])) {
                        int k = Integer.parseInt(minitableArray[3]);
                        INDArray mlA = groupMeanDistance.gt(0).castTo(Nd4j.dataType().INT);
                        INDArray mlB = groupMeanDistance.lt(0).castTo(Nd4j.dataType().INT);
                        INDArray ml = mlA.sub(mlB);
                        float[] input = ml.toFloatVector();
                        float[] kernel = new float[k];
                        for (int i = 0; i < k; i++) {
                            kernel[i] = (float) Math.pow(-1, i);
                        }
                        float[] convolution = convolution(input, kernel);
                        INDArray convolutionINDArray = Nd4j.create(convolution);
                        INDArray convolutionINDArrayA = convolutionINDArray.eq(k).castTo(Nd4j.dataType().INT);
                        INDArray convolutionINDArrayB = convolutionINDArray.eq(-k).castTo(Nd4j.dataType().INT);
                        convolutionINDArray = convolutionINDArrayA.add(convolutionINDArrayB);
                        convolutionINDArray = convolutionINDArray.get(interval(0, resultLength));
                        if (convolutionINDArray.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                INDArray shiftedU9mlIndex = Nd4j.concat(0, convolutionINDArray.get(interval(1, convolutionINDArray.length())), Nd4j.createFromArray(0));
                                convolutionINDArray = convolutionINDArray.add(shiftedU9mlIndex);
                            }
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(convolutionINDArray);
                            List<Integer> trueValues = Arrays.stream(convolutionINDArray.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            result.add(String.format("4. 连续%d个点上下交错，[%d]。", k, trueValues.size()));
                        }
                    }
                    if (StringUtils.isNotBlank(minitableArray[4]) && resultLength >= Integer.parseInt(minitableArray[4]) + 1) {
                        int k = Integer.parseInt(minitableArray[4]);
                        INDArray u2std = groupMeanArray.sub(groupMean + 2 * groupMeanSigma).gt(0).castTo(Nd4j.dataType().INT);
                        float[] u2stdfloatArray = u2std.data().asFloat();
                        int i1 = u2stdfloatArray.length - k;
                        INDArray l2std = groupMeanArray.sub(groupMean - 2 * groupMeanSigma).lt(0).castTo(Nd4j.dataType().INT);
                        float[] l2stdfloatArray = l2std.data().asFloat();
                        List<float[]> u2stdList = new ArrayList<>();
                        for (int i = 0; i < k + 1; i++) {
                            float[] aFloat = new float[i1];
                            System.arraycopy(u2stdfloatArray, i, aFloat, 0, i1);
                            u2stdList.add(aFloat);
                        }
                        float[][] u2stdArray = u2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray u2stdIndArray = Nd4j.create(u2stdArray);
                        Function<INDArray, INDArray> testsum = x -> x.sum();
                        INDArray u2stdListSum = applyAlongAxis(testsum, 0, u2stdIndArray);
                        List<float[]> l2stdList = new ArrayList<>();
                        for (int i = 0; i < k + 1; i++) {
                            float[] aFloat = new float[i1];
                            System.arraycopy(l2stdfloatArray, i, aFloat, 0, i1);
                            l2stdList.add(aFloat);
                        }
                        float[][] l2stdArray = l2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray l2stdIndArray = Nd4j.create(l2stdArray);
                        INDArray l2stdListSum = applyAlongAxis(testsum, 0, l2stdIndArray);
                        INDArray u2stdindex = u2stdListSum.sub(k).gte(0).castTo(Nd4j.dataType().INT).add(l2stdListSum.sub(k).gte(0).castTo(Nd4j.dataType().INT));
                        float[] anInt = u2stdindex.data().asFloat();

                        //需要改良
                        if (u2stdindex.length() < resultLength) {
                            float[] fillArray = new float[(int) (resultLength - u2stdindex.length())];
                            for (int i = 0; i < resultLength - u2stdindex.length(); i++) {
                                fillArray[i] = 0f;
                            }
//                                u2stdindex = Nd4j.concat(0, u2stdindex, Nd4j.create(fillArray));
                            float[] mergedArray = new float[anInt.length + fillArray.length];

                            // 将array1复制到mergedArray的前部
                            System.arraycopy(anInt, 0, mergedArray, 0, anInt.length);

                            // 将array2复制到mergedArray的后部
                            System.arraycopy(fillArray, 0, mergedArray, anInt.length, fillArray.length);
                            anInt = mergedArray;
                        }
                        u2stdindex = Nd4j.create(anInt);
                        if (u2stdindex.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                INDArray shiftedU9mlIndex = Nd4j.concat(0, Nd4j.createFromArray(0f), u2stdindex.get(interval(0, u2stdindex.length() - 1)));
                                u2stdindex = u2stdindex.add(shiftedU9mlIndex);
                            }
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(u2stdindex.castTo(Nd4j.dataType().INT));
                            List<Integer> trueValues = Arrays.stream(u2stdindex.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            String message = "5. " + (k + 1) + "个点中有" + k + "个点（同侧）距中心线大于2个标准差，[" + trueValues.size() + "]。";
                            result.add(message);
                        }
                    }
                    if (StringUtils.isNotBlank(minitableArray[5]) && resultLength >= Integer.parseInt(minitableArray[5]) + 1) {
                        int k = Integer.parseInt(minitableArray[5]);
                        INDArray u2std = groupMeanArray.sub(groupMean + 1 * groupMeanSigma).gt(0).castTo(Nd4j.dataType().INT);
                        float[] u2stdfloatArray = u2std.data().asFloat();
                        int i1 = u2stdfloatArray.length - k;
                        INDArray l2std = groupMeanArray.sub(groupMean - 1 * groupMeanSigma).lt(0).castTo(Nd4j.dataType().INT);
                        float[] l2stdfloatArray = l2std.data().asFloat();
                        List<float[]> u2stdList = new ArrayList<>();
                        for (int i = 0; i < k + 1; i++) {
                            float[] aFloat = new float[i1];
                            System.arraycopy(u2stdfloatArray, i, aFloat, 0, i1);
                            u2stdList.add(aFloat);
                        }
                        float[][] u2stdArray = u2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray u2stdIndArray = Nd4j.create(u2stdArray);
                        Function<INDArray, INDArray> testsum = x -> x.sum();
                        INDArray u2stdListSum = applyAlongAxis(testsum, 0, u2stdIndArray);
                        List<float[]> l2stdList = new ArrayList<>();
                        for (int i = 0; i < k + 1; i++) {
                            float[] aFloat = new float[i1];
                            System.arraycopy(l2stdfloatArray, i, aFloat, 0, i1);
                            l2stdList.add(aFloat);
                        }
                        float[][] l2stdArray = l2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray l2stdIndArray = Nd4j.create(l2stdArray);
                        INDArray l2stdListSum = applyAlongAxis(testsum, 0, l2stdIndArray);
                        INDArray u2stdindex = u2stdListSum.sub(k).gte(0).castTo(Nd4j.dataType().INT).add(l2stdListSum.sub(k).gte(0).castTo(Nd4j.dataType().INT));
                        float[] anInt = u2stdindex.data().asFloat();
                        if (u2stdindex.length() < resultLength) {
                            float[] fillArray = new float[(int) (resultLength - u2stdindex.length())];
                            for (int i = 0; i < resultLength - u2stdindex.length(); i++) {
                                fillArray[i] = 0f;
                            }
//                                u2stdindex = Nd4j.concat(0, u2stdindex, Nd4j.create(fillArray));
                            float[] mergedArray = new float[anInt.length + fillArray.length];

                            // 将array1复制到mergedArray的前部
                            System.arraycopy(anInt, 0, mergedArray, 0, anInt.length);

                            // 将array2复制到mergedArray的后部
                            System.arraycopy(fillArray, 0, mergedArray, anInt.length, fillArray.length);
                            anInt = mergedArray;
                        }
                        u2stdindex = Nd4j.create(anInt);
                        if (u2stdindex.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                INDArray shiftedU9mlIndex = Nd4j.concat(0, Nd4j.createFromArray(0f), u2stdindex.get(interval(0, u2stdindex.length() - 1)));
                                u2stdindex = u2stdindex.add(shiftedU9mlIndex);
                            }
                            resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(u2stdindex.castTo(Nd4j.dataType().INT));
                            List<Integer> trueValues = Arrays.stream(u2stdindex.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            String message = "6. " + (k + 1) + "个点中有" + k + "个点（同侧）距中心线大于1个标准差，[" + trueValues.size() + "]。";
                            result.add(message);
                        }
                    }
                    if (StringUtils.isNotBlank(minitableArray[6]) && resultLength >= Integer.parseInt(minitableArray[6])) {
                        int k = Integer.parseInt(minitableArray[6]);
                        INDArray u1std = groupMeanArray.lte(groupMean + groupMeanSigma);
                        INDArray l1std = groupMeanArray.gte(groupMean - groupMeanSigma);
                        INDArray ul1std = u1std.castTo(DataType.INT).add(l1std.castTo(DataType.INT));
                        ul1std = ul1std.eq(2).castTo(DataType.INT);
                        float[] kernel = new float[k];
                        Arrays.fill(kernel, 1);
                        float[] ul1stdConvoluve = convolution(ul1std.data().asFloat(), kernel);

                        INDArray convolutionINDArray = Nd4j.create(Arrays.copyOfRange(ul1stdConvoluve, 0, resultLength));
                        convolutionINDArray = convolutionINDArray.eq(k).castTo(DataType.INT);

                        if (convolutionINDArray.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                List<Integer> step1 = Arrays.stream(convolutionINDArray.data().asInt()).boxed().collect(Collectors.toList());
                                step1.remove(0);
                                step1.add(0);
                                convolutionINDArray = Nd4j.create(step1).add(convolutionINDArray);
                            }
                            // resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(convolutionINDArray);
                            List<Integer> trueValues = Arrays.stream(convolutionINDArray.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            result.add(String.format("7. 连续[%d]个点（任一侧)距中心线1个标准差以内,[%d]", k, trueValues.size()));
                        }
                    }


                    //
                    if (StringUtils.isNotBlank(minitableArray[7]) && resultLength >= Integer.parseInt(minitableArray[7])) {
                        int k = Integer.parseInt(minitableArray[7]);
                        INDArray u1std = groupMeanArray.gt(groupMean + groupMeanSigma);
                        INDArray l1std = groupMeanArray.lt(groupMean - groupMeanSigma);
                        INDArray ul1std = u1std.castTo(DataType.INT).add(l1std.castTo(DataType.INT));

                        float[] kernel = new float[k];
                        Arrays.fill(kernel, 1);
                        float[] ul1stdConvoluve = convolution(ul1std.data().asFloat(), kernel);
                        INDArray convolutionINDArray = Nd4j.create(Arrays.copyOfRange(ul1stdConvoluve, 0, resultLength));
                        convolutionINDArray = convolutionINDArray.cond(Conditions.equals(k)).castTo(DataType.INT);

                        if (convolutionINDArray.sumNumber().intValue() > 0) {
                            for (int i = 0; i < k - 1; i++) {
                                List<Integer> step1 = Arrays.stream(convolutionINDArray.data().asInt()).boxed().collect(Collectors.toList());
                                step1.remove(0);
                                step1.add(0);
                                convolutionINDArray = Nd4j.create(step1).add(convolutionINDArray);
                            }
                            //resultIndexINDArray = resultIndexINDArray.castTo(Nd4j.dataType().INT).add(convolutionINDArray);
                            List<Integer> trueValues = Arrays.stream(convolutionINDArray.data().asInt()).boxed().filter(x -> x > 0).collect(Collectors.toList());
                            result.add(String.format("8. 连续[%d]个点（任一侧)距中心线大于1个标准差,[%d]", k, trueValues.size()));
                        }
                    }


                    if (featureGLength >= 25) {
                        INDArray l2std = Nd4j.create(featureGArray).gte(LCL).castTo(Nd4j.dataType().INT);
                        INDArray u2std = Nd4j.create(featureGArray).lte(UCL).castTo(Nd4j.dataType().INT);

                        float[] icl = l2std.add(u2std).eq(2).data().asFloat();
                        float[] kernel = new float[25];
                        Arrays.fill(kernel, 1);
                        float[] iclIndex = convolution(icl, kernel);
                        boolean testOK = false;
                        for (int i = 0; i < iclIndex.length; i++) {
                            if (iclIndex[i] == 25) {
                                testOK = true;
                                break;
                            }
                        }
                        if (testOK == true) {
                            result.add("9. 连续25点在控制线内。");
                        }
                    }
                    //
                    if (featureGLength >= 35) {
                        INDArray l2std = Nd4j.create(featureGArray).lt(LCL).castTo(Nd4j.dataType().INT);
                        INDArray u2std = Nd4j.create(featureGArray).gt(UCL).castTo(Nd4j.dataType().INT);
                        float[] ocl = l2std.add(u2std).gt(0).data().asFloat();
                        List<float[]> u2stdList = new ArrayList<>();

                        for (int i = 0; i < 35; i++) {
                            u2stdList.add(Arrays.copyOfRange(ocl, i, ocl.length - (34 - i)));
                        }

                        float[][] u2stdArray = u2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray u2stdIndArray = Nd4j.create(u2stdArray);

                        Function<INDArray, INDArray> testsum = x -> x.sum();
                        INDArray u2stdListSum = applyAlongAxis(testsum, 0, u2stdIndArray);

                        float[] testU2stdListSum = u2stdListSum.data().asFloat();
                        for (int i = 0; i < testU2stdListSum.length; i++) {
                            if (testU2stdListSum[i] <= 1) {
                                result.add("10. 连续35点最多有1点出界。");
                                break;
                            }
                        }
                    }
                    //
                    if (featureGLength >= 100) {
                        INDArray l2std = Nd4j.create(featureGArray).lt(LCL).castTo(Nd4j.dataType().INT);
                        INDArray u2std = Nd4j.create(featureGArray).gt(UCL).castTo(Nd4j.dataType().INT);
                        float[] ocl = l2std.add(u2std).gt(0).data().asFloat();
                        List<float[]> u2stdList = new ArrayList<>();

                        for (int i = 0; i < 100; i++) {
                            u2stdList.add(Arrays.copyOfRange(ocl, i, ocl.length - (99 - i)));
                        }

                        float[][] u2stdArray = u2stdList.stream().map(floats -> floats.clone()).toArray(float[][]::new);
                        INDArray u2stdIndArray = Nd4j.create(u2stdArray);

                        Function<INDArray, INDArray> testsum = x -> x.sum();
                        INDArray u2stdListSum = applyAlongAxis(testsum, 0, u2stdIndArray);

                        float[] testU2stdListSum = u2stdListSum.data().asFloat();
                        for (int i = 0; i < testU2stdListSum.length; i++) {
                            if (testU2stdListSum[i] <= 2) {
                                result.add("11. 连续100点最多有1点出界。");
                                break;
                            }
                        }
                    }
                    //组装返回数据
                    featureInfoDtos.setDateStampDay("all");
                    featureInfoDtos.setFeatureName(spcDto.getFeatureName());
                    featureInfoDtos.setLSL(LSL);
                    featureInfoDtos.setUSL(USL);
                    featureInfoDtos.setFeatureLength((int) featureLength);
                    featureInfoDtos.setGroupResize(featureGLength);
                    featureInfoDtos.setFeatureGPoints(featureFValueList);
                    featureInfoDtos.setMean(featureMean[0]);
                    featureInfoDtos.setGroupMean(groupMean);
                    featureInfoDtos.setSigma(featureSigma[0]);
                    featureInfoDtos.setGroupSigma(groupSigmaMean);
                    featureInfoDtos.setSigmaC(sigmaC);
                    featureInfoDtos.setSigmaP(sigmaP);
                    featureInfoDtos.setLCL(LCL);
                    featureInfoDtos.setUCL(UCL);
                    featureInfoDtos.setCP(CP);
                    featureInfoDtos.setCPL(CPL);
                    featureInfoDtos.setCPU(CPU);
                    featureInfoDtos.setCPK(CPK);
                    featureInfoDtos.setPP(PP);
                    featureInfoDtos.setPPL(PPL);
                    featureInfoDtos.setPPU(PPU);
                    featureInfoDtos.setPPK(PPK);
                    featureInfoDtos.setGroupMeanList(teampGroupMrsT0.data().asFloat());
                    featureInfoDtos.setGroupRList(teampGroupMrsT1.data().asFloat());
                    featureInfoDtos.setGroupStdList(teampGroupMrsT2.data().asFloat());
                    featureInfoDtos.setFirstTimePoints(firsttimeGroup);
                    featureInfoDtos.setGLCL(gLcl);
                    featureInfoDtos.setGUCL(gUcl);
                    featureInfoDtos.setMr(MR.data().asFloat());
                    featureInfoDtos.setResult(result);
                    featureInfoDtos.setXbarRLcl(xBarRLcl);
                    featureInfoDtos.setXbarRUcl(xBarRUcl);
                    featureInfoDtos.setXbarRMean(xBarRMean);
                    featureInfoDtos.setXbarSLcl(xBarSLcl);
                    featureInfoDtos.setXbarSUcl(xBarSUcl);
                    featureInfoDtos.setXbarSMean(xbarSMean);
                    featureInfoDtos.setStdLcl(stdLcl);
                    featureInfoDtos.setStdUcl(stdUcl);
                    featureInfoDtos.setRLcl(rLcl);
                    featureInfoDtos.setRUcl(rUcl);
                    featureInfoDtos.setMrLcl(mrLCL.getFloat());
                    featureInfoDtos.setMrUcl(mrUCL.getFloat());
                    featureInfoDtos.setMrMean(MRMean.getFloat());
                    featureInfoDtos.setStdMean(groupSigmaMean);
                    featureInfoDtos.setRMean(groupRMean);
                    featureInfoDtos.setSigmaStd(sigmaStd);
                    featureInfoDtos.setSigmaR(sigmaR);
                }
                featureResDto.setFeatureInfos(featureInfoDtos);
                String yBeginStr = String.valueOf(yBegin);
                BigDecimal bdYBegin = new BigDecimal(yBeginStr);
                BigDecimal adjustment = bdYBegin.multiply(new BigDecimal("0.15")).abs();
                bdYBegin = bdYBegin.subtract(adjustment).setScale(2, RoundingMode.HALF_UP);
                yBegin = (float) bdYBegin.doubleValue();

                // 对y_end进行四舍五入
                String yEndStr = String.valueOf(yEnd);
                BigDecimal bdYEnd = new BigDecimal(yEndStr);
                BigDecimal adjustment1 = bdYEnd.multiply(new BigDecimal("0.15")).abs();
                bdYEnd = bdYEnd.add(adjustment1).setScale(2, RoundingMode.HALF_UP);
                yEnd = (float) bdYEnd.doubleValue();
                featureResDto.setMinY(yBegin);
                featureResDto.setMaxY(yEnd);
                featureResponseDtos.add(featureResDto);
            } else {
                throw new InkelinkException("筛选后数据为空,请重新选择筛选条件");

            }

        }
        //


        featureInfo.setFeatureResponseDtos(featureResponseDtos);
        //
        return new ResultVO<FeatureInfo>().ok(featureInfo, "计算SPC成功");
    }

    private float[] noZeroArray(float[] groupDatum) {
        List<Float> nonZeroElements = new ArrayList<>();

        // 遍历groupDatum数组，将非零元素添加到nonZeroElements列表中
        for (float element : groupDatum) {
            if (element != 0) {
                nonZeroElements.add(element);
            }
        }

        // 将nonZeroElements列表转换为新的float[]数组
        float[] resultArray = new float[nonZeroElements.size()];
        for (int i = 0; i < nonZeroElements.size(); i++) {
            resultArray[i] = nonZeroElements.get(i);
        }
        return resultArray;
    }

    private String[] minitableToArray(String minitable) {
        String[] parts = minitable.split(",");
        int length = parts.length;
        int targetLength = StringUtils.countMatches(minitable, ",") + 1;
        if (length < targetLength) {
            parts = Arrays.copyOf(parts, targetLength);
            for (int i = length; i < targetLength; i++) {
                parts[i] = "";
            }
        }
        return parts;
    }


    private List<String> getFirstTime(String[] timeRArray, int groupSize) {
        List<List<String>> groups = new ArrayList<>();
        for (int i = 0; i < timeRArray.length; i += groupSize) {
            if (i + groupSize > timeRArray.length) {
                break;
            }

            List<String> subList = Arrays.asList(Arrays.copyOfRange(timeRArray, i, i + groupSize));
            groups.add(subList);
        }

        List<String> firstElements = new ArrayList<>();
        for (List<String> group : groups) {
            if (!group.isEmpty()) {
                firstElements.add(group.get(0));
            }
        }
        return firstElements;
    }

    private float[][] listToArray(List<List<Float>> featureRList) {
        int rows = featureRList.size();
        int cols = featureRList.get(0).size();

        float[][] featureArray = new float[rows][cols];
        for (int i = 0; i < featureRList.size(); i++) {
            for (int j = 0; j < featureRList.get(i).size(); j++) {
                featureArray[i][j] = featureRList.get(i).get(j);
            }
        }
        return featureArray;
    }

    private List<FeatureValueDto> removeEndQuery(List<FeatureValueDto> featureValueList, String sampleNum) {
        int sampleSize = 0;
        try {
            sampleSize = Integer.parseInt(sampleNum);
        } catch (NumberFormatException e) {
            //无法转换则取默认值
        }
        int n = Math.max(sampleSize > 0 ? sampleSize : 5, 1);

        for (int i = 0; i < n; i++) {
            featureValueList.remove(featureValueList.size() - 1);
        }
        return featureValueList;
    }

    private List<FeatureValueDto> hourQuery(List<FeatureValueDto> featureValueList, String sampleNum) {
        int sampleSize = 0;
        try {
            sampleSize = Integer.parseInt(sampleNum);
        } catch (NumberFormatException e) {
            //无法转换则取默认值
        }
        int n = Math.max(sampleSize > 0 ? sampleSize : 5, 1);
        List<FeatureValueDto> sampledList = new ArrayList<>();

        HashMap<Integer, List<FeatureValueDto>> objectObjectHashMap = new HashMap<>();
        for (FeatureValueDto featureValueDto : featureValueList) {
            String conditionValue = featureValueDto.getConditionValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(conditionValue);
                Integer hour = date.getHours();
                List<FeatureValueDto> objectsForHour = objectObjectHashMap.getOrDefault(hour, new ArrayList<>());
                objectsForHour.add(featureValueDto);
                objectObjectHashMap.put(hour, objectsForHour);
            } catch (ParseException e) {
                throw new InkelinkException("解析日期错误,请检查");
            }
        }
        for (List<FeatureValueDto> value : objectObjectHashMap.values()) {
            Collections.shuffle(value);
            if (value.size() > n) {
                sampledList.addAll(value.subList(0, n));
            } else {
                sampledList.addAll(value);
            }
        }

        return sampledList;
    }

    private List<FeatureValueDto> sampleQuery(List<FeatureValueDto> featureValueList, String sampleNum) {
        int sampleSize = 0;
        try {
            sampleSize = Integer.parseInt(sampleNum);
        } catch (NumberFormatException e) {
            //无法转换则取默认值
        }
        int n = Math.max(sampleSize > 0 ? sampleSize : 1, 1);
        List<FeatureValueDto> sampledList = new ArrayList<>();

        for (int i = 0; i < featureValueList.size(); i += n) {
            sampledList.add(featureValueList.get(i));
        }

        return sampledList;
    }

    private Random random = SecureRandom.getInstanceStrong();
    private List<FeatureValueDto> randomQuery(List<FeatureValueDto> featureValueList, String sampleNum) {
        int sampleSize = 0;
        try {
            sampleSize = Integer.parseInt(sampleNum);
        } catch (NumberFormatException e) {
            //无法转换则取默认值
        }
        int maxSampleNum = Math.max(sampleSize > 0 ? sampleSize : 200, 1);

        List<FeatureValueDto> sampledList = new ArrayList<>(maxSampleNum);

        if (featureValueList.size() <= maxSampleNum) {
            return new ArrayList<>(featureValueList);
        }

        for (int i = 0; i < maxSampleNum; i++) {
            int index = random.nextInt(featureValueList.size());
            sampledList.add(featureValueList.get(index));
            featureValueList.remove(index); // 移除已选中的元素以避免重复
        }

        return sampledList;

    }

    private List<FeatureValueDto> resultFilter(List<FeatureValueDto> featureValueList, String resultType, Float lowerLimit, Float upperLimit) {
        if ("pass".equals(resultType)) {
            //筛选成功
            return featureValueList.stream().filter(featureValueDto -> featureValueDto.getFeatureValue() <= upperLimit && featureValueDto.getFeatureValue() >= lowerLimit).collect(Collectors.toList());
        } else {
            //筛选失败
            return featureValueList.stream().filter(featureValueDto -> featureValueDto.getFeatureValue() > upperLimit || featureValueDto.getFeatureValue() < lowerLimit).collect(Collectors.toList());
        }
    }

    private List<FeatureValueDto> featureValueFilter(List<FeatureValueDto> featureValueList, String filterFeatureValue, String flag) {
        float value = Float.parseFloat(filterFeatureValue);
        if (flag == "upper") {
            //筛选小于等于最大住
            return featureValueList.stream().filter(featureValueDto -> featureValueDto.getFeatureValue() <= value).collect(Collectors.toList());
        } else {
            //筛选大于等于最小值
            return featureValueList.stream().filter(featureValueDto -> featureValueDto.getFeatureValue() >= value).collect(Collectors.toList());
        }
    }

    private static void xlsData(Sheet sheet) {
        // sheet.getPhysicalNumberOfRows()获取总的行数
        // 循环读取每一行
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            // 循环读取每一行
            Row row = sheet.getRow(i);
            // row.getPhysicalNumberOfCells()获取总的列数
            for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                // 获取数据，但是我们获取的cell类型
                //代码上的内容自己根据实际需要自己调整就可以，这里只是展示一个样式···~
                Cell cell = row.getCell(index);
                System.out.println("cell:" + cell);
                // 转换为字符串类型
                //cell.setCellType(CellType.STRING);
                //获取值
                cell.getStringCellValue();

            }
        }
    }

    private static ArrayList<SpcFileDto> xlsxData(XSSFWorkbook xssfWorkbook) {
        ArrayList<SpcFileDto> spcFileDtoList = new ArrayList<>();
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        //获取最后一行的num，即总行数。此处从0开始
        int maxRow = sheet.getLastRowNum();
        for (int row = 1; row <= maxRow; row++) {
            SpcFileDto spcFileDto = new SpcFileDto();
            if (StringUtils.isBlank(dataFormatter.formatCellValue(sheet.getRow(row).getCell(1)))) {
                break;
            }
            spcFileDto.setFeatureName(dataFormatter.formatCellValue(sheet.getRow(row).getCell(1)));
            String featureValue = dataFormatter.formatCellValue(sheet.getRow(row).getCell(2));
            if (StringUtils.isNotBlank(featureValue)) {
                spcFileDto.setFeatureValue(Float.parseFloat(featureValue));
            } else {
                throw new InkelinkException("表格特征值不能为空");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateCellValue = sheet.getRow(row).getCell(3).getDateCellValue();
            if (dateCellValue == null) {
                throw new InkelinkException("表格时间不能为空");
            }
            String conditionValue = sdf.format(dateCellValue);
            spcFileDto.setConditionValue(conditionValue);
            String isSpcParam = dataFormatter.formatCellValue(sheet.getRow(row).getCell(4));
            spcFileDto.setIsSpcParam(isSpcParam == "0" ? true : false);
            String upperLimit = dataFormatter.formatCellValue(sheet.getRow(row).getCell(5));
            if (StringUtils.isNotBlank(upperLimit)) {
                spcFileDto.setUpperLimit(Float.parseFloat(upperLimit));
            }
            String lowerLimit = dataFormatter.formatCellValue(sheet.getRow(row).getCell(6));
            if (StringUtils.isNotBlank(lowerLimit)) {
                spcFileDto.setLowerLimit(Float.parseFloat(lowerLimit));
            }
            String unit = dataFormatter.formatCellValue(sheet.getRow(row).getCell(7));
            if (StringUtils.isNotBlank(unit)) {
                spcFileDto.setUnit(unit);
            }
            String vin = dataFormatter.formatCellValue(sheet.getRow(row).getCell(8));
            if (StringUtils.isNotBlank(vin)) {
                spcFileDto.setVin(vin);
            }
            spcFileDtoList.add(spcFileDto);
        }


        return spcFileDtoList;
    }

    private static List<FeatureValueDto> timeQuery(List<FeatureValueDto> featureValueDtos, String range) {
        String[] rangeList = range.split(",");
        return featureValueDtos.stream()
                .filter(featureValue -> {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime conditionDate = LocalDateTime.parse(featureValue.getConditionValue(), formatter);
                        LocalDateTime start = LocalDateTime.parse(rangeList[0], formatter);
                        LocalDateTime end = LocalDateTime.parse(rangeList[1], formatter);
                        return conditionDate.isAfter(start) && conditionDate.isBefore(end);
                    } catch (Exception e) {
                        throw new InkelinkException("通过时间范围条件筛选失败: " + e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }


    private static Tuple2<float[], float[]> simpleHistogram(float[] Sample) throws Exception {
        return simpleHistogram(Sample, 10);
    }

    private static Tuple2<float[], float[]> simpleHistogram(float[] Sample, int Bin) throws Exception {
        INDArray a = Nd4j.create(Sample);
        //这一部分返回binedge
        Tuple2<float[], Tuple3<Float, Float, Integer>> temp = getBinEdges(a, Bin);
        float[] beginEdges = temp._1;
        Tuple3<Float, Float, Integer> uniform_bins = temp._2;
        //拆箱
        float firstEdge = uniform_bins._1;
        float lastEdge = uniform_bins._2;
        int nEqualBins = uniform_bins._3;
        //新建一个长度为nEqualBins 全0的直方图数组
        float[] n = new float[nEqualBins];
        for (int i2 = 0; i2 < nEqualBins; i2++) {
            n[i2] = 0;
        }
        //计算出合理分布的最大上限
        float norm = nEqualBins / Math.abs(lastEdge - firstEdge);
        INDArray fIndices = a.sub(firstEdge).mul(norm);
        //快速转换类型的方法
        INDArray indices = fIndices.castTo(DataType.INT64);

        //indices= indices.assignIf(indices.sub(1), Conditions.equals(nEqualBins));
        //
        BooleanIndexing.replaceWhere(indices, (nEqualBins - 1), Conditions.equals(nEqualBins));

        INDArray binEdges = Nd4j.create(beginEdges);

        INDArray decrement = a.lt(binEdges.get(indices));

        indices = indices.subi(Nd4j.scalar(1.0).mul(decrement.castTo(indices.dataType())));

        indices = indices.castTo(DataType.FLOAT);
        //将BOOL值转换为Int8进行处理
        INDArray tempInt8a = binEdges.get(indices.add(1)).lt(a);
        //.castTo(DataType.INT);
        INDArray tempInt8b = indices.neq(nEqualBins - 1);

        INDArray increment = tempInt8a.castTo(DataType.INT).mul(tempInt8b.castTo(DataType.INT)).castTo(DataType.BOOL);

        indices = indices.add(Nd4j.scalar(1.0).mul(increment.castTo(indices.dataType())));

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : indices.data().asInt()) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        //
        int maxKey = Collections.max(frequencyMap.keySet());
        for (int i = 0; i <= maxKey; i++) {
            frequencyMap.putIfAbsent(i, 0);
        }

        // Float.parseFloat()

        Object[] op = frequencyMap.values().toArray();

        float[] result2 = new float[op.length];
        for (int i = 0; i < op.length; i++) {
            result2[i] = Float.parseFloat(op[i].toString()); // 注意：自动拆箱
        }


        // 计算每个唯一值的频数


        return Tuple.of(result2, beginEdges);
    }

    private static Tuple2<float[], Tuple3<Float, Float, Integer>> getBinEdges(INDArray a, int Bin) throws Exception {
        int nEqualBins = Bin;
        INDArray binEdges;
        float firstEdge = getOuterEdges(a)._1;
        float lastEdge = getOuterEdges(a)._2;

        return npLinspace(firstEdge, lastEdge, nEqualBins + 1);
    }

    private static Tuple2<Float, Float> getOuterEdges(INDArray a) throws Exception {
        //float c =23.34F;
        Float min = 0F;
        Float max = 0F;
        if (a.length() == 0) {
            throw new InkelinkException("错误：样本数量不能为0");
        } else {
            min = a.min().data().asFloat()[0];
            max = a.max().data().asFloat()[0];
        }
        if (min.equals(max)) {
            min = min - 0.5F;
            max = max - 0.5F;
        }
        return Tuple.of(min, max);
    }


    private static Tuple2<float[], Tuple3<Float, Float, Integer>> npLinspace(float firstEdge, float lastEdge, int interval) {
        //   interval
        int div = interval - 1;
        float delta = lastEdge - firstEdge;
        //开辟一个从0开始inerval 大小的数
        float[] y = new float[interval];
        for (int i = 0; i < interval; i++) {
            y[i] = (float) i;
        }
        INDArray ndy = Nd4j.create(y);
        if (interval > 1) {
            float step = delta / div;
            ndy = ndy.mul(step);
        } else {
            ndy = ndy.mul(delta);
        }
        ndy = ndy.add(firstEdge);
        //
        ndy.putScalar(ndy.length() - 1, lastEdge);
        return Tuple.of(ndy.data().asFloat(), Tuple.of(firstEdge, lastEdge, div));
    }


    private static float[] normFun(float[] x, float mean, float sigma) {

        if (sigma == 0) {
            return new float[x.length];
        } else {
            INDArray a = Nd4j.create(x);


            INDArray teamINDarray = Nd4j.math().exp((((a.sub(mean)).mul(a.sub(mean)).mul(-1)).div(2 * Math.pow(sigma, 2)))).div(sigma * Math.pow(2 * Math.PI, 0.5));


            return teamINDarray.data().asFloat();
        }

    }

    public static float[] convolution(float[] input, float[] kernel) {
        float[] output = new float[input.length + kernel.length - 1];
        for (int i = 0; i < output.length; i++) {
            for (int j = Math.max(0, i - (kernel.length - 1)); j <= Math.min(i, input.length - 1); j++) {
                output[i] += input[j] * kernel[i - j];
            }
        }
        return output;
    }

    public static INDArray applyAlongAxis(Function<INDArray, INDArray> function, int axis, INDArray input) {
        //INDArray slices

        int totalAxis;
        if (axis == 0) {
            totalAxis = input.columns();
        } else {
            totalAxis = input.rows();
        }
        INDArray[] ss = new INDArray[totalAxis];


        for (int i = 0; i < totalAxis; i++) {
            INDArray teapArray;
            if (axis == 0) {
                teapArray = input.get(NDArrayIndex.all(), NDArrayIndex.point(i));
            } else {
                teapArray = input.get(NDArrayIndex.point(i), NDArrayIndex.all());

            }
            ss[i] = function.apply(teapArray);
        }
        INDArray temp = ss[0];

        if (ss.length > 1) {
            for (int i = 1; i < ss.length; i++) {
                temp = Nd4j.concat(axis, temp, ss[i]);
            }
        }
        return temp;
    }

    public static Double allStd(INDArray testArray) {
        Double meanValue = testArray.mean().data().asDouble()[0];
        Double sampleCount = Double.parseDouble(String.valueOf(testArray.length()));

        INDArray computArray = Nd4j.create(testArray.data().asDouble());

        Double powerSum = Nd4j.math().pow(Nd4j.math().sub(computArray, meanValue), 2).sum().data().asDouble()[0];

        Double powerSub = powerSum / sampleCount;

        Double finalValue = Math.pow(powerSub, 0.5);
        return finalValue;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://10.23.1.152:8079/fs/pqs/23/12/acad0a65b03a6deb.xlsx";
        XSSFWorkbook workbook = downloadWorkbookFromUrl(url);
        ArrayList<SpcFileDto> spcDtos = new ArrayList<>();
        spcDtos = xlsxData(workbook);
        workbook.close();
        Map<String, List<SpcFileDto>> groupedUsers = spcDtos.stream()
                .collect(Collectors.groupingBy(SpcFileDto::getFeatureName));
        ArrayList<SpcDto> spcDtoList = new ArrayList<>();
        for (Map.Entry<String, List<SpcFileDto>> entry : groupedUsers.entrySet()) {
            SpcDto spcDto = new SpcDto();
            List<SpcFileDto> spcFileDtos = entry.getValue();
            SpcFileDto spcFileDto = spcFileDtos.get(0);
            String unit = spcFileDto.getUnit();
            String vin = spcFileDto.getVin();
            String featureName = spcFileDto.getFeatureName();
            Float upperLimit = spcFileDto.getUpperLimit();
            Float lowerLimit = spcFileDto.getLowerLimit();
            spcDto.setFeatureName(featureName);
            spcDto.setLowerLimit(lowerLimit);
            spcDto.setUpperLimit(upperLimit);
            spcDto.setUnit(unit);
            spcDto.setVin(vin);
            ArrayList<FeatureValueDto> featureValueDtoList = new ArrayList<>();
            for (SpcFileDto spcFile : spcFileDtos) {
                FeatureValueDto featureValueDto = new FeatureValueDto();
                featureValueDto.setFeatureValue(spcFile.getFeatureValue());
                featureValueDto.setConditionValue(spcFile.getConditionValue());
                featureValueDto.setIsSpcParam(spcFile.getIsSpcParam());
                featureValueDtoList.add(featureValueDto);
            }
            spcDto.setFeatureValueList(featureValueDtoList);
            spcDtoList.add(spcDto);
        }

    }

    public static XSSFWorkbook downloadWorkbookFromUrl(String urlString) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlString).openStream())) {
            return new XSSFWorkbook(in);
        }catch (Exception e){
            throw new InkelinkException("请按规范上传文件" );
        }
    }

}
