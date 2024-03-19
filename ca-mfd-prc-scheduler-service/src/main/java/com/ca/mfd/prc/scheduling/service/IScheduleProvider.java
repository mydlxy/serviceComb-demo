package com.ca.mfd.prc.scheduling.service;

import com.ca.mfd.prc.scheduling.entity.JobInfoModel;
import com.ca.mfd.prc.scheduling.entity.JobKeyDTO;
import com.ca.mfd.prc.scheduling.entity.LogModel;
import com.ca.mfd.prc.scheduling.entity.ScheduleModel;
import com.ca.mfd.prc.scheduling.entity.TriggerStateModel;
import com.ca.mfd.prc.scheduling.entity.UpdateJobModel;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;

import java.util.List;

public interface IScheduleProvider {

    /**
     * 获取任务状态
     *
     * @param jobKey
     * @return
     */
    TriggerStateModel getTriggerStatus(Scheduler scheduler, JobKey jobKey) throws Exception;

    /**
     * 开启调度器
     */
    void startAsync(Scheduler scheduler) throws Exception;

    /**
     * 添加任务
     *
     * @param entity
     * @param runNumber
     */
    void addScheduleJobAsync(Scheduler scheduler, Class<? extends Job> jobClass, ScheduleModel entity, Long runNumber) throws Exception;
    /*void AddScheduleJobAsync<TJob>(ScheduleModel entity, long runNumber)
    where TJob : IJob;*/

    /**
     * 修改任务
     *
     * @param request
     */
    void updateJob(Scheduler scheduler, UpdateJobModel request) throws Exception;
    //  Task UpdateJob<TJob>(UpdateJobModel request) where TJob : IJob;


    /**
     * 暂停/删除 指定的计划
     *
     * @param jobGroup 任务分组
     * @param jobName  任务名称
     * @param isDelete 停止并删除任务
     */
    void pauseOrDelScheduleJobAsync(Scheduler scheduler, String jobGroup, String jobName, boolean isDelete) throws Exception;
    //   Task StopOrDelScheduleJobAsync(string jobGroup, string jobName, bool isDelete = false);

    /**
     * 恢复运行暂停的任务
     *
     * @param jobGroup 任务分组
     * @param jobName  任务名称
     */
    void resumeJobAsync(Scheduler scheduler, String jobGroup, String jobName) throws Exception;

    /// <summary>
/// 查询任务
/// </summary>
/// <param name="jobGroup"></param>
/// <param name="jobName"></param>
/// <returns></returns>

    /**
     * 查询任务
     *
     * @param jobGroup 任务分组
     * @param jobName  任务名称
     */
    ScheduleModel queryJobAsync(Scheduler scheduler, String jobGroup, String jobName) throws Exception;
    //  Task<ScheduleModel> QueryJobAsync(string jobGroup, string jobName);

    /**
     * 立即执行
     *
     * @param jobKey
     */
    void triggerJobAsync(Scheduler scheduler, JobKey jobKey) throws Exception;


    /**
     * 获取job日志
     *
     * @param jobKey
     * @return
     */
    List<LogModel> getJobLogsAsync(Scheduler scheduler, JobKeyDTO jobKey) throws Exception;
    //  Task<List<string>> GetJobLogsAsync(JobKey jobKey);

    /**
     * 获取运行次数
     *
     * @param jobKey
     * @return
     */
    long getRunNumberAsync(Scheduler scheduler, JobKey jobKey) throws Exception;

    /**
     * 获取所有的job
     *
     * @return
     */
    List<JobInfoModel> getAllJobAsync(Scheduler scheduler) throws Exception;


    /**
     * 暂停任务调度
     */
    void stopScheduleAsync(Scheduler scheduler) throws Exception;
}
