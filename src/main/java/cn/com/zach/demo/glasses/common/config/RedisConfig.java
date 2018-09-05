package cn.com.zach.demo.glasses.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.zach.demo.glasses.common.property.SystemMessage;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	// 缓存管理器
	@Bean
	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		CacheManager cacheManager = new RedisCacheManager(redisTemplate);
		return cacheManager;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(factory);
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的key 和 value值（默认使用JDK的序列化方式）
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(serializer);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	@Primary
	public RedisConnectionFactory redisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 连接池最大连接数(使用负值表示没有限制,比如-1)
		poolConfig.setMaxTotal(SystemMessage.getInteger("spring.redis.pool.max-active"));
		// 连接池中的最大空闲连接
		poolConfig.setMaxIdle(SystemMessage.getInteger("spring.redis.pool.max-idle"));
		// 连接池中的最小空闲连接
		poolConfig.setMinIdle(SystemMessage.getInteger("spring.redis.pool.min-idle"));
		//连接池最大阻塞等待时间(使用负值表示没有限制,比如-1)
		poolConfig.setMaxWaitMillis(SystemMessage.getInteger("spring.redis.pool.max-wait"));
		
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		// Redis服务器地址
		redisConnectionFactory.setHostName(SystemMessage.getString("spring.redis.host"));
		// Redis服务器连接端口
		redisConnectionFactory.setPort(SystemMessage.getInteger("spring.redis.port"));
		// Redis服务器连接密码(默认为空)
		redisConnectionFactory.setPassword(SystemMessage.getString("spring.redis.password"));
		// Redis数据库索引(默认为0)
		redisConnectionFactory.setDatabase(SystemMessage.getInteger("spring.redis.database"));
		// 连接超时时间(毫秒)
		redisConnectionFactory.setTimeout(SystemMessage.getInteger("spring.redis.timeout"));
		redisConnectionFactory.setUsePool(true);
		redisConnectionFactory.setPoolConfig(poolConfig);
		return redisConnectionFactory;
	}
	
}
