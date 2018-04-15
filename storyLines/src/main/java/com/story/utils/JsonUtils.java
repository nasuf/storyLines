package com.story.utils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static Map<String, Object> buildJson(String json)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(json)) {
			throw new NullPointerException("Json string is empty.");
		}
		Map<String, Object> result = objectMapper.readValue(json, new TypeReference<LinkedHashMap<String, Object>>() {
		});
		return result;
	}

	public static String buildJsonString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (StringUtils.isEmpty(String.valueOf(srcValue))) {
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

}
