package com.story.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.story.model.Phase;
import com.story.model.Story;
import com.story.repository.PhaseRepository;
import com.story.repository.StoryRepository;
import com.story.utils.Constant;
import com.story.utils.HttpResult;

@RequestMapping("/story")
@Controller
public class StoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
	
	@Autowired
	private PhaseRepository phaseRepository;
	@Autowired
	private StoryRepository storyRepository;

	@RequestMapping(value = "/story", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addPhase(@RequestBody Phase phaseObject, 
			@RequestParam("isNewStory") Boolean isNewStory,
			@RequestParam(value = "storyId", required = false) String storyId) {
		if (isNewStory.equals(true)) {
			Story story = new Story();
			story.setDepth(Constant.ONE);
			story.setPhaseCount(Constant.ONE);
			story.setCreatedDate(new Date().getTime());
			Story savedStory = this.storyRepository.save(story);
			if (null == savedStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + story + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			phaseObject.setIsStart(true);
			phaseObject.setLevel(Constant.ONE);
			phaseObject.setStoryId(savedStory.getId());
			phaseObject.setLike(Constant.ZERO);
			phaseObject.setDislike(Constant.ZERO);
			phaseObject.setCreatedDate(new Date().getTime());
			Phase savedPhase = this.phaseRepository.save(phaseObject);
			if (null == savedPhase) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			ArrayList<String> phaseIdList = new ArrayList<String>();
			phaseIdList.add(savedPhase.getId());
			savedStory.setPhaseIdList(phaseIdList);
			Story updatedStory = this.storyRepository.save(savedStory);
			if (null == updatedStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + savedStory + "] updated failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constant.RESULT_STORY, updatedStory);
			data.put(Constant.RESULT_PHASE, savedPhase);
			return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
					"New Story "+ updatedStory.getId() +" and new phase " + savedPhase.getId() + " saved successfully.", data).build(), HttpStatus.OK);
		} else {
			if (StringUtils.isEmpty(storyId)) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "StoryId can't be null").build(),
						HttpStatus.BAD_REQUEST);
			}
			Story foundStory = this.storyRepository.findOne(storyId);
			if (null == foundStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find story").build(),
						HttpStatus.BAD_REQUEST);
			}
			phaseObject.setCreatedDate(new Date().getTime());
			Phase savedPhase = this.phaseRepository.save(phaseObject);
			if (null == savedPhase) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			foundStory.setPhaseCount(foundStory.getPhaseCount() + Constant.ONE);
			ArrayList<String> phaseIdList = foundStory.getPhaseIdList();
			phaseIdList.add(savedPhase.getId());
			foundStory.setPhaseIdList(phaseIdList);
			foundStory.setLastUpdatedDate(new Date().getTime());
			Story savedStory = this.storyRepository.save(foundStory);
			if (null == savedStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + foundStory + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constant.RESULT_STORY, savedStory);
			data.put(Constant.RESULT_PHASE, savedPhase);
			return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
					"Story "+ savedStory.getId() +" and phase " + savedPhase.getId() + " saved successfully.", data).build(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/phase/{phaseId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> ratePhase(@PathVariable("phaseId") String phaseId, @RequestParam("like") Boolean like) {
		if (StringUtils.isEmpty(phaseId)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "phaseId can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		Phase foundPhase = this.phaseRepository.findOne(phaseId);
		if (null == foundPhase) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find phase").build(),
					HttpStatus.BAD_REQUEST);
		}
		if (like.equals(true)) {
			foundPhase.setLike(foundPhase.getLike() + Constant.ONE);
		} else {
			foundPhase.setDislike(foundPhase.getDislike() + Constant.ONE);
		}
		Phase savedPhase = this.phaseRepository.save(foundPhase);
		if (null == savedPhase) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + foundPhase + "] saved failed.").build(),
					HttpStatus.BAD_REQUEST);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.RESULT_PHASE, savedPhase);
		return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
				"Phase " + savedPhase.getId() + " updated successfully.", data).build(), HttpStatus.OK);
	}
	
}
