package com.ww.model.container.rival.war;


import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieValueChange;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.outside.wisie.AbstractWisieAttributes;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.ww.helper.WisieHelper.f1;

@Setter
@Getter
@ToString
public class WarWisie extends AbstractWisieAttributes {

    private OwnedWisie wisie;
    private double originalValue;

    private double wisdomSum;
    private double speedF1;
    private double reflexF1;
    private double cunningF1;
    private double concentrationF1;
    private double confidenceF1;
    private double intuitionF1;

    private double value;

    private boolean isHobby;
    private double hobbyFactor;
    private double hobbyPart;

    public WarWisie(OwnedWisie wisie) {
        super(wisie);
        this.wisie = wisie;
        this.originalValue = wisie.calculateValue();
        this.value = calculateValue();
    }

    public void cacheHobbies(Question question) {
        this.isHobby = wisie.getHobbies().contains(question.getType().getCategory());
        this.hobbyFactor = isHobby ? 2 + (3 - wisie.getHobbies().size()) * 0.5 : 1d;
        this.hobbyPart = isHobby ? 0.25 : 0;
    }

    public void cacheAttributes(Question question) {
        this.wisdomSum = prepareWisdomSum(question);
        this.speedF1 = f1(speed);
        this.reflexF1 = f1(reflex);
        this.cunningF1 = f1(cunning);
        this.concentrationF1 = f1(concentration);
        this.confidenceF1 = f1(confidence);
        this.intuitionF1 = f1(intuition);
    }

    public WisieValueChange getWisieValueChange() {
        if (originalValue > value) {
            return WisieValueChange.DECREASE;
        }
        if (originalValue < value) {
            return WisieValueChange.INCREASE;
        }
        return WisieValueChange.NONE;
    }

    private double prepareWisdomSum(Question question) {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }

    public void decreaseAttributesByHalf() {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            setWisdomAttributeValue(wisdomAttribute, getWisdomAttributeValue(wisdomAttribute) / 2);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            setMentalAttributeValue(mentalAttribute, getMentalAttributeValue(mentalAttribute) / 2);
        }
        this.value = calculateValue();
    }

    public void increaseAttributesByHalf() {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            setWisdomAttributeValue(wisdomAttribute, getWisdomAttributeValue(wisdomAttribute) * 2);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            setMentalAttributeValue(mentalAttribute, getMentalAttributeValue(mentalAttribute) * 2);
        }
        this.value = calculateValue();
    }

    public void increaseWisdomAttributes(WarWisie source, double factor) {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            double actualValue = getWisdomAttributeValue(wisdomAttribute);
            double changeValue = source.getWisie().getWisdomAttributeValue(wisdomAttribute) * factor;
            setWisdomAttributeValue(wisdomAttribute, actualValue + changeValue);
        }
        this.value = calculateValue();
    }

    public void increaseMentalAttributes(WarWisie source, double factor) {
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            double actualValue = getMentalAttributeValue(mentalAttribute);
            double changeValue = source.getWisie().getMentalAttributeValue(mentalAttribute) * factor;
            setMentalAttributeValue(mentalAttribute, actualValue + changeValue);
        }
        this.value = calculateValue();
    }
}
