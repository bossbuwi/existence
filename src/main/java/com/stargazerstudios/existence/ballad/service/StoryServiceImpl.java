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
import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidCollectionException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
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
    public StoryDTO getStory(StoryWrapper story) throws EntityNotFoundException, InvalidInputException {
        String storyName = stringUtil.checkInputToUpper(story.getName());
        if (storyName.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
        Optional<Story> storyData = storyDAO.findByName(storyName);
        if (storyData.isPresent()) {
            StoryDTO storyDTO = storyUtil.wrapStory(storyData.get());
            return storyDTO;
        } else {
            throw new EntityNotFoundException("Story with name: " + storyName + " not found.");
        }
    }

    @Override
    public StoryDTO createStory(StoryWrapper wStory) throws DuplicateEntityException, InvalidInputException {
        String name = stringUtil.checkInputToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isPresent()) throw new DuplicateEntityException("Story with name: " + name + " already exists.");
        Story story = new Story();
        story.setName(name);
        return storyUtil.wrapStory(storyDAO.save(story));
    }

    @Override
    public StoryDTO updateStory(StoryWrapper wStory)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        String name = stringUtil.checkInputToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String newName = stringUtil.checkInputToUpper(wStory.getNew_name());
        if (newName.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("new_name");

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("Story with name: " + name + " not found.");

        Optional<Story> storyDataNew = storyDAO.findByName(newName);
        if (storyDataNew.isPresent()) throw new DuplicateEntityException("Story with name: " + newName + " already exists.");

        Story story = storyData.get();
        story.setName(newName);
        StoryDTO storyDTO = storyUtil.wrapStory(storyDAO.save(story));
        return storyDTO;
    }

    @Override
    public StoryDTO addTags(StoryWrapper wStory)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        String name = stringUtil.checkInputToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String[] tags = wStory.getTags();
        if (tags == null || tags.length == 0) throw new InvalidInputException("tags");
        Set<String> tagQuery = new HashSet<>();

        for (String tag : tags) {
            String item = stringUtil.checkInputToUpper(tag);
            if (item.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
            tagQuery.add(item);
        }

        List<String> tagResultList = tagDAO.findTagNames(tagQuery);
        Set<String> tagResultSet = new HashSet<>(tagResultList);

        if (!tagQuery.equals(tagResultSet)) {
          tagQuery.removeAll(tagResultSet);
          throw new InvalidCollectionException("tags");
          // TODO: This needs to quantify the invalid tags.
        }

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("Story with name: " + name + " not found.");

        Story story = storyData.get();
        Set<Tag> dbTags = new HashSet<>(story.getTags());
        List<Tag> tagQueryAdd = tagDAO.findTagsByName(tagQuery);
        dbTags.addAll(tagQueryAdd);
        story.setTags(dbTags);

        StoryDTO storyDTO = storyUtil.wrapStory(storyDAO.save(story));
        return storyDTO;
    }

    @Override
    public StoryDTO removeTags(StoryWrapper wStory)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        String name = stringUtil.checkInputToUpper(wStory.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String[] tags = wStory.getTags();
        if (tags == null || tags.length == 0) throw new InvalidInputException("tags");

        Optional<Story> storyData = storyDAO.findByName(name);
        if (storyData.isEmpty()) throw new EntityNotFoundException("Story with name: " + name + " not found.");

        Story story = storyData.get();
        Set<Tag> dbTags = new HashSet<>(story.getTags());
        Set<String> dbTagNames = tagUtil.extractTagNamesFromArray(dbTags);

        Set<String> inputTagNames = new HashSet<>();
        for (String tag: tags) {
            inputTagNames.add(stringUtil.checkInputToUpper(tag));
        }
        if (!dbTagNames.containsAll(inputTagNames)) throw new InvalidCollectionException("tags");

        Set<Tag> newTags = new HashSet<>();
        for (String tagName: inputTagNames) {
            Optional<Tag> foundTag = dbTags.stream().filter(x -> x.getName().equals(tagName)).findAny();
            foundTag.ifPresent(newTags::add);
        }

        dbTags.removeAll(newTags);
        story.setTags(dbTags);
        StoryDTO storyDTO = storyUtil.wrapStory(storyDAO.save(story));
        return storyDTO;
    }

    @Override
    public StoryDTO deleteStory(String name) throws EntityNotFoundException {
        return null;
    }
}
