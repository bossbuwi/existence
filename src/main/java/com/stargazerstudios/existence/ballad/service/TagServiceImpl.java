package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.constants.ConsBalladConstraint;
import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import com.stargazerstudios.existence.ballad.repository.StoryDAO;
import com.stargazerstudios.existence.ballad.repository.TagDAO;
import com.stargazerstudios.existence.ballad.utils.StoryUtil;
import com.stargazerstudios.existence.ballad.utils.TagUtil;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
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
    public TagDTO getTag(TagWrapper tag)
            throws UnknownInputException, EntityErrorException {
        String tagName = stringUtil.checkInputTrimToUpper(tag.getName());
        Optional<Tag> tagData = tagDAO.findByName(tagName);
        if (tagData.isPresent()) {
            return tagUtil.wrapTag(tagData.get());
        } else {
            throw new EntityNotFoundException("tag", "name", tagName);
        }
    }

    @Override
    public TagDTO createTag(TagWrapper wTag) throws DatabaseErrorException, UnknownInputException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());

        Tag tag = new Tag();
        tag.setName(name);

        try {
            tagDAO.save(tag);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsBalladConstraint.UNIQUE_TAG_NAME))
                throw new DuplicateEntityException("tag", "name", name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("tag");
        }

        return tagUtil.wrapTag(tag);
    }

    @Override
    public TagDTO updateTag(TagWrapper wTag)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());

        String newName = stringUtil.checkInputTrimToUpper(wTag.getNew_name());

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("tag", "name", name);

        Tag tag = tagData.get();
        tag.setName(newName);

        try {
            tagDAO.save(tag);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsBalladConstraint.UNIQUE_TAG_NAME))
                throw new DuplicateEntityException("tag", "name", name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("tag");
        }

        return tagUtil.wrapTag(tag);
    }

    @Override
    public TagDTO addStories(TagWrapper wTag)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException{
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());

        String[] stories = wTag.getStories();
        Set<String> storyQuery = new HashSet<>();

        for (String story : stories) {
            String item = stringUtil.checkInputTrimToUpper(story);
            if (item.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("stories");
            storyQuery.add(item);
        }

        List<String> storyResultList = storyDAO.findStoryNames(storyQuery);
        Set<String> storyResultSet = new HashSet<>(storyResultList);

        if (!storyQuery.equals(storyResultSet)) {
            storyQuery.removeAll(storyResultSet);
            throw new InvalidInputException("stories");
        }

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("tag", "name", name);

        Tag tag = tagData.get();
        Set<Story> dbStories = new HashSet<>(tag.getStories());
        List<Story> storyQueryAdd = storyDAO.findStoriesByName(storyQuery);
        dbStories.addAll(storyQueryAdd);
        tag.setStories(dbStories);

        try {
            tagDAO.save(tag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("tag");
        }

        return tagUtil.wrapTag(tag);
    }

    @Override
    public TagDTO removeStories(TagWrapper wTag)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        String name = stringUtil.checkInputTrimToUpper(wTag.getName());

        String[] stories = wTag.getStories();

        Optional<Tag> tagData = tagDAO.findByName(name);
        if (tagData.isEmpty()) throw new EntityNotFoundException("tag", "name", name);

        Tag tag = tagData.get();
        Set<Story> dbStories = new HashSet<>(tag.getStories());
        Set<String> dbStoryNames = storyUtil.extractStoryNamesFromArray(dbStories);

        Set<String> inputStoryNames = new HashSet<>();
        for (String story: stories) {
            inputStoryNames.add(stringUtil.checkInputTrimToUpper(story));
        }
        if (!dbStoryNames.containsAll(inputStoryNames)) throw new InvalidInputException("stories");

        Set<Story> newStories = new HashSet<>();
        for (String storyName: inputStoryNames) {
            Optional<Story> foundStory = dbStories.stream().filter(x -> x.getName().equals(storyName)).findAny();
            foundStory.ifPresent(newStories::add);
        }

        dbStories.removeAll(newStories);
        tag.setStories(dbStories);

        try {
            tagDAO.save(tag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("tag");
        }

        return tagUtil.wrapTag(tag);
    }

    @Override
    public TagDTO deleteTag(TagWrapper wTag)
            throws UnknownInputException, DatabaseErrorException, EntityErrorException{
        String tagName = stringUtil.checkInputTrimToUpper(wTag.getName());

        Optional<Tag> tagData = tagDAO.findByName(tagName);
        if (tagData.isPresent()) {
            Tag tag = tagData.get();

            try {
                tagDAO.delete(tag);
            } catch (Exception e) {
                e.printStackTrace();
                throw new EntityDeletionErrorException("tag");
            }

            return tagUtil.wrapTag(tag);
        } else {
            throw new EntityNotFoundException("tag", "name", tagName);
        }
    }
}
