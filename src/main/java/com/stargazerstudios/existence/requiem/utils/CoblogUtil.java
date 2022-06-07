package com.stargazerstudios.existence.requiem.utils;

import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.entity.Coblog;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class CoblogUtil {

    public CoblogDTO wrapCoblog(Coblog coblog) {
        CoblogDTO coblogDTO = new CoblogDTO();
        coblogDTO.setId(coblog.getId());
        coblogDTO.setDescription(coblog.getDescription());
        coblogDTO.setRun_day(coblog.getRunDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        coblogDTO.setNext_run_day(coblog.getNextRunDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        coblogDTO.setOpen(coblog.isOpen());
        coblogDTO.setConclusion(coblog.getConclusion());
        coblogDTO.setActive_user(coblog.getActiveUser());
        coblogDTO.setCreated_by(coblog.getCreatedBy());
        coblogDTO.setLast_changed_by(coblog.getLastChangedBy());
        coblogDTO.setCreation_date(coblog.getDateCreated());
        coblogDTO.setLast_changed_date(coblog.getDateChanged());
        return coblogDTO;
    }
}
