package com.stargazerstudios.existence.ballad.controller;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.service.StoryServiceImpl;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
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
public class StoryController {

    @Autowired
    private StoryServiceImpl storyService;

    /* Unguarded Endpoints */
    @GetMapping("/con/stories/index")
    public ResponseEntity<List<StoryDTO>> getStories() {
        return new ResponseEntity<>(storyService.getAllStories(), HttpStatus.OK);
    }

    @GetMapping("/con/stories/story")
    public ResponseEntity<StoryDTO> getStory(@Validated(GetValidation.class)
                                                 @RequestBody StoryWrapper story)
            throws UnknownInputException, EntityException {
        return new ResponseEntity<>(storyService.getStory(story), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/stories/story")
    public ResponseEntity<StoryDTO> createStory(@Validated(PostValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseException, UnknownInputException {
        return new ResponseEntity<>(storyService.createStory(story), HttpStatus.OK);
    }

    @PutMapping("/stories/story")
    public ResponseEntity<StoryDTO> updateStory(@Validated(PutValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(storyService.updateStory(story), HttpStatus.OK);
    }

    @PutMapping("/stories/story/add-tags")
    public ResponseEntity<StoryDTO> addTags(@Validated(PutRelationValidation.class)
                                                @RequestBody StoryWrapper story)
            throws DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(storyService.addTags(story), HttpStatus.OK);
    }

    @PutMapping("/stories/story/remove-tags")
    public ResponseEntity<StoryDTO> removeTags(@Validated(PutRelationValidation.class)
                                                   @RequestBody StoryWrapper story)
            throws DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(storyService.removeTags(story), HttpStatus.OK);
    }

    @DeleteMapping("/stories/story")
    public ResponseEntity<StoryDTO> deleteStory(@Validated(DeleteValidation.class)
                                                    @RequestBody StoryWrapper story)
            throws DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(storyService.deleteStory(story), HttpStatus.OK);
    }
}
