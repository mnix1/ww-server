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

    public synchronized void rankingGameResult(RivalType rivalType, Boolean isDraw, Profile winner, Profile looser) {
        Profile p1 = profileService.getProfile(winner.getId());
        Profile p2 = profileService.getProfile(looser.getId());
        if (rivalType == RivalType.BATTLE) {
            if (isDraw) {
                p1.setBattleElo(prepareNewElo(p1.getBattleElo(), p2.getBattleElo(), DRAW));
                p2.setBattleElo(prepareNewElo(p2.getBattleElo(), p1.getBattleElo(), DRAW));
            } else {
                p1.setBattleElo(prepareNewElo(p1.getBattleElo(), p2.getBattleElo(), WINNER));
                p2.setBattleElo(prepareNewElo(p2.getBattleElo(), p1.getBattleElo(), LOOSER));
            }
        } else if (rivalType == RivalType.WAR) {
            if (isDraw) {
                p1.setWarElo(prepareNewElo(p1.getWarElo(), p2.getWarElo(), DRAW));
                p2.setWarElo(prepareNewElo(p2.getWarElo(), p1.getWarElo(), DRAW));
            } else {
                p1.setWarElo(prepareNewElo(p1.getWarElo(), p2.getWarElo(), WINNER));
                p2.setWarElo(prepareNewElo(p2.getWarElo(), p1.getWarElo(), LOOSER));
            }
        }
        profileService.save(p1);
        profileService.save(p2);
    }

}
