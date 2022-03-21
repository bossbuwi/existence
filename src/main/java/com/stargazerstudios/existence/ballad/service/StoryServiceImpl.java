package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import com.stargazerstudios.existence.ballad.repository.StoryDAO;
import com.stargazerstudios.existence.ballad.repository.TagDAO;
import com.stargazerstudios.existence.ballad.utils.StoryUtil;
import com.stargazerstudios.existence.ballad.utils.TagUtil;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StoryServiceImpl implements StoryService{

    // TODO: Test the CRUD methods because the saving was changed. Check if the system allows saving duplicates.
    //  It should not. If it does, modify the table of the respective entities and add a unique constraint.
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
    public StoryDTO getStory(StoryWrapper story) throws UnknownInputException, EntityErrorException {
        String storyName = stringUtil.checkInputTrimToUpper(story.getName());
        if (storyName.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");
        Optional<Story> storyData = storyDAO.findByName(storyName);
        if (storyData.isPresent()) {
            StoryDTO storyDTO = storyUtil.wrapStory(storyData.get());
            return storyDTO;
        } else {
            throw new EntityNotFoundException("story", "name", storyName);
        }
    }

    @Override
    public StoryDTO createStory(StoryWrapper wStory) throws UnknownInputException, DatabaseErrorException {
        String name = stringUtil.checkInputTrimToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");
        Story story = new Story();
        story.setName(name);

        try {
            storyDAO.save(story);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO updateStory(StoryWrapper wStory)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException{
        String name = stringUtil.checkInputTrimToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");

        String newName = stringUtil.checkInputTrimToUpper(wStory.getNew_name());
        if (newName.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("new_name");

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", name);

        Story story = storyData.get();
        story.setName(newName);

        try {
            storyDAO.save(story);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO addTags(StoryWrapper wStory)
            throws UnknownInputException, DatabaseErrorException, EntityErrorException {
        String name = stringUtil.checkInputTrimToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");

        String[] tags = wStory.getTags();
        if (tags == null || tags.length == 0) throw new InvalidInputException("tags");
        Set<String> tagQuery = new HashSet<>();

        for (String tag : tags) {
            String item = stringUtil.checkInputTrimToUpper(tag);
            if (item.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");
            tagQuery.add(item);
        }

        List<String> tagResultList = tagDAO.findTagNames(tagQuery);
        Set<String> tagResultSet = new HashSet<>(tagResultList);

        if (!tagQuery.equals(tagResultSet)) {
          tagQuery.removeAll(tagResultSet);
          throw new InvalidInputException("tags");
          // TODO: This needs to quantify the invalid tags.
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
            throw new EntitySaveErrorException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO removeTags(StoryWrapper wStory)
            throws UnknownInputException, DatabaseErrorException, EntityErrorException {
        String name = stringUtil.checkInputTrimToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");

        String[] tags = wStory.getTags();
        if (tags == null || tags.length == 0) throw new InvalidInputException("tags");

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("story", "name", name);

        Story story = storyData.get();
        Set<Tag> dbTags = new HashSet<>(story.getTags());
        Set<String> dbTagNames = tagUtil.extractTagNamesFromArray(dbTags);

        Set<String> inputTagNames = new HashSet<>();
        for (String tag: tags) {
            inputTagNames.add(stringUtil.checkInputTrimToUpper(tag));
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
            throw new EntitySaveErrorException("story");
        }

        return storyUtil.wrapStory(story);
    }

    @Override
    public StoryDTO deleteStory(StoryWrapper wStory)
            throws UnknownInputException, DatabaseErrorException, EntityErrorException{
        String storyName = stringUtil.checkInputTrimToUpper(wStory.getName());
        if (storyName.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("name");
        Optional<Story> storyData = storyDAO.findByName(storyName);
        if (storyData.isPresent()) {
            Story story = storyData.get();

            try {
                storyDAO.delete(story);
            } catch (Exception e) {
                e.printStackTrace();
                throw new EntityDeletionErrorException("story");
            }

            return storyUtil.wrapStory(story);
        } else {
            throw new EntityNotFoundException("story", "name", storyName);
        }
    }
}
