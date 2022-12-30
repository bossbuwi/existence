package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

import java.util.List;

public interface StoryService {
    List<StoryDTO> getAllStories();
    StoryDTO getStory(StoryWrapper story) throws EntityException, UnknownInputException;
    StoryDTO createStory(StoryWrapper story) throws UnknownInputException, DatabaseException;
    StoryDTO updateStory(StoryWrapper story) throws UnknownInputException, EntityException, DatabaseException;
    StoryDTO addTags(StoryWrapper story) throws UnknownInputException, DatabaseException, EntityException;
    StoryDTO removeTags(StoryWrapper story) throws UnknownInputException, DatabaseException, EntityException;
    StoryDTO deleteStory(StoryWrapper wStory) throws UnknownInputException, DatabaseException, EntityException;
}
