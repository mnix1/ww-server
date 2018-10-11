package com.ww.service.rival.global;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.dto.social.ClassificationProfileDTO;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
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

@Service
public class RivalClassificationService {
    public static final int CLASSIFICATION_POSITIONS_COUNT = 10;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RivalSeasonService rivalSeasonService;
    @Autowired
    private RivalProfileSeasonService rivalProfileSeasonService;

    public List<ClassificationProfileDTO> classification(RivalType type) {
        String profileTag = profileService.getProfileTag();
        Season season = rivalSeasonService.actual(type);
        List<ProfileSeason> profileSeasons = rivalProfileSeasonService.findProfileSeasons(season.getId());
        return IntStream.range(0, profileSeasons.size())
                .mapToObj(value -> new ClassificationProfileDTO(profileSeasons.get(value), (long) value + 1))
                .filter(value -> value.getPosition() <= CLASSIFICATION_POSITIONS_COUNT || value.getTag().equals(profileTag))
                .collect(Collectors.toList());
    }

}
