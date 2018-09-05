package cn.com.zach.demo.glasses.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class RedisFacade {
	
	/**
     * redis默认每次最大匹配大小为10000
     */
    private static final int DEFAULT_MATCH_SIZE = 10000;
    
	@SuppressWarnings("unchecked")
	private static RedisTemplate<Object, Object> redisTemplate = BeanFactory.getBean(RedisTemplate.class);

	public static RedisTemplate<Object, Object> getRedisTemplate() {
		return redisTemplate;
	}
	
	/**
     * 根据key从redis中获取值
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz) {
        if (StringUtil.isEmpty(key))
            return null;
        String json = (String) get(key);
        if(clazz.equals(String.class)){
            return (T)json;
        }
        return JSONObject.parseObject(json, clazz);
    }
	 
	/**
     * 判断key在redis中是否存在
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        return getRedisTemplate().hasKey(key);
    }
    
    /**
     * 根据key从redis中获取值
     *
     * @param key
     * @return
     */
    public static Object get(Object key) {
    		ValueOperations<Object, Object> operations = getRedisTemplate().opsForValue();
        return operations.get(key);
    }
    
    /**
     * 从redis中批量获取值
     *
     * @param keys
     * @return
     */
    public static List<Object> multiGet(Object... keys) {
    		ValueOperations<Object, Object> operations = getRedisTemplate().opsForValue();
    		return operations.multiGet(Arrays.asList(keys));
    }
    
    /**
     * 保存字符串到redis中，永不过期
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static void set(Object key, Object value) {
    		ValueOperations<Object, Object> operations = getRedisTemplate().opsForValue();
    		operations.set(key, value);
    }
    
    /**
     * 批量保存字符串到redis中，永不过期
     *
     * @param map   
     * @return
     */
    public static void set(Map<String, Object> map) {
    		ValueOperations<Object, Object> operations = getRedisTemplate().opsForValue();
    		operations.multiSet(map);
    }
    
    /**
     * 设置key的值为value,并设置过期时间为指定的seconds秒
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public static void setex(Object key, Object value, int seconds) {
    		ValueOperations<Object, Object> operations = getRedisTemplate().opsForValue();
    		operations.set(key, value, seconds, TimeUnit.SECONDS);
    }
    
    /**
     * 设置key的值和超时时间
     *
     * @param key
     * @param value
     * @param time
     * @param unit	时间单位
     * @return
     */
    public static void set(String key, Object value, long time, TimeUnit unit) {
        getRedisTemplate().opsForValue().set(key, value, time, unit);
    }
    
    /**
     * 设置key的值，仅当key不存在时
     * @param key
     * @param value
     * @return
     */
    public static boolean setnx(String key, Object value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    
    /**
     * 批量设置 设置key的值，仅当key不存在时
     * @param map
     * @return
     */
    public static boolean multiSetNx(Map<String, Object> map) {
        return getRedisTemplate().opsForValue().multiSetIfAbsent(map);
    }
    
    /**
     * 返回key对应的值，并设置key对应的新值为value
     *
     * @param key
     * @param value
     * @return
     */
    public static Object getSet(Object key, Object value) {
        if (StringUtil.isEmpty(key))
            return null;
        return getRedisTemplate().opsForValue().getAndSet(key, value);
    }
    
    /**
     * 在key对应的值后面追加字符串，如果key不存在，则相当于set(key,value)
     *
     * @param key
     * @param value
     */
    public static void append(Object key, String value) {
        getRedisTemplate().opsForValue().append(key, value);
    }
    
    /**
     * 截取key对应的字符串,[start,end]，start和end部分都包括。 <br/>
     * 若end大于key对应的字符串的最大长度，end为最大长度
     * <p>
     * start = -1 表示从后面截取，即最后一个字符，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static String substr(Object key, int start, int end) {
        return getRedisTemplate().opsForValue().get(key, start, end);
    }
    
    /**
     * 设置key的超时时间，若key超时，redis服务器将删除key对应的值
     * @param key
     * @param seconds
     * @return
     */
    public static boolean expire(final Object key, final int seconds) {
        if (StringUtil.isEmpty(key))
            return false;
        return getRedisTemplate().expire(key, seconds, TimeUnit.SECONDS);
    }
    
    /**
     * 查询key对应的值在redis中的剩余时间，单位秒
     *
     * @param key
     * @return -1 没有设置过期时间，或者key不存在
     */
    public static Long ttl(Object key) {
        if (StringUtil.isEmpty(key))
            return -1L;
        return getRedisTemplate().getExpire(key);
    }
    
    /**
     * 删除多个key
     *
     * @param keys
     * @return 0 key不存在，大于1删除成功的key的个数
     */
    public static void del(String... keys) {
        Collection<String> collection = (Collection<String>) Arrays.asList(keys);
        getRedisTemplate().delete(collection);
    }
    
    /**
    * 重命名一个key
    *
    * @param oldKey
    * @param newKey
    * @return
    */
   public static boolean rename(String oldKey, String newKey) {
       return getRedisTemplate().renameIfAbsent(oldKey, newKey);
   }
   
   /**
    * 递增一个key对应的整数值，返回递增后的值。 如果key对应的值不存在，<br/>
    * 或者key对应的值不是数字类型， 则先将key对应的值设为0，然后递增
    *
    * @param key
    * @return 0 key为空，或者执行异常
    */
   public static Long incr(String key) {
       return getRedisTemplate().opsForValue().increment(key, 1);
   }
   
   /**
    * key对应的值加上value
    *
    * @param key
    * @param value
    * @return
    */
   public static Long incrBy(String key, long value) {
       return getRedisTemplate().opsForValue().increment(key, value);
   }
   
   /**
    * 递减key对应的值，并返回递减后的值。如果key不存在或者key不是数字类型，<br/>
    * 则先将key对应的值设为0，然后递减
    *
    * @param key
    * @return
    */
   public static Long decr(String key) {
       return getRedisTemplate().opsForValue().increment(key, -1);
   }
   
   /**
    * key对应的值减去value
    *
    * @param key
    * @param value
    * @return
    */
   public static Long decrBy(Object key, long value) {
       return getRedisTemplate().opsForValue().increment(key, value);
   }
   
   /**
    * key对应的值为redis中存储的一个hashmap，<br/>
    * 设置hashmap中的feild 对应的值为value
    *
    * @param key
    * @param field
    * @param value
    * @return
    */
   public static void hset(Object key, Object field, Object value) {
       getRedisTemplate().opsForHash().put(key, field, value);
   }

   /**
    * 批量put Map数据
    *
    * @param key
    * @param mapValues
    */
   public static void hmset(Object key, Map<Object, Object> mapValues) {
       getRedisTemplate().opsForHash().putAll(key, mapValues);
   }

   /**
    * 获取key对应的hashmap中field对应的值
    *
    * @param key
    * @param field
    * @return
    */
   public static Object hget(Object key, Object field) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForHash().get(key, field);
   }
   
   /**
    * 批量获取key对应的hashmap中fields对应的值
    *
    * @param key
    * @param fields
    * @return
    */
   public static List<Object> hmget(Object key, Object... hashKeys) {
       return getRedisTemplate().opsForHash().multiGet(key, Arrays.asList(hashKeys));
   }

   /**
    * 判断key对应的map中是否存在field键
    *
    * @param key
    * @param field
    * @return
    */
   public boolean hexists(Object key, Object hashKey) {
       if (StringUtil.isEmpty(key))
           return false;
       return getRedisTemplate().opsForHash().hasKey(key, hashKey);
   }


   /**
    * 从key相关的hashmap中删除指定的field键
    *
    * @param key
    * @param fields
    * @return
    */
   public static Long hdel(Object key, Object... hashKeys) {
       if (StringUtil.isEmpty(key))
           return 0L;
       return getRedisTemplate().opsForHash().delete(key, hashKeys);
   }

   /**
    * 查询hashmap的长度
    *
    * @param key
    * @return
    */
   public static Long hlen(String key) {
       if (StringUtil.isEmpty(key))
           return 0L;
       return getRedisTemplate().opsForHash().size(key);
   }

   /**
    * 获取key对应的hashmap中所有键('key')
    *
    * @param key
    * @return
    */
   public static Set<Object> hkeys(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForHash().keys(key);
   }
   
   /**
    * 获取key对应的hashmap中所有值('value')
    *
    * @param key
    * @return
    */
   public static List<Object> hvalues(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForHash().values(key);
   }

   /**
    * 获取key对应的hashmap
    *
    * @param key
    * @return
    */
   public static Map<Object, Object> hgetAll(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForHash().entries(key);
   }
   
   /**
    * 增加字符串数据到key对应的链表中<br/>
    * 从尾部添加 lpush 从头部添加，左边 ← ; rpush 从尾部添加，右边→
    *
    * @param key
    * @param values
    * @return
    */
   public static Long rpush(Object key, Object... value) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForList().rightPushAll(key, value);
   }


   /**
    * 增加对象到key对应的链表中<br/>
    * 从尾部添加 lpush 从头部添加，左边 ← ；rpush 从尾部添加，右边→
    *
    * @param key
    * @param values
    * @return
    */
   public static Long lpush(Object key, Object... value) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForList().leftPush(key, value);
   }


   /**
    * 获取redis链表的长度
    *
    * @param key
    * @return
    */
   public static Long llen(int database, Object key) {
       if (StringUtil.isEmpty(key))
           return 0L;
       return getRedisTemplate().opsForList().size(key);
   }

   /**
    * 获取子链表
    *
    * @param key
    * @param start 开始位置 从0开始
    * @param end   结束位置（包含）
    * @return
    */
   public static List<Object> lrange(Object key, long start, long end) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForList().range(key, start, end);
   }


   /**
    * 压缩链表到指定的[start,end]
    *
    * @param key
    * @param start
    * @param end
    * @return
    */
   public static void ltrim(Object key, long start, long end) {
       if (StringUtil.isEmpty(key))
           return;
       getRedisTemplate().opsForList().trim(key, start, end);
   }

   /**
    * 返回链表指定位置的元素
    *
    * @param key
    * @param index
    * @return
    */
   public static Object lindex(String key, long index) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForList().index(key, index);
   }


   /**
    * 设置链表中index位置的元素
    *
    * @param key
    * @param index
    * @param value
    * @return
    */
   public static void lset(Object key, long index, Object value) {
       if (StringUtil.isEmpty(key))
           return;
       getRedisTemplate().opsForList().set(key, index, value);
   }


   /**
    * 如果count为正，则从左往右（从头部到尾部），删除链表中count个value，<br/>
    * 若count为0，则全部删除。 如果count为负，则从右往左（从尾部到头部）删除。
    *
    * @param key
    * @param count
    * @param value
    * @return 成功删除的个数
    */
   public static Long lrem(Object key, long count, Object value) {
       if (StringUtil.isEmpty(key))
           return 0L;
       return getRedisTemplate().opsForList().remove(key, count, value);
   }

   /**
    * 如果count为正，则从左往右（从头部到尾部），删除链表中count个value，<br/>
    * 若count为0，则全部删除。 如果count为负，则从右往左（从尾部到头部）删除。
    *
    * @param key
    * @param count
    * @param value
    * @return 成功删除的个数
    */
   public static Long lrem(String key, long count, String value) {
       return getRedisTemplate().opsForList().remove(key, count, value);
   }

   /**
    * 返回并删除链表最后一个元素
    *
    * @param key
    * @return
    */
   public static Object rpop(Object key) {
       return getRedisTemplate().opsForList().rightPop(key);
   }

   /**
    * 返回并删除链表第一个元素
    *
    * @param key
    * @return
    */
   public static Object lpop(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForList().leftPop(key);
   }


   /**
    * 添加元素到redis集合（set）中
    *
    * @param key
    * @param values
    * @return
    */
   public static Long sadd(Object key, Object... values) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForSet().add(key, values);
   }


   /**
    * 返回redis集合（set）中所有元素
    *
    * @param key
    * @return
    */
   public static Set<Object> smembers(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForSet().members(key);
   }


   /**
    * 从redis集合（set）中删除指定元素
    *
    * @param key
    * @param members
    * @return
    */
   public static Long srem(Object key, Object... values) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForSet().remove(key, values);
   }

   /**
    * 随机从redis集合中删除一个元素并返回，如果集合不存在或者集合为空，返回null
    *
    * @param key
    * @return
    */
   public static Object spop(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForSet().pop(key);
   }


   /**
    * 从原集合移动指定元素到目标集合中，原子操作，线程安全
    *
    * @param srcKey  原集合对应的key
    * @param destKey 目标集合对应的key
    * @param member  元素
    * @return
    */
   public static boolean smove(Object srcKey, Object destKey, Object member) {
       if (StringUtil.isEmpty(srcKey) || StringUtil.isEmpty(destKey))
           return false;
       if (member == null)
           return false;
       return getRedisTemplate().opsForSet().move(srcKey, member, destKey);
   }


   /**
    * 获取redis集合中的元素个数
    *
    * @param key
    * @return
    */
   public static Long slen(Object key) {
       if (StringUtil.isEmpty(key))
           return null;
       return getRedisTemplate().opsForSet().size(key);
   }


   /**
    * 集合匹配元素， 判断member是否为集合中的元素
    *
    * @param key
    * @param member
    * @return
    */
   public static boolean sismember(Object key, Object member) {
       if (StringUtil.isEmpty(key))
           return false;
       return getRedisTemplate().opsForSet().isMember(key, member);
   }


   /**
    * 求集合的交集
    *
    * @param keys 一个key对应一个集合
    * @return
    */
   public static Set<Object> sinter(Object key, Object... keys) {
       if (key == null)
           return null;
       return getRedisTemplate().opsForSet().intersect(key, keys);
   }

   /**
    * 求集合的交集，结果存储到新的集合（destKey对应的集合）中去
    *
    * @param dstkey
    * @param keys
    * @return
    */
   public static Long sinterstore(Object destKey, Object srckey, Object... srcKeys) {
       if (StringUtil.isEmpty(destKey) || srckey == null)
           return null;
       return getRedisTemplate().opsForSet().intersectAndStore(srckey, srcKeys, destKey);
   }


   /**
    * 求集合的并集
    *
    * @param keys 一个key对应一个集合
    * @return
    */
   public static Set<Object> sunion(Object... keys) {
       if (keys == null || keys.length == 0) {
           return null;
       }
       List<Object> keyList = Arrays.asList(keys);
       return getRedisTemplate().opsForSet().union(keyList.remove(0), keyList);
   }

   /**
    * 求集合的并集，结果存储到新的集合（destKey对应的集合）中去
    *
    * @param dstkey
    * @param keys
    * @return
    */
   public static Long sunionstore(final Object dstKey, final Object... keys) {
       if (keys == null || keys.length == 0 || dstKey == null) {
           return null;
       }
       List<Object> keyList = Arrays.asList(keys);
       return getRedisTemplate().opsForSet().unionAndStore(keyList.remove(0), keyList, dstKey);

   }


   /**
    * 求集合的差集
    * <p>
    * <pre>
    * key1 = [x, a, b, c]
    * key2 = [c]
    * key3 = [a, d]
    * SDIFF key1,key2,key3 => [x, b]
    * </pre>
    *
    * @param keys 一个key对应一个集合
    * @return
    */
   public static Set<Object> sdiff(Object... keys) {
       if (keys == null || keys.length == 0) {
           return null;
       }
       List<Object> keyList = Arrays.asList(keys);
       return getRedisTemplate().opsForSet().difference(keyList.remove(0), keyList);
   }


   /**
    * 求集合的交集，结果存储到新的集合（destKey对应的集合）中去
    *
    * @param dstkey
    * @param keys
    * @return
    */
   public static Long sdiffstore(final Object dstKey, final Object... keys) {
       if (StringUtil.isEmpty(dstKey) || keys == null || keys.length == 0)
           return null;
       List<Object> keyList = Arrays.asList(keys);
       return getRedisTemplate().opsForSet().differenceAndStore(keyList.remove(0), keyList, dstKey);
   }


   /**
    * 批量判断Redis key所指向的集合中是否存在values列表中的数据;采用单线程方式<br/>
    * 如果value存在，返回true，否则false
    *
    * @param key Redis集合的key
    * @param values 需要匹配的值列表
    * @return
    */
   public static List<Object> multiSismember(String key, List<Object> values) {
       if (values == null || values.isEmpty())
           return null;

       List<Object> returnList = new ArrayList<Object>();
       if (values.size() <= DEFAULT_MATCH_SIZE) {
           List<Object> result = innerMatch(key, values);
           if (result != null)
               returnList.addAll(result);
           return returnList;
       }

       List<List<Object>> lists = partition(values, DEFAULT_MATCH_SIZE);
       for (List<Object> list : lists) {
           List<Object> result = innerMatch(key, list);
           if (result != null)
               returnList.addAll(result);
       }

       return returnList;
   }
   
   /**
    * 匹配列表
    * @param database
    * @param key
    * @param values
    * @return
    */
   private static List<Object> innerMatch(String key, List<Object> values) {
	   HashOperations<Object, Object, Object> operations = getRedisTemplate().opsForHash();
	   return operations.multiGet(key, values);
   }
   
   /**
    * 拆分列表
    *
    * @param values 源列表
    * @param devideSize 拆分大小
    * @return
    */
   private static List<List<Object>> partition(List<Object> values, int devideSize) {
       int size = values.size();
       int resultSize = (int) Math.ceil(1.0 * size / devideSize);
       List<List<Object>> result = new ArrayList<List<Object>>(resultSize);
       for (int i = 0; i < resultSize; i++) {
           int start = i * devideSize;
           int end = Math.min(start + devideSize, size);
           List<Object> subList = values.subList(start, end);
           result.add(subList);
       }
       return result;
   }
   
   /**
    * 删除列表指定索引位置:[start,end]的元素，并返回
    *
    * @param key 列表对应的key
    * @param start 开始位置
    * @param end 结束位置
    * @return
    */
   public static List<Object> lremove(String key, long start, long end) {
       if (StringUtil.isEmpty(key)){
           return null;
       }
       List<Object> result = lrange(key, start, end);
       ltrim(key, end + 1, -1);
       return result;
   }
}
