package com.stargazerstudios.existence.ballad.utils;

import com.stargazerstudios.existence.ballad.dto.StoryDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Service
public class StoryUtil {

    public StoryDTO wrapStory(Story story) {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setName(story.getName());
        storyDTO.setId(story.getId());
        storyDTO.setCreation_date(story.getDateCreated());
        storyDTO.setLast_changed_date(story.getDateChanged());

        Set<Tag> tagsSet = story.getTags();
        ArrayList<String> tags = new ArrayList<>();
        if (tagsSet != null && !tagsSet.isEmpty()) {
            for (Tag tag: tagsSet) {
                tags.add(tag.getName());
            }
        }
        storyDTO.setTags(tags);

        return storyDTO;
    }

    public Set<String> extractStoryNamesFromArray(Set<Story> stories) {
        Set<String> nameList = new HashSet<>();
        for (Story story: stories) {
            nameList.add(story.getName());
        }

        return nameList;
    }
}
