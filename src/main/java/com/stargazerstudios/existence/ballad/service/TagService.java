package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

import java.util.List;

public interface TagService {
    List<TagDTO> getAllTags();
    TagDTO getTag(TagWrapper tag) throws UnknownInputException, EntityErrorException;
    TagDTO createTag(TagWrapper tag) throws DatabaseErrorException, UnknownInputException;
    TagDTO updateTag(TagWrapper tag) throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    TagDTO addStories(TagWrapper tag) throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    TagDTO removeStories(TagWrapper tag) throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    TagDTO deleteTag(TagWrapper tag) throws UnknownInputException, DatabaseErrorException, EntityErrorException;
}
