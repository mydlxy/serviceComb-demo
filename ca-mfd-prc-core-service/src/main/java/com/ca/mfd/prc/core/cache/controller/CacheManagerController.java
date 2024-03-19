package com.ca.mfd.prc.core.cache.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("cache/manager")
@Tag(name = "缓存管理")
@Slf4j
public class CacheManagerController {
    @Autowired
    @Lazy
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("getpagedata")
    @Operation(summary = "获取缓存分页数据")
    public Map<String, Object> getPageData(@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "1") int pageNumber,@RequestParam(defaultValue = "*",required = false) String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (CollectionUtils.isEmpty(keys)) {
            return new HashMap<>();
        }
        List<String> keysList = new ArrayList<>(keys);
        int totalItems = keysList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);
        List<String> paginatedKeys = keysList.subList(startIndex, endIndex);
        Map<String, Object> cacheData = new HashMap<>();
        List<DataType> missType = new ArrayList<>();
        for (String paginatedKey : paginatedKeys) {
            boolean isMissType = false;
            DataType dataType = redisTemplate.type(paginatedKey);
            Object value = null;
            if (dataType != null) {
                switch (dataType) {
                    case STRING:
                        value = redisTemplate.opsForValue().get(paginatedKey);
                        break;
                    case LIST:
                        value = redisTemplate.opsForList().range(paginatedKey, 0, -1);
                        break;
                    case HASH:
                        value = redisTemplate.opsForHash().entries(paginatedKey);
                        break;
                    default:
                        isMissType = true;
                        missType.add(dataType);
                        break;
                }
                if (!isMissType) {
                    cacheData.put(paginatedKey, value);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(missType)) {
            log.info("缺失的redis数据类型{}", missType);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", cacheData);
        result.put("pageNumber", pageNumber);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalItems);
        return result;
    }
}
