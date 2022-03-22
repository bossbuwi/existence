package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

import java.util.List;

public interface StoryService {
    List<StoryDTO> getAllStories();
    StoryDTO getStory(StoryWrapper story) throws EntityErrorException, UnknownInputException;
    StoryDTO createStory(StoryWrapper story) throws UnknownInputException, DatabaseErrorException;
    StoryDTO updateStory(StoryWrapper story) throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    StoryDTO addTags(StoryWrapper story) throws UnknownInputException, DatabaseErrorException, EntityErrorException;
    StoryDTO removeTags(StoryWrapper story) throws UnknownInputException, DatabaseErrorException, EntityErrorException;
    StoryDTO deleteStory(StoryWrapper wStory) throws UnknownInputException, DatabaseErrorException, EntityErrorException;
}
