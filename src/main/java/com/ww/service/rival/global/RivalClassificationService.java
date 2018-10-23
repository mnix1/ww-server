package com.ww.service.rival.global;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.dto.social.ClassificationDTO;
import com.ww.model.dto.social.ClassificationPositionDTO;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.Season;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class RivalClassificationService {
    public static final int CLASSIFICATION_POSITIONS_COUNT = 10;

    private final ProfileService profileService;
    private final RivalSeasonService rivalSeasonService;
    private final RivalProfileSeasonService rivalProfileSeasonService;

    public ClassificationDTO classification(RivalType type) {
        String profileTag = profileService.getProfileTag();
        Season season = rivalSeasonService.actual(type);
        List<SeasonGrade> seasonGrades = rivalSeasonService.findSeasonGrades(season.getType());
        List<ProfileSeason> profileSeasons = rivalProfileSeasonService.findProfileSeasons(season.getId());
        List<ClassificationPositionDTO> positions = IntStream.range(0, profileSeasons.size())
                .mapToObj(value -> new ClassificationPositionDTO(profileSeasons.get(value), (long) value + 1, profileTag))
                .filter(value -> value.getPosition() <= CLASSIFICATION_POSITIONS_COUNT || value.getMe())
                .collect(Collectors.toList());
        return new ClassificationDTO(season, seasonGrades, positions);
    }

}
