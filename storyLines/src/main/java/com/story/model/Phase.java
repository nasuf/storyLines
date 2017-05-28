package com.story.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Phase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6825278386404683610L;
	@Id
	@NotNull
	private String id;
	@Field
	private String storyId;
	@Field
	private String parentPhaseId;
	@Field
	private String content;
	@Field
	private Boolean isStart;
	@Field
	private Boolean isEnd;
	@Field
	private Integer level;
	@Field
	private Integer like;
	@Field
	private Integer dislike;
	@Field
	private String comment;
	@Field
	private Long createdDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoryId() {
		return storyId;
	}
	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Boolean getIsStart() {
		return isStart;
	}
	public void setIsStart(Boolean isStart) {
		this.isStart = isStart;
	}
	public Boolean getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getLike() {
		return like;
	}
	public void setLike(Integer like) {
		this.like = like;
	}
	public Integer getDislike() {
		return dislike;
	}
	public void setDislike(Integer dislike) {
		this.dislike = dislike;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getParentPhaseId() {
		return parentPhaseId;
	}
	public void setParentPhaseId(String parentPhaseId) {
		this.parentPhaseId = parentPhaseId;
	}
	
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "Phase [id=" + id + ", storyId=" + storyId + ", parentPhaseId=" + parentPhaseId + ", content=" + content
				+ ", isStart=" + isStart + ", isEnd=" + isEnd + ", level=" + level + ", like=" + like + ", dislike="
				+ dislike + ", comment=" + comment + ", createdDate=" + createdDate + "]";
	}

}
