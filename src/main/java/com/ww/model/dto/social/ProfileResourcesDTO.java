package com.ww.model.dto.social;

import com.ww.model.constant.Language;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class ProfileResourcesDTO extends ProfileDTO {

    private Long level;
    private Long experience;
    private Long wisdom;
    private Long crystal;
    private Long gold;
    private Long elixir;
    private Language language;
    private Boolean introductionCompleted;
    private Integer introductionStepIndex;

    public ProfileResourcesDTO(Profile profile) {
        super(profile);
        this.language = profile.getLanguage();
        this.level = profile.getLevel();
        this.experience = profile.getExperience();
        this.wisdom = profile.getWisdom();
        this.crystal = profile.getCrystal();
        this.gold = profile.getGold();
        this.elixir = profile.getElixir();
        this.introductionCompleted = profile.getIntroductionCompleted();
        this.introductionStepIndex = profile.getIntroductionStepIndex();
    }
}
