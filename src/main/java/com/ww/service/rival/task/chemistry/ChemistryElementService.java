package com.ww.service.rival.task.chemistry;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.entity.rival.task.ChemistryElement;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.repository.rival.task.category.ChemistryElementRepository;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.FileHelper.getResource;
import static com.ww.helper.FileHelper.saveToFile;
import static com.ww.helper.NetworkHelper.downloadContent;

@Service
public class ChemistryElementService {

    @Autowired
    ChemistryElementRepository chemistryElementRepository;

    public void loadResource() {
        try {
            File file = ResourceUtils.getFile("classpath:task/chemistryPeriodicTable.json");
            JsonNode json = JsonLoader.fromFile(file);
            List<ChemistryElement> chemistryElements = new ArrayList<>();
            json.forEach(jsonNode -> {
                ChemistryElement chemistryElement = new ChemistryElement(jsonNode);
                chemistryElements.add(chemistryElement);
            });
            chemistryElementRepository.saveAll(chemistryElements);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
