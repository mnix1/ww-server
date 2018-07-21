package com.ww.service.rival.task.geography;

import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.entity.rival.task.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeographyTaskService {

    @Autowired
    GeographyTaskCountryCapitalTypeService geographyTaskCountryCapitalTypeService;
    @Autowired
    GeographyTaskMinMaxTypeService geographyTaskMinMaxTypeService;

    public Question generate(GeographyTaskType type) {
        if (type == GeographyTaskType.MAX_AREA
                || type == GeographyTaskType.MIN_AREA
                || type == GeographyTaskType.MAX_POPULATION
                || type == GeographyTaskType.MIN_POPULATION) {
            return geographyTaskMinMaxTypeService.generate(type);
        }

        return geographyTaskCountryCapitalTypeService.generate(type);
    }

}
