package com.ca.mfd.prc.common.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.IUnitOfWorkService;
import com.ca.mfd.prc.common.service.SqlContent;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * IUnitOfWorkService
 *
 * @author inkelink
 */
@Service
public class UnitOfWorkServiceImpl implements IUnitOfWorkService {

    private static final Logger logger = LoggerFactory.getLogger(UnitOfWorkServiceImpl.class);
    private static final Log log = org.apache.ibatis.logging.LogFactory.getLog(UnitOfWorkServiceImpl.class);
    private final ThreadLocal<List<SqlContent>> sqlContentList = new ThreadLocal<List<SqlContent>>();
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    LocalCache localCache;

    @Override
    public Boolean addContent(String statement, Object parameter) {
        return this.addContent(statement, parameter, 0);
    }

    @Override
    public Boolean addContent(String statement, Object parameter, int batchSize) {
        return this.getSqlContentList().add(new SqlContent(statement, parameter, batchSize));
    }

    @Override
    public Boolean addContent(String statement, Object parameter, int batchSize, Integer batchType) {
        return this.getSqlContentList().add(new SqlContent(statement, parameter, batchSize,batchType));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveChange() {
        try {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            for (SqlContent sqlContent : this.getSqlContentList()) {
                if (sqlContent.getBatchSize() > 0) {
                    List<Object> list = (List<Object>) sqlContent.getParameter();
                    if (list.size() > 1 && Objects.equals(sqlContent.getBatchType(), 1)) {
                        SqlHelper.executeBatch(sqlSessionFactory, log, list, sqlContent.getBatchSize(), (sqlSessionBt, entity) -> {
                            sqlSessionBt.update(sqlContent.getStatement(), entity);
                        });
                    } else {
                        for (Object parameter : list) {
                            sqlSession.update(sqlContent.getStatement(), parameter);
                        }
                    }
                } else {
                    sqlSession.update(sqlContent.getStatement(), sqlContent.getParameter());
                }
            }
            //处理缓存
            localCache.setRemoteTime();
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.clearContent();
        }
    }

    private List<SqlContent> getSqlContentList() {
        if (sqlContentList.get() == null) {
            sqlContentList.set(new ArrayList<>());
        }
        return sqlContentList.get();
    }

    private void clearContent() {
        sqlContentList.remove();
    }

    @Override
    public void clearChange() {
        clearContent();
    }

//    @Override
//    public List<String> addTest() {
//        for (int i = 0; i < 10000; i++) {
//            String text = String.valueOf(RandomUtils.nextInt(0, 100));
//            this.getContentList().add(text);
//        }
//
//        List<String> result = this.getContentList().stream().collect(Collectors.toList());
////        logger.info(String.join(",", result));
//        return result;
//    }
//
//    private List<String> getContentList()
//    {
//        if( contentList.get() == null)
//        {
//            contentList.set(new ArrayList<>());
//        }
//        return  contentList.get();
//    }
//
//    private void clearContent()
//    {
//        contentList.remove();
//    }

}
