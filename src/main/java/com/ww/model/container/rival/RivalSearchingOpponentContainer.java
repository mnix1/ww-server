package com.ww.model.container.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RivalSearchingOpponentContainer {
    private RivalType type;
    private RivalImportance importance;
    private Profile profile;

    public RivalSearchingOpponentContainer(RivalType type, RivalImportance importance, Profile profile) {
        this.type = type;
        this.importance = importance;
        this.profile = profile;
    }
}
