package com.ww.model.entity.outside.wisie;


import com.ww.helper.WisieHelper;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractWisieAttributes {
    protected Double memory;
    protected Double logic;
    protected Double perceptivity;
    protected Double counting;
    protected Double combiningFacts;
    protected Double patternRecognition;
    protected Double imagination;

    protected Double speed;
    protected Double reflex;
    protected Double concentration;
    protected Double confidence;
    protected Double intuition;

    protected AbstractWisieAttributes(AbstractWisieAttributes profileWisie) {
        this.memory = profileWisie.memory;
        this.logic = profileWisie.logic;
        this.perceptivity = profileWisie.perceptivity;
        this.counting = profileWisie.counting;
        this.combiningFacts = profileWisie.combiningFacts;
        this.patternRecognition = profileWisie.patternRecognition;
        this.imagination = profileWisie.imagination;
        this.speed = profileWisie.speed;
        this.reflex = profileWisie.reflex;
        this.concentration = profileWisie.concentration;
        this.confidence = profileWisie.confidence;
        this.intuition = profileWisie.intuition;
    }

    public Double getWisdomAttributeValue(WisdomAttribute wisdomAttribute) {
        if (wisdomAttribute == WisdomAttribute.MEMORY) {
            return memory;
        }
        if (wisdomAttribute == WisdomAttribute.LOGIC) {
            return logic;
        }
        if (wisdomAttribute == WisdomAttribute.PERCEPTIVITY) {
            return perceptivity;
        }
        if (wisdomAttribute == WisdomAttribute.COUNTING) {
            return counting;
        }
        if (wisdomAttribute == WisdomAttribute.COMBINING_FACTS) {
            return combiningFacts;
        }
        if (wisdomAttribute == WisdomAttribute.PATTERN_RECOGNITION) {
            return patternRecognition;
        }
        if (wisdomAttribute == WisdomAttribute.IMAGINATION) {
            return imagination;
        }
        throw new IllegalArgumentException();
    }

    public void setWisdomAttributeValue(WisdomAttribute wisdomAttribute, Double value) {
        if (wisdomAttribute == WisdomAttribute.MEMORY) {
            setMemory(value);
        } else if (wisdomAttribute == WisdomAttribute.LOGIC) {
            setLogic(value);
        } else if (wisdomAttribute == WisdomAttribute.PERCEPTIVITY) {
            setPerceptivity(value);
        } else if (wisdomAttribute == WisdomAttribute.COUNTING) {
            setCounting(value);
        } else if (wisdomAttribute == WisdomAttribute.COMBINING_FACTS) {
            setCombiningFacts(value);
        } else if (wisdomAttribute == WisdomAttribute.PATTERN_RECOGNITION) {
            setPatternRecognition(value);
        } else if (wisdomAttribute == WisdomAttribute.IMAGINATION) {
            setImagination(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Double getMentalAttributeValue(MentalAttribute mentalAttribute) {
        if (mentalAttribute == MentalAttribute.SPEED) {
            return speed;
        }
        if (mentalAttribute == MentalAttribute.REFLEX) {
            return reflex;
        }
        if (mentalAttribute == MentalAttribute.CONCENTRATION) {
            return concentration;
        }
        if (mentalAttribute == MentalAttribute.CONFIDENCE) {
            return confidence;
        }
        if (mentalAttribute == MentalAttribute.INTUITION) {
            return intuition;
        }
        throw new IllegalArgumentException();
    }

    public void setMentalAttributeValue(MentalAttribute mentalAttribute, Double value) {
        if (mentalAttribute == MentalAttribute.SPEED) {
            setSpeed(value);
        } else if (mentalAttribute == MentalAttribute.REFLEX) {
            setReflex(value);
        } else if (mentalAttribute == MentalAttribute.CONCENTRATION) {
            setConcentration(value);
        } else if (mentalAttribute == MentalAttribute.CONFIDENCE) {
            setConfidence(value);
        } else if (mentalAttribute == MentalAttribute.INTUITION) {
            setIntuition(value);
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
