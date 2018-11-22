package com.ww.model.container.rival.init;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalGroup;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RivalTwoInit implements RivalInit {
    private RivalType type;
    private RivalImportance importance;
    private Profile creatorProfile;
    private Profile opponentProfile;
    private Season season;
    private ProfileSeason creatorProfileSeason;
    private ProfileSeason opponentProfileSeason;
    private Language commonLanguage;

    public RivalTwoInit(RivalType type, RivalImportance importance, Profile creatorProfile, Profile opponentProfile) {
        this.type = type;
        this.importance = importance;
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
        this.commonLanguage = prepareCommonLanguage();
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "type=" + type +
                ", importance=" + importance +
                ", creatorProfile=" + creatorProfile +
                ", opponentProfile=" + opponentProfile +
                '}';
    }

    public ProfileSeason getProfileSeasons(Long profileId) {
        if (creatorProfile.getId().equals(profileId)) {
            return creatorProfileSeason;
        }
        return opponentProfileSeason;
    }

    protected Language prepareCommonLanguage() {
        Language creatorLanguage = creatorProfile.getLanguage();
        Language opponentLanguage = opponentProfile.getLanguage();
        if (creatorLanguage == opponentLanguage) {
            return creatorLanguage;
        }
        if (opponentLanguage == Language.NONE) {
            return creatorLanguage;
        }
        return Language.NO_COMMON;
    }

    @Override
    public RivalGroup getPlayer() {
        return RivalGroup.TWO;
    }

    @Override
    public List<Profile> getProfiles() {
        return Arrays.asList(creatorProfile, opponentProfile);
    }
}
