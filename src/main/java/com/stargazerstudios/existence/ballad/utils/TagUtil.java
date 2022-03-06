package com.stargazerstudios.existence.ballad.utils;

import com.stargazerstudios.existence.ballad.dto.TagDTO;
import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class TagUtil {

    public TagDTO wrapTag(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        tagDTO.setCreation_date(tag.getDateCreated());
        tagDTO.setLast_changed_date(tag.getDateChanged());

        Set<Story> storySet = tag.getStories();
        ArrayList<String> stories = new ArrayList<>();
        if (storySet != null && !storySet.isEmpty()) {
            for (Story story: storySet) {
                stories.add(story.getName());
            }
        }
        tagDTO.setStories(stories);

        return tagDTO;
    }

    public Set<String> extractTagNamesFromArray(Set<Tag> tags) {
        Set<String> nameList = new HashSet<>();
        for (Tag tag: tags) {
            nameList.add(tag.getName());
        }

        return nameList;
    }
}
