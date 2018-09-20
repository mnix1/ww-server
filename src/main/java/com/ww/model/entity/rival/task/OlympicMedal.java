package com.ww.model.entity.rival.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.OlympicGamesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class OlympicMedal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private OlympicGamesType type;
    private Integer year;
    private String city;
    private String cityPolish;
    private String sport;
    private String sportPolish;
    private String discipline;
    private String athlete;
    private String country;
    private String gender;
    private String event;
    private String medal;

    public OlympicMedal(String[] row, OlympicGamesType type, JsonNode mapping) {
        ObjectNode cityMapping = (ObjectNode) mapping.get("city");
        ObjectNode sportMapping = (ObjectNode) mapping.get("sport");
        this.type = type;
        this.year = Integer.parseInt(row[0]);
        this.city = row[1];
        this.cityPolish = cityMapping.get(this.city).asText();
        this.sport = row[2];
        this.sportPolish = sportMapping.get(this.sport).asText();
        this.discipline = row[3];
        this.athlete = row[4];
        this.country = row[5];
        this.gender = row[6];
        this.event = row[7];
        this.medal = row[8];
    }

    public String getTypeLang(Language language) {
        if (language == Language.POLISH) {
            return type.getNamePolish();
        }
        return type.getNameEnglish();
    }
}
