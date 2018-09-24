package com.ww.model.container.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RivalInitContainer {
    private RivalType type;
    private RivalImportance importance;
    private Profile creatorProfile;
    private Profile opponentProfile;

    public RivalInitContainer(RivalType type, RivalImportance importance, Profile creatorProfile, Profile opponentProfile) {
        this.type = type;
        this.importance = importance;
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
    }
}
