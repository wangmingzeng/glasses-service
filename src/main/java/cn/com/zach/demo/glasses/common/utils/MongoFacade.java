package cn.com.zach.demo.glasses.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

import cn.com.zach.demo.glasses.mode.PageInfo;


@Component
public class MongoFacade<T> {

	private static MongoTemplate mongoTemplate = BeanFactory.getBean(MongoTemplate.class);
	
	public void insert(T t) {
		mongoTemplate.insert(t);
	}

	public T getById(Object id, Class<T> clazz) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		return (T) mongoTemplate.findOne(query, clazz);
	}

	public T getById(Long id, Class<T> clazz, String collectionName) {
		return (T) mongoTemplate.findById(id, clazz, collectionName);
	}

	public List<T> getByCondition(String key, Object value, Class<T> clazz){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(key, value);
		return getByCondition(param, clazz);
	}
	
	public List<T> getByCondition(Map<String, Object> param, Class<T> clazz){
		if(param != null) {
			Query query = new Query();
			Iterator<Entry<String, Object>> iter = param.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
			}
			return mongoTemplate.find(query, clazz);
		}
		return null;
	}
	
	public List<T> getListByLikeCondition(String key, Object content, Class<T> clazz) {
		// 模糊匹配
		Pattern pattern = Pattern.compile("^.*" + content.toString() + ".*$", Pattern.CASE_INSENSITIVE);
		Query query = Query.query(Criteria.where(key).regex(pattern));
		return mongoTemplate.find(query, clazz);
	}

	public List<T> getListBySectionCondition(String key, Object start, Object end, Class<T> clazz) {
		Query query = new Query();
		query.addCriteria(Criteria.where(key).gte(start).lte(end));
		return mongoTemplate.find(query, clazz);
	}
	
	public List<T> getByQuery(Query query, Class<T> clazz) {
		return mongoTemplate.find(query, clazz);
	}
	
	public List<T> getListByPageSort(PageInfo pageInfo, Map<String, Object> param,Class<T> clazz) {
		return getListByPageSort(pageInfo, param, null, null,clazz);
	}
	
	public List<T> getListByPageSort(PageInfo pageInfo, Map<String, Object> param, String direction, String sortBy,Class<T> clazz) {
		Query query = new Query();
		if(param != null) {
			Iterator<Entry<String, Object>> iter = param.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				if(entry.getKey() != "page" && entry.getKey() != "pageSize"){
					query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
				}
			}
		}
		Sort.Direction directions = null;
		if(StringUtil.isNotEmpty(sortBy)) {
			if(direction == "asc") {
				directions = Sort.Direction.ASC;
			}else {
				directions = Sort.Direction.DESC;
			}
		}else {
			sortBy = "_id";
			directions = Sort.Direction.DESC;
		}
		return getListByPageUseSort(pageInfo, query, directions, sortBy, clazz);
	}

	public List<T> getListByPageUseSort(PageInfo pageInfo, Query query, Sort.Direction direction, String sortBy, Class<T> clazz) {
		// 如果没有条件 则所有全部
		query = query == null ? new Query(Criteria.where("_id").exists(true)) : query;
		long count = count(query, clazz);
		int currentPage = pageInfo.getCurPage();
		int pageSize = pageInfo.getPageLimit();
		query.skip((currentPage - 1) * pageSize).limit(pageSize);

		if (StringUtil.isNotBlank(sortBy) && null != direction) {
			query.with(new Sort(new Sort.Order(direction, sortBy)));
		}
		List<T> rows = getByQuery(query, clazz);
		pageInfo.setTotal(count);
		return rows;
	}
	
	public long count(Query query, Class<T> clazz) {
		return mongoTemplate.count(query, clazz);
	}
	
	public List<T> getAll(Class<T> clazz) {
		// 如果没有条件 则所有全部
		Query query = new Query(Criteria.where("_id").exists(true));
		List<T> rows = getByQuery(query, clazz);
		return rows;
	}
	
	public boolean update(Update update, Object id, Class<T> clazz) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, clazz);
		return writeResult.getN() > 0 ? true : false;
	}

	public boolean delete(Class<T> clazz, Object... ids) {
		boolean flag = true;
		if (ids == null || ids.length == 0) {
			flag = false;
		}
		for (Object id : ids) {
			Query query = new Query(Criteria.where("_id").is(id));
			WriteResult remove = mongoTemplate.remove(query, clazz);
			if (remove.getN() <= 0) {
				flag = false;
			}
			remove = null;
		}
		return flag;
	}
	
	public boolean delete(String key, Object value, Class<T> clazz) {
		boolean flag = true;
		if (key == null || key.length() == 0 || value == null) {
			flag = false;
		}
		Query query = new Query(Criteria.where(key).is(value));
		WriteResult remove = mongoTemplate.remove(query, clazz);
		if (remove.getN() <= 0) {
			flag = false;
		}
		remove = null;
		return flag;
	}

}