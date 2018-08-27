package com.ww.model.entity.hero;


import com.ww.helper.HeroHelper;
import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileHero {
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

    private Double mentalAttributeReflex;
    private Double mentalAttributeConcentration;
    private Double mentalAttributeLeadership;
    private Double mentalAttributeCharisma;
    private Double mentalAttributeIntuition;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "hero_id", nullable = false, updatable = false)
    private Hero hero;

    public ProfileHero(Profile profile, Hero hero) {
        this.profile = profile;
        this.hero = hero;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((ProfileHero) obj).id);
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

    public Double getMentalAttributeValue(MentalAttribute mentalAttribute) {
        if (mentalAttribute == MentalAttribute.REFLEX) {
            return mentalAttributeReflex;
        }
        if (mentalAttribute == MentalAttribute.CONCENTRATION) {
            return mentalAttributeConcentration;
        }
        if (mentalAttribute == MentalAttribute.LEADERSHIP) {
            return mentalAttributeLeadership;
        }
        if (mentalAttribute == MentalAttribute.CHARISMA) {
            return mentalAttributeCharisma;
        }
        if (mentalAttribute == MentalAttribute.INTUITION) {
            return mentalAttributeIntuition;
        }
        throw new IllegalArgumentException();
    }

    public Double calculateValue() {
        return HeroHelper.calculateValue(this);
    }
}
