package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.wrapper.CoblogWrapper;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;

import java.util.List;

public interface CoblogService {
    List<CoblogDTO> findAllCoblogs();
    CoblogDTO findCoblogById(long id) throws EntityException;
    CoblogDTO createCoblog(CoblogWrapper wCoblog) throws UnknownInputException, DatabaseException, EntityException;
    CoblogDTO updateCoblog(CoblogWrapper wCoblog);
    CoblogDTO deleteCoblog(CoblogWrapper wCoblog);
    CoblogDTO attachComponent(ComponentWrapper wComponent);
    CoblogDTO detachComponent(ComponentWrapper wComponent);
}
