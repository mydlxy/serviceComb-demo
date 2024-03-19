package com.ca.mfd.prc.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * 高效分布式ID生成算法
 *
 * @author eric.zhou
 * @date 2023/8/24
 */
public class IdGenerator {
    private static final Sequence WORKER = new Sequence(null);

    /**
     * 获取id
     *
     * @return
     */
    public static long getId() {
        return WORKER.nextId();
    }

    /**
     * 判断id是否为空
     *
     * @return
     */
    public static boolean isEmpty(Long id) {
        if (id == null || id == 0) {
            return true;
        }
        return false;
    }

}
