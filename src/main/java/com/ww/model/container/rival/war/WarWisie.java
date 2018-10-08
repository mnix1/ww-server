package com.ww.model.container.rival.war;


import com.ww.model.constant.wisie.WisieValueChange;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.rival.task.TaskWisdomAttribute;
import com.ww.model.entity.outside.wisie.AbstractWisieAttributes;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import static com.ww.helper.WisieHelper.f1;

@Setter
@Getter
public class WarWisie extends AbstractWisieAttributes {

    private OwnedWisie wisie;
    private double originalValue;

    private Question question;

    private double wisdomSum;
    private double speedF1;
    private double reflexF1;
    private double concentrationF1;
    private double confidenceF1;
    private double intuitionF1;

    private double value;

    private boolean isHobby;
    private int hobbyCount;
    private double hobbyFactor;

    public WarWisie(OwnedWisie wisie) {
        super(wisie);
        this.wisie = wisie;
        this.originalValue = wisie.calculateValue();
        this.value = calculateValue();
    }

    public void cacheAttributes() {
        this.isHobby = wisie.getHobbies().contains(question.getType().getCategory());
        this.hobbyCount = wisie.getHobbies().size();
        this.hobbyFactor = 1 + 1d / hobbyCount;
        this.wisdomSum = prepareWisdomSum();
        this.speedF1 = f1(speed);
        this.reflexF1 = f1(reflex);
        this.concentrationF1 = f1(concentration);
        this.confidenceF1 = f1(confidence);
        this.intuitionF1 = f1(intuition);
        this.value = calculateValue();
    }

    public WisieValueChange getWisieValueChange() {
        if (originalValue > value) {
            return WisieValueChange.DECREASE;
        }
        if (originalValue < value) {
            return WisieValueChange.DECREASE;
        }
        return WisieValueChange.NONE;
    }

    private double prepareWisdomSum() {
        double sum = 0;
        for (TaskWisdomAttribute attribute : question.getType().getWisdomAttributes()) {
            sum += f1(getWisdomAttributeValue(attribute.getWisdomAttribute())) * attribute.getValue();
        }
        return sum;
    }
}
