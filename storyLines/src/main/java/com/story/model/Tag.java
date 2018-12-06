package com.story.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Tag {

	@Id
	@NotNull
	private String id;
	@Field
	private String value;
	@Field
	private Integer count;
	@Field
	private String color;

	public Tag() {
	}

	public Tag(String value, Integer count) {
		super();
		this.value = value;
		this.count = count;
	}
	
	public Tag(String value, Integer count, String color) {
		super();
		this.value = value;
		this.count = count;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", value=" + value + ", count=" + count + ", color=" + color + "]";
	}

}
