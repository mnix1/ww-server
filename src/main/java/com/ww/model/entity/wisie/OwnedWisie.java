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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OwnedWisie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected Boolean inTeam = false;

    protected Double wisdomAttributeMemory;
    protected Double wisdomAttributeLogic;
    protected Double wisdomAttributePerceptivity;
    protected Double wisdomAttributeCounting;
    protected Double wisdomAttributeCombiningFacts;
    protected Double wisdomAttributePatternRecognition;
    protected Double wisdomAttributeImagination;

    protected Double mentalAttributeSpeed;
    protected Double mentalAttributeReflex;
    protected Double mentalAttributeConcentration;
    protected Double mentalAttributeConfidence;
    protected Double mentalAttributeIntuition;

    @Column
    @Convert(converter = WisieHobbyConverter.class)
    protected Set<Category> hobbies;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;
    @ManyToOne
    @JoinColumn(name = "wisie_id", nullable = false, updatable = false)
    protected Wisie wisie;

    protected OwnedWisie(Profile profile, Wisie wisie) {
        this.profile = profile;
        this.wisie = wisie;
    }

    protected OwnedWisie(OwnedWisie profileWisie) {
        this.wisdomAttributeMemory = profileWisie.wisdomAttributeMemory;
        this.wisdomAttributeLogic = profileWisie.wisdomAttributeLogic;
        this.wisdomAttributePerceptivity = profileWisie.wisdomAttributePerceptivity;
        this.wisdomAttributeCounting = profileWisie.wisdomAttributeCounting;
        this.wisdomAttributeCombiningFacts = profileWisie.wisdomAttributeCombiningFacts;
        this.wisdomAttributePatternRecognition = profileWisie.wisdomAttributePatternRecognition;
        this.wisdomAttributeImagination = profileWisie.wisdomAttributeImagination;
        this.mentalAttributeSpeed = profileWisie.mentalAttributeSpeed;
        this.mentalAttributeReflex = profileWisie.mentalAttributeReflex;
        this.mentalAttributeConcentration = profileWisie.mentalAttributeConcentration;
        this.mentalAttributeConfidence = profileWisie.mentalAttributeConfidence;
        this.mentalAttributeIntuition = profileWisie.mentalAttributeIntuition;
        this.hobbies = profileWisie.hobbies;
        this.inTeam = profileWisie.inTeam;
        this.wisie = profileWisie.wisie;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((OwnedWisie) obj).id);
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
