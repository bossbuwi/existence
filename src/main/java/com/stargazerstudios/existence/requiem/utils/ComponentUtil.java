package com.stargazerstudios.existence.requiem.utils;

import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.entity.Component;
import org.springframework.stereotype.Service;

@Service
public class ComponentUtil {

    public ComponentDTO wrapComponent(Component component) {
        ComponentDTO componentDTO = new ComponentDTO();
        componentDTO.setId(component.getId());
        componentDTO.setName(component.getName());
        componentDTO.setSequence(component.getSequence());
        componentDTO.setDetails(component.getDetails());
        componentDTO.setResolution(component.getResolution());
        componentDTO.setCreated_by(component.getCreatedBy());
        componentDTO.setCreation_date(component.getDateCreated());
        componentDTO.setLast_changed_by(component.getLastChangedBy());
        componentDTO.setLast_changed_date(component.getDateChanged());
        return componentDTO;
    }
}
