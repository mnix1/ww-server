package com.ww.model.entity.wisie;


import com.ww.helper.WisieHelper;
import com.ww.helper.WisieHobbyConverter;
import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileWisie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean inTeam = false;

    private Double wisdomAttributeMemory;
    private Double wisdomAttributeLogic;
    private Double wisdomAttributePerceptivity;
    private Double wisdomAttributeCounting;
    private Double wisdomAttributeCombiningFacts;
    private Double wisdomAttributePatternRecognition;
    private Double wisdomAttributeImagination;

    private Double mentalAttributeSpeed;
    private Double mentalAttributeReflex;
    private Double mentalAttributeConcentration;
    private Double mentalAttributeConfidence;
    private Double mentalAttributeIntuition;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @Column
    @Convert(converter = WisieHobbyConverter.class)
    private Set<Category> hobbies;
    @ManyToOne
    @JoinColumn(name = "wisie_id", nullable = false, updatable = false)
    private Wisie wisie;

    public ProfileWisie(Profile profile, Wisie wisie) {
        this.profile = profile;
        this.wisie = wisie;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((ProfileWisie) obj).id);
    }

    public Double getWisdomAttributeValue(WisdomAttribute wisdomAttribute) {
        if (wisdomAttribute == WisdomAttribute.MEMORY) {
            return wisdomAttributeMemory;
        }
        if (wisdomAttribute == WisdomAttribute.LOGIC) {
            return wisdomAttributeLogic;
        }
        if (wisdomAttribute == WisdomAttribute.PERCEPTIVITY) {
            return wisdomAttributePerceptivity;
        }
        if (wisdomAttribute == WisdomAttribute.COUNTING) {
            return wisdomAttributeCounting;
        }
        if (wisdomAttribute == WisdomAttribute.COMBINING_FACTS) {
            return wisdomAttributeCombiningFacts;
        }
        if (wisdomAttribute == WisdomAttribute.PATTERN_RECOGNITION) {
            return wisdomAttributePatternRecognition;
        }
        if (wisdomAttribute == WisdomAttribute.IMAGINATION) {
            return wisdomAttributeImagination;
        }
        throw new IllegalArgumentException();
    }

    public void setWisdomAttributeValue(WisdomAttribute wisdomAttribute, Double value) {
        if (wisdomAttribute == WisdomAttribute.MEMORY) {
            setWisdomAttributeMemory(value);
        } else if (wisdomAttribute == WisdomAttribute.LOGIC) {
            setWisdomAttributeLogic(value);
        } else if (wisdomAttribute == WisdomAttribute.PERCEPTIVITY) {
            setWisdomAttributePerceptivity(value);
        } else if (wisdomAttribute == WisdomAttribute.COUNTING) {
            setWisdomAttributeCounting(value);
        } else if (wisdomAttribute == WisdomAttribute.COMBINING_FACTS) {
            setWisdomAttributeCombiningFacts(value);
        } else if (wisdomAttribute == WisdomAttribute.PATTERN_RECOGNITION) {
            setWisdomAttributePatternRecognition(value);
        } else if (wisdomAttribute == WisdomAttribute.IMAGINATION) {
            setWisdomAttributeImagination(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Double getMentalAttributeValue(MentalAttribute mentalAttribute) {
        if (mentalAttribute == MentalAttribute.SPEED) {
            return mentalAttributeSpeed;
        }
        if (mentalAttribute == MentalAttribute.REFLEX) {
            return mentalAttributeReflex;
        }
        if (mentalAttribute == MentalAttribute.CONCENTRATION) {
            return mentalAttributeConcentration;
        }
        if (mentalAttribute == MentalAttribute.CONFIDENCE) {
            return mentalAttributeConfidence;
        }
        if (mentalAttribute == MentalAttribute.INTUITION) {
            return mentalAttributeIntuition;
        }
        throw new IllegalArgumentException();
    }

    public void setMentalAttributeValue(MentalAttribute mentalAttribute, Double value) {
        if (mentalAttribute == MentalAttribute.SPEED) {
            setMentalAttributeSpeed(value);
        } else if (mentalAttribute == MentalAttribute.REFLEX) {
            setMentalAttributeReflex(value);
        } else if (mentalAttribute == MentalAttribute.CONCENTRATION) {
            setMentalAttributeConcentration(value);
        } else if (mentalAttribute == MentalAttribute.CONFIDENCE) {
            setMentalAttributeConfidence(value);
        } else if (mentalAttribute == MentalAttribute.INTUITION) {
            setMentalAttributeIntuition(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void upgradeWisdomAttribute(WisdomAttribute wisdomAttribute, double change) {
        setWisdomAttributeValue(wisdomAttribute, getWisdomAttributeValue(wisdomAttribute) + change);
    }

    public void upgradeMentalAttribute(MentalAttribute mentalAttribute, double change) {
        setMentalAttributeValue(mentalAttribute, getMentalAttributeValue(mentalAttribute) + change);
    }

    public Double calculateValue() {
        return WisieHelper.calculateValue(this);
    }
}
