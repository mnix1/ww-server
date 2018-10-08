package com.ww.model.container.rival.war;


import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import static com.ww.helper.WisieHelper.f1;

@Setter
@Getter
public class WarWisie {

    private OwnedWisie wisie;

    private Question question;

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

    private double wisdomSum;
    private double speedF1;
    private double reflexF1;
    private double concentrationF1;
    private double confidenceF1;
    private double intuitionF1;

    private boolean isHobby;
    private int hobbyCount;
    private double hobbyFactor;

    public WarWisie(OwnedWisie wisie, Question question) {
        this.wisie = wisie;
        this.question = question;
    }

    public void initAttributes() {
//        this.wisdomAttributeMemory = wisie.getWisdomAttributeMemory();
//        this.wisdomAttributeLogic = wisie.getWisdomAttributeLogic();
//        this.wisdomAttributePerceptivity = wisie.getWisdomAttributePerceptivity();
//        this.wisdomAttributeCounting = wisie.getWisdomAttributeCounting();
//        this.wisdomAttributeCombiningFacts = wisie.getWisdomAttributeCombiningFacts();
//        this.wisdomAttributePatternRecognition = wisie.getWisdomAttributePatternRecognition();
//        this.wisdomAttributeImagination = wisie.getWisdomAttributeImagination();
//
//        this.mentalAttributeSpeed = wisie.getMentalAttributeSpeed();
//        this.mentalAttributeReflex = wisie.getMentalAttributeReflex();
//        this.mentalAttributeConcentration = wisie.getMentalAttributeConcentration();
//        this.mentalAttributeConfidence = wisie.getMentalAttributeConfidence();
//        this.mentalAttributeIntuition = wisie.getMentalAttributeIntuition();
    }

    public void cacheAttributes() {
        this.isHobby = wisie.getHobbies().contains(question.getType().getCategory());
        this.hobbyCount = wisie.getHobbies().size();
        this.hobbyFactor = 1 + 1d / hobbyCount;
        this.wisdomSum = prepareWisdomSum();
        this.speedF1 = f1(wisie.getMentalAttributeSpeed());
        this.reflexF1 = f1(wisie.getMentalAttributeReflex());
        this.concentrationF1 = f1(wisie.getMentalAttributeConcentration());
        this.confidenceF1 = f1(wisie.getMentalAttributeConfidence());
        this.intuitionF1 = f1(wisie.getMentalAttributeIntuition());
    }

    private double prepareWisdomSum() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(wisie.getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
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
}
