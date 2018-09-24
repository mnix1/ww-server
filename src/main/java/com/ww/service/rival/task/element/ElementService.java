package com.ww.service.rival.task.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.entity.inside.task.Element;
import com.ww.repository.inside.category.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElementService {

    @Autowired
    ElementRepository elementRepository;

    public void loadResource() {
        try {
            File file = ResourceUtils.getFile("classpath:task/chemistryPeriodicTable.json");
            JsonNode json = JsonLoader.fromFile(file);
            List<Element> elements = new ArrayList<>();
            json.forEach(jsonNode -> {
                Element element = new Element(jsonNode);
                elements.add(element);
            });
            elementRepository.saveAll(elements);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
