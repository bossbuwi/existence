package com.stargazerstudios.existence.ballad.controller;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.service.StoryServiceImpl;
import com.stargazerstudios.existence.ballad.wrapper.StoryWrapper;
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
@RequestMapping("/ballad/stories")
public class StoryController {

    // TODO: Story controller needs a rework. Request params should not be used because they may be hashtags in there.

    @Autowired
    private StoryServiceImpl storyService;

    @GetMapping("/index")
    public ResponseEntity<List<StoryDTO>> getStories() {
        return new ResponseEntity<>(storyService.getAllStories(), HttpStatus.OK);
    }

    @GetMapping("/story")
    public ResponseEntity<StoryDTO> getStory(@RequestBody StoryWrapper story)
            throws EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(storyService.getStory(story), HttpStatus.OK);
    }

    @PostMapping("/story")
    public ResponseEntity<StoryDTO> createStory(@RequestBody StoryWrapper story)
            throws DuplicateEntityException, InvalidInputException {
        return new ResponseEntity<>(storyService.createStory(story), HttpStatus.OK);
    }

    @PutMapping("/story")
    public ResponseEntity<StoryDTO> updateStory(@RequestBody StoryWrapper story)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(storyService.updateStory(story), HttpStatus.OK);
    }

    @PutMapping("/story/add-tags")
    public ResponseEntity<StoryDTO> addTags(@RequestBody StoryWrapper story)
            throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        // TODO: Ongoing
        return new ResponseEntity<>(storyService.addTags(story), HttpStatus.OK);
    }

    @PutMapping("/story/remove-tags")
    public ResponseEntity<StoryDTO> removeTags(@RequestBody StoryWrapper story)
            throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        return new ResponseEntity<>(storyService.removeTags(story), HttpStatus.OK);
    }

    @DeleteMapping("/story/{name}")
    public ResponseEntity<StoryDTO> deleteStory(@PathVariable String name)
            throws EntityNotFoundException {
        // TODO: Ongoing
        return new ResponseEntity<>(storyService.deleteStory(name), HttpStatus.OK);
    }
}
