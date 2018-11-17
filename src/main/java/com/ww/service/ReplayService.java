package com.ww.service;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.replay.Replay;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.RivalRepository;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
@AllArgsConstructor
public class ReplayService {

    private final ProfileConnectionService profileConnectionService;
    private final RivalRepository rivalRepository;


    public void replay(Long rivalId, Long perspectiveProfileId, Long targetProfileId) {
        Optional<Rival> optionalRival = rivalRepository.findById(rivalId);
        if (!optionalRival.isPresent()) {
            return;
        }
        Rival rival = optionalRival.get();
        new Replay(profileConnectionService, ).play();
    }
}
