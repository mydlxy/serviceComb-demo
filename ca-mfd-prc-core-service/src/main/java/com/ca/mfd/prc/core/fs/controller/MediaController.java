package com.ca.mfd.prc.core.fs.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.obs.config.LocalUploadProperties;
import com.ca.mfd.prc.common.obs.config.ObsClientStrategy;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SerialNoHelper;
import com.ca.mfd.prc.core.fs.dto.DownFileModelDto;
import com.ca.mfd.prc.core.fs.dto.FileUploadViewModel2;
import com.ca.mfd.prc.core.fs.dto.ImgModel;
import com.ca.mfd.prc.core.fs.dto.MultiDirSelectDto;
import com.ca.mfd.prc.core.fs.vo.DirectoryNode;
import com.ca.mfd.prc.core.fs.vo.MultiDirFilesVO;
import com.ca.mfd.prc.core.fs.vo.ServiceFileInfo;
import com.ca.mfd.prc.core.main.entity.SysMediaEntity;
import com.ca.mfd.prc.core.main.service.ISysMediaService;
import com.obs.services.ObsClient;
import com.obs.services.model.CreateBucketRequest;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 媒体中心-上传服务
 *
 * @author inkelink
 * @date 2023/10/11
 */
@Tag(name = "上传服务", description = "上传服务API")
@RestController
@RequestMapping("fs/media")
public class MediaController {


    public static final String NO_FILE_ERR_MSG = "不包含任何文件";
    public static final String WRONG_TYPE_FILE = "请上传指定格式的文件";
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);
    public static final String PROVIDER_HUAWEI = "HUAWEI";
    private static final String PUBLIC_LEVEL = "public";
    private static final String PRIVATE_LEVEL = "private";
    public static final String PROTOCOL = "http://";
    @Autowired
    private ISysMediaService sysMediaService;


    @Autowired
    private LocalUploadProperties localUploadProperties;

    @Value("${ca.cloud.obs.enable:}")
    private boolean ossEnable;

    @Autowired
    @Lazy
    private ObsClientStrategy obsClientStrategy;

    @Operation(summary = "上传图片（压缩）base64转换")
    @PostMapping("/uploadfilebybase64")
    public ResultVO<List<String>> uploadFileByBase64(@RequestBody FileUploadViewModel2 fileUploadViewModel2) {
        return uploadFileByBase64BySecurityStrategy(fileUploadViewModel2, PUBLIC_LEVEL);
    }

    @Operation(summary = "上传图片（压缩）base64转换(私有桶)")
    @PostMapping("/uploadfilebybase64private")
    public ResultVO<List<String>> uploadFileByBase64Private(@RequestBody FileUploadViewModel2 fileUploadViewModel2) {
        return uploadFileByBase64BySecurityStrategy(fileUploadViewModel2, PRIVATE_LEVEL);
    }

    private ResultVO<List<String>> uploadFileByBase64BySecurityStrategy(FileUploadViewModel2 fileUploadViewModel2, String strategy) {
        List<String> list = new ArrayList<>();
        List<SysMediaEntity> medias = new ArrayList<>();
        String category = (fileUploadViewModel2.getCategory() == null || fileUploadViewModel2.getCategory().isEmpty()) ? "pub" : fileUploadViewModel2.getCategory();
        for (ImgModel data : fileUploadViewModel2.getData()) {
            if (data == null || data.getBase64() == null || data.getBase64().isEmpty()) {
                throw new InkelinkException("文件流为空");
            }

            String base64Data = getBase64Data(data);

            byte[] bytes = Base64.getDecoder().decode(base64Data);

            String fileName = SerialNoHelper.initializeSerialNo() + getFileNameExtension(data.getFileName());
            String oneFilePath;
            String dbFilePath;

            dbFilePath = (ossEnable ? "" : "/fs/") + category.toLowerCase() + StringPool.SLASH + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy")) + StringPool.SLASH + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
            oneFilePath = Paths.get(localUploadProperties.getRootDir(), "fs", category.toLowerCase(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy")), LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"))).toString();
            Path getOneFilePath = Paths.get(oneFilePath);
            mkdirIfNotExist(getOneFilePath);

            String path = Paths.get(oneFilePath, fileName).toString();

            String newPath;
            try {
                Path getPath = Paths.get(path);
                Files.write(getPath, bytes);
                newPath = dbFilePath + StringPool.SLASH + fileName;
                if (ossEnable) {
                    uploadLocalToOss(newPath, getPath, strategy);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + strategy + StringPool.SLASH + newPath;
                    String ossPath = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + newPath;
                    medias.add(new SysMediaEntity(category, data.getFileName(), filePath, ossPath, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(strategy)));
                } else {
                    Files.write(getPath, bytes);
                    medias.add(new SysMediaEntity(category, data.getFileName(), newPath, path));
                }
            } catch (IOException e) {
                throw new InkelinkException("无法写入文件", e);
            }


            list.add(newPath);
        }

        CompletableFuture.runAsync(() -> {
            sysMediaService.insertBatch(medias);
            sysMediaService.saveChange();
        });

        ResultVO<List<String>> resultVO = new ResultVO<>();
        return resultVO.ok(list, "成功");
    }

    private String getBase64Data(ImgModel data) {
        String base64Data;
        String[] dataStr = data.getBase64().split(",");
        String imgType;
        String ext = ".png";

        if (dataStr.length > 1) {
            imgType = getImageType(dataStr[0]);
            ext = getImageExt(imgType);
            base64Data = dataStr[1];
        } else {
            base64Data = dataStr[0];
        }

        noFileNameHandler(data, ext);
        return base64Data;
    }

    private void mkdirIfNotExist(Path getOneFilePath) {
        if (!Files.exists(getOneFilePath)) {
            try {
                Files.createDirectories(getOneFilePath);
            } catch (IOException e) {
                throw new InkelinkException("无法创建目录", e);
            }
        }
    }


    private void noFileNameHandler(ImgModel data, String ext) {
        if (data.getFileName() == null || data.getFileName().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            data.setFileName(LocalDateTime.now().format(formatter) + ext);
        }
    }

    private void uploadLocalToOss(String objectName, Path getPath, String strategy) throws IOException {
        String bucketName = obsClientStrategy.getBucketNameByCode(strategy);
        try (ObsClient obsClient = obsClientStrategy.getClientByCode(strategy)) {
            boolean found = obsClient.headBucket(bucketName);
            if (!found) {
                throw new IllegalArgumentException("未配置桶信息：" + bucketName);
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, getPath.toFile());
            obsClient.putObject(putObjectRequest);
        }
        Files.delete(getPath);
    }

    @Operation(summary = "上传到Oss")
    @PostMapping("/uploadfiletooss/{category}")
    public ResultVO<List<String>> uploadFileToOss(HttpServletRequest request, @PathVariable String category) throws ServletException, IOException {
        return uploadFileToOssByStrategy(request, category, PUBLIC_LEVEL);
    }


    @Operation(summary = "上传到Oss(私有桶)")
    @PostMapping("/uploadfiletoprivateoss/{category}")
    public ResultVO<List<String>> uploadFileToPrivateOss(HttpServletRequest request, @PathVariable String category) throws ServletException, IOException {
        return uploadFileToOssByStrategy(request, category, PRIVATE_LEVEL);
    }

    private ResultVO<List<String>> uploadFileToOssByStrategy(HttpServletRequest request, String category, String strategy) throws IOException, ServletException {
        List<Part> files = new ArrayList<>(request.getParts());
        if (files.isEmpty()) {
            throw new InkelinkException(NO_FILE_ERR_MSG);
        }
        List<Part> limitFiles = files.stream().filter(part -> !localUploadProperties.getFileTypes().contains(getFileNameExtension(part.getSubmittedFileName()).toLowerCase())).collect(Collectors.toList());
        if (!limitFiles.isEmpty()) {
            throw new InkelinkException(WRONG_TYPE_FILE);
        }
        List<String> list = new ArrayList<>();
        String oneFilePath = new File(localUploadProperties.getRootDir(), "fs/oss").getAbsolutePath();
        File directory = new File(oneFilePath);
        boolean mkdirs = true;
        if (!directory.exists()) {
            mkdirs = directory.mkdirs();
        }

        if (mkdirs) {
            for (Part file : files) {
                String fileName = UUID.randomUUID() + "_" + file.getSubmittedFileName();
                String path = oneFilePath + File.separator + fileName;
                String ossPath;

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                }

                String bucketName = obsClientStrategy.getBucketNameByCode(strategy);
                String objectName = category + StringPool.SLASH + fileName;
                ObsClient obsClient = obsClientStrategy.getClientByCode(strategy);
                boolean found = obsClient.headBucket(bucketName);
                if (!found) {
                    CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                    obsClient.createBucket(createBucketRequest);
                }
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(path));

                obsClient.putObject(putObjectRequest);

                ossPath = PROTOCOL + bucketName + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + objectName;
                obsClient.close();

                sysMediaService.insert(new SysMediaEntity(category, "obs" + StringPool.SLASH + strategy + StringPool.SLASH + objectName, PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + objectName, path));

                list.add(ossPath);

                File tempFile = new File(path);
                try {
                    Files.delete(tempFile.toPath());
                } catch (Exception e) {
                    throw new InkelinkException("删除临时文件失败");
                }
            }
            sysMediaService.saveChange();
        }
        ResultVO<List<String>> resultVO = new ResultVO<>();
        return resultVO.ok(list, "成功");
    }

    @Operation(summary = "通过路径换取原始文件")
    @GetMapping("/getossfile/**")
    public ResultVO<String> getOssFile(HttpServletRequest request) throws IOException {
        return getOssFileByStrategy(request, PUBLIC_LEVEL);
    }

    @Operation(summary = "通过路径换取原始文件(私有桶)")
    @GetMapping("/getprivateossfile/**")
    public ResultVO<String> getPrivateOSSFile(HttpServletRequest request) throws IOException {
        return getOssFileByStrategy(request, PRIVATE_LEVEL);
    }

    private ResultVO<String> getOssFileByStrategy(HttpServletRequest request, String strategy) throws UnsupportedEncodingException {
        String requestURI = request.getRequestURI();
        String methodName = "getOSSFile";
        String objectNameAndExpire = requestURI.substring(requestURI.lastIndexOf(methodName) + methodName.length() + 1);
        String allowFileType = localUploadProperties.getFileTypes().stream().filter(objectNameAndExpire::contains).findAny().orElse("");
        String ossPath = "";
        if (StringUtils.isNotBlank(allowFileType)) {
            String objectName = objectNameAndExpire.substring(0, objectNameAndExpire.lastIndexOf(allowFileType)) + allowFileType;
            String decodeObjectName = URLDecoder.decode(objectName, "UTF-8");
            ossPath = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + decodeObjectName;
        }
        ResultVO<String> resultVO = new ResultVO<>();
        return resultVO.ok(ossPath, "成功");
    }

    @Operation(summary = "分类文件上传")
    @PostMapping("/posts/{category}")
    public ResultVO<List<String>> posts(@PathVariable String category, @RequestParam("file") MultipartFile[] file) {
        return posts(category, StringUtils.EMPTY, false, file);
    }

    @Operation(summary = "分类文件上传指定目录")
    @PostMapping("/posts/{category}/{customerpath}")
    public ResultVO<List<String>> posts(@PathVariable String category, @PathVariable(required = false) String customerpath, @RequestParam("file") MultipartFile[] file) {
        return posts(category, customerpath, false, file);
    }

    @Operation(summary = "分类文件上传指定目录并覆盖")
    @PostMapping("/posts/{category}/{customerpath}/{cover}")
    public ResultVO<List<String>> posts(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @RequestParam("file") MultipartFile[] file) {
        return postsByStrategy(category, customerpath, cover, file, PUBLIC_LEVEL);
    }

    @Operation(summary = "分类文件上传(私有桶)")
    @PostMapping("/privateposts/{category}")
    public ResultVO<List<String>> privatePosts(@PathVariable String category, @RequestParam("file") MultipartFile[] file) {
        return privatePosts(category, StringUtils.EMPTY, false, file);
    }

    @Operation(summary = "分类文件上传指定目录(私有桶)")
    @PostMapping("/privateposts/{category}/{customerpath}")
    public ResultVO<List<String>> privatePosts(@PathVariable String category, @PathVariable(required = false) String customerpath, @RequestParam("file") MultipartFile[] file) {
        return privatePosts(category, customerpath, false, file);
    }

    @Operation(summary = "分类文件上传指定目录并覆盖(私有桶)")
    @PostMapping("/privateposts/{category}/{customerpath}/{cover}")
    public ResultVO<List<String>> privatePosts(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @RequestParam("file") MultipartFile[] file) {
        return postsByStrategy(category, customerpath, cover, file, PRIVATE_LEVEL);
    }

    private ResultVO<List<String>> postsByStrategy(String category, String customerpath, Boolean cover, MultipartFile[] file, String strategy) {
        if (file == null || file.length == 0) {
            throw new InkelinkException(NO_FILE_ERR_MSG);
        }

        category = (category == null || category.trim().isEmpty()) ? "pub" : category;
        category = category.toLowerCase();

        try {
            checkFileSizeLimit(file);
            List<String> list = new ArrayList<>();
            for (MultipartFile f : file) {
                UploadPathSet pathSet = getUploadPathSet(category, customerpath);
                promiseDir(pathSet.oneFilePath);

                String fileName = genFileName(cover, f);

                String newPath = pathSet.dbFilePath + StringPool.SLASH + fileName;

                String path = getOneFilePath(pathSet, fileName);

                Path getPath = Paths.get(path);
                Files.copy(f.getInputStream(), getPath, StandardCopyOption.REPLACE_EXISTING);
                if (ossEnable) {
                    uploadLocalToOss(newPath, getPath, strategy);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + strategy + StringPool.SLASH + newPath;
                    String ossUrl = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + newPath;
                    sysMediaService.insert(new SysMediaEntity(category, f.getOriginalFilename(), filePath, ossUrl, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(strategy)));
                    list.add(filePath);
                } else {
                    promiseDir(pathSet.oneFilePath);
                    Files.copy(f.getInputStream(), getPath, StandardCopyOption.REPLACE_EXISTING);
                    sysMediaService.insert(new SysMediaEntity(category, f.getOriginalFilename(), newPath, path));
                    list.add(newPath);
                }


            }

            sysMediaService.saveChange();
            ResultVO<List<String>> resultVO = new ResultVO<>();
            return resultVO.ok(list, "成功");
        } catch (Exception ex) {
            throw new InkelinkException(ex.getMessage());
        }
    }

    private static String getOneFilePath(UploadPathSet pathSet, String fileName) {
        return pathSet.oneFilePath + StringPool.SLASH + fileName;
    }

    private String genFileName(Boolean cover, MultipartFile f) {
        if (Boolean.TRUE.equals(cover)) {
            logger.debug("cover is deprecated");
        }
        return SerialNoHelper.initializeSerialNo() + getFileNameExtension(Objects.requireNonNull(f.getOriginalFilename())).toLowerCase();
    }

    private UploadPathSet getUploadPathSet(String category, String customerpath) {
        // 本地路径
        String oneFilePath = localUploadProperties.getRootDir() + File.separator + "fs" + File.separator + category.toLowerCase() + File.separator + String.format("%ty", new Date()) + File.separator + String.format("%tm", new Date());
        String dbFilePath = (ossEnable ? "" : "/fs/") + category.toLowerCase() + StringPool.SLASH + String.format("%ty", new Date()) + StringPool.SLASH + String.format("%tm", new Date());

        if (customerpath != null && !customerpath.trim().isEmpty()) {
            // 本地路径
            oneFilePath = localUploadProperties.getRootDir() + File.separator + "fs" + File.separator + category.toLowerCase() + File.separator + customerpath;
            dbFilePath = (ossEnable ? "" : "/fs/") + category.toLowerCase() + StringPool.SLASH + customerpath;
        }
        return new UploadPathSet(oneFilePath, dbFilePath);
    }

    private static class UploadPathSet {
        public final String oneFilePath;
        public final String dbFilePath;

        public UploadPathSet(String oneFilePath, String dbFilePath) {
            this.oneFilePath = oneFilePath;
            this.dbFilePath = dbFilePath;
        }
    }

    @Operation(summary = "上传压缩文件到指定目录并且按照自定文件解压-覆盖")
    @PostMapping("/postszip/{category}/{customerpath}/{cover}/{Uncompress}")
    public ResultVO<List<String>> postsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @PathVariable(required = false, name = "Uncompress") Boolean uncompress, @RequestParam("file") MultipartFile[] file) throws IOException {

        return postsZipByStrategy(category, customerpath, cover, uncompress, file, PUBLIC_LEVEL);
    }

    @Operation(summary = "上传压缩文件到指定目录并且按照自定文件解压-覆盖(私有桶)")
    @PostMapping("/privatepostszip/{category}/{customerpath}/{cover}/{Uncompress}")
    public ResultVO<List<String>> privatePostsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @PathVariable(required = false, name = "Uncompress") Boolean uncompress, @RequestParam("file") MultipartFile[] file) throws IOException {

        return postsZipByStrategy(category, customerpath, cover, uncompress, file, PRIVATE_LEVEL);
    }


    private void promiseDir(String oneFilePath) {
        File directory = new File(oneFilePath);
        if (!directory.exists() && (directory.mkdirs())) {
            logger.debug("directory mkdir");

        }
    }

    private UploadPathSetWithDatePatternDir getUploadPathSetWithDatePatternDir(String category, String customerpath) {
        // 本地路径
        String oneFilePath = Paths.get(localUploadProperties.getRootDir(), "fs", category.toLowerCase(), String.format("%ty", LocalDateTime.now()), String.format("%tm", LocalDateTime.now())).toString();
        String dbFilePath = (ossEnable ? "" : "/fs/") + category.toLowerCase() + StringPool.SLASH + String.format("%ty", LocalDateTime.now()) + StringPool.SLASH + String.format("%tm", LocalDateTime.now());

        if (customerpath != null && !customerpath.trim().isEmpty()) {
            // 本地路径
            oneFilePath = Paths.get(localUploadProperties.getRootDir(), "fs", category.toLowerCase(), customerpath).toString();
            dbFilePath = (ossEnable ? "" : "/fs/") + category.toLowerCase() + StringPool.SLASH + customerpath;
        }
        return new UploadPathSetWithDatePatternDir(oneFilePath, dbFilePath);
    }

    private static class UploadPathSetWithDatePatternDir {
        public final String oneFilePath;
        public final String dbFilePath;

        public UploadPathSetWithDatePatternDir(String oneFilePath, String dbFilePath) {
            this.oneFilePath = oneFilePath;
            this.dbFilePath = dbFilePath;
        }
    }

    private void checkFileSizeLimit(@RequestParam("file") MultipartFile[] file) {
        List<MultipartFile> limitSizes = Arrays.stream(file).filter(f -> f.getSize() > (200 * 1024 * 1024)).collect(Collectors.toList());

        if (!limitSizes.isEmpty()) {
            throw new InkelinkException("上传的文件中有超过" + 200 + "M的文件");
        }

        List<MultipartFile> limitFiles = Arrays.stream(file).filter(f -> !localUploadProperties.getFileTypes().contains(getFileNameExtension(Objects.requireNonNull(f.getOriginalFilename())).toLowerCase())).collect(Collectors.toList());

        if (!limitFiles.isEmpty()) {
            throw new InkelinkException(WRONG_TYPE_FILE);
        }

    }

    @Operation(summary = "上传压缩文件到指定目录-覆盖")
    @PostMapping("/postszip/{category}/{customerpath}/{cover}")
    public ResultVO<List<String>> postsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @RequestParam("file") MultipartFile[] file) throws IOException {
        return postsZip(category, customerpath, cover, false, file);
    }

    @Operation(summary = "上传压缩文件到指定目录")
    @PostMapping("/postszip/{category}/{customerpath}")
    public ResultVO<List<String>> postsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @RequestParam("file") MultipartFile[] file) throws IOException {
        return postsZip(category, customerpath, false, false, file);
    }

    @Operation(summary = "上传压缩文件(私有桶)")
    @PostMapping("/privatepostszip/{category}")
    public ResultVO<List<String>> privatePostsZip(@PathVariable String category, @RequestParam("file") MultipartFile[] file) throws IOException {
        return privatePostsZip(category, StringUtils.EMPTY, false, false, file);
    }

    @Operation(summary = "上传压缩文件到指定目录-覆盖(私有桶)")
    @PostMapping("/privatepostszip/{category}/{customerpath}/{cover}")
    public ResultVO<List<String>> privatePostsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @PathVariable(required = false) Boolean cover, @RequestParam("file") MultipartFile[] file) throws IOException {
        return privatePostsZip(category, customerpath, cover, false, file);
    }

    @Operation(summary = "上传压缩文件到指定目录(私有桶)")
    @PostMapping("/privatepostszip/{category}/{customerpath}")
    public ResultVO<List<String>> privatePostsZip(@PathVariable String category, @PathVariable(required = false) String customerpath, @RequestParam("file") MultipartFile[] file) throws IOException {
        return privatePostsZip(category, customerpath, false, false, file);
    }

    @Operation(summary = "上传压缩文件")
    @PostMapping("/postszip/{category}")
    public ResultVO<List<String>> postsZip(@PathVariable String category, @RequestParam("file") MultipartFile[] file) throws IOException {
        return postsZip(category, StringUtils.EMPTY, false, false, file);
    }

    private ResultVO<List<String>> postsZipByStrategy(String category, String customerpath, Boolean cover, Boolean uncompress, MultipartFile[] file, String strategy) throws IOException {
        if (file == null || file.length == 0) {
            throw new InkelinkException(NO_FILE_ERR_MSG);
        }

        category = (category == null || category.trim().isEmpty()) ? "pub" : category;
        category = category.toLowerCase();

        checkFileSizeLimit(file);
        List<String> list = new ArrayList<>();

        for (MultipartFile f : file) {
            UploadPathSetWithDatePatternDir pathSet = getUploadPathSetWithDatePatternDir(category, customerpath);

            promiseDir(pathSet.oneFilePath);

            String fileName = genFileName(cover, f);

            String newPath = pathSet.dbFilePath + StringPool.SLASH + fileName;
            String path = pathSet.oneFilePath + StringPool.SLASH + fileName;

            Path getPath = Paths.get(path);
            Files.copy(f.getInputStream(), getPath, StandardCopyOption.REPLACE_EXISTING);
            if (Boolean.TRUE.equals(uncompress)) {

                uncompress(path, pathSet.oneFilePath, pathSet.dbFilePath, category, strategy, list);
                Files.delete(getPath);
            } else {
                if (ossEnable) {
                    uploadLocalToOss(newPath, getPath, strategy);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + strategy + StringPool.SLASH + newPath;
                    String ossUrl = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + newPath;
                    sysMediaService.insert(new SysMediaEntity(category, f.getOriginalFilename(), filePath, ossUrl, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(strategy)));
                    list.add(filePath);
                } else {
                    sysMediaService.insert(new SysMediaEntity(category, f.getOriginalFilename(), newPath, path));
                    list.add(newPath);
                }

            }


        }

        sysMediaService.saveChange();
        ResultVO<List<String>> resultVO = new ResultVO<>();
        return resultVO.ok(list, "成功");
    }

    @Operation(summary = "上传服务文件")
    @PostMapping("/postservices/{category}")
    public ResultVO<List<String>> postServices(@PathVariable String category, @RequestParam(required = false, defaultValue = "上传服务文件") String remark, @RequestParam("file") MultipartFile[] file) {

        if (file == null || file.length == 0) {
            throw new InkelinkException(NO_FILE_ERR_MSG);
        }

        category = category.toLowerCase();
        String oneFilePath = Paths.get(localUploadProperties.getRootDir(), "fs", category.toLowerCase()).toString();

        promiseDir(oneFilePath);

        try {
            List<String> list = new ArrayList<>();

            // 获取文件后缀是否存在数组中
            List<MultipartFile> limitFiles = Arrays.stream(file).filter(f -> !localUploadProperties.getFileTypes().contains(getFileNameExtension(Objects.requireNonNull(f.getOriginalFilename())).toLowerCase())).collect(Collectors.toList());

            if (!limitFiles.isEmpty()) {
                throw new InkelinkException(WRONG_TYPE_FILE);
            }

            for (MultipartFile f : file) {
                // 本地路径
                String dbFilePath = (ossEnable ? "services/" : "/fs/") + category.toLowerCase();
                String newPath = dbFilePath + StringPool.SLASH + f.getOriginalFilename();

                String path = oneFilePath + StringPool.SLASH + f.getOriginalFilename();
                File destFile = new File(path);
                if (destFile.exists()) {
                    Files.delete(destFile.toPath());
                }
                File infoFile = copyServiceInfoFile(category, remark, f, path, oneFilePath);
                Path getPath = Paths.get(path);
                if (ossEnable) {
                    uploadLocalToOss(newPath, getPath, PUBLIC_LEVEL);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + PUBLIC_LEVEL + StringPool.SLASH + newPath;
                    String ossUrl = PROTOCOL + obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL) + StringPool.DOT + obsClientStrategy.getEndpointByCode(PUBLIC_LEVEL) + StringPool.SLASH + newPath;
                    sysMediaService.insert(new SysMediaEntity(category, f.getOriginalFilename(), filePath, ossUrl, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL)));
                    list.add(filePath);
                    if (infoFile != null) {
                        uploadLocalToOss(dbFilePath + StringPool.SLASH + infoFile.getName(), infoFile.toPath(), PUBLIC_LEVEL);
                    }
                } else {
                    list.add(newPath);
                    SysMediaEntity sysMediaInfo = new SysMediaEntity();
                    sysMediaInfo.setCategory(category);
                    sysMediaInfo.setFileName(f.getOriginalFilename());
                    sysMediaInfo.setFilePath(newPath);
                    sysMediaInfo.setPhysicalPath(path);
                    sysMediaService.insert(sysMediaInfo);
                }


            }

            sysMediaService.saveChange();
            ResultVO<List<String>> resultVO = new ResultVO<>();
            return resultVO.ok(list, "成功");
        } catch (Exception ex) {
            throw new InkelinkException(ex.getMessage());
        }

    }

    private static File copyServiceInfoFile(String category, String remark, MultipartFile f, String path, String oneFilePath) {
        try {
            Files.copy(f.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

            ServiceFileInfo fileInfo = new ServiceFileInfo();
            fileInfo.setServiceName(category);
            fileInfo.setLastUpdateTime(new Date());
            fileInfo.setVersionInfoName(f.getOriginalFilename());
            fileInfo.setRemark(remark);

            String infoFilePath = Paths.get(oneFilePath, category + ".info").toString();
            File infoFile = new File(infoFilePath);
            FileUtils.writeStringToFile(infoFile, JSON.toJSONString(fileInfo), StandardCharsets.UTF_8);
            return infoFile;
        } catch (IOException e) {
            logger.error("service file upload error");
        }
        return null;
    }


    @Operation(summary = "获取服务版本下所有文件")
    @GetMapping("/getserviceversion/{category}")
    public ResultVO<List<ComboInfoDTO>> getServiceVersion(@PathVariable String category) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取成功");
        String versionFile;
        if (ossEnable) {
            ObsClient obsClient = obsClientStrategy.getClientByCode(PUBLIC_LEVEL);
            versionFile = "services/" + category.toLowerCase();
            String bucketName = obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL);
            ListObjectsRequest request = new ListObjectsRequest(bucketName);
            request.setPrefix(versionFile);
            ObjectListing objectListing = obsClient.listObjects(request);
            result.setData(convertObsObjListToCombo(objectListing));
        } else {
            versionFile = Paths.get(localUploadProperties.getRootDir(), "fs", category.toLowerCase()).toString();
            File directory = new File(versionFile);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    result.setData(Arrays.stream(files).filter(f -> f.isFile() && StringUtils.endsWith(getFileNameExtension(f.getName()).toLowerCase(), "zip")).map(f -> new ComboInfoDTO(f.getName().replace(".zip", StringUtils.EMPTY), f.getName().replace(".zip", StringUtils.EMPTY))).collect(Collectors.toList()));
                }
            }
        }

        if (result.getData() == null) {
            return result.ok(new ArrayList<>());
        }
        result.setCode(ErrorCode.SUCCESS);
        return result;
    }

    private List<ComboInfoDTO> convertObsObjListToCombo(ObjectListing objectListing) {
        List<ObsObject> obsObjects = objectListing.getObjects();
        List<ComboInfoDTO> comboInfoList = new ArrayList<>();
        for (ObsObject obsObject : obsObjects) {
            String fileName = obsObject.getObjectKey();
            if (fileName.endsWith(".zip")) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                String comboInfoValue = fileName.replace(".zip", StringUtils.EMPTY);
                ComboInfoDTO comboInfoDTO = new ComboInfoDTO(comboInfoValue, comboInfoValue);
                comboInfoList.add(comboInfoDTO);
            }
        }
        return comboInfoList;
    }

    @Operation(summary = "获取最新版本文件")
    @GetMapping("/getnewversion/{category}")
    public ResultVO<String> getNewVersion(@PathVariable String category) {
        ResultVO<String> resultVO = new ResultVO<>();
        category = category.toLowerCase();
        String versionFile;
        if (ossEnable) {
            versionFile = getOSSVersionFile(category);
            if (versionFile != null) {
                String fileName = versionFile.substring(versionFile.lastIndexOf('/') + 1);
                String version = fileName.substring(0, fileName.lastIndexOf('.'));
                return resultVO.ok(version, version);
            }
        } else {
            versionFile = getLocalVersionFile(category);
            if (versionFile != null) {
                String fileName = versionFile.substring(versionFile.lastIndexOf(File.separator) + 1);
                String version = fileName.substring(0, fileName.lastIndexOf('.'));
                return resultVO.ok(version, version);
            }
        }
        return resultVO.error(-1, NO_FILE_ERR_MSG);
    }

    private String getOSSVersionFile(String category) {
        ObsClient obsClient = obsClientStrategy.getClientByCode(PUBLIC_LEVEL);
        String versionFile = "services/" + category.toLowerCase();
        String bucketName = obsClientStrategy.getBucketNameByCode(PUBLIC_LEVEL);
        ListObjectsRequest request = new ListObjectsRequest(bucketName);
        request.setPrefix(versionFile);
        ObjectListing objectListing = obsClient.listObjects(request);
        List<ObsObject> obsObjects = objectListing.getObjects();

        if (!obsObjects.isEmpty()) {
            ObsObject newestFile = obsObjects.stream().filter(obsObject -> StringUtils.endsWithIgnoreCase(obsObject.getObjectKey(), ".zip")).max(Comparator.comparing(obsObject -> obsObject.getMetadata().getLastModified())).orElse(null);

            if (newestFile != null) {
                return newestFile.getObjectKey();
            }
        }

        return null;
    }

    private String getLocalVersionFile(String category) {
        String versionFile = Paths.get(localUploadProperties.getRootDir(), "fs", category).toString();
        File directory = new File(versionFile);

        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {
                File newestFile = Arrays.stream(files).filter(f -> f.isFile() && StringUtils.endsWithIgnoreCase(f.getName(), ".zip")).max(Comparator.comparingLong(File::lastModified)).orElse(null);

                if (newestFile != null) {
                    return newestFile.getName();
                }
            }
        }

        return null;
    }

    @Operation(summary = "获取上传文件目录树(不包含文件)")
    @GetMapping("/dirtree")
    public ResultVO<List<DirectoryNode>> dirTree() {
        List<DirectoryNode> list = new ArrayList<>();

        if (ossEnable) {
            buildBucketDirTreeByStrategy(list, PUBLIC_LEVEL);
            buildBucketDirTreeByStrategy(list, PRIVATE_LEVEL);
        } else {
            File directory = new File(Paths.get(localUploadProperties.getRootDir(), "fs").toString());
            if (!directory.exists() || !directory.isDirectory()) {
                throw new InkelinkException("文件路径不存在");
            }
            DirectoryNode localRoot = new DirectoryNode(directory.getName(), directory.getAbsolutePath(), "");
            buildDirectoryTreeRecursive(directory, localRoot);
            list.add(localRoot);
        }

        ResultVO<List<DirectoryNode>> result = new ResultVO<>();
        return result.ok(list);
    }

    private void buildBucketDirTreeByStrategy(List<DirectoryNode> treeList, String strategy) {
        ObsClient obsClient = obsClientStrategy.getClientByCode(strategy);
        String bucketName = obsClientStrategy.getBucketNameByCode(strategy);
        ObjectListing objectListing = obsClient.listObjects(bucketName);
        List<ObsObject> objects = objectListing.getObjects();
        DirectoryNode root = new DirectoryNode(bucketName, "", strategy);
        buildObsDirTree(objects, root, strategy);
        treeList.add(root);
    }

    private void buildObsDirTree(List<ObsObject> objects, DirectoryNode root, String strategy) {
        objects.forEach(obsObject -> {
            String objectKey = obsObject.getObjectKey();
            String[] pathSegments = objectKey.split("/");
            DirectoryNode currentNode = root;
            for (String segment : pathSegments) {
                if (!segment.isEmpty() && (Objects.nonNull(currentNode))) {
                    DirectoryNode childNode = currentNode.findChildByName(segment);
                    if (childNode == null && (!localUploadProperties.getFileTypes().contains(getFileNameExtension(segment)))) {
                        childNode = new DirectoryNode(segment, currentNode.getPath() + "/" + segment, strategy);
                        currentNode.addChild(childNode);
                    }
                    currentNode = childNode;

                }
            }
        });
    }

    @Operation(summary = "多个目录下的文件列表")
    @PostMapping("/multidirfiles")
    public List<MultiDirFilesVO> multiDirFiles(@RequestBody MultiDirSelectDto dto) {
        List<MultiDirFilesVO> fileList = new ArrayList<>();

        for (MultiDirSelectDto.PathInfo pathInfo : dto.getFilePaths()) {
            List<MultiDirFilesVO> directoryFileList = getDirectoryFileList(pathInfo);
            fileList.addAll(directoryFileList);
        }
        return fileList;

    }

    private List<MultiDirFilesVO> getDirectoryFileList(MultiDirSelectDto.PathInfo pathInfo) {
        List<MultiDirFilesVO> fileList = new ArrayList<>();

        if (ossEnable) {
            listObsFiles(pathInfo, fileList);
        } else {
            listLocalFiles(pathInfo, fileList);
        }

        return fileList;
    }

    private void listLocalFiles(MultiDirSelectDto.PathInfo pathInfo, List<MultiDirFilesVO> fileList) {
        File directory = new File(pathInfo.getFilePath());
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    MultiDirFilesVO vo = new MultiDirFilesVO();
                    vo.setStrategy("");
                    vo.setFileName(file.getName());
                    vo.setFilePath(file.getPath());
                    fileList.add(vo);
                }
            }
        }
    }

    private void listObsFiles(MultiDirSelectDto.PathInfo pathInfo, List<MultiDirFilesVO> fileList) {
        ListObjectsRequest request = new ListObjectsRequest(obsClientStrategy.getBucketNameByCode(pathInfo.getStrategy()));
        String dirPath = pathInfo.getFilePath();
        String folderPrefix = (dirPath.startsWith("/") ? dirPath.substring(1) : dirPath) + (dirPath.endsWith("/") ? "" : "/");
        request.setPrefix(folderPrefix);
        request.setMaxKeys(1000);
        ObjectListing objectListing;
        do {
            ObsClient obsClient = obsClientStrategy.getClientByCode(pathInfo.getStrategy());
            objectListing = obsClient.listObjects(request);
            for (ObsObject object : objectListing.getObjects()) {
                String objectKey = object.getObjectKey();
                if (!objectKey.equals(folderPrefix) && !objectKey.substring(folderPrefix.length()).contains("/")) {
                    String fileName = objectKey.substring(folderPrefix.length());
                    MultiDirFilesVO vo = new MultiDirFilesVO();
                    vo.setStrategy(pathInfo.getStrategy());
                    vo.setFileName(fileName);
                    vo.setFilePath(objectKey);
                    fileList.add(vo);
                }
            }
        } while (objectListing.isTruncated());
    }

    private void buildDirectoryTreeRecursive(File directory, DirectoryNode parentNode) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    DirectoryNode childDirectoryNode = new DirectoryNode(file.getName(), file.getAbsolutePath(), "");
                    parentNode.addChild(childDirectoryNode);
                    buildDirectoryTreeRecursive(file, childDirectoryNode);
                }
            }
        }
    }


    private void uncompress(String zipFile, String extractPath, String dbFilePath, String category, String strategy, List<String> list) {
        try (ZipFile zip = new ZipFile(zipFile, "UTF-8")) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String entryName = entry.getName();
                File entryFile = new File(extractPath, entryName);
                if (entry.isDirectory() && (entryFile.mkdirs())) {
                    logger.debug("entryFIle mkdir");
                    continue;
                }
                promiseParentDir(entryFile);
                FileUtils.copyInputStreamToFile(zip.getInputStream(entry), entryFile);
                if (ossEnable) {
                    uploadLocalToOss(dbFilePath + StringPool.SLASH + entryName, entryFile.toPath(), strategy);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + strategy + StringPool.SLASH + dbFilePath + StringPool.SLASH + entryName;
                    String ossPath = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + dbFilePath + StringPool.SLASH + entryName;
                    sysMediaService.insert(new SysMediaEntity(category, entryName, filePath, ossPath, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(strategy)));
                    list.add(filePath);
                } else {
                    promiseParentDir(entryFile);
                    FileUtils.copyInputStreamToFile(zip.getInputStream(entry), entryFile);
                    list.add(dbFilePath + StringPool.SLASH + entryFile);
                    sysMediaService.insert(new SysMediaEntity(category, entryName, dbFilePath + StringPool.SLASH + entryFile, extractPath + StringPool.SLASH + entryFile));
                }

                logger.debug("文件验证中=={}", entryName);
            }
            obsClientStrategy.getClientByCode(strategy).close();
        } catch (IOException e) {
            throw new InkelinkException("ServiceAbstract " + zipFile + " 文件解压失败 解压路径 " + extractPath, e);
        }
    }

    private void promiseParentDir(File entryFile) {
        File parentDir = entryFile.getParentFile();
        if (!parentDir.exists() && (parentDir.mkdirs())) {
            logger.debug("parentDir mkdir");
        }
    }

    @PostMapping("/downloadzip")
    @Operation(summary = "媒体中心-文件下载")
    public void downLoadZip(@RequestBody DownFileModelDto downFileModelDto, HttpServletResponse response) throws IOException {
        if (localUploadProperties.getDownCount() < downFileModelDto.getFilePaths().size()) {
            throw new InkelinkException("超过最大下载数量" + localUploadProperties.getDownCount());
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); ZipOutputStream zipStream = new ZipOutputStream(byteStream)) {

            String basePath = localUploadProperties.getRootDir();

            List<String> filePaths = getFilePaths(basePath, downFileModelDto.getFilePaths());

            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (file.exists()) {
                    zipStream.putNextEntry(new ZipEntry(file.getName()));
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            zipStream.write(buffer, 0, length);
                        }
                    }
                    zipStream.closeEntry();
                }
            }
            response.setHeader("DownLoadFileName", "DownLoad.zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"DownLoad.zip\"");

            zipStream.finish();
            byte[] zipBytes = byteStream.toByteArray();
            response.getOutputStream().write(zipBytes);
            if (ossEnable) {
                filePaths.forEach(filePath -> {
                    try {
                        Files.delete(Paths.get(filePath));
                    } catch (IOException e) {
                        logger.debug("删除临时文件失败");
                    }
                });
            }
        }
    }


    private List<String> getFilePaths(String rootPath, List<DownFileModelDto.PathInfo> paths) throws IOException {
        List<String> files = new ArrayList<>();

        for (DownFileModelDto.PathInfo pathInfo : paths) {
            String filePath;
            if (ossEnable) {
                ObsClient obsClient = obsClientStrategy.getClientByCode(pathInfo.getStrategy());
                ObsObject obsObject = obsClient.getObject(obsClientStrategy.getBucketNameByCode(pathInfo.getStrategy()), pathInfo.getFilePath());
                Path localFilePath = Paths.get(rootPath, "fs", pathInfo.getFilePath());
                Files.copy(obsObject.getObjectContent(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
                filePath = localFilePath.toString();
            } else {

                filePath = Paths.get(rootPath, (pathInfo.getFilePath().startsWith("/") ? pathInfo.getFilePath().substring(1) : pathInfo.getFilePath())).toString();
            }
            File file = new File(filePath);
            if (file.exists()) {
                files.add(filePath);
            }
        }

        return files;
    }

    private String getImageType(String prefix) {
        String[] parts = prefix.split(";");
        if (parts.length == 0) {
            throw new InkelinkException("未读取到图片类型");
        }
        return parts[0].toLowerCase();
    }

    private String getImageExt(String imgType) {
        Map<String, String> types = new HashMap<>(5);
        types.put("data:image/jpg", ".jpg");
        types.put("data:image/jpeg", ".jpeg");
        types.put("data:image/gif", ".gif");
        types.put("data:image/bmp", ".bmp");
        types.put("data:image/png", ".png");

        if (!types.containsKey(imgType)) {
            throw new InkelinkException("不支持的图片类型");
        }

        return types.get(imgType);
    }

    private String getFileNameExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return StringUtils.EMPTY;
    }
    @Operation(summary = "spc结果文件上传")
    @PostMapping("/postfiles/{category}")
    public ResultVO<String> postfiles(@PathVariable String category, @RequestParam("json") String json) {
        ResultVO<String> stringResultVO = null;
        String fileMd5 = EncryptionUtils.md5(json);

        try {
            // 将字符串转换为字节数组
            byte[] bytes = json.getBytes("UTF-8");
            // 创建一个ByteArrayInputStream来读取字节数组
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            // 不要忘记关闭输入流
            byteArrayInputStream.close();
            stringResultVO = postFile(category, StringUtils.EMPTY, false, byteArrayInputStream, PUBLIC_LEVEL, fileMd5+".txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringResultVO;
    }


    private   ResultVO<String> postFile(String category, String customerpath, Boolean cover, InputStream inputStream, String strategy,String oriFileName) {

        category = (category == null || category.trim().isEmpty()) ? "pub" : category;
        category = category.toLowerCase();

        try {

                UploadPathSet pathSet = getUploadPathSet(category, customerpath);
                promiseDir(pathSet.oneFilePath);

                String fileName = oriFileName;

                String newPath = pathSet.dbFilePath + StringPool.SLASH + fileName;

                String path = getOneFilePath(pathSet, fileName);

                Path getPath = Paths.get(path);
                Files.copy(inputStream, getPath, StandardCopyOption.REPLACE_EXISTING);
                if (ossEnable) {
                    uploadLocalToOss(newPath, getPath, strategy);
                    String filePath = StringPool.SLASH + "obs" + StringPool.SLASH + strategy + StringPool.SLASH + newPath;
                    String ossUrl = PROTOCOL + obsClientStrategy.getBucketNameByCode(strategy) + StringPool.DOT + obsClientStrategy.getEndpointByCode(strategy) + StringPool.SLASH + newPath;
                    sysMediaService.insert(new SysMediaEntity(category, oriFileName, filePath, ossUrl, PROVIDER_HUAWEI, obsClientStrategy.getBucketNameByCode(strategy)));
                } else {
                    sysMediaService.insert(new SysMediaEntity(category, fileName, newPath, path));
                }




            sysMediaService.saveChange();
            ResultVO<String> resultVO = new ResultVO<>();
            return resultVO.ok(newPath, "成功");
        } catch (Exception ex) {
            throw new InkelinkException(ex.getMessage());
        }
    }


}