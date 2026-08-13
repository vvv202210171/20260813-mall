package com.macro.mall.common.log;

import com.macro.mall.common.domain.WebLog;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DbLogService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DbLogService.class);

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public DbLogService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * 异步保存 WebLog，避免阻塞请求线程。
     */
    @Async("webLogExecutor")
    public void saveAsync(WebLog webLog) {
        String sql = "INSERT INTO web_log (username, ip, method, parameter, result, spend_time, start_time, uri, url, description) " +
                     "VALUES (:username, :ip, :method, :parameter, :result, :spend_time, :start_time, :uri, :url, :description)";
        Map<String, Object> params = new HashMap<>();
        params.put("username", webLog.getUsername());
        params.put("ip", webLog.getIp());
        params.put("method", webLog.getMethod());
        params.put("parameter", webLog.getParameter() != null ? webLog.getParameter().toString() : null);
        params.put("result", webLog.getResult() != null ? webLog.getResult().toString() : null);
        params.put("spend_time", webLog.getSpendTime());
        params.put("start_time", webLog.getStartTime());
        params.put("uri", webLog.getUri());
        params.put("url", webLog.getUrl());
        params.put("description", webLog.getDescription());
        try {
            jdbc.update(sql, params);
        } catch (Exception e) {
            // Do not break application flow if logging fails
            logger.error("Failed to persist web log", e);
        }
    }

    /**
     * 同步保存（保留以防需要同步场景）
     */
    public void save(WebLog webLog) {
        saveAsync(webLog); // best-effort: delegate to async
    }
}
