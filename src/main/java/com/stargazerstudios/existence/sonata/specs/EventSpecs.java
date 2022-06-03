package com.stargazerstudios.existence.sonata.specs;

import com.stargazerstudios.existence.sonata.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EventSpecs {

    public static Specification<Event> withJiraCase(String jira) {
        if (jira == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("jiraCase"), jira));
        }
    }

    public static Specification<Event> withFeaturesOn(String featuresOn) {
        if (featuresOn == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("featuresOn"), featuresOn));
        }
    }

    public static Specification<Event> withFeaturesOff(String featuresOff) {
        if (featuresOff == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("featuresOff"), featuresOff));
        }
    }

    public static Specification<Event> withCompiledSources(String compiledSources) {
        if (compiledSources == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("compiledSources"), compiledSources));
        }
    }

    public static Specification<Event> withApiUsed(String apiUsed) {
        if (apiUsed == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("apiUsed"), apiUsed));
        }
    }

    public static Specification<Event> withCreatedBy(String createdBy) {
        if (createdBy == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), createdBy));
        }
    }

    public static Specification<Event> withLastChangedBy(String lastChangedBy) {
        if (lastChangedBy == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastChangedBy"), lastChangedBy));
        }
    }

    public static Specification<Event> withStartDate(String startDate) {
        if (startDate == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startDate"), startDate));
        }
    }

    public static Specification<Event> withEndDate(String endDate) {
        if (endDate == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("endDate"), endDate));
        }
    }

    public static Specification<Event> betweenDateLiterals(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(
                            root.get("startDate"),
                            criteriaBuilder.literal(LocalDate.parse(startDate)),
                            criteriaBuilder.literal(LocalDate.parse(endDate))
                    ));
        }
    }

    public static Specification<Event> betweenDateFields(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(
                            criteriaBuilder.literal(LocalDate.parse(startDate)),
                            root.get("startDate"),
                            root.get("endDate")
                    ));
        }
    }

    public static Specification<Event> withId(long id) {
        if (id == 0) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.get("id"), id);
        }
    }

    public static Specification<Event> withSystem(String system) {
        if (system == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.join("system").get("globalPrefix"), system);
        }
    }

    public static Specification<Event> withMachine(String machine) {
        if (machine == null) {
            return null;
        } else {
            return (root, query, cb) -> cb.equal(root.join("system").get("machine").get("name"), machine);
        }
    }
}
