package com.ca.mfd.prc.common.utils.yidiantong;

import java.util.HashMap;

public class BizContent extends HashMap<String, Object> {

    public BizContent add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
