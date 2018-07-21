package com.ww.service.rival.task.geography;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.task.GeographyTaskType;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.model.entity.rival.task.Question;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.NetworkHelper.downloadContent;

@Service
public class GeographyCountryService {

    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public void loadAndDownloadResources() {
        try {
            File file = ResourceUtils.getFile("classpath:rival/geographyCountry.json");
            JsonNode json = JsonLoader.fromFile(file);
            List<GeographyCountry> geographyCountries = new ArrayList<>();
            json.forEach(jsonNode -> {
                GeographyCountry geographyCountry = new GeographyCountry(jsonNode);
                downloadFlag(geographyCountry);
                downloadMap(geographyCountry);
                geographyCountries.add(geographyCountry);
            });
            geographyCountryRepository.saveAll(geographyCountries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fileExists(String path) {
        try {
            ResourceUtils.getFile("classpath:" + path);
            return true;
        } catch (FileNotFoundException e) {
        }
        return false;
    }

    private void downloadFlag(GeographyCountry geographyCountry) {
        if (!fileExists(geographyCountry.getFlagResourceUrl())) {
            downloadSvg(geographyCountry.getFlagUrl(), geographyCountry.getFlagResourceUrl("/"));
        }
    }

    private void downloadMap(GeographyCountry geographyCountry) {
        if (!fileExists(geographyCountry.getMapResourceUrl())) {
            downloadSvg(geographyCountry.getMapSvgLocationMapUrl(), geographyCountry.getMapResourceUrl("/"));
        }
    }

    private void downloadSvg(String url, String path) {
        String content = downloadContent(url);
        if (content == null) {
            System.out.println("Cannot downloadMap");
            return;
        }
        try {
            String rootPath = System.getProperty("user.dir");
            File file = new File(rootPath + path);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(content);
            osw.close();
            Thread.sleep(200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
