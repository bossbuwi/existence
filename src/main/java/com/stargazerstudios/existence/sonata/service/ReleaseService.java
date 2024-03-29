package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.sonata.dto.ReleaseDTO;
import com.stargazerstudios.existence.sonata.wrapper.ReleaseWrapper;

import java.util.List;

public interface ReleaseService {
    List<ReleaseDTO> getReleases();
    ReleaseDTO getRelease(String name);
    ReleaseDTO getRelease(long id);
    ReleaseDTO createRelease(ReleaseWrapper releaseWrapper) throws DatabaseException, AuthorizationException;
    ReleaseDTO updateRelease(ReleaseWrapper releaseWrapper);
    ReleaseDTO deleteRelease(long id);
}
