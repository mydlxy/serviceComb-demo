package com.ca.mfd.prc.scheduling.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.ConditionsDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.scheduling.config.SchedulerConfig;
import com.ca.mfd.prc.scheduling.entity.JobInfoModel;
import com.ca.mfd.prc.scheduling.entity.JobKeyDTO;
import com.ca.mfd.prc.scheduling.entity.LogModel;
import com.ca.mfd.prc.scheduling.entity.ScheduleModel;
import com.ca.mfd.prc.scheduling.entity.TriggerStateModel;
import com.ca.mfd.prc.scheduling.entity.TriggerTypeEnum;
import com.ca.mfd.prc.scheduling.entity.UpdateJobModel;
import com.ca.mfd.prc.scheduling.quartz.QuartzJob;
import com.ca.mfd.prc.scheduling.service.IScheduleProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口推送日志
 *
 * @author jay.he
 * @date 2023-09-05
 */
@RestController
@RequestMapping("scheduling")
@Tag(name = "接口推送日志")
public class SchedulerController extends BaseApiController {  //BaseController

    @Lazy
    @Autowired
    IScheduleProvider scheduleProvider;
    @Lazy
    @Autowired
    SchedulerConfig schedulerConfig;


    /**
     * 获取数据条数
     *
     * @param model
     * @return
     */
    @PostMapping(value = "getcount", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取数据条数")
    public ResultVO<Integer> getCount(@RequestBody ConditionsDto model) throws Exception {
        List<JobInfoModel> jobInfoModels = scheduleProvider.getAllJobAsync(schedulerConfig.scheduler());
        List<ConditionDto> conditionDtoList = model.getConditions();
        filterJobInfoModel(jobInfoModels, conditionDtoList);

        Integer counts = jobInfoModels.size();
        return new ResultVO<Integer>().ok(counts, "获取数据成功");
    }

    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<JobInfoModel>> getPageData(@RequestBody PageDataDto model) throws Exception {

        Integer pageIndex = model.getPageIndex() <= 0 ? 1 : model.getPageIndex();
        Integer pageSize = model.getPageSize() <= 0 ? 20 : model.getPageSize();
        // List<SortDto> sortDtoList = model.getSorts();
        List<ConditionDto> conditionDtoList = model.getConditions();

        List<JobInfoModel> jobInfoModels = scheduleProvider.getAllJobAsync(schedulerConfig.scheduler());
        jobInfoModels = jobInfoModels.stream().sorted(Comparator.comparing(JobInfoModel::getAppId)).collect(Collectors.toList());
        filterJobInfoModel(jobInfoModels, conditionDtoList);

        List<JobInfoModel> subList = jobInfoModels.stream().skip((long) (pageIndex - 1) * pageSize).limit(pageSize).
                collect(Collectors.toList());

        for (int i = 1; i <= subList.size(); i++) {
            subList.get(i - 1).setId((long) i);
        }
        PageData<JobInfoModel> pageData = new PageData<>();
        pageData.setDatas(subList);
        pageData.setTotal(jobInfoModels.size());
        pageData.setPageSize(pageSize);
        pageData.setPageIndex(pageIndex);

        return new ResultVO<PageData<JobInfoModel>>().ok(pageData, "获取数据成功");
    }

    /**
     * 过滤任务集合
     *
     * @param jobInfoModels
     * @param conditionDtoList
     * @return
     */
    private List<JobInfoModel> filterJobInfoModel(List<JobInfoModel> jobInfoModels, List<ConditionDto> conditionDtoList) {
        if (CollectionUtils.isEmpty(conditionDtoList)) {
            return jobInfoModels;
        }
        List<JobInfoModel> result = new ArrayList<>();
        //nextFireTime >=
        // triggerState = 任务状态  int  NORMAL不传，PAUSED--1 COMPLETE---2  ERROR--3  BLOCKED--4 NONE--5(沟通一下传字符算了，按照枚举对应起来)
        //groupName/name/description/interval/triggerAddress  全模糊，interval就是cron（直接将interval改为cron）
        for (ConditionDto eachConditionDto : conditionDtoList) {
            try {
                filterJobInfoModelEachHandle(eachConditionDto, jobInfoModels, result);
            } catch (ParseException e) {
                //continue;
            }
            /*if ("nextFireTime".equals(eachConditionDto.getColumnName())) {
                Date date;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = dateFormat.parse(eachConditionDto.getValue());
                } catch (ParseException e) {
                    continue;
                }
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getNextFireTime().compareTo(date) >= 0) {
                        //任务的时间在传入时间之后
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("triggerState".equals(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getTriggerState().name().equalsIgnoreCase(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("groupName".equals(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getGroupName().contains(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("name".equals(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getName().contains(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("description".equals(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getDescription().contains(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("cron".equals(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getCron().contains(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            } else if ("triggerAddress".contains(eachConditionDto.getColumnName())) {
                for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                    if (eachJobInfoModel.getTriggerAddress().equals(eachConditionDto.getValue())) {
                        result.add(eachJobInfoModel);
                    }
                }
                jobInfoModels.clear();
                jobInfoModels.addAll(result);
                result.clear();
            }*/
        }

        return jobInfoModels;
    }

    private void filterJobInfoModelEachHandle(ConditionDto eachConditionDto, List<JobInfoModel> jobInfoModels, List<JobInfoModel> result) throws ParseException {
        if ("nextFireTime".equals(eachConditionDto.getColumnName())) {
            Date date;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //try {
            date = dateFormat.parse(eachConditionDto.getValue());
           /* } catch (ParseException e) {
                continue;
            }*/
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getNextFireTime().compareTo(date) >= 0) {
                    //任务的时间在传入时间之后
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("triggerState".equals(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getTriggerState().name().equalsIgnoreCase(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("groupName".equals(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getGroupName().contains(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("name".equals(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getName().contains(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("description".equals(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getDescription().contains(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("cron".equals(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getCron().contains(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        } else if ("triggerAddress".contains(eachConditionDto.getColumnName())) {
            for (JobInfoModel eachJobInfoModel : jobInfoModels) {
                if (eachJobInfoModel.getTriggerAddress().equals(eachConditionDto.getValue())) {
                    result.add(eachJobInfoModel);
                }
            }
            jobInfoModels.clear();
            jobInfoModels.addAll(result);
            result.clear();
        }
    }

    /**
     * 查询任务详情
     *
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("getdetailjobasync")
    @Operation(summary = "查询任务详情")
    public ResultVO<JobInfoModel> getDetailJobAsync(String group, String name) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("查询成功！");

        ScheduleModel model = scheduleProvider.queryJobAsync(schedulerConfig.scheduler(), group, name);
        if (model != null && model.getTriggerType().equals(TriggerTypeEnum.SIMPLE)) {
            //如果是简单触发器，那么将间隔时间赋值到Cron给前端显示，相当于前端传值和接收值都只管Cron字段
            if (model.getIntervalSecond() != null) {
                model.setCron(String.valueOf(model.getIntervalSecond()));
            }
        }

        // result.setData(model);

        return result.ok(model);
    }

    /**
     * 查询状态
     *
     * @param group
     * @param name
     * @return
     * @throws Exception
     */
    @GetMapping("gettriggerstatus")
    @Operation(summary = "查询状态")
    public ResultVO<JobInfoModel> getTriggerStatus(String group, String name) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("查询状态成功！");

        TriggerStateModel model = scheduleProvider.getTriggerStatus(schedulerConfig.scheduler(), new JobKey(name, group));

        //  result.setData(model);

        return result.ok(model);
    }

    /**
     * 查询日志
     *
     * @param jobKey
     * @return
     * @throws Exception
     */
    @PostMapping(value = "getjoblogs", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "查询日志")
    public ResultVO<JobInfoModel> getJobLogs(@RequestBody JobKeyDTO jobKey) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("查询日志成功！");
        List<LogModel> logs = scheduleProvider.getJobLogsAsync(schedulerConfig.scheduler(), jobKey);
        //  result.setData(logs);

        return result.ok(logs);
    }

    /**
     * 添加一个job
     *
     * @param model
     * @return
     */
    @PostMapping(value = "addschedulejobasync", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "添加一个job")
    public ResultVO addScheduleJobAsync(@RequestBody ScheduleModel model) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("添加任务成功！");
        try {
            //如果是CRON触发器，Integer.parseInt就会报错，就转入到异常处理构造CRON触发器相关参数；如果是简单触发器，CRON会存间隔时间
            //简单触发器将Cron参数设置为空字符串
            //CRON触发器将IntervalSecond设置为空
            model.setIntervalSecond(Integer.parseInt(model.getCron()));
            model.setCron("");
            model.setTriggerType(TriggerTypeEnum.SIMPLE);
        } catch (Exception ex) {
            if (!CronExpression.isValidExpression(model.getCron())) {
                throw new InkelinkException("Cron表达式不正确...");
            }
            model.setIntervalSecond(null);
            model.setTriggerType(TriggerTypeEnum.CRON);
        }

        scheduleProvider.addScheduleJobAsync(schedulerConfig.scheduler(), QuartzJob.class, model, 0l);
        //说明：.NET版本后端代码支持简单触发器，前端cron传数字的话，就认为是简单触发器（转int成功就表示传了数字），长安项目只支持cron，不管简单的了
        scheduleProvider.pauseOrDelScheduleJobAsync(schedulerConfig.scheduler(), model.getJobGroup(), model.getJobName(), false);

        return result.ok("");
    }

    @PostMapping(value = "updatejob", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新一个job")
    public ResultVO updateJob(@RequestBody ScheduleModel model) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("更新任务成功！");

        ScheduleModel oldModel = scheduleProvider.queryJobAsync(schedulerConfig.scheduler(), model.getJobGroup(), model.getJobName());

        UpdateJobModel updateJobModel = new UpdateJobModel();
        updateJobModel.setOldModel(oldModel);
        updateJobModel.setNewModel(model);
        if (updateJobModel.getNewModel().getTriggerType().equals(TriggerTypeEnum.CRON)
                && updateJobModel.getNewModel().getCron().equals("* * * * * ?")) {
            return result.error(403, "当前环境不允许过频繁执行任务！");
        }
        try {
            //如果是CRON触发器，Integer.parseInt就会报错，就转入到异常处理构造CRON触发器相关参数；如果是简单触发器，CRON会存间隔时间
            //简单触发器将Cron参数设置为空字符串
            //CRON触发器将IntervalSecond设置为空
            updateJobModel.getNewModel().setIntervalSecond(Integer.parseInt(updateJobModel.getNewModel().getCron()));
            updateJobModel.getNewModel().setCron("");
            updateJobModel.getNewModel().setTriggerType(TriggerTypeEnum.SIMPLE);
        } catch (Exception ex) {
            if (!CronExpression.isValidExpression(updateJobModel.getNewModel().getCron())) {
                throw new InkelinkException("Cron表达式不正确...");
            }
            updateJobModel.getNewModel().setIntervalSecond(null);
            updateJobModel.getNewModel().setTriggerType(TriggerTypeEnum.CRON);
        }
        scheduleProvider.updateJob(schedulerConfig.scheduler(), updateJobModel);

        return result.ok("");
    }

    /**
     * 执行
     *
     * @param jobKey
     * @return
     * @throws Exception
     */
    @PostMapping(value = "triggerexecuteasync", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "执行")
    public ResultVO triggerExecuteAsync(@RequestBody JobKeyDTO jobKey) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("执行任务成功！");
        scheduleProvider.resumeJobAsync(schedulerConfig.scheduler(), jobKey.getGroup(), jobKey.getName());

        return result.ok("");
    }

    /**
     * 暂停
     *
     * @param jobKey
     * @return
     * @throws Exception
     */
    @PostMapping(value = "pausejobasync", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "暂停")
    public ResultVO pauseJobAsync(@RequestBody JobKeyDTO jobKey) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("暂停任务成功！");
        scheduleProvider.pauseOrDelScheduleJobAsync(schedulerConfig.scheduler(), jobKey.getGroup(), jobKey.getName(), false);

        return result.ok("");
    }

    @PostMapping(value = "removeasync", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "删除")
    public ResultVO removeAsync(@RequestBody JobKeyDTO jobKey) throws Exception {
        ResultVO result = new ResultVO<>();
        result.setMessage("删除任务成功！");
        scheduleProvider.pauseOrDelScheduleJobAsync(schedulerConfig.scheduler(), jobKey.getGroup(), jobKey.getName(), true);

        return result.ok("");
    }


}