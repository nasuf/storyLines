package com.story.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
			@RequestParam(value = "storyId", required = false) String storyId,
			@RequestParam(value = "parentPhaseId", required = false) String parentPhaseId) {
		if (isNewStory.equals(true)) {
			Story story = new Story();
			story.setDepth(Constant.ONE);
			story.setPhaseCount(Constant.ONE);
			story.setCreatedDate(new Date().getTime());
			story.setLastUpdatedDate(new Date().getTime());
			Story savedStory = this.storyRepository.save(story);
			if (null == savedStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + story + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			logger.debug("Save new story: [" + savedStory + "] successfully.");
			phaseObject.setIsStart(true);
			phaseObject.setLevel(Constant.ONE);
			phaseObject.setStoryId(savedStory.getId());
			phaseObject.setLike(Constant.ZERO);
			phaseObject.setDislike(Constant.ZERO);
			phaseObject.setCreatedDate(new Date().getTime());
			phaseObject.setIsStart(true);
			phaseObject.setIsEnd(true);
			Phase savedPhase = this.phaseRepository.save(phaseObject);
			if (null == savedPhase) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			logger.debug("Save new phase: [" + savedPhase + "] successfully.");
			ArrayList<String> phaseIdList = new ArrayList<String>();
			phaseIdList.add(savedPhase.getId());
			savedStory.setPhaseIdList(phaseIdList);
			Story updatedStory = this.storyRepository.save(savedStory);
			if (null == updatedStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + savedStory + "] updated failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			logger.debug("Add phase [" + savedPhase.getId() + "] to story: [" + updatedStory.getId() + "] successfully.");
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
			phaseObject.setStoryId(storyId);
			phaseObject.setCreatedDate(new Date().getTime());
			phaseObject.setIsStart(false);
			phaseObject.setParentPhaseId(parentPhaseId);
			Phase foundParentPhase = this.phaseRepository.findOne(parentPhaseId);
			if (null == foundParentPhase) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find parent phase for: [" + parentPhaseId + "]").build(),
						HttpStatus.BAD_REQUEST);
			}
			if (foundParentPhase.getIsEnd().equals(true)) {
				phaseObject.setIsEnd(true);
				// update parent phase isEnd to false
				foundParentPhase.setIsEnd(false);
				Phase updatedParentPhase = this.phaseRepository.save(foundParentPhase);
				if (null == updatedParentPhase) {
					return new ResponseEntity<Map<String, Object>>(
							new HttpResult(Constant.RESULT_STATUS_FAILURE, "Parent phase [" + foundParentPhase + "] updated failed.").build(),
							HttpStatus.BAD_REQUEST);
				}
			} else {
				phaseObject.setIsEnd(false);
			}
			Phase savedPhase = this.phaseRepository.save(phaseObject);
			if (null == savedPhase) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.").build(),
						HttpStatus.BAD_REQUEST);
			}
			logger.debug("Save new phase: [" + savedPhase + "] successfully.");
			
			Story foundStory = this.storyRepository.findOne(storyId);
			if (null == foundStory) {
				return new ResponseEntity<Map<String, Object>>(
						new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find story").build(),
						HttpStatus.BAD_REQUEST);
			}
			if (savedPhase.getIsEnd().equals(true)) {
				foundStory.setDepth(foundStory.getDepth() + Constant.ONE);
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
			logger.debug("Add phase [" + savedPhase.getId() + "] to story: [" + savedStory.getId() + "] successfully.");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(Constant.RESULT_STORY, savedStory);
			data.put(Constant.RESULT_PHASE, savedPhase);
			return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
					"Story "+ savedStory.getId() +" and phase " + savedPhase.getId() + " saved successfully.", data).build(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/phase/{phaseId}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> ratePhase(@PathVariable("phaseId") String phaseId, @RequestParam("like") Boolean like) {
		if (StringUtils.isEmpty(phaseId)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "phaseId can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		logger.debug("phaseId: [" + phaseId + "], like: [" + like + "]");
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
		logger.debug("Update phase: [" + savedPhase.getId() + "] successfully");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.RESULT_PHASE, savedPhase);
		return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
				"Phase " + savedPhase.getId() + " updated successfully.", data).build(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/phase/{phaseLevel}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findPhases(@PathVariable("phaseLevel") Integer phaseLevel, 
			@RequestParam("storyId") String storyId) {
		if (StringUtils.isEmpty(phaseLevel)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase level can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(storyId)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story id can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		List<Phase> foundPhases = this.phaseRepository.findByStoryIdAndLevel(storyId, phaseLevel);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.RESULT_DATA, foundPhases);
		return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
				"Found " + foundPhases.size() + " phases for story id: [" + storyId + "] and phase level: [" + phaseLevel + "]", data).build(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{storyId}/phase/{parentPhaseId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findPhases(@PathVariable("storyId") String storyId, 
			@PathVariable("parentPhaseId") String parentPhaseId) {
		if (StringUtils.isEmpty(storyId)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story id can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(parentPhaseId)) {
			return new ResponseEntity<Map<String, Object>>(
					new HttpResult(Constant.RESULT_STATUS_FAILURE, "Parent phase id can't be null").build(),
					HttpStatus.BAD_REQUEST);
		}
		List<Phase> foundPhases = this.phaseRepository.findByStoryIdAndParentPhaseId(storyId, parentPhaseId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.RESULT_DATA, foundPhases);
		return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
				"Found " + foundPhases.size() + " phases for story id: [" + storyId + "] and parent phase id: [" + parentPhaseId + "]", 
				data).build(), HttpStatus.OK);
		
	}
	
}
