package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;

import java.util.List;

public interface StoryService {
    List<StoryDTO> getAllStories();
    StoryDTO getStory(StoryWrapper story) throws EntityNotFoundException, InvalidInputException;
    StoryDTO createStory(StoryWrapper story) throws DuplicateEntityException, InvalidInputException;
    StoryDTO updateStory(StoryWrapper story)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;
    StoryDTO addTags(StoryWrapper story)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;
    StoryDTO removeTags(StoryWrapper story)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;
    StoryDTO deleteStory(StoryWrapper story) throws EntityNotFoundException, InvalidInputException;
}
