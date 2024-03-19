package com.ca.mfd.prc.pps.communication.controller;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.config.RestTemplateConfig;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanService;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ognl.IntHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: AS批次计划Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/asbathplan")
@Tag(name = "AS批次计划服务", description = "AS批次计划")
public class MidAsBathPlanController  extends BaseController<MidAsBathPlanEntity> {
    final Logger logger = LoggerFactory.getLogger(MidAsBathPlanController.class);

    private final IMidAsBathPlanService midAsBathPlanService;

    @Autowired
    public MidAsBathPlanController(IMidAsBathPlanService midAsBathPlanService) {
        this.crudService = midAsBathPlanService;
        this.midAsBathPlanService = midAsBathPlanService;
    }

    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @PostMapping(value = "receive")
    @Operation(summary = "保存")
    public ResultVO<String> receive(@Valid @RequestBody List<MidAsBathPlanEntity> models
    ) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS批次计划[" + reqNo + "]开始接收数据:" + (models == null ? 0 : models.size()));
        //校验
        if (models == null || models.size() == 0) {
            throw new InkelinkException("AS批次计划接收数据不能为空");
        }
        logger.info("AS批次计划[" + reqNo + "]接收校验通过:");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_BATH_PLAN);
        loginfo.setDataLineNo(models.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            for (MidAsBathPlanEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(loginfo.getId());
            }
            midAsBathPlanService.insertBatch(models,200,false,1);
            midAsBathPlanService.saveChange();
            status = 1;
            errMsg = "AS批次计划[" + reqNo + "]接收保存成功";
            logger.info(errMsg);
        } catch (Exception ex) {
            status = 5;
            errMsg = "AS批次计划[" + reqNo + "]接收失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midAsBathPlanService.saveChange();

        logger.info("AS批次计划[" + reqNo + "]接收执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "接收成功");
        } else {
            return new ResultVO<String>().error(-1, "接收失败:" + errMsg);
        }

    }


    @GetMapping(value = "excute")
    @Operation(summary = "执行数据处理逻辑")
    public ResultVO<String> excute(String logid) {
        String msg = midAsBathPlanService.excute(logid);
        if (StringUtils.isBlank(msg)) {
            return new ResultVO<String>().ok("", "成功");
        } else {
            return new ResultVO<String>().error(-1, msg);
        }
    }

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @PostMapping(value = "test")
    @Operation(summary = "测试")
    public ResultVO<String> test() throws Exception {

//        String jsonstr = "{\"pageIndex\":1,\"pageSize\":20,\"conditions\":[{\"columnName\":\"orderCategory\",\"value\":\"3\",\"operator\":1}],\"sorts\":[{\"columnName\":\"lastUpdateDate\",\"direction\":2}]}";
//        PageDataDto model = JsonUtils.parseObject(jsonstr, PageDataDto.class);
//        HashMap<String,Object> urlData = new LinkedHashMap<>();
//        urlData.put("category","PlanPartsStatus");
//        HashMap<String,String> headers = new LinkedHashMap<>();
//        headers.put("Authorization", "Bearer MTcyMjQ2MTM1MjI2NTA1NjI1Nys0");
//        String rev = HttpsHelper.getRequest("http://10.23.1.152:8079/main/sysconfiguration/getcategorylist", HttpMethod.GET
//        ,urlData,null,headers);
//        //RestTemplateConfig restTemplateConfig = AppContextUtil.getBean(RestTemplateConfig.class);
//        RestTemplateUtil restTemplateUtil = new RestTemplateUtil(restTemplateConfig.newRestTemplate());
//        //http://10.23.1.152:8888/equality.mes.avi/v1.0/scheduling/entryreportpassavijob/3
//        //http://10.23.1.152:8079/equality.mes.avi/v1.0/scheduling/entryreportpassavijob/3
//        cn.hutool.json.JSONObject jsonData = restTemplateUtil.getJson("http://10.23.1.152:8079/main/sysconfiguration/getcategorylist?category=PlanPartsStatus", headers);
//
//        MidAsBathPlanEntity et = new MidAsBathPlanEntity();
//        et.setOrgCode("test111");
        PmVersionProvider pmVersionProvider = SpringContextUtils.getBean(PmVersionProvider.class);
        Integer scnt = pmVersionProvider.getObjectedPm().getAvis().size();
        return new ResultVO<String>().ok(scnt.toString(), "成功");
    }

    @GetMapping(value = "apiget")
    @Operation(summary = "测试")
    public Object apiget(String path, String json, String head) {
        /********************** ApiPtform调用示例 **************************/
        String ars = apiPtService.getapi(path, JSONObject.parseObject(json), JSONObject.parseObject(head, Map.class));
        logger.warn("API平台测试url调用：" + ars);
        //ResultVO<String> rep = JSON.parseObject(ars,ResultVO.class );
        return JSONObject.parseObject(ars);
    }

    @GetMapping(value = "apipost")
    @Operation(summary = "测试")
    public Object apipost(String path, String json, String head) {
        /********************** ApiPtform调用示例 **************************/
        String ars = apiPtService.postapi(path, JSONObject.parseObject(json), JSONObject.parseObject(head, Map.class));
        logger.warn("API平台测试url调用：" + ars);
        //ResultVO<String> rep = JSON.parseObject(ars,ResultVO.class );
        return JSONObject.parseObject(ars);
    }

//    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Operation(summary = "获取分页数据")
//    public ResultVO<PageData<MidAsBathPlanEntity>> getPageData(@RequestBody PageDataDto model) {
//        PageData<MidAsBathPlanEntity> page = midAsBathPlanService.page(model);
//        return new ResultVO<PageData<MidAsBathPlanEntity>>().ok(page, "获取数据成功");
//    }
}