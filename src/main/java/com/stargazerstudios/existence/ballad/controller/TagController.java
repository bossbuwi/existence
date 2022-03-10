package com.stargazerstudios.existence.ballad.controller;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.service.TagServiceImpl;
import com.stargazerstudios.existence.ballad.wrapper.TagWrapper;
import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ballad/tags")
public class TagController {

    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("/index")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagDTO> getTag(@RequestBody TagWrapper tag)
            throws EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(tagService.getTag(tag), HttpStatus.OK);
    }

    @PostMapping("/tag")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagWrapper tag)
            throws DuplicateEntityException, InvalidInputException {
        return new ResponseEntity<>(tagService.createTag(tag), HttpStatus.OK);
    }

    @PutMapping("/tag")
    public ResponseEntity<TagDTO> updateTag(@RequestBody TagWrapper tag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(tagService.updateTag(tag), HttpStatus.OK);
    }

    @PutMapping("/tag/add-stories")
    public ResponseEntity<TagDTO> addStory(@RequestBody TagWrapper tag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(tagService.addStories(tag), HttpStatus.OK);
    }

    @PutMapping("/tag/remove-stories")
    public ResponseEntity<TagDTO> removeStory(@RequestBody TagWrapper tag)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(tagService.removeStories(tag), HttpStatus.OK);
    }

    @DeleteMapping("/tag")
    public ResponseEntity<TagDTO> deleteTag(@RequestBody TagWrapper tag) {
        return null;
    }
}
