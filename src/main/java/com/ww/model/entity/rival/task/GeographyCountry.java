package com.ww.model.entity.rival.task;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class GeographyCountry {
    public static String MAP_DIRECTORY = "map/";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String alpha2Code;
    private String alpha3Code;
    private String namePolish;
    private String nameEnglish;
    private String nameNative;
    private String wikiDetailsUrl;
    private String capitalPolish;
    private String capitalEnglish;
    private String capitalDetailsUrl;
    private String continentPolish;
    private String continentEnglish;
    private String mapSvgLocationMapUrl;
    private String wikiLocationMapUrl;
    private String flagUrl;
    private Double lat;
    private Double lng;
    private Double population;
    private Double area;
    private String topLevelDomain;
    private String currencyCode;
    private String currencyNameEnglish;
    private String currencySymbol;

    public GeographyCountry(JsonNode jsonNode) {
        this.alpha2Code = jsonNode.get("alpha2Code").asText();
        this.alpha3Code = jsonNode.get("alpha3Code").asText();
        this.namePolish = jsonNode.get("namePolish").asText();
        this.nameEnglish = jsonNode.get("nameEnglish").asText();
        this.nameNative = jsonNode.get("nameNative").asText();
        this.wikiDetailsUrl = jsonNode.get("wikiDetailsUrl").asText();
        this.capitalPolish = jsonNode.get("capitalPolish").asText();
        this.capitalEnglish = jsonNode.get("capitalEnglish").asText();
        if (jsonNode.has("capitalDetailsUrl")) {
            this.capitalDetailsUrl = jsonNode.get("capitalDetailsUrl").asText();
        }
        this.continentPolish = jsonNode.get("continentPolish").asText();
        this.continentEnglish = jsonNode.get("continentEnglish").asText();
        this.wikiLocationMapUrl = jsonNode.get("wikiLocationMapUrl").asText();
        this.mapSvgLocationMapUrl = jsonNode.get("mapSvgLocationMapUrl").asText();
        this.flagUrl = jsonNode.get("flagUrl").asText();
        this.lat = jsonNode.get("lat").asDouble();
        this.lng = jsonNode.get("lng").asDouble();
        this.population = jsonNode.get("population").asDouble();
        this.area = jsonNode.get("area").asDouble();
        this.topLevelDomain = jsonNode.get("topLevelDomain").asText();
        this.currencyCode = jsonNode.get("currencyCode").asText();
        this.currencyNameEnglish = jsonNode.get("currencyNameEnglish").asText();
        this.currencySymbol = jsonNode.get("currencySymbol").asText();
    }

    public String getSvgFileName() {
        return alpha2Code + ".svg";
    }

    public String getFlagResourcePath(String prefix) {
        return prefix + getFlagResourcePath();
    }

    public String getFlagResourcePath() {
        return "task/image/flag/" + getSvgFileName();
    }

    public String getMapResourcePath(String prefix) {
        return prefix + getMapResourcePath();
    }

    public String getMapResourcePath() {
        return "task/image/" + MAP_DIRECTORY + getSvgFileName();
    }

}
