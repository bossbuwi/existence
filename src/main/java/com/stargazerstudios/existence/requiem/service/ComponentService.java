package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;

import java.util.List;

public interface ComponentService {

    List<ComponentDTO> getAllComponents();
    ComponentDTO getComponent(long id) throws EntityErrorException;
    List<ComponentDTO> getComponentByName(String name);
    ComponentDTO createComponent(ComponentWrapper wComponent) throws EntityErrorException, DatabaseErrorException;
    ComponentDTO updateComponent(ComponentWrapper wComponent);
    ComponentDTO deleteComponent(ComponentWrapper wComponent);
}
