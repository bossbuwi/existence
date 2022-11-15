package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.constants.ConsBalladConstraint;
import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import com.stargazerstudios.existence.ballad.repository.StoryDAO;
import com.stargazerstudios.existence.ballad.repository.TagDAO;
import com.stargazerstudios.existence.ballad.utils.StoryUtil;
import com.stargazerstudios.existence.ballad.utils.TagUtil;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class StoryServiceImpl implements StoryService{

    @Autowired
    private StoryDAO storyDAO;

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private StoryUtil storyUtil;

    @Autowired
    private TagUtil tagUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<StoryDTO> getAllStories() {
        List<StoryDTO> storyDTOList = new ArrayList<>();
        List<Story> stories = storyDAO.findAll();
        if (!stories.isEmpty()) {
            for (Story story: stories) {
                storyDTOList.add(storyUtil.wrapStory(story));
            }
        }
        return storyDTOList;
    }

    @Override
    public StoryDTO getStory(StoryWrapper story)
            throws UnknownInputException, EntityException {
        String storyName = story.getName();
        Optional<Story> storyData = storyDAO.findByName(storyName);
        if (storyData.isPresent()) {
            return storyUtil.wrapStory(storyData.get());
        } else {
            throw new EntityNotFoundException("story", "name", storyName);
        }
    }

    @Override
    public StoryDTO createStory(StoryWrapper wStory)
            throws UnknownInputException, DatabaseException {
        String name = stringUtil.trimToUpper(wStory.getName());
        Story story = new Story();
        story.setName(name);

        try {
            storyDAO.save(story);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsBalladConstraint.UNIQUE_STORY_NAME))
                throw new DuplicateEntityException("story", "name", name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO updateStory(StoryWrapper wStory)
            throws UnknownInputException, EntityException, DatabaseException {
        String name = stringUtil.trimToUpper(wStory.getName());
        String newName = stringUtil.trimToUpper(wStory.getNew_name());

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", name);

        Story story = storyData.get();
        story.setName(newName);

        try {
            storyDAO.save(story);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsBalladConstraint.UNIQUE_STORY_NAME))
                throw new DuplicateEntityException("story", "name", name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO addTags(StoryWrapper wStory)
            throws UnknownInputException, DatabaseException, EntityException {
        String name = stringUtil.trimToUpper(wStory.getName());
        String[] tags = wStory.getTags();
        Set<String> tagQuery = new HashSet<>();

        for (String tag : tags) {
            String item = stringUtil.trimToUpper(tag);
            if (item.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("tags");
            tagQuery.add(item);
        }

        List<String> tagResultList = tagDAO.findTagNames(tagQuery);
        Set<String> tagResultSet = new HashSet<>(tagResultList);

        if (!tagQuery.equals(tagResultSet)) {
          tagQuery.removeAll(tagResultSet);
          throw new InvalidInputException("tags");
        }

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", name);

        Story story = storyData.get();
        Set<Tag> dbTags = new HashSet<>(story.getTags());
        List<Tag> tagQueryAdd = tagDAO.findTagsByName(tagQuery);
        dbTags.addAll(tagQueryAdd);
        story.setTags(dbTags);

        try {
            storyDAO.save(story);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO removeTags(StoryWrapper wStory)
            throws UnknownInputException, DatabaseException, EntityException {
        String name = stringUtil.trimToUpper(wStory.getName());
        String[] tags = wStory.getTags();

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", name);

        Story story = storyData.get();
        Set<Tag> dbTags = new HashSet<>(story.getTags());
        Set<String> dbTagNames = tagUtil.extractTagNamesFromArray(dbTags);

        Set<String> inputTagNames = new HashSet<>();
        for (String tag: tags) {
            inputTagNames.add(stringUtil.trimToUpper(tag));
        }
        if (!dbTagNames.containsAll(inputTagNames)) throw new InvalidInputException("tags");

        Set<Tag> newTags = new HashSet<>();
        for (String tagName: inputTagNames) {
            Optional<Tag> foundTag = dbTags.stream().filter(x -> x.getName().equals(tagName)).findAny();
            foundTag.ifPresent(newTags::add);
        }

        dbTags.removeAll(newTags);
        story.setTags(dbTags);

        try {
            storyDAO.save(story);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO deleteStory(StoryWrapper wStory)
            throws UnknownInputException, DatabaseException, EntityException {
        String storyName = stringUtil.trimToUpper(wStory.getName());
        Optional<Story> storyData = storyDAO.findByName(storyName);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", storyName);
        
        Story story = storyData.get();

        try {
            storyDAO.delete(story);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityDeletionException("story");
        }
        
        return storyUtil.wrapStory(story);
    }
}
