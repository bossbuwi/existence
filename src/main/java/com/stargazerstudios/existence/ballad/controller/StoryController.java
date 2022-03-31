package com.stargazerstudios.existence.ballad.controller;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.service.StoryServiceImpl;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
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
@RequestMapping("/ballad/stories")
public class StoryController {

    @Autowired
    private StoryServiceImpl storyService;

    @GetMapping("/index")
    public ResponseEntity<List<StoryDTO>> getStories() {
        return new ResponseEntity<>(storyService.getAllStories(), HttpStatus.OK);
    }

    @GetMapping("/story")
    public ResponseEntity<StoryDTO> getStory(@Validated(GetValidation.class)
                                                 @RequestBody StoryWrapper story)
            throws UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(storyService.getStory(story), HttpStatus.OK);
    }

    @PostMapping("/story")
    public ResponseEntity<StoryDTO> createStory(@Validated(PostValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseErrorException, UnknownInputException {
        return new ResponseEntity<>(storyService.createStory(story), HttpStatus.OK);
    }

    @PutMapping("/story")
    public ResponseEntity<StoryDTO> updateStory(@Validated(PutValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(storyService.updateStory(story), HttpStatus.OK);
    }

    @PutMapping("/story/add-tags")
    public ResponseEntity<StoryDTO> addTags(@Validated(PutRelationValidation.class)
                                                @RequestBody StoryWrapper story)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(storyService.addTags(story), HttpStatus.OK);
    }

    @PutMapping("/story/remove-tags")
    public ResponseEntity<StoryDTO> removeTags(@Validated(PutRelationValidation.class)
                                                   @RequestBody StoryWrapper story)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(storyService.removeTags(story), HttpStatus.OK);
    }

    @DeleteMapping("/story")
    public ResponseEntity<StoryDTO> deleteStory(@Validated(DeleteValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(storyService.deleteStory(story), HttpStatus.OK);
    }
}
