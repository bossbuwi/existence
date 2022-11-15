package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.entity.Coblog;
import com.stargazerstudios.existence.requiem.entity.Component;
import com.stargazerstudios.existence.requiem.repository.CoblogDAO;
import com.stargazerstudios.existence.requiem.repository.ComponentDAO;
import com.stargazerstudios.existence.requiem.utils.ComponentUtil;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentDAO componentDAO;

    @Autowired
    private CoblogDAO coblogDAO;

    @Autowired
    private ComponentUtil componentUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<ComponentDTO> getAllComponents() {
        List<Component> components = componentDAO.findAll();
        List<ComponentDTO> componentDTOs = new ArrayList<>();

        if (!components.isEmpty()) {
            for (Component component: components) {
                componentDTOs.add(componentUtil.wrapComponent(component));
            }
        }

        return componentDTOs;
    }

    @Override
    public ComponentDTO getComponent(long id)
            throws EntityException {
        Optional<Component> componentData = componentDAO.findById(id);
        if (componentData.isEmpty()) throw new EntityNotFoundException("component", "id", Long.toString(id));

        Component component = componentData.get();
        return componentUtil.wrapComponent(component);
    }

    @Override
    public List<ComponentDTO> getComponentByName(String name) {
        List<Component> components = componentDAO.findByName(name);
        List<ComponentDTO> componentDTOS = new ArrayList<>();

        if (!components.isEmpty()) {
            for (Component component: components) {
                componentDTOS.add(componentUtil.wrapComponent(component));
            }
        }

        return componentDTOS;
    }

    @Override
    public ComponentDTO createComponent(ComponentWrapper wComponent)
            throws EntityException, DatabaseException {
        String name = stringUtil.trimToUpper(wComponent.getName());
        String sequence = stringUtil.trimToUpper(wComponent.getSequence());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        long coblogId = wComponent.getCoblog_id();
        Optional<Coblog> coblogData = coblogDAO.findById(coblogId);
        if (coblogData.isEmpty()) throw new EntityNotFoundException("coblog", "id", Long.toString(coblogId));
        Coblog coblog = coblogData.get();
        Set<Coblog> coblogs = new HashSet<>();
        coblogs.add(coblog);

        Component component = new Component();
        component.setName(name);
        component.setSequence(sequence);
        component.setResolution(wComponent.getResolution());
        component.setDetails(wComponent.getDetails());
        component.setCreatedBy(username);
        component.setLastChangedBy(username);
        component.setCoblogs(coblogs);

        try {
            componentDAO.save(component);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("component");
        }

        return componentUtil.wrapComponent(component);
    }

    @Override
    public ComponentDTO updateComponent(ComponentWrapper wComponent) {
        return null;
    }

    @Override
    public ComponentDTO deleteComponent(ComponentWrapper wComponent) {
        return null;
    }
}
