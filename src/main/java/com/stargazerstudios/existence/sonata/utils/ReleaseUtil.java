package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.ReleaseDTO;
import com.stargazerstudios.existence.sonata.entity.Release;
import org.springframework.stereotype.Service;

@Service
public class ReleaseUtil {

    public ReleaseDTO wrapRelease(Release release) {
        ReleaseDTO releaseDTO = new ReleaseDTO();
        releaseDTO.setId(release.getId());
        releaseDTO.setName(release.getName());

        if (release.getSystems() != null) releaseDTO.setSystem_count(release.getSystems().size());
        else releaseDTO.setSystem_count(0);

        return releaseDTO;
    }
}
