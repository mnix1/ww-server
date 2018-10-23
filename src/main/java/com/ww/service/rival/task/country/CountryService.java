package com.ww.service.rival.task.country;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.entity.inside.task.Country;
import com.ww.repository.inside.category.CountryRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public void loadAndDownloadResources() {
        try {
            File file = ResourceUtils.getFile("classpath:task/geographyCountry.json");
            JsonNode json = JsonLoader.fromFile(file);
            List<Country> geographyCountries = new ArrayList<>();
            json.forEach(jsonNode -> {
                Country country = new Country(jsonNode);
                downloadFlag(country);
                downloadMap(country);
                geographyCountries.add(country);
            });
            countryRepository.saveAll(geographyCountries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFlag(Country country) {
        if (getResource(country.getFlagResourcePath()) == null) {
            downloadSvg(country.getFlagUrl(), country.getFlagResourcePath("/"));
        }
    }

    private void downloadMap(Country country) {
        if (getResource(country.getMapResourcePath()) == null) {
            downloadSvg(country.getMapSvgLocationMapUrl(), country.getMapResourcePath("/"));
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
