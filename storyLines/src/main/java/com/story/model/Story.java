package com.story.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Story implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3095787688160277053L;
	
	@Id
	@NotNull
	private String id;
	@Field
	private Integer depth;
	@Field
	private Integer phaseCount;
	@Field
	private ArrayList<String> phaseIdList;
	@Field
	private Long createdDate;
	@Field
	private Long lastUpdatedDate;
	@Field
	private String title;
	@Field
	private Boolean isPrivate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Integer getPhaseCount() {
		return phaseCount;
	}
	public void setPhaseCount(Integer phaseCount) {
		this.phaseCount = phaseCount;
	}
	public ArrayList<String> getPhaseIdList() {
		return phaseIdList;
	}
	public void setPhaseIdList(ArrayList<String> phaseIdList) {
		this.phaseIdList = phaseIdList;
	}
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Long getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Long lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	@Override
	public String toString() {
		return "Story [id=" + id + ", depth=" + depth + ", phaseCount=" + phaseCount + ", phaseIdList=" + phaseIdList
				+ ", createdDate=" + createdDate + ", lastUpdatedDate=" + lastUpdatedDate + ", title=" + title
				+ ", isPrivate=" + isPrivate + "]";
	}

}
