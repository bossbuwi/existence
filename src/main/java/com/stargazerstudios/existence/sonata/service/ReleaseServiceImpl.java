package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.constants.ConsSonataConstraint;
import com.stargazerstudios.existence.sonata.dto.ReleaseDTO;
import com.stargazerstudios.existence.sonata.entity.Release;
import com.stargazerstudios.existence.sonata.repository.ReleaseDAO;
import com.stargazerstudios.existence.sonata.utils.ReleaseUtil;
import com.stargazerstudios.existence.sonata.wrapper.ReleaseWrapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private ReleaseDAO releaseDAO;

    @Autowired
    private ReleaseUtil releaseUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<ReleaseDTO> getReleases() {
        List<Release> releases = releaseDAO.findAll();
        List<ReleaseDTO> releaseDTOs = new ArrayList<>();
        if (!releases.isEmpty()) {
            for (Release release: releases) {
                releaseDTOs.add(releaseUtil.wrapRelease(release));
            }
        }

        return releaseDTOs;
    }

    @Override
    public ReleaseDTO getRelease(String name) {
        return null;
    }

    @Override
    public ReleaseDTO getRelease(long id) {
        return null;
    }

    @Override
    public ReleaseDTO createRelease(ReleaseWrapper wRelease)
            throws DatabaseErrorException {
        Release release = new Release();
        String name = stringUtil.trimToUpper(wRelease.getName());
        release.setName(name);

        try {
            releaseDAO.save(release);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSonataConstraint.UNIQUE_RELEASE_NAME))
                throw new DuplicateEntityException("release", "name", name);
            throw new EntitySaveErrorException("release");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("release");
        }

        return releaseUtil.wrapRelease(release);
    }
}