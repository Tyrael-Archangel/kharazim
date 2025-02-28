/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tyrael.kharazim.dubboregistry.redis;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_TIMEOUT;
import static org.apache.dubbo.common.constants.CommonConstants.TIMEOUT_KEY;

public class MonoRedisClient implements RedisClient {

    private final JedisPool jedisPool;

    public MonoRedisClient(URL url) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(url.getParameter("test.on.borrow", true));
        config.setTestOnReturn(url.getParameter("test.on.return", false));
        config.setTestWhileIdle(url.getParameter("test.while.idle", false));
        if (url.getParameter("max.idle", 0) > 0) {
            config.setMaxIdle(url.getParameter("max.idle", 0));
        }
        if (url.getParameter("min.idle", 0) > 0) {
            config.setMinIdle(url.getParameter("min.idle", 0));
        }
        if (url.getParameter("max.active", 0) > 0) {
            config.setMaxTotal(url.getParameter("max.active", 0));
        }
        if (url.getParameter("max.total", 0) > 0) {
            config.setMaxTotal(url.getParameter("max.total", 0));
        }
        if (url.getParameter("num.tests.per.eviction.run", 0) > 0) {
            config.setNumTestsPerEvictionRun(url.getParameter("num.tests.per.eviction.run", 0));
        }
        jedisPool = new JedisPool(config, url.getHost(), url.getPort(),
                url.getParameter(TIMEOUT_KEY, DEFAULT_TIMEOUT), url.getPassword(), url.getParameter("db.index", 0));
    }

    @Override
    public Long hset(String key, String field, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, field, value);
        }
    }

    @Override
    public Long publish(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.publish(channel, message);
        }
    }

    @Override
    public boolean isConnected() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.isConnected();
        }
    }

    @Override
    public void destroy() {
        jedisPool.close();
    }

    @Override
    public Long hdel(String key, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(key, fields);
        }
    }

    @Override
    public Set<String> scan(String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> result = new HashSet<>();
            String cursor = ScanParams.SCAN_POINTER_START;
            ScanParams params = new ScanParams();
            params.match(pattern);
            while (true) {
                ScanResult<String> scanResult = jedis.scan(cursor, params);
                List<String> list = scanResult.getResult();
                if (CollectionUtils.isNotEmpty(list)) {
                    result.addAll(list);
                }
                if (ScanParams.SCAN_POINTER_START.equals(scanResult.getCursor())) {
                    break;
                }
                cursor = scanResult.getCursor();
            }
            return result;
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.psubscribe(jedisPubSub, patterns);
        }
    }

    @Override
    public void disconnect() {
        jedisPool.close();
    }

}
