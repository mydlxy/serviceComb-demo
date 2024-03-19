package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.DeleteScrewPictureConfigPara;
import com.ca.mfd.prc.pm.entity.PmScrConfigEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.mapper.IPmScrConfigMapper;
import com.ca.mfd.prc.pm.entity.PmScrConfigEntity;
import com.ca.mfd.prc.pm.service.IPmScrConfigService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @Description: 拧紧指示配置服务实现
 * @author inkelink
 * @date 2024年01月24日
 * @变更说明 BY inkelink At 2024年01月24日
 */
@Service
public class PmScrConfigServiceImpl extends AbstractCrudServiceImpl<IPmScrConfigMapper, PmScrConfigEntity> implements IPmScrConfigService {

    private final String cacheName = "PRC_PM_SCR_CONFIG";
    @Autowired
    private IPmWoService pmWoService;
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmScrConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmScrConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmScrConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmScrConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PmScrConfigEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PmScrConfigEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PmScrConfigEntity model, boolean isUpdate) {

       /* List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("LINE_CODE", model.getLineCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PmScrConfigEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("线体编码" + model.getLineCode() + "已经存在，不允许重复录入！");
        }*/
    }

    /**
     * 获取所有的数据
     *
     * @return List<PmScrConfigEntity>
     */
    @Override
    public List<PmScrConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PmScrConfigEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, 6000);
    }


    private PmWoEntity getWoByWoCode(String woId){
        Long lwoId = 0L;
        try{
            lwoId = Long.valueOf(woId);
        }catch (Exception e){
            throw new InkelinkException("工艺编号无效，须是数字类型");
        }
        QueryWrapper<PmWoEntity> qwWo = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lqwWo = qwWo.lambda();
        lqwWo.in(PmWoEntity :: getId,woId);
        lqwWo.in(PmWoEntity :: getVersion,0);
        return this.pmWoService.getData(qwWo,false).stream().findFirst().orElse(null);
    }

    /**
     * 获取图片配置
     * @param job
     * @param woId
     * @return
     */
    public PmScrConfigEntity getScrewPictureConfig(String job, String woId){
        PmWoEntity pmWoEntity = getWoByWoCode(woId);
        if(pmWoEntity == null){
            throw new InkelinkException("请选择有效的工艺编号");
        }
        PmScrConfigEntity pmScrConfigEntity = getScrewPictureConfigByJobAndWoCode(job,pmWoEntity.getWoCode());
        if(pmScrConfigEntity == null){
            return new PmScrConfigEntity();
        }
        return pmScrConfigEntity;
    }

    private PmScrConfigEntity getScrewPictureConfigByJobAndWoCode(String job, String woCode){
        QueryWrapper<PmScrConfigEntity> srcConfigWo = new QueryWrapper<>();
        LambdaQueryWrapper<PmScrConfigEntity> lSrcConfigWo = srcConfigWo.lambda();
        lSrcConfigWo.in(PmScrConfigEntity :: getJobNo,job);
        lSrcConfigWo.in(PmScrConfigEntity :: getPmWoCode,woCode);
        return this.getData(srcConfigWo,false).stream().findFirst().orElse(null);
    }


    /**
     * 修改拧紧图片配置
     * @param info
     */
    public void modifyScrewPictureConfig(PmScrConfigEntity info){
        if (StringUtils.isBlank(info.getJobNo()))
        {
            throw new InkelinkException("请填写有效的JOB号");
        }

        if (StringUtils.isBlank(info.getPmWoId()))
        {
            throw new InkelinkException("请选择有效的工艺编号");
        }
        PmWoEntity pmWoEntity = getWoByWoCode(info.getPmWoId());
        if(pmWoEntity == null){
            throw new InkelinkException("请选择有效的工艺编号");
        }
        info.setPmWoCode(pmWoEntity.getWoCode());
        PmScrConfigEntity pmScrConfigEntity = getScrewPictureConfigByJobAndWoCode(info.getJobNo(),pmWoEntity.getWoCode());
        if(pmScrConfigEntity != null){
            this.delete(pmScrConfigEntity.getId(),true);
        }
        this.insert(info);
        this.removeCache();
    }


    /**
     *  public class DeleteScrewPictureConfigPara
     *    {
     *        public string JobNo { get; set; } = string.Empty;
     *
     *        public long PmWoId { get; set; } = 0;
     *
     *    }
     */

    /*/// <summary>
    /// 获取图片配置
    /// </summary>
    /// <param name="job"></param>
    /// <param name="woCode"></param>
    public EpsScrewPictureInfo GetScrewPictureConfig(string job, Guid woId)
    {
        EpsScrewPictureInfo epsScrewPictureInfo = new EpsScrewPictureInfo();


        var woInfo = _pmWoBll.Table.Where(c => c.Id == woId && c.Version == 0).FirstOrDefault();

        if (woInfo == null)
        {
            throw new InfoException("请选择有效的工艺编号");
        }

        epsScrewPictureInfo = this.Table.Where(c => c.JobNo == job && c.PmWoCode == woInfo.Code).FirstOrDefault();

        if (epsScrewPictureInfo == null)
        {
            return new EpsScrewPictureInfo();
        }

        return epsScrewPictureInfo;
    }

    /// <summary>
    /// 修改拧紧图片配置
    /// </summary>
    /// <param name="info"></param>
    public void ModifyScrewPictureConfig(EpsScrewPictureInfo info)
    {
        if (string.IsNullOrEmpty(info.JobNo))
        {
            throw new InfoException("请填写有效的JOB号");
        }

        if (info.PmWoId == Guid.Empty)
        {
            throw new InfoException("请选择有效的工艺编号");
        }

        var woInfo = _pmWoBll.Table.Where(c => c.Id == info.PmWoId && c.Version == 0).FirstOrDefault();

        if (woInfo == null)
        {
            throw new InfoException("请选择有效的工艺编号");
        }

        info.PmWoCode = woInfo.Code;
        var data = this.Table.Where(c => c.JobNo == info.JobNo && c.PmWoCode == woInfo.Code).FirstOrDefault();

        if (data != null)
        {
            this.Delete(new List<Guid>() { data.Id });
        }

        this.Insert(info);

        RemoveCache();
    }

    /// <summary>
    /// 删除拧紧图片配置
    /// </summary>
    /// <param name="para"></param>
    /// <exception cref="InfoException"></exception>
    public void DeleteScrewPictureConfig(DeleteScrewPictureConfigPara para)
    {
        if (string.IsNullOrEmpty(para.JobNo))
        {
            return;
        }

        if (para.PmWoId==Guid.Empty)
        {
            return;
        }

        var woInfo = _pmWoBll.Table.Where(c => c.Id == para.PmWoId && c.Version == 0).FirstOrDefault();

        if (woInfo == null)
        {
            return;
        }

        var data = this.Table.Where(c => c.JobNo == para.JobNo && c.PmWoCode == woInfo.Code).FirstOrDefault();

        if (data != null)
        {
            this.Delete(new List<Guid>() { data.Id });
            RemoveCache();
        }
    }

    /// <summary>
    /// 根据拧紧工艺信息获取配置信息
    /// </summary>
    /// <param name="paras"></param>
    /// <returns></returns>
    public List<EpsScrewPictureInfo> GetWoScrewPicture(List<WoScrewItem> paras)
    {
        List<EpsScrewPictureInfo> infos = new List<EpsScrewPictureInfo>();

        foreach (var item in paras)
        {
            var result = GetAllDatas().Where(c => c.JobNo == item.JobNo && c.PmWoCode == item.WoCode).FirstOrDefault();

            if (result != null)
            {
                infos.Add(result);
            }
            else
            {
                if (!string.IsNullOrEmpty(item.JobNo))
                {
                    infos.Add(new EpsScrewPictureInfo()
                    {
                        JobNo = item.JobNo,
                        PmWoCode = item.WoCode
                    });
                }
            }

        }

        return infos;
    }*/


}