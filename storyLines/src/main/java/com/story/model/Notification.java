package com.story.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Notification {

	@Id
	@NotNull
	private String id;
	@Field
	private String type; // multi/single
	@Field
	private String destination;
	@Field
	private Set<String> storyTitleList;
	@Field
	private Boolean isStoryUpdateNotification;
	@Field
	private Boolean consumed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Set<String> getStoryTitleList() {
		return storyTitleList;
	}

	public void setStoryTitleList(Set<String> storyTitleList) {
		this.storyTitleList = storyTitleList;
	}

	public Boolean getIsStoryUpdateNotification() {
		return isStoryUpdateNotification;
	}

	public void setIsStoryUpdateNotification(Boolean isStoryUpdateNotification) {
		this.isStoryUpdateNotification = isStoryUpdateNotification;
	}

	public Boolean getConsumed() {
		return consumed;
	}

	public void setConsumed(Boolean consumed) {
		this.consumed = consumed;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", type=" + type + ", destination=" + destination + ", storyTitleList="
				+ storyTitleList + ", isStoryUpdateNotification=" + isStoryUpdateNotification + ", consumed=" + consumed
				+ "]";
	}

}
