package com.story.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * updator 续写了 您的故事《storyTitle》 updator 续写了 您在故事《storyTitle》中的更新 新的故事线
 */
@Document
public class Message {

	@Id
	@NotNull
	private String id;
	@Field
	private Boolean notified;
	@Field
	private String type; // multi/single
	@Field
	private String updator;
	@Field
	private String storyTitle;
	@Field
	private String storyId;
	@Field
	private String phaseId;
	@Field
	private String storyAuthorOpenid;
	@Field
	private String phaseAuthorOpenid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getNotified() {
		return notified;
	}

	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}

	public String getStoryAuthorOpenid() {
		return storyAuthorOpenid;
	}

	public void setStoryAuthorOpenid(String storyAuthorOpenid) {
		this.storyAuthorOpenid = storyAuthorOpenid;
	}

	public String getPhaseAuthorOpenid() {
		return phaseAuthorOpenid;
	}

	public void setPhaseAuthorOpenid(String phaseAuthorOpenid) {
		this.phaseAuthorOpenid = phaseAuthorOpenid;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", notified=" + notified + ", type=" + type + ", updator=" + updator
				+ ", storyTitle=" + storyTitle + ", storyId=" + storyId + ", phaseId=" + phaseId
				+ ", storyAuthorOpenid=" + storyAuthorOpenid + ", phaseAuthorOpenid=" + phaseAuthorOpenid + "]";
	}

}
