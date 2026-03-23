package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.VisitService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String VISIT_KEY_PREFIX = "visit:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void incrementVisitCount() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        String key = VISIT_KEY_PREFIX + today;
        try {
            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, 7, TimeUnit.DAYS);
            log.info("访问量统计成功，日期: {}", today);
        } catch (Exception e) {
            log.error("访问量统计失败: {}", e.getMessage());
        }
    }

    @Override
    public Long getTodayVisitCount() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        String key = VISIT_KEY_PREFIX + today;
        try {
            Object count = redisTemplate.opsForValue().get(key);
            return count != null ? Long.valueOf(count.toString()) : 0L;
        } catch (Exception e) {
            log.error("获取访问量失败: {}", e.getMessage());
            return 0L;
        }
    }
}
