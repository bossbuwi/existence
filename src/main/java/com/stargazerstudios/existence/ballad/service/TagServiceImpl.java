package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import com.stargazerstudios.existence.ballad.repository.StoryDAO;
import com.stargazerstudios.existence.ballad.repository.TagDAO;
import com.stargazerstudios.existence.ballad.utils.StoryUtil;
import com.stargazerstudios.existence.ballad.utils.TagUtil;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
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
public class TagServiceImpl implements TagService{

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private StoryDAO storyDAO;

    @Autowired
    private TagUtil tagUtil;

    @Autowired
    private StoryUtil storyUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<TagDTO> getAllTags() {
        List<TagDTO> tagDTOList = new ArrayList<>();
        List<Tag> tags = tagDAO.findAll();
        if (!tags.isEmpty()) {
            for (Tag tag: tags) {
                tagDTOList.add(tagUtil.wrapTag(tag));
            }
        }
        return tagDTOList;
    }

    @Override
    public TagDTO getTag(TagWrapper tag) throws EntityNotFoundException, InvalidInputException {
        String tagName = stringUtil.checkInputTrimToUpper(tag.getName());
        if (tagName.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
        Optional<Tag> tagData = tagDAO.findByName(tagName);
        if (tagData.isPresent()) {
            TagDTO tagDTO = tagUtil.wrapTag(tagData.get());
            return tagDTO;
        } else {
            throw new EntityNotFoundException("Tag with name: " + tagName + " not found.");
        }
    }

    @Override
    public TagDTO createTag(TagWrapper wTag) throws DuplicateEntityException, InvalidInputException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isPresent()) throw new DuplicateEntityException("Tag with name: " + name + " already exists.");
        Tag tag = new Tag();
        tag.setName(name);
        return tagUtil.wrapTag(tagDAO.save(tag));
    }

    @Override
    public TagDTO updateTag(TagWrapper wTag)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String newName = stringUtil.checkInputTrimToUpper(wTag.getNew_name());
        if (newName.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("new_name");

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("Tag with name: " + name + " not found.");

        Optional<Tag> tagDataNew = tagDAO.findByName(newName);
        if (tagDataNew.isPresent()) throw new EntityNotFoundException("Tag with name: " + newName + " already exists.");

        Tag tag = tagData.get();
        tag.setName(newName);
        TagDTO tagDTO = tagUtil.wrapTag(tagDAO.save(tag));
        return tagDTO;
    }

    @Override
    public TagDTO addStories(TagWrapper wTag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String[] stories = wTag.getStories();
        if (stories == null || stories.length == 0) throw new InvalidInputException("stories");
        Set<String> storyQuery = new HashSet<>();

        for (String story : stories) {
            String item = stringUtil.checkInputTrimToUpper(story);
            if (item.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");
            storyQuery.add(item);
        }

        List<String> storyResultList = storyDAO.findStoryNames(storyQuery);
        Set<String> storyResultSet = new HashSet<>(storyResultList);

        if (!storyQuery.equals(storyResultSet)) {
            storyQuery.removeAll(storyResultSet);
            throw new InvalidCollectionException("stories");
            // TODO: This needs to quantify the invalid stories.
        }

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("Tag with name: " + name + " not found.");

        Tag tag = tagData.get();
        Set<Story> dbStories = new HashSet<>(tag.getStories());
        List<Story> storyQueryAdd = storyDAO.findStoriesByName(storyQuery);
        dbStories.addAll(storyQueryAdd);
        tag.setStories(dbStories);

        TagDTO tagDTO = tagUtil.wrapTag(tagDAO.save(tag));
        return tagDTO;
    }

    @Override
    public TagDTO removeStories(TagWrapper wTag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());
        if (name.equals(EnumUtilOutput.EMPTY.toString())) throw new InvalidInputException("name");

        String[] stories = wTag.getStories();
        if (stories == null || stories.length == 0) throw new InvalidInputException("stories");

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("Tag with name: " + name + " not found.");

        Tag tag = tagData.get();
        Set<Story> dbStories = new HashSet<>(tag.getStories());
        Set<String> dbStoryNames = storyUtil.extractStoryNamesFromArray(dbStories);

        Set<String> inputStoryNames = new HashSet<>();
        for (String story: stories) {
            inputStoryNames.add(stringUtil.checkInputTrimToUpper(story));
        }
        if (!dbStoryNames.containsAll(inputStoryNames)) throw new InvalidCollectionException("stories");

        Set<Story> newStories = new HashSet<>();
        for (String storyName: inputStoryNames) {
            Optional<Story> foundStory = dbStories.stream().filter(x -> x.getName().equals(storyName)).findAny();
            foundStory.ifPresent(newStories::add);
        }

        dbStories.removeAll(newStories);
        tag.setStories(dbStories);
        TagDTO tagDTO = tagUtil.wrapTag(tagDAO.save(tag));
        return tagDTO;
    }

    @Override
    public TagDTO deleteTag(String name) throws EntityNotFoundException {
        return null;
    }
}
