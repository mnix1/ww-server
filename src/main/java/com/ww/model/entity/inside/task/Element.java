package com.ww.model.entity.inside.task;

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
public class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private String namePolish;
    private String nameEnglish;
    private Integer number;
    private Integer xPos;
    private Integer yPos;
    private Integer period;
    private Integer shellCount;
    private Double atomicMass;
    private Double boil;
    private Double density;
    private Double melt;
    private Double molarHeat;
    private String phase;
    private String category;

    public Element(JsonNode jsonNode) {
        this.symbol = jsonNode.get("symbol").asText();
        this.namePolish = jsonNode.get("namePolish").asText();
        this.nameEnglish = jsonNode.get("name").asText().toLowerCase();
        if (jsonNode.hasNonNull("number")) {
            this.number = jsonNode.get("number").asInt();
        }
        if (jsonNode.hasNonNull("xpos")) {
            this.xPos = jsonNode.get("xpos").asInt();
        }
        if (jsonNode.hasNonNull("ypos")) {
            this.yPos = jsonNode.get("ypos").asInt();
        }
        if (jsonNode.hasNonNull("period")) {
            this.period = jsonNode.get("period").asInt();
        }
        this.shellCount = jsonNode.get("shellCount").asInt();
        if (jsonNode.hasNonNull("atomic_mass")) {
            this.atomicMass = jsonNode.get("atomic_mass").asDouble();
        }
        if (jsonNode.hasNonNull("boil")) {
            this.boil = jsonNode.get("boil").asDouble();
        }
        if (jsonNode.hasNonNull("density")) {
            this.density = jsonNode.get("density").asDouble();
        }
        if (jsonNode.hasNonNull("melt")) {
            this.melt = jsonNode.get("melt").asDouble();
        }
        if (jsonNode.hasNonNull("molar_heat")) {
            this.molarHeat = jsonNode.get("molar_heat").asDouble();
        }
        this.phase = jsonNode.get("phase").asText();
        this.category = jsonNode.get("category").asText();
    }

}
