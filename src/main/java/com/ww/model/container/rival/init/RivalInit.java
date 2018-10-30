package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalGroup;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;

import java.util.List;

public interface RivalInit {
    RivalType getType();
    RivalImportance getImportance();
    RivalGroup getPlayer();
    List<Profile> getProfiles();
}
