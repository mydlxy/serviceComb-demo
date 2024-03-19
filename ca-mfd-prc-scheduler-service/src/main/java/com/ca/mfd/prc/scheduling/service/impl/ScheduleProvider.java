package com.ca.mfd.prc.scheduling.service.impl;

import com.alibaba.fastjson.JSON;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.scheduling.entity.JobInfoModel;
import com.ca.mfd.prc.scheduling.entity.JobKeyDTO;
import com.ca.mfd.prc.scheduling.entity.JobTypeEnum;
import com.ca.mfd.prc.scheduling.entity.LogModel;
import com.ca.mfd.prc.scheduling.entity.MailMessageEnum;
import com.ca.mfd.prc.scheduling.entity.QuartzConstant;
import com.ca.mfd.prc.scheduling.entity.RequestTypeEnum;
import com.ca.mfd.prc.scheduling.entity.ScheduleModel;
import com.ca.mfd.prc.scheduling.entity.TriggerStateModel;
import com.ca.mfd.prc.scheduling.entity.TriggerTypeEnum;
import com.ca.mfd.prc.scheduling.entity.UpdateJobModel;
import com.ca.mfd.prc.scheduling.quartz.QuartzJob;
import com.ca.mfd.prc.scheduling.service.IScheduleProvider;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.scheduling.quartz.enable'))}")
@Component
public class ScheduleProvider implements IScheduleProvider {
    /* @Autowired
     SchedulerConfig schedulerConfig;*/
    // private static String TRIGGER_GROUP_NAME = "test_trigger";
    //  private static String JOB_GROUP_NAME = "test_job";
    private static final Logger log = LoggerFactory.getLogger(ScheduleProvider.class);

    /**
     * 获得触发器状态
     *
     * @param jobKey
     * @return
     * @throws Exception
     */
    @Override
    public TriggerStateModel getTriggerStatus(Scheduler scheduler, JobKey jobKey) throws Exception {
        // List<Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        //  Scheduler scheduler = schedulerConfig.scheduler();
        TriggerStateModel triggerStateModel = new TriggerStateModel();
        triggerStateModel.setNextFireTime("");
        triggerStateModel.setTriggerState(Trigger.TriggerState.NONE);
        if (!CollectionUtils.isEmpty(scheduler.getTriggersOfJob(jobKey))) {
            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            triggerStateModel.setNextFireTime(dateFormat.format(trigger.getNextFireTime()));
            triggerStateModel.setTriggerState(scheduler.getTriggerState(new TriggerKey(jobKey.getName(), jobKey.getGroup())));//Trigger的name和group和Job的name和group保持一致
        }
        return triggerStateModel;
    }

    /**
     * 启动调度器
     *
     * @throws Exception
     */
    @Override
    public void startAsync(Scheduler scheduler) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        if (scheduler.isInStandbyMode()) {
            //是否是待机模式
            scheduler.start();
        } else {
            //已开启任务调度...
        }
    }

    /**
     * 向调度器增加一个Job
     *
     * @param scheduleModel
     * @param runNumber
     */
    @Override
    public void addScheduleJobAsync(Scheduler scheduler, Class<? extends Job> jobClass, ScheduleModel scheduleModel, Long runNumber) throws Exception {
        //检查任务是否已存在
        // Scheduler scheduler = schedulerConfig.scheduler();
        JobKey jobKey = new JobKey(scheduleModel.getJobName(), scheduleModel.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            throw new InkelinkException("任务" + scheduleModel.getJobGroup() + "." + scheduleModel.getJobName() + "已经存在！");
        }

        //http请求配置
        HashMap hashMap = new HashMap();
        if (scheduleModel.getJobType() != null) {
            hashMap.put(QuartzConstant.JobTypeEnum, scheduleModel.getJobType().value());
        }
        if (scheduleModel.getMailMessage() != null) {
            hashMap.put(QuartzConstant.MAILMESSAGE, scheduleModel.getMailMessage().value());
        }
        if (scheduleModel.getDataHandler() != null) {
            hashMap.put(QuartzConstant.DATAHANDLER, scheduleModel.getDataHandler());
        }
        if (runNumber != null && runNumber > 0) {
            hashMap.put(QuartzConstant.RUNNUMBER, runNumber);
        }
        //  IJobConfigurator jobConfigurator = null;
        if (scheduleModel.getJobType() != null && scheduleModel.getJobType().equals(JobTypeEnum.URL)) {
            //   jobConfigurator = JobBuilder.Create < TJob > ();
            hashMap.put(QuartzConstant.REQUESTURL, scheduleModel.getRequestUrl());
            hashMap.put(QuartzConstant.HEADERS, scheduleModel.getRequestHeader());
            hashMap.put(QuartzConstant.REQUESTPARAMETERS, scheduleModel.getRequestParameters());
            if (scheduleModel.getRequestType() != null) {
                hashMap.put(QuartzConstant.REQUESTTYPE, scheduleModel.getRequestType().value());
            }
            hashMap.put(QuartzConstant.AppId, scheduleModel.getAppId());
            hashMap.put(QuartzConstant.JobRequestStatus, "True");
        }
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(scheduleModel.getJobName(), scheduleModel.getJobGroup())
                .withDescription(scheduleModel.getDescription())
                .usingJobData(new JobDataMap(hashMap))
                .build();
        // 创建触发器
        // TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
        //校验是否正确的执行周期表达式
        Trigger trigger;
        //  long l = 0;
        if (scheduleModel.getTriggerType().equals(TriggerTypeEnum.CRON)) {
            if (!CronExpression.isValidExpression(scheduleModel.getCron())) {
                throw new InkelinkException("Cron表达式不正确...");
            }
            trigger = createCronTrigger(scheduleModel, jobKey);
        } else {
            //SimpleTrigger 包含几开始时间、结束时间、重复次数以及重复执行的时间间隔
            trigger = createSimpleTrigger(scheduleModel, jobKey);
            //  l = ((SimpleTrigger) trigger).getRepeatInterval();
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private CronTrigger createCronTrigger(ScheduleModel scheduleModel, JobKey jobKey) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleModel.getCron());
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .forJob(jobKey.getName(), jobKey.getGroup())
                .withSchedule(scheduleBuilder).build();

        return trigger;
    }

    private SimpleTrigger createSimpleTrigger(ScheduleModel scheduleModel, JobKey jobKey) {
        TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobKey.getGroup());
        if (scheduleModel.getBeginTime() != null) {
            triggerTriggerBuilder.startAt(scheduleModel.getBeginTime());//有开始时间
        } else {
            triggerTriggerBuilder.startNow();//立即执行
        }
        if (scheduleModel.getEndTime() != null) {
            triggerTriggerBuilder.endAt(scheduleModel.getEndTime());
        }
        //withMisfireHandlingInstructionNextWithRemainingCount 不追赶哑火，正常执行，由于是正常执行下次执行时间不变了(执行次数 = 预计执行次数 - 错过的次数)
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(scheduleModel.getIntervalSecond())//执行时间间隔 单位秒
                .withMisfireHandlingInstructionNextWithRemainingCount();
        if (scheduleModel.getRunTimes() > 0) {
            scheduleBuilder.withRepeatCount(scheduleModel.getRunTimes());//执行次数、默认从0开始

        } else {
            scheduleBuilder.repeatForever();//无限循环
        }
        SimpleTrigger trigger = triggerTriggerBuilder.withSchedule(scheduleBuilder)
                .forJob(jobKey.getName(), jobKey.getGroup()).build();
        return trigger;
    }

    @Override
    public void updateJob(Scheduler scheduler, UpdateJobModel request) throws Exception {
        JobKey jobKey = new JobKey(request.getOldModel().getJobName(), request.getOldModel().getJobGroup());
        long runNumber = getRunNumberAsync(scheduler, jobKey);
        pauseOrDelScheduleJobAsync(scheduler, request.getOldModel().getJobGroup(), request.getOldModel().getJobName(), true);
        addScheduleJobAsync(scheduler, QuartzJob.class, request.getNewModel(), runNumber);
    }

    @Override
    public void pauseOrDelScheduleJobAsync(Scheduler scheduler, String jobGroup, String jobName, boolean isDelete) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        scheduler.pauseJob(new JobKey(jobName, jobGroup));
        if (isDelete) {
            scheduler.deleteJob(new JobKey(jobName, jobGroup));
        }
    }

    /**
     * 恢复运行暂停的任务
     *
     * @param jobGroup 任务分组
     * @param jobName  任务名称
     */
    @Override
    public void resumeJobAsync(Scheduler scheduler, String jobGroup, String jobName) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new InkelinkException("任务不存在...");
        }
        scheduler.resumeJob(jobKey);
    }

    @Override
    public ScheduleModel queryJobAsync(Scheduler scheduler, String jobGroup, String jobName) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        ScheduleModel model = new ScheduleModel();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if (CollectionUtils.isEmpty(triggers)) {
            return null;
        }
        Trigger trigger = triggers.get(0);
        int intervalSeconds = 0;
        if (trigger instanceof SimpleTrigger) {
            intervalSeconds = (int) ((SimpleTrigger) trigger).getRepeatInterval() / 1000;
            model.setIntervalSecond(intervalSeconds);
            model.setTriggerType(TriggerTypeEnum.SIMPLE);
        } else {
            model.setTriggerType(TriggerTypeEnum.CRON);
        }
        if (trigger instanceof CronTrigger) {
            model.setCron(((CronTrigger) trigger).getCronExpression());
        }
        model.setJobName(jobName);
        model.setJobGroup(jobGroup);
        if (jobDetail != null) {
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.MAILMESSAGE)) {
                model.setMailMessage(MailMessageEnum.find(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.MAILMESSAGE))));
            }
            model.setDescription(jobDetail.getDescription());
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.AppId)) {
                model.setAppId(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.AppId)));
            }
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.JobTypeEnum)) {
                model.setJobType(JobTypeEnum.find(Integer.parseInt(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.JobTypeEnum)))));
            }
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.DATAHANDLER)) {
                model.setDataHandler(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.DATAHANDLER)));
            }
        }
        if (model.getJobType().equals(JobTypeEnum.URL)) {
            if (jobDetail != null) {
                if (jobDetail.getJobDataMap().containsKey(QuartzConstant.REQUESTURL)) {
                    model.setRequestUrl(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.REQUESTURL)));
                }
                if (jobDetail.getJobDataMap().containsKey(QuartzConstant.REQUESTTYPE)) {
                    model.setRequestType(RequestTypeEnum.find(Integer.parseInt(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.REQUESTTYPE)))));
                }
                if (jobDetail.getJobDataMap().containsKey(QuartzConstant.REQUESTPARAMETERS)) {
                    model.setRequestParameters(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.REQUESTPARAMETERS)));
                }
                if (jobDetail.getJobDataMap().containsKey(QuartzConstant.HEADERS)) {
                    model.setRequestHeader(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.HEADERS)));
                }
            }
        }
        return model;
    }

    @Override
    public void triggerJobAsync(Scheduler scheduler, JobKey jobKey) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        scheduler.triggerJob(jobKey);
    }

    @Override
    public List<LogModel> getJobLogsAsync(Scheduler scheduler, JobKeyDTO jobKey) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobKey.getName(), jobKey.getGroup()));
        if (jobDetail == null) {
            throw new InkelinkException("组名、任务名称不正确！");
        }
        // JSON.toJSONString()
        List<LogModel> logs;
        if (!jobDetail.getJobDataMap().containsKey(QuartzConstant.LOGS)) {
            logs = new ArrayList<>();
        } else {
            String log = jobDetail.getJobDataMap().get(QuartzConstant.LOGS).toString();
            log = JSON.parse(log).toString();
            logs = JSON.parseArray(log, LogModel.class);
        }
        return logs;
    }

    @Override
    public long getRunNumberAsync(Scheduler scheduler, JobKey jobKey) throws Exception {
        //  Scheduler scheduler = schedulerConfig.scheduler();
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        long runNumber = 0;
        if (jobDetail.getJobDataMap().containsKey(QuartzConstant.RUNNUMBER)) {
            runNumber = jobDetail.getJobDataMap().getLong(QuartzConstant.RUNNUMBER);
        }

        return runNumber;
    }

    @Override
    public List<JobInfoModel> getAllJobAsync(Scheduler scheduler) throws Exception {
        //如果是CRON触发器interval字段存cron值，如果是简单触发器interval存间隔时间（该对象没有cron，其实就是这个字段放到界面的cron处）
        // Scheduler scheduler = schedulerConfig.scheduler();
        List<JobKey> jobKeys = new ArrayList<>();
        List<JobInfoModel> jobs = new ArrayList<>();
        List<String> groupNames = scheduler.getJobGroupNames();
        for (String groupName : groupNames) {
            jobKeys.addAll(scheduler.getJobKeys(GroupMatcher.groupEquals(groupName)));
        }
        for (JobKey jobkey : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(jobkey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobkey);
            if (CollectionUtils.isEmpty(triggers)) {
                continue;
            }
            Trigger trigger = triggers.get(0);
            if (trigger == null) {
                continue;
            }
           /* String interval = null;//如果是CRON触发器就存cron，如果是简单触发器，就存执行间隔时长
            if (trigger instanceof CronTrigger) {
                interval = ((CronTrigger) trigger).getCronExpression();
            }
            if (interval == null) {
                interval = String.valueOf(((SimpleTrigger) trigger).getRepeatInterval());
            }*/
            //旧代码没有保存JobTypeEnum，所以None可以默认为Url。
            JobTypeEnum jobtype = JobTypeEnum.NONE;
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.JobTypeEnum)) {
                jobtype = JobTypeEnum.find(Integer.parseInt(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.JobTypeEnum))));
            }
            if (jobtype == null || jobtype.equals(JobTypeEnum.NONE)) {
                jobtype = JobTypeEnum.URL;
            }
            String triggerAddress = "";
            if (jobtype == JobTypeEnum.URL && jobDetail.getJobDataMap().containsKey(QuartzConstant.REQUESTURL)) {
                triggerAddress = String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.REQUESTURL));
            }
            JobInfoModel jobInfoModel = new JobInfoModel();
            jobInfoModel.setGroupName(jobkey.getGroup());
            jobInfoModel.setName(jobkey.getName());
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.EXCEPTION)) {
                jobInfoModel.setLastErrMsg(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.EXCEPTION)));
            }
            jobInfoModel.setTriggerAddress(triggerAddress);
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.DATAHANDLER)) {
                jobInfoModel.setDataHandler(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.DATAHANDLER)));
            }
            jobInfoModel.setTriggerState(scheduler.getTriggerState(trigger.getKey()));
            jobInfoModel.setPreviousFireTime(trigger.getPreviousFireTime());
            jobInfoModel.setNextFireTime(trigger.getNextFireTime());
            if (trigger instanceof CronTrigger) {
                jobInfoModel.setCron(((CronTrigger) trigger).getCronExpression());
            }
            if (trigger instanceof SimpleTrigger) {
                jobInfoModel.setCron(String.valueOf(((SimpleTrigger) trigger).getRepeatInterval() / 1000));
            }
            jobInfoModel.setDescription(jobDetail.getDescription());
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.REQUESTTYPE)) {
                jobInfoModel.setRequestType(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.REQUESTTYPE)));
            }
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.RUNNUMBER)) {
                jobInfoModel.setRunNumber(Long.parseLong(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.RUNNUMBER))));
            }
            jobInfoModel.setJobType(jobtype.value());
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.HEADERS)) {
                jobInfoModel.setRequestHeader(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.HEADERS)));
            }
            if (jobDetail.getJobDataMap().containsKey(QuartzConstant.AppId)) {
                jobInfoModel.setAppId(String.valueOf(jobDetail.getJobDataMap().get(QuartzConstant.AppId)));
            }
            jobs.add(jobInfoModel);

        }

        return jobs;
    }

    @Override
    public void stopScheduleAsync(Scheduler scheduler) throws Exception {
        // Scheduler scheduler = schedulerConfig.scheduler();
        if (scheduler.isInStandbyMode()) {
            scheduler.standby();
        }
        if (scheduler.isInStandbyMode()) {
            log.info("任务暂停成功...");
            // _logger.LogInformation("任务暂停成功...");
        }


    }
}
