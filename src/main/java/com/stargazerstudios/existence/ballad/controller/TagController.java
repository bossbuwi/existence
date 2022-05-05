package com.stargazerstudios.existence.ballad.controller;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.service.TagServiceImpl;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ballad")
public class TagController {

    @Autowired
    private TagServiceImpl tagService;

    /* Unguarded Endpoints */
    @GetMapping("/con/tags/index")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

    @GetMapping("/con/tags/tag")
    public ResponseEntity<TagDTO> getTag(@Validated(GetValidation.class)
                                             @RequestBody TagWrapper tag)
            throws UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(tagService.getTag(tag), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/tags/tag")
    public ResponseEntity<TagDTO> createTag(@Validated(PostValidation.class)
                                                @RequestBody TagWrapper tag)
            throws DatabaseErrorException, UnknownInputException {
        return new ResponseEntity<>(tagService.createTag(tag), HttpStatus.OK);
    }

    @PutMapping("/tags/tag")
    public ResponseEntity<TagDTO> updateTag(@Validated(PutValidation.class)
                                                @RequestBody TagWrapper tag)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(tagService.updateTag(tag), HttpStatus.OK);
    }

    @PutMapping("/tags/tag/add-stories")
    public ResponseEntity<TagDTO> addStory(@Validated(PutRelationValidation.class)
                                               @RequestBody TagWrapper tag)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(tagService.addStories(tag), HttpStatus.OK);
    }

    @PutMapping("/tags/tag/remove-stories")
    public ResponseEntity<TagDTO> removeStory(@Validated(PutRelationValidation.class)
                                                  @RequestBody TagWrapper tag)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(tagService.removeStories(tag), HttpStatus.OK);
    }

    @DeleteMapping("/tags/tag")
    public ResponseEntity<TagDTO> deleteTag(@Validated(DeleteValidation.class)
                                                @RequestBody TagWrapper tag)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(tagService.deleteTag(tag), HttpStatus.OK);
    }
}
