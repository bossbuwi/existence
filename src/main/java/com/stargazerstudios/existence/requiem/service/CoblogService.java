package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.wrapper.CoblogWrapper;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;

import java.util.List;

public interface CoblogService {
    List<CoblogDTO> findAllCoblogs();
    CoblogDTO findCoblogById(long id) throws EntityErrorException;
    CoblogDTO createCoblog(CoblogWrapper wCoblog) throws UnknownInputException, DatabaseErrorException, EntityErrorException;
    CoblogDTO updateCoblog(CoblogWrapper wCoblog);
    CoblogDTO deleteCoblog(CoblogWrapper wCoblog);
    CoblogDTO attachComponent(ComponentWrapper wComponent);
    CoblogDTO detachComponent(ComponentWrapper wComponent);
}
