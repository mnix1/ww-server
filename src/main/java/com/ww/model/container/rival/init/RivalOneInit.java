package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalGroup;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Getter
public class RivalOneInit implements RivalInit {
    private RivalType type;
    private RivalImportance importance;
    private Profile creatorProfile;
    private Instant date = Instant.now();

    public RivalOneInit(RivalType type, RivalImportance importance, Profile creatorProfile) {
        this.type = type;
        this.importance = importance;
        this.creatorProfile = creatorProfile;
    }

    @Override
    public RivalGroup getPlayer() {
        return RivalGroup.ONE;
    }

    @Override
    public List<Profile> getProfiles() {
        return Arrays.asList(creatorProfile);
    }
}
