package com.ww.model.container.rival;

import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RivalInitContainer {
    private Profile creatorProfile;
    private Profile opponentProfile;

    public RivalInitContainer(Profile creatorProfile, Profile opponentProfile) {
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
    }
}
