package com.story.utils;

import java.util.Comparator;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.story.model.Phase;
import com.story.model.Story;
import com.story.repository.StoryRepository;

@Service
public class PhaseComparator implements Comparator<Phase>{
	
	private String field;
	
	@Resource
	private StoryRepository storyRepository;
	
	
	public PhaseComparator() {
		super();
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	@Override
	public int compare(Phase phase_1, Phase phase_2) {
		if (this.field.equals("createdDate")) {
			return phase_1.getCreatedDate() < phase_2.getCreatedDate() ? -1 : 1;
		} else {
			Story foundStory_1 = this.storyRepository.findOne(phase_1.getStoryId());
			Story foundStory_2 = this.storyRepository.findOne(phase_2.getStoryId());
			return foundStory_1.getLastUpdatedDate() > foundStory_2.getLastUpdatedDate() ? -1 : 1;
		}
	}

}
