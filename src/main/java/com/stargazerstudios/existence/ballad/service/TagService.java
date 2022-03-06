package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;

import java.util.List;

public interface TagService {
    List<TagDTO> getAllTags();
    TagDTO getTag(TagWrapper tag)throws EntityNotFoundException, InvalidInputException;
    TagDTO createTag(TagWrapper tag) throws DuplicateEntityException, InvalidInputException;
    TagDTO updateTag(TagWrapper tag)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException;
    TagDTO addStories(TagWrapper tag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;
    TagDTO removeStories(TagWrapper tag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;
    TagDTO deleteTag(String name) throws EntityNotFoundException;
}
