package com.ww.service.rival.task.olympicgames;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.database.InitService;
import com.ww.model.constant.rival.task.olympicgames.OlympicGamesType;
import com.ww.model.entity.rival.task.Country;
import com.ww.model.entity.rival.task.OlympicMedal;
import com.ww.repository.rival.task.category.CountryRepository;
import com.ww.repository.rival.task.category.OlympicMedalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OlympicMedalService {
    private static final Logger logger = LoggerFactory.getLogger(OlympicMedalService.class);

    @Autowired
    private OlympicMedalRepository olympicMedalRepository;
    @Autowired
    private CountryRepository countryRepository;

    public void loadResources() {
        loadResource(OlympicGamesType.SUMMER, "summerOlympicMedal.csv");
        loadResource(OlympicGamesType.WINTER, "winterOlympicMedal.csv");
    }

    public JsonNode mappings() {
        try {
            File file = ResourceUtils.getFile("classpath:task/olympic-games/olympicMedalMapping.json");
            return JsonLoader.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadResource(OlympicGamesType type, String path) {
        JsonNode mapping = mappings();
        Map<String, Country> countryMapping = countryRepository.findAll().stream()
                .collect(Collectors.toMap(Country::getAlpha3Code, o -> o));
        try {
            File file = ResourceUtils.getFile("classpath:task/olympic-games/" + path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<OlympicMedal> olympicMedals = new ArrayList<>();
            String line = br.readLine();
            String cvsSplitChar = ";";
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitChar);
                List<String> params = Arrays.asList(row);
                OlympicMedal olympicMedal = new OlympicMedal(params, type, mapping, countryMapping);
                if (olympicMedal.getYear() > 1904
                        && !olympicMedal.getSport().equals("Equestrian")) {
                    olympicMedals.add(olympicMedal);
                }
                if (olympicMedals.size() > 1000) {
                    logger.debug("SENDING 1000 MEDALS");
                    olympicMedalRepository.saveAll(olympicMedals);
                    logger.debug("SENT 1000 MEDALS, TOTAL: " + index);
                    olympicMedals = new ArrayList<>();
                }
                index++;
            }
            olympicMedalRepository.saveAll(olympicMedals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
