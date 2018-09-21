package com.ww.service.rival.task.olympicgames;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.constant.rival.task.OlympicGamesType;
import com.ww.model.entity.rival.task.OlympicMedal;
import com.ww.repository.rival.task.category.OlympicMedalRepository;
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
import java.util.stream.Collectors;

@Service
public class OlympicMedalService {

    @Autowired
    private OlympicMedalRepository olympicMedalRepository;

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
        try {
            File file = ResourceUtils.getFile("classpath:task/olympic-games/" + path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<OlympicMedal> olympicMedals = new ArrayList<>();
            String line = br.readLine();
            String cvsSplitChar = ",";
            while ((line = br.readLine()) != null) {
                List<String> params = new ArrayList<>();
                String[] row = line.split("\"");
                if (row.length == 0) {
                    continue;
                }
                for (String rowPart : row) {
                    if (params.size() == 0) {
                        params.addAll(Arrays.asList(rowPart.split(cvsSplitChar)));
                    } else if (params.size() == 4) {
                        params.add(rowPart);
                    } else {
                        params.addAll(Arrays.asList(rowPart.replace(", ", " ").split(cvsSplitChar)));
                    }
                }
                params = params.stream().filter(s -> !s.equals("")).collect(Collectors.toList());
                OlympicMedal olympicMedal = new OlympicMedal(params, type, mapping);
                if (olympicMedal.getYear() > 1904) {
                    olympicMedals.add(olympicMedal);
                }
            }
            olympicMedalRepository.saveAll(olympicMedals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
