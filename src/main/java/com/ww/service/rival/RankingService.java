package com.ww.service.rival;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.EloHelper.*;

@Service
public class RankingService {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileConnectionService profileConnectionService;

}
