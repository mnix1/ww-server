package com.ww.model.entity.rival.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ww.model.constant.Gender;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.olympicgames.OlympicGamesMedal;
import com.ww.model.constant.rival.task.olympicgames.OlympicGamesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

import static com.ww.model.constant.Gender.MEN;

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
    private String iocCountryCode;
    private Boolean countryMapped = false;
    private String countryEnglish;
    private String countryPolish;
    private Gender gender;
    private String event;
    private OlympicGamesMedal medal;
    private Boolean popularTeamSport;
    private Boolean onlyTeamSport;
    private Boolean team;

    public OlympicMedal(List<String> params, OlympicGamesType type, JsonNode mapping, Map<String, Country> countryMapping) {
        ObjectNode cityMapping = (ObjectNode) mapping.get("city");
        ObjectNode sportMapping = (ObjectNode) mapping.get("sport");
        this.type = type;
        this.year = Integer.parseInt(params.get(0));
        this.city = params.get(1);
        this.cityPolish = cityMapping.get(this.city).asText();
        this.sport = params.get(2);
        this.sportPolish = sportMapping.get(this.sport).asText();
        this.discipline = params.get(3);
        this.athlete = swapAthleteName(params.get(4));
        this.iocCountryCode = params.get(5);
        if (countryMapping.containsKey(iocCountryCode)) {
            Country country = countryMapping.get(iocCountryCode);
            this.countryMapped = true;
            this.countryEnglish = country.getNameEnglish();
            this.countryPolish = country.getNamePolish();
        }
        this.gender = Gender.fromString(params.get(6));
        this.event = params.get(7);
        this.medal = OlympicGamesMedal.fromString(params.get(8));
        this.popularTeamSport = initPopularTeamSport();
        this.onlyTeamSport = initTeamFromSport();
        this.team = initTeam();
    }

    public String getTypeLang(Language language) {
        if (language == Language.POLISH) {
            return type.getNamePolish();
        }
        return type.getNameEnglish();
    }

    public String getMedalLang(Language language) {
        if (language == Language.POLISH) {
            return medal.getNamePolish();
        }
        return medal.getNameEnglish();
    }

    public String swapAthleteName(String name) {
        int index = name.indexOf(", ");
        if (index == -1) {
            return name;
        }
        return name.substring(index + 2) + " " + name.substring(0, index);
    }

    public String getWonPolish() {
        if (gender == MEN) {
            return "zdobył";
        }
        return "zdobyła";
    }

    @Override
    public String toString() {
        return "{" +
                countryPolish +
                ", " + athlete +
                '}';
    }

    private Boolean initTeam() {
        return onlyTeamSport || initTeamFromEvent() || initTeamFromMixed();
    }

    private Boolean initPopularTeamSport() {
        return sport.contains("Baseball")
                || sport.contains("Ice Hockey")
                || sport.contains("Handball")
                || sport.contains("Volleyball")
                || sport.contains("Rugby")
                || sport.contains("Softball")
                || sport.contains("Basketball")
                || sport.contains("Tug of War")
                || sport.contains("Football");
    }

    private Boolean initTeamFromSport() {
        return popularTeamSport
                || sport.contains("Hockey")
                || sport.contains("Curling")
                || sport.contains("Lacrosse")
                || sport.contains("Polo")
                || sport.contains("Cricket")
                || discipline.contains("Water Polo");
    }

    private Boolean initTeamFromMixed() {
        return (discipline.equals("Short Track Speed Skating") && event.contains("Relay"))
                || (!discipline.equals("Diving") && !discipline.equals("Shooting")
                && (event.startsWith("7M")
                || event.startsWith("5.5M")
                || event.startsWith("6.5M")
                || event.startsWith("8M")
                || event.startsWith("10M ")
                || event.startsWith("12M")
                || event.contains("3X")
                || event.contains(" 10M")
                || event.contains("6M")
                || event.contains("4X")
                || event.contains("2+")
                || event.contains(" 3M")
        )
        );
    }

    private Boolean initTeamFromEvent() {
        return event.contains("team")
                || event.contains("Team")
                || event.contains("Mixed")
                || event.contains("K-4")
                || (event.contains("Double") && !sport.equals("Shooting"))
                || event.contains("Pair")
                || event.contains("Duet")
                || event.startsWith("C-2")
                || event.startsWith("K-2")
                || event.contains(" Tandem")
                || event.contains("Quadruple")
                || event.startsWith("Dragon")
                || event.startsWith("Military Patrol")
                || event.startsWith("Lightweight 4")
                || event.startsWith("Eight")
                || event.contains("group")
                || event.contains("Group")
                || event.contains("Five-")
                || event.contains("Four")
                || event.contains("Two-")
                || event.contains(" Two ");
    }
}
