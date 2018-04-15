package com.story.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class MongoUtils {

	private static final Logger logger = LoggerFactory.getLogger(MongoUtils.class);
	
	public static final Sort buildSort(String json) {
		Sort sort = null;
		if(!StringUtils.isEmpty(json)){
			LinkedHashMap<String, Object> sortMap = null;
			try {
				sortMap = (LinkedHashMap<String, Object>) JsonUtils.buildJson(json);
			} catch (Exception e){
				logger.error("Json convert exception.");
				return null;
			}
			List<Order> sortList = new ArrayList<Order>();
			for(Entry<String, Object> e: sortMap.entrySet()){
				sortList.add(new Order(Direction.fromString((String.valueOf(e.getValue())).toLowerCase()), e.getKey()));
			}
			sort = new Sort(sortList.toArray(new Order[0]));
		}
		return sort;
	}
}
