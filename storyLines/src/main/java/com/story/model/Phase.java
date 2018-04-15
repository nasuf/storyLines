package com.story.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Phase implements Serializable, Comparable{

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
	@Field
	private Boolean isDeleted;
	@Field
	private Long lastUpdatedDate;
	@Field
	private ArrayList<String> branchPhases;
	
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
	public ArrayList<String> getBranchPhases() {
		return branchPhases;
	}
	public void setBranchPhases(ArrayList<String> branchPhases) {
		this.branchPhases = branchPhases;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Long getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Long lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	@Override
	public int hashCode() {

		return StringUtils.isEmpty(this.getId()) ? 0 : this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phase clazz = (Phase) obj;
		return clazz.getId().equals(this.getId());
	}
	
	@Override
	public String toString() {
		return "Phase [id=" + id + ", storyId=" + storyId + ", storyTitle=" + storyTitle + ", parentPhaseId="
				+ parentPhaseId + ", content=" + content + ", isStart=" + isStart + ", isEnd=" + isEnd + ", level="
				+ level + ", like=" + like + ", dislike=" + dislike + ", comments=" + comments + ", createdDate="
				+ createdDate + ", author=" + author + ", isPrivate=" + isPrivate + ", isDeleted=" + isDeleted
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", branchPhases=" + branchPhases + "]";
	}
	@Override
	public int compareTo(Object o) {
		if (this ==o) {
            return 0;            
        } else if (o!=null && o instanceof Phase) {   
            Phase phase = (Phase) o; 
            if (like <= phase.like) {
                return -1;
            } else {
	            return 1;
	        }
	    } else {
	        return -1;
	    }
	}

}
