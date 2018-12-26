package com.story.controller;

import com.story.model.*;
import com.story.repository.*;
import com.story.services.NotificationService;
import com.story.utils.Constant;
import com.story.utils.HttpResult;
import com.story.utils.MongoUtils;
import com.story.utils.PhaseComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/story")
@Controller
public class StoryController {

    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);

    @Autowired
    private PhaseRepository phaseRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private PhaseComparator phaseComparator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    ArrayList<Phase> phases = new ArrayList<Phase>();

    /**
     * create a new story line or a new phase
     *
     * @param phaseObject
     * @param isNewStory
     * @param storyId
     * @param parentPhaseId
     * @return
     */
    @RequestMapping(value = "/story", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<Map<String, Object>> addPhase(@RequestBody Phase phaseObject,
                                                 @RequestParam("isNewStory") Boolean isNewStory,
                                                 @RequestParam(value = "storyId", required = false) String storyId,
                                                 @RequestParam(value = "parentPhaseId", required = false) String parentPhaseId,
                                                 @RequestParam(value = "rootPhaseId", required = false) String rootPhaseId,
                                                 @RequestParam(value = "needApproval", required = false) Boolean needApproval,
                                                 @RequestParam(value = "isPublic", required = false) Boolean isPublic,
                                                 @RequestParam(value = "openid", required = true) String openid) {
        User requestor = this.userRepository.findByOpenid(openid);
        long date = new Date().getTime();
        if (isNewStory.equals(true)) {
            Story story = new Story();
            story.setTitle(phaseObject.getStoryTitle());
            story.setDepth(Constant.ONE);
            story.setPhaseCount(Constant.ONE);
            story.setCreatedDate(date);
            story.setLastUpdatedDate(date);
            story.setNeedApproval(needApproval);
            story.setIsPublic(isPublic);
            story.setCreatorOpenid(openid);
            Set<String> authorSet = story.getAuthorSet();
            if (null == authorSet) {
                authorSet = new HashSet<String>();
            }
            authorSet.add(openid);
            story.setAuthorSet(authorSet);
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
            phaseObject.setLikes(Constant.ZERO);
            phaseObject.setCreatedDate(date);
            phaseObject.setLastUpdatedDate(date);
            phaseObject.setIsStart(true);
            phaseObject.setIsEnd(true);
            phaseObject.setIsStoryNeedApproval(needApproval);
            phaseObject.setAuthor(requestor.getNickName());
            phaseObject.setAuthorAvatarUrl(requestor.getAvatarUrl());
            phaseObject.setAuthorOpenid(openid);
            phaseObject.setStoryAuthorOpenid(savedStory.getCreatorOpenid());
            // if the story need approval, then this first phase should be approved already by author himself,
            // or this should be PENDING_APPROVAL by default
            phaseObject.setApprovalStatus(needApproval ? Constant.APPROVAL_STATUS_APPROVED : Constant.APPROVAL_STATUS_NO_NEED_APPROVAL);
            Phase savedPhase = this.phaseRepository.save(phaseObject);
            if (null == savedPhase) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.")
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }
            logger.debug("Save new phase: [" + savedPhase + "] successfully.");
            ArrayList<String> phaseIdList = new ArrayList<String>();
            phaseIdList.add(savedPhase.getId());
            savedStory.setPhaseIdList(phaseIdList);
            Story updatedStory = this.storyRepository.save(savedStory);
            if (null == updatedStory) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + savedStory + "] updated failed.")
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }
            logger.debug(
                    "Add phase [" + savedPhase.getId() + "] to story: [" + updatedStory.getId() + "] successfully.");
            Set<String> tags = phaseObject.getTags();
            List<Tag> allTags = this.tagRepository.findAll();
            HashSet<String> tagValueSet = new HashSet<String>();
            for (Tag tag : allTags) {
                tagValueSet.add(tag.getValue());
            }
            for (String tagValue : tags) {
                if (!tagValueSet.contains(tagValue)) {
                    Tag newTag = new Tag(tagValue, 1, "yellow");
                    Tag insertedTag = this.tagRepository.insert(newTag);
                    if (null != insertedTag)
                        logger.debug("Add new tag: [" + insertedTag.getValue() + "] successfully.");
                } else {
                    Tag existedTag = this.tagRepository.findByValue(tagValue);
                    Integer count = existedTag.getCount();
                    existedTag.setCount(++count);
                    Tag updatedTag = this.tagRepository.save(existedTag);
                    if (null != updatedTag)
                        logger.debug("Update existed tag: [" + updatedTag.getValue() + "] count to ["
                                + updatedTag.getCount() + "] successfully.");
                }
            }

            // create notification message
//			this.messageService.notify(Constant.MESSAGE_TYPE_MULTIPLE, null, null, null, null, null, null);
            this.notificationService.createNotificatoin(Constant.MESSAGE_TYPE_MULTIPLE, null, false, savedPhase.getStoryTitle());

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(Constant.RESULT_STORY, updatedStory);
            data.put(Constant.RESULT_PHASE, savedPhase);
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_SUCCESS, "New Story " + updatedStory.getId()
                            + " and new phase " + savedPhase.getId() + " saved successfully.", data).build(),
                    HttpStatus.OK);
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
            phaseObject.setRootPhaseId(rootPhaseId);
            phaseObject.setAuthor(requestor.getNickName());
            phaseObject.setAuthorAvatarUrl(requestor.getAvatarUrl());
            phaseObject.setAuthorOpenid(openid);
            phaseObject.setIsStoryNeedApproval(needApproval);
            Phase foundParentPhase = this.phaseRepository.findOne(parentPhaseId);
            if (null == foundParentPhase) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE,
                                "Can't find parent phase for: [" + parentPhaseId + "]").build(),
                        HttpStatus.BAD_REQUEST);
            }
            if (foundParentPhase.getIsEnd().equals(true)) {
                phaseObject.setLevel(foundParentPhase.getLevel() + Constant.ONE);
                phaseObject.setIsEnd(true);
                // update parent phase isEnd to false
                foundParentPhase.setIsEnd(false);
                Phase updatedParentPhase = this.phaseRepository.save(foundParentPhase);
                if (null == updatedParentPhase) {
                    return new ResponseEntity<Map<String, Object>>(
                            new HttpResult(Constant.RESULT_STATUS_FAILURE,
                                    "Parent phase [" + foundParentPhase + "] updated failed.").build(),
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                phaseObject.setIsEnd(false);
            }
            Story foundStory = this.storyRepository.findOne(storyId);
            if (null == foundStory) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find story").build(),
                        HttpStatus.BAD_REQUEST);
            }
            phaseObject.setStoryTitle(foundStory.getTitle());
            phaseObject.setStoryAuthorOpenid(foundStory.getCreatorOpenid());

            // if the story author is himself, then the phase approved status should be APPROVED
            if (needApproval) {
                if (foundStory.getCreatorOpenid().equals(openid)) {
                    phaseObject.setApprovalStatus(Constant.APPROVAL_STATUS_APPROVED);
                } else {
                    phaseObject.setApprovalStatus(Constant.APPROVAL_STATUS_PENDING_APPROVAL);
                }
            } else {
                phaseObject.setApprovalStatus(Constant.APPROVAL_STATUS_NO_NEED_APPROVAL);
            }
//            phaseObject.setApprovalStatus((foundStory.getCreatorOpenid().equals(openid) && needApproval) ? Constant.APPROVAL_STATUS_APPROVED : Constant.APPROVAL_STATUS_REJECTED);
            Phase savedPhase = this.phaseRepository.save(phaseObject);
            if (null == savedPhase) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase [" + phaseObject + "] saved failed.")
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }
            logger.debug("Save new phase: [" + savedPhase + "] successfully.");
            ArrayList<String> branchPhases = this.phaseRepository.findOne(parentPhaseId).getBranchPhases();
            if (null == branchPhases) {
                branchPhases = new ArrayList<String>();
            }
            branchPhases.add(savedPhase.getId());
            foundParentPhase.setBranchPhases(branchPhases);
            Phase updatedParentPhase = this.phaseRepository.save(foundParentPhase);
            if (null == updatedParentPhase) {
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_FAILURE,
                                "Parent phase [" + foundParentPhase + "] updated failed.").build(),
                        HttpStatus.BAD_REQUEST);
            }
            logger.debug("Update phase into parent phase [" + updatedParentPhase.getId() + "] successfully.");
            // update rootPhaseObject lastUpdatedDate
            Phase rootPhase = this.phaseRepository.findOne(rootPhaseId);
            if (null != rootPhase) {
                rootPhase.setLastUpdatedDate(date);
                Phase savedRootPhase = this.phaseRepository.save(rootPhase);
                if (null != savedRootPhase)
                    logger.debug("Update root phase " + date + " lastUpdatedDate successfully");
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
                        new HttpResult(Constant.RESULT_STATUS_FAILURE, "Story [" + foundStory + "] saved failed.")
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }
            logger.debug("Add phase [" + savedPhase.getId() + "] to story: [" + savedStory.getId() + "] successfully.");

            // create notification
            // create notification message
            /*this.messageService.notify(Constant.MESSAGE_TYPE_SINGLE, requestor.getNickName(),
                    savedPhase.getStoryTitle(), savedPhase.getStoryId(), savedPhase.getParentPhaseId(),
					rootPhase.getAuthorOpenid(), updatedParentPhase.getAuthorOpenid());*/
            // need 2 notifications, one for story author, one for parent phase author

            // the first continue for the story, then only notify story author
            if (!rootPhase.getAuthorOpenid().equals(openid)) // don't send to myself if the story author is myself
                this.notificationService.createNotificatoin(Constant.MESSAGE_TYPE_SINGLE, rootPhase.getAuthorOpenid(), true, savedPhase.getStoryTitle());
            if (!updatedParentPhase.getAuthorOpenid().equals(openid)) {
                // need notify parent phase author
                if (updatedParentPhase.getAuthorOpenid() != openid)
                    ; // don't send to myself if the parent phase author is myself
                this.notificationService.createNotificatoin(Constant.MESSAGE_TYPE_SINGLE, updatedParentPhase.getAuthorOpenid(), false, savedPhase.getStoryTitle());
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(Constant.RESULT_STORY, savedStory);
            data.put(Constant.RESULT_PHASE, savedPhase);
            return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                    "Story " + savedStory.getId() + " and phase " + savedPhase.getId() + " saved successfully.", data)
                    .build(),
                    HttpStatus.OK);
        }
    }

    /**
     * update like or dislike of phase
     *
     * @param phaseId
     * @param like
     * @return
     */
    @RequestMapping(value = "/ratePhase", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> ratePhase(@RequestParam("phaseId") String phaseId,
                                                         @RequestParam("like") Boolean like, @RequestParam("openid") String openid) {
        if (StringUtils.isEmpty(phaseId)) {
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_FAILURE, "phaseId can't be null").build(),
                    HttpStatus.BAD_REQUEST);
        }
        logger.debug("phaseId: [" + phaseId + "], like: [" + like + "]");
        Phase foundPhase = this.phaseRepository.findOne(phaseId);
        if (null == foundPhase) {
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_FAILURE, "Can't find phase").build(), HttpStatus.BAD_REQUEST);
        }
        if (like.equals(true)) {
            foundPhase.setLikes(null == foundPhase.getLikes() ? Constant.ONE : (foundPhase.getLikes() + Constant.ONE));
        } else {
            if (!foundPhase.getLikes().equals(0)) {
                foundPhase.setLikes(foundPhase.getLikes() - 1);
            }
        }
        Set<String> likesUsers = foundPhase.getLikesUsers();
        if (null == likesUsers)
            likesUsers = new HashSet<String>();
        if (like)
            likesUsers.add(openid);
        else
            likesUsers.remove(openid);
        foundPhase.setLikesUsers(likesUsers);
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

    /**
     * find by story id and parent phase id.
     *
     * @param storyId
     * @param parentPhaseId
     * @return
     */
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
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Found " + foundPhases.size() + " phases for story id: ["
                        + storyId + "] and parent phase id: [" + parentPhaseId + "]", data).build(),
                HttpStatus.OK);

    }

    @RequestMapping(value = "/parentLine", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findParentLine(@RequestParam("parentPhaseId") String parentPhaseId) {
        LinkedList<Phase> list = new LinkedList<>();
        Phase parentPhase = this.phaseRepository.findById(parentPhaseId);
        while (null != parentPhase) {
            list.addFirst(parentPhase);
            if (null != parentPhase.getParentPhaseId()) {
                parentPhase = this.phaseRepository.findById(parentPhase.getParentPhaseId());
            } else {
                break;
            }
        }
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Found paren", list).build(),
                HttpStatus.OK);

    }

    /**
     * find story list ( the phases without parentPhaseId )
     *
     * @return
     */
    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findStoryList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", required = false) String sort) {
        Page<Phase> phasePage = this.phaseRepository.findByParentPhaseIdIgnoreIsStoryNeedApproval(null,
                new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
        List<Phase> topPhases = phasePage.getContent();
		/*
		 * this.phaseComparator.setField("lastUpdatedDate");
		 * Collections.sort(topPhases, this.phaseComparator);
		 */
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                        "Found " + topPhases.size() + " phases without parentPhaseId", topPhases).build(),
                HttpStatus.OK);
    }

    /**
     * find story list filtered with tags( the phases without parentPhaseId )
     *
     * @return
     */
    @RequestMapping(value = "/story/filtered", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findStoryListFilteredWithTags(
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        if (null == tags) {
            tags = new ArrayList<String>();
        }
        Page<Phase> phasePage = this.phaseRepository.findByParentPhaseIdAndTags(null, tags,
                new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
        List<Phase> topPhases = phasePage.getContent();
		/*
		 * this.phaseComparator.setField("lastUpdatedDate");
		 * Collections.sort(topPhases, this.phaseComparator);
		 */
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                        "Found " + topPhases.size() + " phases without parentPhaseId", topPhases).build(),
                HttpStatus.OK);
    }

    /**
     * find story list for single user ( the phases without parentPhaseId )
     *
     * @return
     */
    @RequestMapping(value = "/story/withUser", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findUserStoryList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "openid", required = true) String openid) {
        Page<Phase> phasePage = this.phaseRepository.findByParentPhaseIdAndOpenid(null, openid,
                new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
        List<Phase> topPhases = phasePage.getContent();
        return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                "Found " + topPhases.size() + " phases without parentPhaseId and with openid", topPhases).build(),
                HttpStatus.OK);
    }

    /**
     * find story list for user continued ( the phases without parentPhaseId )
     *
     * @return
     */
    @RequestMapping(value = "/story/withUser/continues", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findUserStoryListContinued(
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "openid", required = true) String openid) {
        Page<Phase> phasePage = this.phaseRepository.findByParentPhaseIdNotNullAndOpenid(null, openid,
                new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
        List<Phase> topPhases = phasePage.getContent();
        return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                "Found " + topPhases.size() + " phases without parentPhaseId and with openid", topPhases).build(),
                HttpStatus.OK);
    }

    /**
     * traverse find the phase by parent phase id
     *
     * @param parentPhaseId
     * @return
     */
    @RequestMapping(value = "/story/phases/{parentPhaseId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findStoryLineByPhaseId(
            @PathVariable("parentPhaseId") String parentPhaseId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "childPhasesPageSize", required = false, defaultValue = "15") int childPhasePageSize,
            @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "init") Boolean init,
            @RequestParam(value = "needIncReviewCount") Boolean needIncReviewCount) {
        if (StringUtils.isEmpty(parentPhaseId)) {
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_FAILURE, "Head phase id can't be null").build(),
                    HttpStatus.BAD_REQUEST);
        }
        // phases.clear();
        Stack<Phase> stack = new Stack<Phase>();
        Phase parentPhase = this.phaseRepository.findOne(parentPhaseId);
        // update reviewCount
        Integer reviewCount = parentPhase.getReviewCount();
        parentPhase.setReviewCount(null == parentPhase.getReviewCount() ? 1 : ++reviewCount);
        this.phaseRepository.save(parentPhase);
        if (init)
            stack.push(parentPhase);

        // phases.add(foundParentPhase);
        while (null != parentPhase.getBranchPhases() && parentPhase.getBranchPhases().size() > 0) {
            Page<Phase> branchPhasePage = this.phaseRepository.findByParentPhaseId(parentPhase.getId(),
                    new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
            List<Phase> branchPhases = branchPhasePage.getContent();
            if (!branchPhases.isEmpty() && branchPhases.size() > 0) {
                Phase childPhase = branchPhases.get(0);
                stack.push(childPhase);
                parentPhase = childPhase;
                if (stack.size() >= childPhasePageSize)
                    break;
            } else {
                break;
            }
        }

        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                        "Found " + stack.size() + " phases for parentPhaseId [" + parentPhaseId + "]", stack).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/phase/{phaseId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findPhaseById(@PathVariable("phaseId") String phaseId) {
        if (StringUtils.isEmpty(phaseId)) {
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_FAILURE, "Phase id can't be null").build(),
                    HttpStatus.BAD_REQUEST);
        }
        Phase foundPhase = this.phaseRepository.findOne(phaseId);
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Found " + phases.size() + " phases.", foundPhase)
                        .build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/branchPhases", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findBranchPhases(@RequestParam("parentPhaseId") String parentPhaseId,
                                                                // @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
                                                                // @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                                @RequestParam(value = "sort", required = false) String sort) {
        if (StringUtils.isEmpty(parentPhaseId)) {
            return new ResponseEntity<Map<String, Object>>(
                    new HttpResult(Constant.RESULT_STATUS_FAILURE, "Parent PhaseId id can't be null").build(),
                    HttpStatus.BAD_REQUEST);
        }
        Phase foundParentPhase = this.phaseRepository.findOne(parentPhaseId);
        if (null != foundParentPhase) {
            ArrayList<String> branchPhaseIds = foundParentPhase.getBranchPhases();
            if (null != branchPhaseIds && !branchPhaseIds.isEmpty()) {
                List<Phase> branchPhases = this.phaseRepository.findByIdIn(branchPhaseIds);
                return new ResponseEntity<Map<String, Object>>(
                        new HttpResult(Constant.RESULT_STATUS_SUCCESS,
                                "Found " + branchPhases.size() + " branch phases.", branchPhases).build(),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<Map<String, Object>>(new HttpResult(Constant.RESULT_STATUS_FAILURE, "Error").build(),
                HttpStatus.OK);

    }

    @RequestMapping(value = "/createTag", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> createTag(@RequestParam("tagValue") String tagValue,
                                                         @RequestParam("tagColor") String tagColor) {
        Tag tag = new Tag(tagValue, 0);
        tag.setColor(tagColor);
        this.tagRepository.insert(tag);
        List<Tag> tags = this.tagRepository.findAll();
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Create new tag successfully", tags).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/loadTags", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> loadTags() {
        List<Tag> tags = this.tagRepository.findAll();
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Found " + tags.size() + " tags", tags).build(),
                HttpStatus.OK);
    }

	/*@RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
	public void sendMessage() {
		try {
			Message m = new Message();
			m.setType(Constant.MESSAGE_TYPE_MULTIPLE);
			m.setPhaseId("xx");
			m.setStoryTitle("test");
			this.socketHandker.sendMessage(m, "oVlMF0R0juHxAmEBYoAOsbLF-yV8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public void allMsgs() {
        List<Message> all = this.messageRepository.findByStoryAuthorOpenid("oVlMF0Ubo71bFpuLDJEvActM3W68");
        System.out.println("aaa");
    }

    @RequestMapping(value = "/consumeNotification", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> consumeNotification(@RequestBody Notification nfc) {
        logger.info("Consuming Notification: [" + nfc.getId() + "]");
        nfc.setConsumed(true);
        this.notificationRepository.save(nfc);
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Consume Notifications", null).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/approvalList", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findApprovalList(@RequestParam("storyAuthorOpenid") String storyAuthorOpenid,
                                                                @RequestParam("isStoryNeedApproval") Boolean isStoryNeedApproval,
                                                                @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
                                                                @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                                @RequestParam(value = "sort", required = false) String sort) {
        Page<Phase> foundPhases = this.phaseRepository.findByStoryAuthorOpenidAndNeedApproval(storyAuthorOpenid, isStoryNeedApproval,
                new PageRequest(pageNumber, pageSize, MongoUtils.buildSort(sort)));
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Consume Notifications", foundPhases.getContent()).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> approvePhase(@RequestParam("phaseId") String phaseId, @RequestParam("approved") Boolean approved) {
        Phase foundPhase = this.phaseRepository.findById(phaseId);
        if (approved) {
            foundPhase.setApprovalStatus(Constant.APPROVAL_STATUS_APPROVED);
        } else {
            foundPhase.setApprovalStatus(Constant.APPROVAL_STATUS_REJECTED);
        }
        Phase updatedPhase = this.phaseRepository.save(foundPhase);
        return new ResponseEntity<Map<String, Object>>(
                new HttpResult(Constant.RESULT_STATUS_SUCCESS, "Phase has been [" + (approved ? "Approved!" : "Rejected!"), updatedPhase).build(),
                HttpStatus.OK);

    }

}
