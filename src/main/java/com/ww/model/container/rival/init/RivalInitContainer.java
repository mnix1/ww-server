package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalPlayer;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public interface RivalInitContainer {
    RivalType getType();
    RivalImportance getImportance();
    RivalPlayer getPlayer();
    List<Profile> getProfiles();
}
