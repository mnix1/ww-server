package com.ww.service.rival.global;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.dto.social.ClassificationProfileDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.SeasonRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ww.helper.EloHelper.*;
import static com.ww.helper.EloHelper.LOOSER;
import static com.ww.helper.EloHelper.updateElo;

@Service
public class RivalClassificationService {
    public static final int CLASSIFICATION_POSITIONS_COUNT = 10;

    @Autowired
    private ProfileService profileService;

    public List<ClassificationProfileDTO> classification(RivalType type) {
        String profileTag = profileService.getProfileTag();
        List<Profile> profiles = profileService.classification(type);
        return IntStream.range(0, profiles.size())
                .mapToObj(value -> new ClassificationProfileDTO(profiles.get(value), type, (long) value + 1))
                .filter(value -> value.getPosition() <= CLASSIFICATION_POSITIONS_COUNT || value.getTag().equals(profileTag))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProfilesElo(RivalModel model) {
        Profile winner = model.getWinner();
        Profile creator = profileService.getProfile(model.getCreatorProfile().getId());
        Profile opponent = profileService.getProfile(model.getOpponentProfile().getId());
        Instant lastPlay = Instant.now();
        Long creatorEloChange = 0L;
        Long opponentEloChange = 0L;
        if (model.getType() == RivalType.BATTLE) {
            if (model.getDraw()) {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setBattleLastPlay(lastPlay);
            opponent.setBattleLastPlay(lastPlay);
        } else if (model.getType() == RivalType.WAR) {
            if (model.getDraw()) {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setWarLastPlay(lastPlay);
            opponent.setWarLastPlay(lastPlay);
        }
        updateElo(creator, creatorEloChange, model.getType());
        model.setCreatorEloChange(creatorEloChange);
        updateElo(opponent, opponentEloChange, model.getType());
        model.setOpponentEloChange(opponentEloChange);
        profileService.save(creator);
        profileService.save(opponent);
    }

}
