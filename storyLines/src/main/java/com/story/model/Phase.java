package com.story.model;

import java.io.Serializable;
import java.util.ArrayList;

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
	private String storyTitle;
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
	private ArrayList<String> comments;
	@Field
	private Long createdDate;
	@Field
	private String author;
	@Field
	private Boolean isPrivate;
	
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public ArrayList<String> getComments() {
		return comments;
	}
	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}
	public String getStoryTitle() {
		return storyTitle;
	}
	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}
	@Override
	public String toString() {
		return "Phase [id=" + id + ", storyId=" + storyId + ", storyTitle=" + storyTitle + ", parentPhaseId="
				+ parentPhaseId + ", content=" + content + ", isStart=" + isStart + ", isEnd=" + isEnd + ", level="
				+ level + ", like=" + like + ", dislike=" + dislike + ", comments=" + comments + ", createdDate="
				+ createdDate + ", author=" + author + ", isPrivate=" + isPrivate + "]";
	}

}
