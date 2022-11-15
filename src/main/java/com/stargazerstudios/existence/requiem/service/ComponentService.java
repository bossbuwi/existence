package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;

import java.util.List;

public interface ComponentService {

    List<ComponentDTO> getAllComponents();
    ComponentDTO getComponent(long id) throws EntityException;
    List<ComponentDTO> getComponentByName(String name);
    ComponentDTO createComponent(ComponentWrapper wComponent) throws EntityException, DatabaseException;
    ComponentDTO updateComponent(ComponentWrapper wComponent);
    ComponentDTO deleteComponent(ComponentWrapper wComponent);
}
