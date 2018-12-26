package com.story.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Phase implements Serializable, Comparable {

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
	private String storyAuthorOpenid;
	@Field
	private String parentPhaseId;
	@Field
	private String rootPhaseId;
	@Field
	private String content;
	@Field
	private Boolean isStart;
	@Field
	private Boolean isEnd;
	@Field
	private Integer level;
	@Field
	private Integer likes;
	@Field
	private ArrayList<String> comments;
	@Field
	private Long createdDate;
	@Field
	private String author;
	@Field
	private String authorOpenid;
	@Field
	private String authorAvatarUrl;
	@Field
	private Boolean isPrivate;
	@Field
	private Boolean isDeleted;
	@Field
	private Long lastUpdatedDate;
	@Field
	private ArrayList<String> branchPhases;
	@Field
	private Set<String> tags;
	@Field
	private Integer reviewCount;
	@Field
	private Boolean isStoryNeedApproval;
	@Field
	private String approvalStatus;
	@Field
	private Integer contentLengthMax;
	@Field
	private Integer contentLengthMin;
	
	private Set<String> likesUsers;

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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
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

	public String getAuthorOpenid() {
		return authorOpenid;
	}

	public void setAuthorOpenid(String authorOpenid) {
		this.authorOpenid = authorOpenid;
	}

	public String getAuthorAvatarUrl() {
		return authorAvatarUrl;
	}

	public void setAuthorAvatarUrl(String authorAvatarUrl) {
		this.authorAvatarUrl = authorAvatarUrl;
	}

	public Set<String> getLikesUsers() {
		return likesUsers;
	}

	public void setLikesUsers(Set<String> likesUsers) {
		this.likesUsers = likesUsers;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getRootPhaseId() {
		return rootPhaseId;
	}

	public void setRootPhaseId(String rootPhaseId) {
		this.rootPhaseId = rootPhaseId;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getStoryAuthorOpenid() {
		return storyAuthorOpenid;
	}

	public void setStoryAuthorOpenid(String storyAuthorOpenid) {
		this.storyAuthorOpenid = storyAuthorOpenid;
	}

	public Boolean getIsStoryNeedApproval() {
		return isStoryNeedApproval;
	}

	public void setIsStoryNeedApproval(Boolean isStoryNeedApproval) {
		this.isStoryNeedApproval = isStoryNeedApproval;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getContentLengthMax() {
		return contentLengthMax;
	}

	public void setContentLengthMax(Integer contentLengthMax) {
		this.contentLengthMax = contentLengthMax;
	}

	public Integer getContentLengthMin() {
		return contentLengthMin;
	}

	public void setContentLengthMin(Integer contentLengthMin) {
		this.contentLengthMin = contentLengthMin;
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
		return "Phase{" +
				"id='" + id + '\'' +
				", storyId='" + storyId + '\'' +
				", storyTitle='" + storyTitle + '\'' +
				", storyAuthorOpenid='" + storyAuthorOpenid + '\'' +
				", parentPhaseId='" + parentPhaseId + '\'' +
				", rootPhaseId='" + rootPhaseId + '\'' +
				", content='" + content + '\'' +
				", isStart=" + isStart +
				", isEnd=" + isEnd +
				", level=" + level +
				", likes=" + likes +
				", comments=" + comments +
				", createdDate=" + createdDate +
				", author='" + author + '\'' +
				", authorOpenid='" + authorOpenid + '\'' +
				", authorAvatarUrl='" + authorAvatarUrl + '\'' +
				", isPrivate=" + isPrivate +
				", isDeleted=" + isDeleted +
				", lastUpdatedDate=" + lastUpdatedDate +
				", branchPhases=" + branchPhases +
				", tags=" + tags +
				", reviewCount=" + reviewCount +
				", isStoryNeedApproval=" + isStoryNeedApproval +
				", approvalStatus='" + approvalStatus + '\'' +
				", contentLengthMax=" + contentLengthMax +
				", contentLengthMin=" + contentLengthMin +
				", likesUsers=" + likesUsers +
				'}';
	}

	@Override
	public int compareTo(Object o) {
		if (this == o) {
			return 0;
		} else if (o != null && o instanceof Phase) {
			Phase phase = (Phase) o;
			if (likes <= phase.likes) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return -1;
		}
	}

}
