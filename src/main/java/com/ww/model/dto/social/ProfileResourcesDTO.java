package com.ww.model.dto.social;

import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class ProfileResourcesDTO extends ProfileDTO {

    private Long level;
    private Long experience;
    private Long wisdom;
    private Long crystal;
    private Long gold;
    private Long elixir;
    private Long battleElo;
    private Long warElo;
    private Boolean teamInitialized;

    public ProfileResourcesDTO(Profile profile) {
        super(profile);
        this.level = profile.getLevel();
        this.experience = profile.getExperience();
        this.wisdom = profile.getWisdom();
        this.crystal = profile.getCrystal();
        this.gold = profile.getGold();
        this.elixir = profile.getElixir();
        this.battleElo = profile.getBattleElo();
        this.warElo = profile.getWarElo();
        this.teamInitialized = profile.getTeamInitialized();
    }
}
