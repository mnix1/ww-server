package com.ww.model.entity.inside.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static com.ww.helper.FileHelper.IMAGE_RESOURCE_DIRECTORY;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class MemoryShape {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String key;
    private String namePolish;
    private String nameEnglish;

    public MemoryShape(String key, String namePolish, String nameEnglish) {
        this.key = key;
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }

    public String getPath() {
        return IMAGE_RESOURCE_DIRECTORY + "shape/" + key + ".svg";
    }
}
