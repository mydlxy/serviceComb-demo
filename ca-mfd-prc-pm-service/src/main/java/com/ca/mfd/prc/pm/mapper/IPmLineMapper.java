package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.dto.LineVO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.excel.PmAllModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 生产线
 * @date 2023-08-29
 */
@Mapper
public interface IPmLineMapper extends IBaseMapper<PmLineEntity> {

    /**
     * 获取一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回列表
     */
    List<PmLineEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") Integer version);

    /**
     * 通过线体编码集合获得线体对象集合
     *
     * @param lineCodeList
     * @return
     */
    List<LineVO> getByLineCodeList(@Param("lineCodeList") List<String> lineCodeList);

    List<PmAllModel> getAllExcelDatas(@Param("shopId") String shopId);
}