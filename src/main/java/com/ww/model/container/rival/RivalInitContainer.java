package com.ww.model.container.rival;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RivalInitContainer {
    private RivalType type;
    private Profile creatorProfile;
    private Profile opponentProfile;

    public RivalInitContainer(RivalType type, Profile creatorProfile, Profile opponentProfile) {
        this.type = type;
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
    }
}
