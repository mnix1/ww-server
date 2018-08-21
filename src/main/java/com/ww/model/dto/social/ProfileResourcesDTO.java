package com.ww.model.dto.social;

import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class ProfileResourcesDTO extends ProfileDTO {

    private Long experience;
    private Long wisdom;
    private Long crystal;
    private Long gold;
    private Long elixir;

    public ProfileResourcesDTO(Profile profile) {
        super(profile);
        this.experience = profile.getExperience();
        this.wisdom = profile.getWisdom();
        this.crystal = profile.getCrystal();
        this.gold = profile.getGold();
        this.elixir = profile.getElixir();
    }
}
