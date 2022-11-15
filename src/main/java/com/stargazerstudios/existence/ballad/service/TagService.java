package com.stargazerstudios.existence.ballad.service;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;

import java.util.List;

public interface TagService {
    List<TagDTO> getAllTags();
    TagDTO getTag(TagWrapper tag) throws UnknownInputException, EntityException;
    TagDTO createTag(TagWrapper tag) throws DatabaseException, UnknownInputException;
    TagDTO updateTag(TagWrapper tag) throws UnknownInputException, EntityException, DatabaseException;
    TagDTO addStories(TagWrapper tag) throws UnknownInputException, EntityException, DatabaseException;
    TagDTO removeStories(TagWrapper tag) throws UnknownInputException, EntityException, DatabaseException;
    TagDTO deleteTag(TagWrapper tag) throws UnknownInputException, DatabaseException, EntityException;
}
