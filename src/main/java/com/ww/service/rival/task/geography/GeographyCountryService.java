package com.ww.service.rival.task.geography;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.entity.rival.task.GeographyCountry;
import com.ww.repository.rival.task.category.GeographyCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.FileHelper.getResource;
import static com.ww.helper.FileHelper.saveToFile;
import static com.ww.helper.NetworkHelper.downloadContent;

@Service
public class GeographyCountryService {

    @Autowired
    GeographyCountryRepository geographyCountryRepository;

    public void loadAndDownloadResources() {
        try {
            File file = ResourceUtils.getFile("classpath:task/geographyCountry.json");
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

    private void downloadFlag(GeographyCountry geographyCountry) {
        if (getResource(geographyCountry.getFlagResourcePath()) == null) {
            downloadSvg(geographyCountry.getFlagUrl(), geographyCountry.getFlagResourcePath("/"));
        }
    }

    private void downloadMap(GeographyCountry geographyCountry) {
        if (getResource(geographyCountry.getMapResourcePath()) == null) {
            downloadSvg(geographyCountry.getMapSvgLocationMapUrl(), geographyCountry.getMapResourcePath("/"));
        }
    }

    private void downloadSvg(String url, String path) {
        String content = downloadContent(url);
        if (content == null) {
            System.out.println("Cannot downloadMap");
            return;
        }
        try {
            saveToFile(content, path);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
