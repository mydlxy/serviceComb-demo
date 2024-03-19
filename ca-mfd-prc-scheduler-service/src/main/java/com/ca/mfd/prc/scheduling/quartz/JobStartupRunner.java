package com.ca.mfd.prc.scheduling.quartz;

import com.ca.mfd.prc.scheduling.config.SchedulerConfig;
import com.ca.mfd.prc.scheduling.service.impl.ScheduleProvider;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Date;

//@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.scheduling.quartz.enable'))}")
@Component
public class JobStartupRunner implements CommandLineRunner {
   /* private static final String TRIGGER_GROUP_NAME = "test_trigger";
    private static final String JOB_GROUP_NAME = "test_job";*/
    private static final Logger logger = LoggerFactory.getLogger(JobStartupRunner.class);
    @Autowired
    SchedulerConfig schedulerConfig;
    @Autowired
    ScheduleProvider scheduleProvider;

    @Override
    public void run(String... args) throws Exception {
        Scheduler scheduler;
        try {
            scheduler = schedulerConfig.scheduler();

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            Date startTime = calendar.getTime();
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 10);
            Date endTime = calendar.getTime();

           /* ScheduleModel scheduleModel1 = new ScheduleModel();
            scheduleModel1.setJobName("jobname1");
            scheduleModel1.setJobGroup("jobgroup1");
            scheduleModel1.setAppId("appid1");
            scheduleModel1.setJobType(JobTypeEnum.URL);
            scheduleModel1.setBeginTime(startTime);
            scheduleModel1.setEndTime(endTime);
            scheduleModel1.setCron("0/30 * * * * ?");
            scheduleModel1.setTriggerType(TriggerTypeEnum.CRON);
            scheduleModel1.setDescription("Description11");
            scheduleModel1.setRequestUrl("url1");
            scheduleModel1.setRequestParameters("Parameters111");
            scheduleModel1.setRequestHeader("RequestHeader111");
            scheduleModel1.setRequestType(RequestTypeEnum.GET);*/
            //    scheduleProvider.addScheduleJobAsync(scheduleModel1, null);
            /*ScheduleModel scheduleModel2 = new ScheduleModel();
            scheduleModel2.setJobName("jobname2");
            scheduleModel2.setJobGroup("jobgroup2");
            scheduleModel2.setAppId("appid2");
            scheduleModel2.setJobType(JobTypeEnum.URL);
            scheduleModel2.setBeginTime(startTime);
            scheduleModel2.setEndTime(endTime);
            scheduleModel2.setCron("0/10 * * * * ?");
            scheduleModel2.setTriggerType(TriggerTypeEnum.CRON);
            scheduleModel2.setDescription("Description22");
            scheduleModel2.setRequestUrl("url2");
            scheduleModel2.setRequestParameters("Parameters222");
            scheduleModel2.setRequestHeader("RequestHeader222");
            scheduleModel2.setRequestType(RequestTypeEnum.POST);*/
            //  scheduleProvider.addScheduleJobAsync(scheduleModel2, null);

           /* TriggerKey triggerKey = TriggerKey.triggerKey("trigger1", TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                Class clazz = QuartzJob.class;
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity("job1", JOB_GROUP_NAME).usingJobData("name", "weiz QuartzJob").build();
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
                trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", TRIGGER_GROUP_NAME)
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                logger.info("Quartz 创建了job:...:" + jobDetail.getKey());
            } else {
                logger.info("job已存在:{}" + trigger.getKey());
            }

            TriggerKey triggerKey2 = TriggerKey.triggerKey("trigger2", TRIGGER_GROUP_NAME);
            CronTrigger trigger2 = (CronTrigger) scheduler.getTrigger(triggerKey2);
            if (null == trigger2) {
                Class clazz = QuartzJob.class;
                JobDetail jobDetail2 = JobBuilder.newJob(clazz).withIdentity("job2", JOB_GROUP_NAME).usingJobData("name", "weiz QuartzJob2").build();
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
                trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2", TRIGGER_GROUP_NAME)
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail2, trigger2);
                logger.info("Quartz 创建了job:...:{}" + jobDetail2.getKey());
            } else {
                logger.info("job已存在:{}" + trigger2.getKey());
            }*/
            scheduler.start();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

}
