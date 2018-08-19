package com.ww.model.entity.rival.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.ww.helper.FileHelper.IMAGE_RESOURCE_DIRECTORY;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Clipart {

    public static String CLIPART_DIRECTORY = "clipart/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String path;
    private String namePolish;
    private String nameEnglish;

    public Clipart(String path, String namePolish, String nameEnglish) {
        this.path = path;
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }

    public String getResourcePath() {
        return IMAGE_RESOURCE_DIRECTORY + CLIPART_DIRECTORY + path;
    }
}
